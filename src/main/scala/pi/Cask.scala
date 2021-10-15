package pi

import cask.main.{Main, Routes}
import cask.router.{Decorator, EndpointMetadata}
import geny.{Internal, Writable}
import io.undertow.Undertow
import io.undertow.server.handlers.BlockingHandler

import java.io.OutputStream
import java.nio.charset.StandardCharsets

object MinimalRoutes extends cask.MainRoutes {

  class HtmlWritable(s: String) extends Writable {
    def writeBytesTo(out: OutputStream): Unit = {
      s.grouped(8192).foreach(ss => out.write(ss.getBytes(StandardCharsets.UTF_8)))
    }

    override def httpContentType = Some("text/html; charset=utf-8")

    override def contentLength = Some(Internal.encodedLength(s))
  }

  def response(message: Option[String]): HtmlWritable = {
    val htmlMessage = message.map(s => s"<h2>$s</h2>").getOrElse("")
    HtmlWritable(
      s"""
         |<html>
         |<head>
         |<title>pi controller</title>
         |</head>
         |<body>
         |<h1>pi control panel</h1>
         |<a href="/action1">action1</a></br>
         |<a href="/action2">action2</a></br>
         |<a href="/draw/ran1">draw ran1</a></br>
         |$htmlMessage
         |</body>
         |</html>
         |""".stripMargin)
  }

  @cask.get("/")
  def index() = response(None)

  @cask.get("/action1")
  def action1() = response(Some("action1"))

  @cask.get("/action2")
  def action2() = response(Some("action2"))

  @cask.get("/draw", subpath = true)
  def action2(request: cask.Request) = {
    val id = request.remainingPathSegments(0)
    val cfg = Config.cfgs
      .map(c => (c.id, c))
      .toMap
      .get(id)
      .getOrElse(throw IllegalArgumentException(s"Unknown draw id $id"))
    response(Some(s"draw cfg: ${cfg}"))
  }


  initialize()

}

class CaskServer {
  def mainDecorators: Seq[Decorator[_, _, _]] = Nil
  def allRoutes: Seq[Routes] = Seq(MinimalRoutes)
  def port: Int = 8080
  def host: String = "localhost"
  def verbose = false
  def debugMode: Boolean = true

  def createExecutionContext = castor.Context.Simple.executionContext
  def createActorContext = new castor.Context.Simple(executionContext, log.exception)

  val executionContext = createExecutionContext
  implicit val actorContext: castor.Context = createActorContext

  implicit def log: cask.util.Logger = new cask.util.Logger.Console()

  def routeTries = Main.prepareRouteTries(allRoutes)

  def defaultHandler = new BlockingHandler(
    new Main.DefaultHandler(routeTries, mainDecorators, debugMode, handleNotFound, handleMethodNotAllowed, handleEndpointError)
  )

  def handleNotFound() = Main.defaultHandleNotFound()

  def handleMethodNotAllowed() = Main.defaultHandleMethodNotAllowed()

  def handleEndpointError(routes: Routes,
                          metadata: EndpointMetadata[_],
                          e: cask.router.Result.Error) = {
    Main.defaultHandleError(routes, metadata, e, debugMode)
  }

  def run(): Unit = {
    if (!verbose) Main.silenceJboss()
    val server = Undertow.builder
      .addHttpListener(port, host)
      .setHandler(defaultHandler)
      .build
    server.start()
  }

}
