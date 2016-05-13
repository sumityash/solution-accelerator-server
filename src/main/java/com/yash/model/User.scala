package com.yash.model
import spray.json.DefaultJsonProtocol


case class User (userId:Int,name:String,emailId:String,contactNumber:Long,companyName:String,password:String)
case class UserForReg(name:String,emailId:String,contactNumber:Long,companyName:String,password:String)
  
object UserJsonImplicits  extends  DefaultJsonProtocol
{
  implicit val userSourceFormat= jsonFormat6(User.apply)
  implicit val userRegSourceFormat= jsonFormat5(UserForReg.apply)
}
