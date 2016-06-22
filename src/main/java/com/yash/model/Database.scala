package com.yash.model

import spray.json.DefaultJsonProtocol

/**
 * @author dishant.mishra
 * DbSource class is working here as a model
 */
case class Database(driver:String,hostIp:String,username:String,userId:Int)

/**JsonImplicits object is to provide marshalling  and unmarshalling of Employee object in Json format*/
object DbJsonImplicits  extends  DefaultJsonProtocol
{
  implicit val dbSourceFormat= jsonFormat4(Database.apply)
}