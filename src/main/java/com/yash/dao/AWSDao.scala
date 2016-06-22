package com.yash.dao

import com.yash.model.AWS
import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }
import com.yash.utilities.ConnectionManager

object AWSDao {


  def getUserAWSConfiguration(userId: String):AWS =
    {
      var awsSource: AWS = null
      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM AWSConfig where userId =" + userId)

      try {
        while (resultSet.next()) {
          val s3Location = resultSet.getString("s3Location")
          val awsAccessKey = resultSet.getString("awsAccessKey")
          val awsSecretKey = resultSet.getString("awsSecretKey")
          val userId = resultSet.getInt("userId")
          val partitionColumn = resultSet.getString("partitionColumn")

          println("s3Location : " + s3Location)
          println("awsAccessKey : " + awsAccessKey)
          println("awsSecretKey : " + awsSecretKey)
          println("userId : " + userId)
          println("partitionColumn : " + partitionColumn)

          awsSource = new AWS(s3Location, awsAccessKey, awsSecretKey, userId, partitionColumn)
        }
      } catch {
        case t: Throwable => awsSource // TODO: handle error
      }
      awsSource
    }

  def insertUserAWSConfiguration(awsSource: AWS) =
    {

      try {
        val statement = ConnectionManager.provideConnection().createStatement()
        val resultSet = statement.executeQuery(("select * from AWSConfig where userId = '" + awsSource.userId + "' "))
        if (resultSet.next()) {
          statement.executeUpdate("UPDATE AWSConfig SET s3Location = '" + awsSource.s3Location + "', " + "awsAccessKey = '" + awsSource.awsAccessKey + "', " + "awsSecretKey = '" + awsSource.awsSecretKey + "', " + "partitionColumn = '" + awsSource.partitionColumn + "' " + "WHERE userId ='" + awsSource.userId + "'");
          System.out.println("updated in AWSConfig database...");
          "updated in given database..."
        } else {

          val prep = ConnectionManager.provideConnection().prepareStatement("INSERT INTO AWSConfig VALUES (?, ?, ?, ?, ?)")
          prep.setString(1, awsSource.s3Location)
          prep.setString(2, awsSource.awsAccessKey)
          prep.setString(3, awsSource.awsSecretKey)
          prep.setInt(4, awsSource.userId)
          prep.setString(5, awsSource.partitionColumn)

          prep.executeUpdate()
          "AWS Configuration Saved"
        }
      } catch {
        case t: Throwable => "Problem Occurred While Saving AWS Configuration  " // TODO: handle error
      }
    }

}