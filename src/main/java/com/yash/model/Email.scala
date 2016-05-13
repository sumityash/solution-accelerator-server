package com.yash.model

import spray.json.DefaultJsonProtocol

case class Email(mailHost:String,mailPort:String,mailUsername:String,mailPassword:String,mailFolder:String,isExchangeServer:String,userId:Int,aliasName:String)

object EmailJsonImplicits  extends DefaultJsonProtocol {
  implicit val emailFormat=jsonFormat8(Email.apply) 
}