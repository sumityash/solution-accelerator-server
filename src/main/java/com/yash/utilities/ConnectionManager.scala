package com.yash.utilities

import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }

object ConnectionManager {

  var databaseUrl: String = _
  var databaseUsername: String = _
  var databasePassword: String = _
  val driver = "com.mysql.jdbc.Driver"
  var connection: Connection = null

  def provideConnection(): Connection = {
    if(connection==null)
    {
    Class.forName(driver)
    connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword)
    return connection
    }else
    {
      return connection;
    }
  }

}