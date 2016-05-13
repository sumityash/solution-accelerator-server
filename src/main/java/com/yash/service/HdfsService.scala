package com.yash.service

import com.yash.model.HDFS
import com.google.gson.Gson
import com.yash.dao.HdfsDao._

object HdfsService {
  
  val gson=new Gson
  
  def fetchHDFSConfigurationService(userId:String)={
      gson.toJson(getUserHDFSConfiguration(userId))
    }
    
  def insertHDFSConfigurationService(hdfsSource: HDFS)={
     insertUserHDFSConfiguration(hdfsSource)
    }
}