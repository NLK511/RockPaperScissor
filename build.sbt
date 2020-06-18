name := "RockPaperScissor"

version := "0.1"

scalaVersion := Versions.scala
autoScalaLibrary := false

ThisBuild / useCoursier := false

resolvers := Seq("sgodbillon" at "https://bitbucket.org/sgodbillon/repository/raw/master/snapshots/",
  "Typesafe backup repo" at "https://repo.typesafe.com/typesafe/repo/",
  "Maven repo1" at "https://repo1.maven.org/")


//libraryDependencies += "com.typesafe" % "config" % Versions.typeSafeConfig
libraryDependencies += "org.scalatest" %% "scalatest" % Versions.scalatest % "test"
libraryDependencies += "org.scala-lang" % "scala-library" % Versions.scala
