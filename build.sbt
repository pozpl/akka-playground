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
    "com.iheart" %% "ficus" % "1.4.0",

    // WebJars (i.e. client-side) dependencies
    "org.webjars" %% "webjars-play" % "2.5.0",

    "org.webjars" % "requirejs" % "2.3.2",
    "org.webjars" % "underscorejs" % "1.8.3",
    "org.webjars" % "jquery" % "3.2.0",
    "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery"),
    "org.webjars" % "angularjs" % "1.4.10" exclude("org.webjars", "jquery"),
    "org.webjars" % "angular-ui-router" % "0.2.15",
    "org.webjars" % "angular-sanitize" % "1.3.11",
    "org.webjars" % "html5shiv" % "3.7.0",
    "org.webjars" % "respond" % "1.4.2",
    "org.webjars" % "momentjs" % "2.18.1",
    "com.adrianhurt" %% "play-bootstrap" % "1.1.1-P25-B3-SNAPSHOT",
    "org.webjars.bower" % "compass-mixins" % "0.12.7"
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers := ("Atlassian Releases" at "https://maven.atlassian.com/public/") +: resolvers.value

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

routesGenerator := InjectedRoutesGenerator

// Scala Compiler Options
//scalacOptions in ThisBuild ++= Seq(
//    "-target:jvm-1.8",
//    "-encoding", "UTF-8",
//    "-deprecation", // warning and location for usages of deprecated APIs
//    "-feature", // warning and location for usages of features that should be imported explicitly
//    "-unchecked", // additional warnings where generated code depends on assumptions
//    "-Xlint", // recommended additional warnings
//    "-Xcheckinit", // runtime error when a val is not initialized due to trait hierarchies (instead of NPE somewhere else)
//    "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
//    "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver
//    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
//    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures
//    "-Ywarn-dead-code", // Warn when dead code is identified
//    "-Ywarn-unused", // Warn when local and private vals, vars, defs, and types are unused
//    "-Ywarn-unused-import", //  Warn when imports are unused (don't want IntelliJ to do it automatically)
//    "-Ywarn-numeric-widen" // Warn when numerics are widened
//)

//
// sbt-web configuration
// https://github.com/sbt/sbt-web
//

// Configure the steps of the asset pipeline (used in stage and dist tasks)
// rjs = RequireJS, uglifies, shrinks to one file, replaces WebJars with CDN
// digest = Adds hash to filename
// gzip = Zips all assets, Asset controller serves them automatically when client accepts them
pipelineStages := Seq(rjs)

// RequireJS with sbt-rjs (https://github.com/sbt/sbt-rjs#sbt-rjs)
// ~~~
//RjsKeys.paths += ("jsRoutes" -> ("/jsroutes" -> "empty:"))

//RjsKeys.mainModule := "main"

// Asset hashing with sbt-digest (https://github.com/sbt/sbt-digest)
// ~~~
// md5 | sha1
//DigestKeys.algorithms := "md5"
//includeFilter in digest := "..."
//excludeFilter in digest := "..."

// HTTP compression with sbt-gzip (https://github.com/sbt/sbt-gzip)
// ~~~
// includeFilter in GzipKeys.compress := "*.html" || "*.css" || "*.js"
// excludeFilter in GzipKeys.compress := "..."

// JavaScript linting with sbt-jshint (https://github.com/sbt/sbt-jshint)
// ~~~
// JshintKeys.config := ".jshintrc"

// All work and no play...
//emojiLogs