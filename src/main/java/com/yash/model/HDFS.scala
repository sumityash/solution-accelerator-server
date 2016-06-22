package com.yash.model

import spray.json.DefaultJsonProtocol

case class HDFS(hdfsLocation: String, userId: Int)
object HDFSJsonImplicits extends DefaultJsonProtocol {
  
  implicit val awsSourceFormat = jsonFormat2(HDFS.apply)

}

