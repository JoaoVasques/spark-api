name := """spark-api"""

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.11"

val json = "3.4.2"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
  "org.scalatest"     %% "scalatest"         % "3.0.1" % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "org.json4s" %% "json4s-native" % json
)

parallelExecution in Test := false
