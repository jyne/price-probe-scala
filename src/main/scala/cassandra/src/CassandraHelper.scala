package cassandra.src

import com.datastax.driver.core._
import com.google.common.util.concurrent.{FutureCallback, Futures, ListenableFuture}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.language.implicitConversions
import monix.eval.Task
import monix.reactive.Observable
import monix.execution.Scheduler.Implicits.global

/**
  * Created by andream16 on 25.08.17.
  */
object CassandraHelper {

  /**
    * ListenableFuture are automatically converted into Scala Future
    *
    * @param context StringContext
    * @return AnyVal
    **/
  implicit class CqlStrings(val context: StringContext) extends AnyVal {
    def cql(args: Any*)(implicit session: Session): Future[PreparedStatement] = {
      val statement = new SimpleStatement(context.raw(args: _*))
      session.prepareAsync(statement)
    }
  }

  /**
    * Execute PreparedStatement
    *
    * @param statement Future[PreparedStatement], params Any*
    * @return Future[ResultSet]
    **/
  def execute(statement: Future[PreparedStatement], pageSize: Int, params: Any*)(implicit executionContext: ExecutionContext, session: Session): Future[ResultSet] =
    for {
      ps <- statement
      bs = ps.bind(params.map(_.asInstanceOf[Object]): _* )
      rs <- session.executeAsync(bs.setFetchSize(pageSize))
    } yield rs

  /**
    * @param resultSet ResultSet
    * @return Future[ResultSet]
    **/
  def fetchMoreResults(resultSet: ResultSet)(implicit executionContext: ExecutionContext, session: Session): Future[ResultSet] =
    if (resultSet.isFullyFetched) {
      Future.failed(new NoSuchElementException("No more results to fetch"))
    } else {
      resultSet.fetchMoreResults()
    }

  /**
    * @param cql Future[PreparedStatement]
    * @param page Int
    * @param parameters Any*
    * @return Observable[Row]
    * */
  def query(cql: Future[PreparedStatement], page: Int, parameters: Any*)(implicit executionContext: ExecutionContext, cassandraSession: Session): Observable[Row] = {
    def fetchRows(nextResultSet: Future[ResultSet]): Task[(Iterable[Row], Future[ResultSet])] =
      Task.fromFuture(nextResultSet).flatMap {
        case resultSet if resultSet.isExhausted => Task.never
        case resultSet =>
          val rows = (1 to resultSet.getAvailableWithoutFetching) map (_ => resultSet.one)
          Task((rows, resultSet.fetchMoreResults))
      }

    val observable = Observable.fromAsyncStateAction[Future[ResultSet], Iterable[Row]](
      fetchRows
    )(execute(cql, page, parameters: _*))

    observable.flatMap(Observable.fromIterable)
  }

  /**
    * Convert a ListenableFuture into a Future by means of a Promise.
    * The idea is to complete the promise from the callback of the ListenableFuture and return the Future of the Promise.
    *
    * @param listenableFuture ListenableFuture[T]
    * @return Future[T]
    **/
  implicit def listenableFutureToFuture[T](listenableFuture: ListenableFuture[T]): Future[T] = {
    val promise = Promise[T]()
    Futures.addCallback(listenableFuture, new FutureCallback[T] {
      def onFailure(error: Throwable): Unit = {
        promise.failure(error)
        ()
      }

      def onSuccess(result: T): Unit = {
        promise.success(result)
        ()
      }
    })
    promise.future
  }

  def executeQuery(queryString: String, page: Int, args: Any*): Observable[Row] = {
    query(cql"$queryString"(CassandraSession.getCassandraSession), page, args: _*)(global, CassandraSession.getCassandraSession)
  }

}

