name := "PlayChat"

version := "1.0"

lazy val `playchat` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
    //    jdbc ,
    cache, ws, specs2 % Test,
    evolutions, filters,
    //    "com.typesafe.slick" %% "slick" % "3.2.0",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    //    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
    "org.scalaz" %% "scalaz-core" % "7.2.10",
    "mysql" % "mysql-connector-java" % "5.1.34",
    "org.scala-stm" %% "scala-stm" % "0.7",
    "com.typesafe.play" %% "play-slick" % "2.0.0",
    "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
    "com.mohiva" %% "play-silhouette" % "4.0.0",
    "com.mohiva" %% "play-silhouette-password-bcrypt" % "4.0.0",
    "com.mohiva" %% "play-silhouette-crypto-jca" % "4.0.0",
    "com.mohiva" %% "play-silhouette-persistence" % "4.0.0",
    "com.mohiva" %% "play-silhouette-testkit" % "4.0.0" % "test",
    "net.codingwell" %% "scala-guice" % "4.1.0",
    "org.webjars" %% "webjars-play" % "2.5.0",
    "org.webjars" % "bootstrap" % "3.3.7",
    "com.adrianhurt" %% "play-bootstrap" % "1.1.1-P25-B3-SNAPSHOT",
    "org.webjars" % "html5shiv" % "3.7.0",
    "org.webjars" % "respond" % "1.4.2",
    "com.iheart" %% "ficus" % "1.4.0"
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers := ("Atlassian Releases" at "https://maven.atlassian.com/public/") +: resolvers.value

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

routesGenerator := InjectedRoutesGenerator