package com.yash.dao

import org.apache.spark.{ SparkContext, SparkConf }
import org.apache.spark.streaming.{ Seconds, StreamingContext }
import org.apache.spark.streaming.twitter.TwitterUtils
import com.yash.utilities.Helper._
import com.yash.model.{ AWS, Twitter }
import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }
import com.yash.model.TwitterDB
import com.yash.utilities.ConnectionManager

object TwitterDao {
  var twitterSource: TwitterDB = null

  def getTwitterData(twitterSource: Twitter, awsSource: AWS) =
    {
      val sparkConf = new SparkConf().setAppName("SparkTwitterSource").setMaster("local[*]").set("spark.driver.allowMultipleContexts", "true")
      val sc = new SparkContext(sparkConf)
      val ssc = new StreamingContext(sparkConf, Seconds(twitterSource.batchingInterval))
      ssc.sparkContext.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", awsSource.awsAccessKey);
      ssc.sparkContext.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", awsSource.awsSecretKey);
      val stream = TwitterUtils.createStream(ssc, None, twitterSource.keyword)
      stream.repartition(1).foreachRDD { x =>
        if (!x.isEmpty()) {
          x.saveAsTextFile(getPrefix(awsSource))
        }
      }
      ssc.start()
      ssc.awaitTermination()
    }

  def getUserTwitterConfiguration(userId: String) =
    {

      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM TwitterConfiguration where userId=" + userId)

      try {
        while (resultSet.next()) {
          val consumerKey = resultSet.getString("consumerKey")
          val consumerSecret = resultSet.getString("consumerSecret")
          val accessToken = resultSet.getString("accessToken")
          val accessTokenSecret = resultSet.getString("accessTokenSecret")
          val batchingInterval = resultSet.getLong("batchingInterval")
          val userId = resultSet.getInt("userId")
          val aliasName = resultSet.getString("aliasName")
          println("consumerKey : " + consumerKey)
          println("userId : " + userId)
          println("aliasName : " + aliasName)
          twitterSource = new TwitterDB(consumerKey, consumerSecret, accessToken, accessTokenSecret, batchingInterval, userId, aliasName)
        }

      } catch {
        case t: Throwable => twitterSource // TODO: handle error
      }

      System.out.println("Fetched Data from twitter " + twitterSource);
      twitterSource
    }

  def insertUserTwitterConfigurations(twitterSource: Twitter) = {

    val statement = ConnectionManager.provideConnection().createStatement()

    val resultSet = statement.executeQuery(("select * from TwitterConfiguration where aliasName = '" + twitterSource.aliasName + "' "))
    if (resultSet.next()) {
      statement.executeUpdate("UPDATE twitterconfiguration SET consumerKey = '" + twitterSource.consumerKey + "', " + "consumerSecret = '" + twitterSource.consumerSecret + "', " + "accessToken = '" + twitterSource.accessToken + "', " + "accessTokenSecret = '" + twitterSource.accessTokenSecret + "', " + "batchingInterval = '" + twitterSource.batchingInterval + "' " + "WHERE aliasName = '" + twitterSource.aliasName + "' ");
      System.out.println("Updated in email database...");
      "updated in email database..."
    } else {

      val prep = ConnectionManager.provideConnection().prepareStatement("INSERT INTO TwitterConfiguration VALUES (?, ?, ?, ?, ?, ?, ? )")
      prep.setString(1, twitterSource.consumerKey)
      prep.setString(2, twitterSource.consumerSecret)
      prep.setString(3, twitterSource.accessToken)
      prep.setString(4, twitterSource.accessTokenSecret)
      prep.setLong(5, twitterSource.batchingInterval)
      prep.setInt(6, twitterSource.userId)
      prep.setString(7, twitterSource.aliasName)
      prep.executeUpdate()
      "DATA INSERTED IN EMAIL DATABASE"
    }

  }
}