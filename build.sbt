lazy val akkaHttpVersion = "10.2.10"
lazy val akkaVersion    = "2.6.20"

fork := true

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.13.4"
    )),
    name := "Scala Betting",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"     %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka"     %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka"     %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka"     %% "akka-stream"              % akkaVersion,
      "com.typesafe.akka"     %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka"     %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"         %% "scalatest"                % "3.2.14"         % Test,
      "com.couchbase.client"  %% "scala-client"             % "1.3.4",
      "io.circe" %% "circe-generic" % "0.14.3",
      "io.circe" %% "circe-core" % "0.14.3",
      "io.circe" %% "circe-parser" % "0.14.3"

))
