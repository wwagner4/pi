package pi

import pi.ColorIterator.colorHue
import pi.Util as PiUtil
import scopt.OParser

import java.awt.Color

object Main {

  sealed trait Command

  case class DrawCommand(id: String, tiles: Boolean, thumbnails: Boolean) extends Command

  case object TryoutCommand extends Command

  case object HpCommand extends Command

  case object ErrorCommand extends Command

  case object ServerCommand extends Command

  def parse(args: Array[String]): Command = {

    def table(selectables: Seq[Config.IdDescriptionPair], withHeader: Boolean) = {
      val headerId = "id"
      val headerDescription = "description"

      def maxlen(strings: Iterable[String]): Int = strings.map(_.length).max

      def line(len: Int): String = Seq.fill(len)('-').mkString

      val lenId = math.max(maxlen(selectables.map(_.id)), headerId.length)
      val lenDesc = math.max(maxlen(selectables.map(_.description)), headerDescription.length)
      if withHeader then {
        val formatString = s"%-${lenId}s | %-${lenDesc}s"
        val header = Seq(formatString.format("id", "description"))
        val sepa = Seq(line(lenId + 1) + "+" + line(lenDesc + 1))
        val lines = selectables.map(e => formatString.format(e.id, e.description))
        (sepa ++ header ++ sepa ++ lines).mkString("\n")
      } else {
        val formatString = s"%-${lenId}s : %-${lenDesc}s"
        val lines = selectables.map(e => formatString.format(e.id, e.description))
        lines.mkString("\n")
      }
    }


    val builder = OParser.builder[Command]
    val parser = {
      import builder.*
      OParser.sequence(
        programName("pi-hilbert"),
        head("Represent pi as a hilbert curve"),
        cmd("tryout")
          .text("To be used during development. Call a function in Tryout")
          .action((_, _) => TryoutCommand
          ),
        cmd("hp")
          .text("Create the 'pi' homepage")
          .action((_, _) => HpCommand
          ),
        cmd("server")
          .text("Starts the web control panal on port 8080")
          .action((_, _) => ServerCommand
          ),
        cmd("draw")
          .text("Draw a hilbert curve")
          .action((_, c) => DrawCommand(id = "", tiles = false, thumbnails = false))
          .children(
            opt[Unit]('t', "tiles")
              .text("Creates tiles from the drawing")
              .action((_, c) => {
                c.asInstanceOf[DrawCommand].copy(tiles = true)
              }),
            opt[Unit]("thumbnails")
              .text("Creates thumbnails")
              .action((_, c) => {
                c.asInstanceOf[DrawCommand].copy(thumbnails = true)
              }),
            opt[String]("id")
              .required()
              .text(s"Draws a configuratin for a hilbert curve. Valid configurations are:\n${table(Config.cfgs, true)}")
              .validate { x =>
                val ids = Config.cfgs.map(_.id)
                if ids.contains(x) then success
                else {
                  val idsStr = ids.mkString(", ")
                  failure(s"id must be one of: $idsStr")
                }
              }
              .action((x, c) => {
                c.asInstanceOf[DrawCommand].copy(id = x)
              }),
          ),
        help('h', "help").text("prints this usage text"),
      )
    }

    OParser.parse(parser, args, TryoutCommand) match {
      case Some(config) => config
      case _ => ErrorCommand
    }
  }

  def main(args: Array[String]): Unit = {

    val command: Command = parse(args)

    command match {
      case DrawCommand(id, tiles, thumbnails) =>
        val cfg = Config.cfgs.filter(_.id == id).head
        if tiles then Tiles.run(cfg)
        else Drawing.run(cfg, thumbnails)
      case TryoutCommand => Tryout.doIt()
      case HpCommand => Hp.create()
      case ServerCommand => {
        CaskServer().run()
      }
      case ErrorCommand => // Nothing to do
    }
  }
}
