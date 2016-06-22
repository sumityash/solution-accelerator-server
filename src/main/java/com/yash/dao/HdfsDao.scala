package com.yash.dao

import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }
import com.yash.utilities.ConnectionManager
import com.yash.model.HDFS

object HdfsDao {

  def getUserHDFSConfiguration(userId: String): HDFS =
    {
      var hdfsSource: HDFS = null
      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM hdfsConfiguration where userId=" + userId)

      try {
        while (resultSet.next()) {
          val hdfsLocation = resultSet.getString("hdfsLocation")
          val userId = resultSet.getInt("userId")

          hdfsSource = new HDFS(hdfsLocation, userId)
        }
      } catch {
        case t: Throwable => hdfsSource
      }
      hdfsSource
    }

  def insertUserHDFSConfiguration(hdfsSource: HDFS) =
    {

      try {
        val statement = ConnectionManager.provideConnection().createStatement()
        val resultSet = statement.executeQuery(("select * from hdfsConfiguration where userId = '" + hdfsSource.userId + "' "))
        if (resultSet.next()) {
          statement.executeUpdate("UPDATE hdfsConfiguration SET hdfsLocation = '" + hdfsSource.hdfsLocation + "' " + "WHERE userId = '" + hdfsSource.userId + "' ");
          System.out.println("updated in AWSConfig database...");
          "updated in given database..."
        } else {

          val prep = ConnectionManager.provideConnection().prepareStatement("INSERT INTO hdfsConfiguration VALUES (?, ?)")
          prep.setString(1, hdfsSource.hdfsLocation)
          prep.setInt(2, hdfsSource.userId)
          prep.executeUpdate()
          System.out.println("inserted in HDFS Configuration database...");
          "HDFS Configuration's Saved"
        }
      } catch {
        case t: Throwable => "Problem Occurred While Saving HDFS Configuration"
      }
    }

}


