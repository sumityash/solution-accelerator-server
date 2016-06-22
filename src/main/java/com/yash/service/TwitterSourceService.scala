package com.yash.service

import org.apache.spark.streaming.twitter.TwitterUtils
import com.yash.model.{Twitter,AWS}
import com.yash.dao.{DbDao}
import com.yash.dao.TwitterDao._
import com.google.gson.Gson


object TwitterSourceService {
  
    val gson=new Gson
  
  /*def process(twitterSource:Twitter,awsSource: AWS) = {
    System.setProperty("twitter4j.oauth.consumerKey", twitterSource.consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", twitterSource.consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", twitterSource.accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", twitterSource.accessTokenSecret)
     getTwitterData(twitterSource,awsSource)
    "Twitter Streaming started"
  }*/
  
  def fetchUserTwitterConfigurationService(userId:String)={
      gson.toJson(getUserTwitterConfiguration(userId))
  
    }
 
   
   def insertTwitterConfigurationService(twitterSource: Twitter)={
    insertUserTwitterConfigurations(twitterSource)
    }


}