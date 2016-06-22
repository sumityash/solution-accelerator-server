package com.yash.model
import spray.json.DefaultJsonProtocol

/**
 * case is class which includes setter and getter methods of pojo class
 * It automatically set the value of variables
 */
case class Twitter(consumerKey:String,consumerSecret:String,accessToken:String,accessTokenSecret:String,batchingInterval:Long,keyword:Array[String],userId:Int)
case class TwitterDB(consumerKey:String,consumerSecret:String,accessToken:String,accessTokenSecret:String,batchingInterval:Long,userId:Int)
/**
 * JsonImplicits is use to unmarshal and marshal the object
 */
object TwitterJsonImplicits extends  DefaultJsonProtocol
{
  implicit val twitterFormat=jsonFormat7(Twitter.apply)
  implicit val twitterDBFormat=jsonFormat6(TwitterDB.apply)
}

