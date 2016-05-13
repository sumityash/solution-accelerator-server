package com.yash.dao

import com.yash.model.Cluster
import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }
import com.yash.utilities.ConnectionManager

object ClusterDao {
  var clusterSource: Cluster = null

  def getUserClusterConfiguration(userId: String) =
    {

      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM ClusterConfiguration where userId=" + userId)

      try {
        while (resultSet.next()) {
          val masterNodeUrl = resultSet.getString("masterNodeUrl")
          val userId = resultSet.getInt("userId")
          val aliasName = resultSet.getString("aliasName")

          println("userId : " + userId)
          clusterSource = new Cluster(masterNodeUrl, userId, aliasName);
        }
      } catch {
        case t: Throwable => clusterSource
      }
      System.out.println("fetching CLUSTER CONFIGURATIONS...");
      clusterSource
    }

  def insertUserClusterConfiguration(clusterSource: Cluster) =
    {

      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery(("select * from ClusterConfiguration where aliasName = '" + clusterSource.aliasName + "' "))
      if (resultSet.next()) {
        statement.executeUpdate("UPDATE ClusterConfiguration SET masterNodeUrl = '" + clusterSource.masterNodeUrl + "' " + "WHERE aliasName = '" + clusterSource.aliasName + "' ");
        System.out.println("Updated in CLUSTER CONFIGURATION database...");
        "updated in given database..."
      } else {

        val prep = ConnectionManager.provideConnection().prepareStatement("INSERT INTO ClusterConfiguration VALUES ( ?, ?, ?)")
        prep.setString(1, clusterSource.masterNodeUrl)
        prep.setInt(2, clusterSource.userId)
        prep.setString(3, clusterSource.aliasName)

        prep.executeUpdate()
        System.out.println("inserted in CLUSTER CONFIGURATION database...");
        "inserted in given database..."
      }

    }

}