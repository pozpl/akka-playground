logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.13")
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.8")
addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.7")