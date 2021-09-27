package pi

import java.io.{BufferedReader, InputStream, InputStreamReader}
import java.util.concurrent.{ExecutorService, Executors}
import collection.JavaConverters.seqAsJavaListConverter

object EasyExec {

  def runAllCommands(cmds: Iterable[Iterable[String]]): Unit = {

    val allCnt = cmds.size

    class StreamGobbler(val name: String, val inputStream: InputStream) extends Runnable {
      private def handleInputStream(in: InputStream): Unit = {

        def handle(cnt: Int): Unit = {
          val br = new BufferedReader(new InputStreamReader(in))
          try {
            br.lines().forEach { line =>
              val msg = if cnt > 0 then s"ERROR occurred $cnt $name - $line" else s"$name - $line"
              println(msg)
            }
            br.close()
          } catch {
            case e: Exception =>
              try {
                println(s"ERROR ${cnt} reading process $name stream")
                e.printStackTrace
                br.close()
              } finally {
                handle(cnt + 1)
              }
          }
        }

        handle(0)
      }

      override def run(): Unit = {
        handleInputStream(inputStream)
      }

    }

    val gobbleExec: ExecutorService = Executors.newFixedThreadPool(2)
    val procExec: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors)
    try {
      def start(cmd: List[String], cnt: Int): Int = {
        val process = new ProcessBuilder()
          .command(cmd.asJava)
          .start()

        val inputGobbler = StreamGobbler("", process.getInputStream())
        gobbleExec.submit(inputGobbler)
        val errorGobbler = StreamGobbler("", process.getErrorStream())
        gobbleExec.submit(errorGobbler)
        println(s"started command $cnt of $allCnt - " + cmd.mkString(" "))
        val procResult = process.waitFor()
        println(s"finished command $cnt of $allCnt - " + cmd.mkString(" ") + " " + procResult)
        procResult
      }

      val futures = for ((cmd, i) <- cmds.zipWithIndex) yield {
        Thread.sleep(500)
        procExec.submit(() => start(cmd.toList, i + 1))
      }
      var states = futures.map(f => f.isDone)
      val sleepTimeMillis = 1000
      var timeMillis = 0
      while (!states.forall(s => s)) {
        Thread.sleep(sleepTimeMillis)
        states = futures.map(f => f.isDone)
        timeMillis += sleepTimeMillis
      }
      val exits = futures.map(f => f.get())
      println(s"finished all commands. Exit values: ${exits.mkString(",")}")
      val errros = exits.filter(_ != 0)
      if errros.nonEmpty then throw IllegalStateException("At least one of the processes finiched with error")
    } finally {
      gobbleExec.shutdownNow()
      procExec.shutdownNow()
    }
  }



}
