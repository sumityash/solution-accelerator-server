package com.yash.model

import spray.json.DefaultJsonProtocol

/**
 * @author dishant.mishra
 * DbSource class is working here as a model
 */
case class Database(driver:String,hostIp:String,username:String,password:String,userId:Int,aliasName:String)

/**JsonImplicits object is to provide marshalling  and unmarshalling of Employee object in Json format*/
object DbJsonImplicits  extends  DefaultJsonProtocol
{
  implicit val dbSourceFormat= jsonFormat6(Database.apply)
}