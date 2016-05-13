package com.yash.dao

import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }
import com.yash.utilities.ConnectionManager
import com.yash.model.HDFS

object HdfsDao {

  var hdfsSource: HDFS = null

  def getUserHDFSConfiguration(userId: String) =
    {
      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM hdfsConfiguration where userId=" +userId)

      try {
        while (resultSet.next()) {
          val hdfsLocation = resultSet.getString("hdfsLocation")
          val userId = resultSet.getInt("userId")
          val aliasName = resultSet.getString("aliasName")

          hdfsSource = new HDFS(hdfsLocation, userId, aliasName)
        }
      } catch {
        case t: Throwable => hdfsSource
      }

      System.out.println("fetching HDFS CONFIGURATIONS...");
      hdfsSource
    }

  def insertUserHDFSConfiguration(hdfsSource: HDFS) =
    {

      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery(("select * from hdfsConfiguration where aliasName = '" + hdfsSource.aliasName + "' "))
      if (resultSet.next()) {
        statement.executeUpdate("UPDATE hdfsConfiguration SET hdfsLocation = '" + hdfsSource.hdfsLocation + "' " + "WHERE aliasName = '" + hdfsSource.aliasName + "' ");
        System.out.println("updated in AWSConfig database...");
        "updated in given database..."
      } else {

        val prep = ConnectionManager.provideConnection().prepareStatement("INSERT INTO hdfsConfiguration VALUES (?, ?, ?)")
        prep.setString(1, hdfsSource.hdfsLocation)
        prep.setInt(2, hdfsSource.userId)
        prep.setString(3, hdfsSource.aliasName)
        prep.executeUpdate()
        System.out.println("inserted in HDFS Configuration database...");
        "inserted in HDFS database..."
      }
    }

}


