package com.yash.model

import spray.json.DefaultJsonProtocol

case class Email(mailHost:String,mailPort:String,mailUsername:String,mailPassword:String,mailFolder:String,isExchangeServer:String,userId:Int)

object EmailJsonImplicits  extends DefaultJsonProtocol {
  implicit val emailFormat=jsonFormat7(Email.apply) 
}