package com.yash.model

import spray.json.DefaultJsonProtocol

case class HDFS(hdfsLocation: String, userId: Int, aliasName: String)
object HDFSJsonImplicits extends DefaultJsonProtocol {
  
  implicit val awsSourceFormat = jsonFormat3(HDFS.apply)

}

