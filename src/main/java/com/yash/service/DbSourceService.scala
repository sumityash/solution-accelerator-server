package com.yash.service

import java.util.HashMap
import com.yash.model.{Database,AWS}
import com.yash.dao.DbDao._
import com.google.gson.Gson

object DbSourceService {
  val gson = new Gson
  /*def process(dbSource: Database,awsSource:AWS) =
    {
      val dbMap = new HashMap[String, String]()
      dbMap.put("driver", dbSource.driver)
      dbMap.put("url", dbSource.dbUrl)
      dbMap.put("dbtable", dbSource.dbtable)
      dbMap.put("partitionColumn", awsSource.partitionColumn)
      dbMap.put("lowerBound", "10001")
      dbMap.put("upperBound", "499999")
      dbMap.put("numPartitions", "10")
      getDbData(dbSource,awsSource, dbMap)
    }*/

    def fetchUserDatabaseConfigurationService(userId:String)={
      gson.toJson(getUserDatabaseConfiguration(userId))
      
     
    }
    
  def insertUserDatabaseConfigurationService(dbSource: Database)={
     insertUserDatabaseConfiguration(dbSource)
    }
  
  /*
   * Service for getting all databases and tables inside the specific database
   */
  def getDatabase(dbUrl: String) =
    {
      var databaseList = getAllDatabases(dbUrl)
      if (databaseList.isEmpty)
        "CONNECTION NOT ESTABLISHED WITH PROVIDED URL"
      else
        gson.toJson(getAllDatabases(dbUrl))
    }
  
  def getTables(databaseName: String) =
    {
    var tablesList = getTablesFromDatabase(databaseName)
      if (tablesList.isEmpty)
        "No Tables Present"
      else
        println("This is the json Table List: "+gson.toJson(getTablesFromDatabase(databaseName)))
      gson.toJson(getTablesFromDatabase(databaseName))

    }
  
 /* def getAliasList(userId: String) =
    {
    var tablesList = getAliasNames(userId)
      if (tablesList.isEmpty)
        "No Tables Present"
      else
      gson.toJson(getTablesFromDatabase(databaseName))

    }*/
  
  
}