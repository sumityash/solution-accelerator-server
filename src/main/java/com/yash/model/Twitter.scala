package com.yash.model
import spray.json.DefaultJsonProtocol

/**
 * case is class which includes setter and getter methods of pojo class
 * It automatically set the value of variables
 */
case class Twitter(consumerKey:String,consumerSecret:String,accessToken:String,accessTokenSecret:String,batchingInterval:Long,keyword:Array[String],userId:Int,aliasName:String)
case class TwitterDB(consumerKey:String,consumerSecret:String,accessToken:String,accessTokenSecret:String,batchingInterval:Long,userId:Int,aliasName:String)
/**
 * JsonImplicits is use to unmarshal and marshal the object
 */
object TwitterJsonImplicits extends  DefaultJsonProtocol
{
  implicit val twitterFormat=jsonFormat8(Twitter.apply)
  implicit val twitterDBFormat=jsonFormat7(TwitterDB.apply)
}

