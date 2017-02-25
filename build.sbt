name := "spark-api"

version := "0.2"

organization := "xyz.joaovasques"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.11"

val json = "3.4.2"

val circeVersion = "0.7.0"
val catsVersion = "0.9.0"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(

  // Akka
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,

  // HTPP
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,


  // Test
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "org.scalatest"     %% "scalatest"         % "3.0.1" % "test",

  // Functional
  "org.typelevel"     %% "cats-core" % catsVersion,

  //JSON
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion

)

parallelExecution in Test := false

// Publishing

licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))

bintrayPackageLabels := Seq("Apache Spark", "REST", "Akka", "Scala")
