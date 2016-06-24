package com.yash.dao

import spray.http.{ HttpResponse, MediaTypes }
import spray.http.StatusCodes._
import scala.util.Try
import org.apache.spark.{ SparkConf, SparkContext }
import java.util.{ HashMap, Calendar }
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{ StreamingContext, Seconds }
import com.yash.utilities.Helper._
import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }
import scala.collection.mutable.{ ListBuffer, ArrayBuffer }
import com.yash.model.{ Database, AWS, Twitter }
import com.yash.utilities.ConnectionManager

/**
 * @author dishant.mishra
 * SolutionAcceleratorDao Object contains various methods for getting data from various sources
 */
object DbDao {

  var urlWithDatabase = null
  var dataBaseList = new ArrayBuffer[String](2)
  var tablesList = new ArrayBuffer[String](2)
  var aliasList = new ArrayBuffer[String](2)
  var dbmd: DatabaseMetaData = _
  var dbURL: String = _

  var connection: Connection = null
  val driver = "com.mysql.jdbc.Driver"

  def getUserDatabaseConfiguration(userId: String): Database =
    {
      var dbSource: Database = null
      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM DbConfiguration where userId=" + userId)

      try {
        while (resultSet.next()) {
          val driver = resultSet.getString("driver")
          val hostIP = resultSet.getString("hostIp")
          val username = resultSet.getString("username")
          val userId = resultSet.getInt("userId")
          println("DB FETCHED " + driver + " " + hostIP)
          dbSource = new Database(driver, hostIP, username, userId)
        }
      } catch {
        case t: Throwable => dbSource
      }
      dbSource
    }

  def insertUserDatabaseConfiguration(dbSource: Database) =
    {
      try {
        val statement = ConnectionManager.provideConnection().createStatement()
        val resultSet = statement.executeQuery(("select * from DbConfiguration where userId = '" + dbSource.userId + "' "))
        if (resultSet.next()) {
          statement.executeUpdate("UPDATE DbConfiguration SET driver = '" + dbSource.driver + "', " + "hostIp = '" + dbSource.hostIp + "' " + "WHERE userId = '" + dbSource.userId + "' ");
          System.out.println("Updated in given database...");
          "Updated in given database..."
        } else {
          val prep = ConnectionManager.provideConnection().prepareStatement("INSERT INTO DbConfiguration VALUES (?, ?,?,?)")
          prep.setString(1, dbSource.driver)
          prep.setString(2, dbSource.hostIp)
          prep.setString(3, dbSource.username)
          prep.setInt(4, dbSource.userId)
          prep.executeUpdate()
          System.out.println("inserted in given database...");
          "inserted in given database..."
        }
      } catch {
        case t: Throwable => "Problem Occurred While Saving Database Configuration"
      }
    }

  /*
   * Logic for getting all databases from the url 
   * and then getting all tables inside the specified database
   */
  def getAllDatabases(dbUrl: String) = {
    var dataBaseList = new ArrayBuffer[String](2)
    try {
      var dbUrlList = dbUrl.split("~")
      dbURL = dbUrlList(0)
      val username = dbUrlList(1)
      val password = dbUrlList(2)
      connection = DriverManager.getConnection(dbURL, username, password)
      dbmd = connection.getMetaData

      val ctlgs: ResultSet = dbmd.getCatalogs();
      while (ctlgs.next()) {
        dataBaseList += ctlgs.getString(1)
        println("" + ctlgs.getString(1));
      }
      connection.close()
    } catch {
      case t: Throwable => t.printStackTrace() // TODO: handle error
    }

    dataBaseList.toArray
  }

  def getTablesFromDatabase(dataBaseName: String) = {
    println(dataBaseName)
    var tablesList = new ArrayBuffer[String](2)
    try {
      var dbUrlList = dataBaseName.split("~")
      val url = dbUrlList(0)
      println(url)
      val username = dbUrlList(1)
      val password = dbUrlList(2)
      connection = DriverManager.getConnection(url, username, password)
      dbmd = connection.getMetaData
      var rs: ResultSet = dbmd.getTables(null, null, "%", null)
      while (rs.next()) {
        tablesList += rs.getString(3);
      }
      connection.close()
    } catch {
      case t: Throwable => t.printStackTrace() // TODO: handle error
    }
    println("This is the Table List: "+tablesList.toArray)
    tablesList.toArray
  }

  def getAliasNames(userId: String) = {

    val statement = ConnectionManager.provideConnection().createStatement()
    val resultSet = statement.executeQuery("SELECT aliasName FROM DbConfiguration where userId=" + userId)

    while (resultSet.next()) {
      aliasList += resultSet.getString(1)
    }
    println("List Received " + aliasList)
    aliasList

  }

}