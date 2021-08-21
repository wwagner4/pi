lazy val root = project
  .in(file("."))
  .settings(
    name := "pi",
    version := "0.1.0",

    scalaVersion := "3.0.0",

    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test",
    libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.32",
    libraryDependencies += "com.lihaoyi" %% "upickle" % "1.4.0",
    libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.3",
    libraryDependencies += "org.rogach" %% "scallop" % "4.0.4",
    libraryDependencies += "org.imgscalr" % "imgscalr-lib" % "4.2"
  )
