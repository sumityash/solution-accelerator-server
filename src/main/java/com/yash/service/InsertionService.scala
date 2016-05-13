package com.yash.service

import com.yash.model.Combined

object InsertionService {

  def checkSource(combinedObject: Combined) = {

    if (combinedObject.database != null && combinedObject.email == null && combinedObject.twitter == null)
      DbSourceService.insertUserDatabaseConfigurationService(combinedObject.database)

    else if (combinedObject.database == null && combinedObject.email != null && combinedObject.twitter == null)
      MailSourceService.insertEmailConfigurationService(combinedObject.email)

    else if (combinedObject.database == null && combinedObject.email == null && combinedObject.twitter != null)
      TwitterSourceService.insertTwitterConfigurationService(combinedObject.twitter)

    if (combinedObject.aws == null && combinedObject.hdfs != null)
      HdfsService.insertHDFSConfigurationService(combinedObject.hdfs)
      
    else if (combinedObject.aws != null && combinedObject.hdfs == null)
      AwsService.insertAWSConfigurationService(combinedObject.aws)
      
    ClusterService.insertClusterConfigurationService(combinedObject.cluster)

  }

}