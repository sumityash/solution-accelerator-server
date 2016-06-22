package com.yash.service

import com.yash.dao.AWSDao._
import com.google.gson.Gson
import com.yash.model.AWS

object AwsService {
  
  val gson=new Gson
  
  def fetchAWSConfigurationService(userId:String)={
      gson.toJson(getUserAWSConfiguration(userId))
   
     
    }
    
  def insertAWSConfigurationService(awsSource: AWS)={
     insertUserAWSConfiguration(awsSource)
    }
  
}