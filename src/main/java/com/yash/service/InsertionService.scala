package com.yash.service

import com.yash.model.Combined

object InsertionService {

  def checkSource(combinedObject: Combined) = {

    if (combinedObject.database != null && combinedObject.email == null && combinedObject.twitter == null)
    {
      println("Inserting Database Configuration "+combinedObject.database.hostIp)
      DbSourceService.insertUserDatabaseConfigurationService(combinedObject.database)
    }
    else if (combinedObject.database == null && combinedObject.email != null && combinedObject.twitter == null)
    {
      MailSourceService.insertEmailConfigurationService(combinedObject.email)
    }
    else if (combinedObject.database == null && combinedObject.email == null && combinedObject.twitter != null)
    {
      println("Inserting Twitter Config "+combinedObject.twitter.accessToken)
      TwitterSourceService.insertTwitterConfigurationService(combinedObject.twitter)
    }
    if (combinedObject.aws == null && combinedObject.hdfs != null)
    {
      println("Inserting HDFS Config "+combinedObject.hdfs.hdfsLocation)
      HdfsService.insertHDFSConfigurationService(combinedObject.hdfs)
    }
    else if (combinedObject.aws != null && combinedObject.hdfs == null)
    {
      println("Inserting AWS Config "+combinedObject.aws.s3Location)
      AwsService.insertAWSConfigurationService(combinedObject.aws)
    } 
 
    println("Inserting Cluster Config "+combinedObject.cluster.masterNodeUrl)
    ClusterService.insertClusterConfigurationService(combinedObject.cluster)

  }

}