import com.typesafe.config.Config

class AppContext(val appConf: Config) {
  /** This config values are mandatory so I decided not to handle the case in which the conf file does not specify them.
   * If these field were optional, or we would like to use default values I would have implemented a fail-safe system using
   * scala options
   * */

  val mode: Mode = appConf.getString("application.mode") match {
    case "cpu" | "c" => Cpu
    case "single" | "s" => Single
    case "multi" | "m" => Multi
  }
}

sealed trait Mode

case object Cpu extends Mode

case object Single extends Mode

case object Multi extends Mode