package models.db

import java.util.Date

/**
  * Created by pozpl on 03.04.17.
  */
case class Message(id:Option[Long], fromUid:String, toSegment: String, toTarget: Option[String], dateTime: Date, text:String)
