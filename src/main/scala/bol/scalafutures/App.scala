package bol.scalafutures

import java.util.concurrent.Executors

import org.slf4j.LoggerFactory

//import scala.concurrent.ExecutionContext.Implicits.global //ForkJoin Pool
import scala.concurrent._
import scala.concurrent.duration._

object App {

  private val pool = Executors.newFixedThreadPool(12)
//  private val pool = Executors.newCachedThreadPool()
   implicit  val ec = ExecutionContext.fromExecutorService(pool)

  private val LOG = LoggerFactory.getLogger("App")

  def startTask(number: Int): Future[Unit] = Future{
//    blocking // max 256 threads!
    {
      LOG.info(s"Starting task#$number")
      Thread.sleep(2000)
      LOG.info(s"Finished task#$number")
    }
  }

  def main(args: Array[String]): Unit = {
    LOG.info(s"Available processors: ${Runtime.getRuntime.availableProcessors}")
    val tasks = Future.traverse(1 to 20)(startTask)
    LOG.info("Continuing Main")
    // waits for all tasks to complete before exiting
    Await.result(tasks, 5 seconds)
    LOG.info("Finishing Main")

    // @PredDestroy
    pool.shutdown()
  }
}
