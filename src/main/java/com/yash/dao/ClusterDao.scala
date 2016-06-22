package com.yash.dao

import com.yash.model.Cluster
import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }
import com.yash.utilities.ConnectionManager

object ClusterDao {

  def getUserClusterConfiguration(userId: String):Cluster =
    {
      var clusterSource: Cluster = null
      println("User id received " + userId)
      try {
        val statement = ConnectionManager.provideConnection().createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM ClusterConfiguration where userId=" + userId)
        println("Query for cluster " + resultSet)
       
        while (resultSet.next()) {
          val masterNodeUrl = resultSet.getString("masterNodeUrl")
          val userId = resultSet.getInt("userId")
          println("userId : " + userId)
          clusterSource = new Cluster(masterNodeUrl, userId);
        }
      } catch {
        case t: Throwable => clusterSource
      }
      clusterSource
    }

  def insertUserClusterConfiguration(clusterSource: Cluster) =
    {

      try {
        val statement = ConnectionManager.provideConnection().createStatement()
        val resultSet = statement.executeQuery(("select * from ClusterConfiguration where userId = '" + clusterSource.userId + "' "))
        if (resultSet.next()) {
          statement.executeUpdate("UPDATE ClusterConfiguration SET masterNodeUrl = '" + clusterSource.masterNodeUrl + "' " + "WHERE userId = '" + clusterSource.userId + "' ");
          System.out.println("Updated in CLUSTER CONFIGURATION database...");
          "updated in given database..."
        } else {

          val prep = ConnectionManager.provideConnection().prepareStatement("INSERT INTO ClusterConfiguration VALUES ( ?, ?)")
          prep.setString(1, clusterSource.masterNodeUrl)
          prep.setInt(2, clusterSource.userId)
          prep.executeUpdate()
          "Cluster Configuration Saved"
        }
      } catch {
        case t: Throwable => "Problem Occurred While Saving Cluster Configuration"
      }
    }

}