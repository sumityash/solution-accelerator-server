package com.yash.service

import com.google.gson.Gson
import com.yash.dao.ClusterDao._
import com.yash.model.Cluster

object ClusterService {
  
    val gson=new Gson
  
  def fetchClusterConfigurationService(userId:String)={
      gson.toJson(getUserClusterConfiguration(userId))
      
     
    }
    
  def insertClusterConfigurationService(clusterSource: Cluster)={
    insertUserClusterConfiguration(clusterSource)
    }
  
}
  