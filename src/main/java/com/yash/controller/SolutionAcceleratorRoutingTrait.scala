package com.yash.controller

import spray.routing.HttpService
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.json._
import com.yash.model._
import com.yash.model.DbJsonImplicits._
import com.yash.model.EmailJsonImplicits._
import com.yash.model.TwitterJsonImplicits._
import com.yash.model.UserJsonImplicits._
import com.yash.model.AWSJsonImplicits._
import com.yash.model.ClusterJsonImplicits._
import com.yash.model.LightningVizImplicits._
import com.yash.dao.DbDao
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.routing.Directive.pimpApply
import spray.routing.RequestContext
import com.yash.service.{ MailSourceService, TwitterSourceService, DbSourceService,UserService,AwsService,ClusterService,InsertionService,HdfsService }
import com.google.gson.Gson
import com.yash.service.LightningService


/**
 * @author dishant.mishra
 * SparkServiceTrait is extending HttpService so that routes can be defined in it
 */
trait SolutionAcceleratorRoutingTrait extends HttpService with CORSSupport {

  /**
   * all structures you build with the routing are subtypes of type Route
   *  Directives and custom routes are combined via nesting and the ~ operator
   */
  val sparkRoutes =
    cors{
      post {
         path("solutionAccelerator" / "getLightning") {entity(as[LightningViz]){lightningviz=>
            complete {
              println(lightningviz.x)
              println(lightningviz.y)
              LightningService.getGraphService(lightningviz)
            }
         }
        } ~
        path("solutionAccelerator" / "validate") {
          entity(as[User]) { userReceived =>
            complete {
              UserService.validateService(userReceived)
            }
          }
        } ~
          path("solutionAccelerator" / "register") {
            entity(as[UserForReg]) { userRegistration =>
              complete {
                println("USer in Trait "+userRegistration.companyName +" "+userRegistration.contactNumber+" "+userRegistration.emailId )
                UserService.registerService(userRegistration)
              }
            }
          }
      } ~
        path("solutionAccelerator" / "insertAllConfigurations") {
          entity(as[String]) { combinedJsonString =>
            complete {
              val gson = new Gson
              var combinedObject: Combined = gson.fromJson(combinedJsonString, (new Combined).getClass)
              println(combinedObject.database)
              InsertionService.checkSource(combinedObject)
              "All Configuration Saved"
            }
          }
        } ~
        path("solutionAccelerator" / "insertDatabaseConfigurations") {
          entity(as[Database]) { dbSource =>
            complete {

              DbSourceService.insertUserDatabaseConfigurationService(dbSource)
            }
          }
        } ~
        path("solutionAccelerator" / "insertAWSConfigurations") {
          entity(as[AWS]) { awsSource =>
            complete {

              AwsService.insertAWSConfigurationService(awsSource)
            }
          }
        }~
        path("solutionAccelerator" / "insertClusterConfigurations") {
          entity(as[Cluster]) { clusterSource =>
            complete {

              ClusterService.insertClusterConfigurationService(clusterSource)
            }
          }
        } ~
        path("solutionAccelerator" / "insertEmailConfigurations") {
          entity(as[Email]) { emailSource =>
            complete {

              MailSourceService.insertEmailConfigurationService(emailSource)
            }
          }
        } ~
        path("solutionAccelerator" / "insertTwitterConfigurations") {
          entity(as[Twitter]) { twitterSource =>
            complete {

              TwitterSourceService.insertTwitterConfigurationService(twitterSource)
            }
          }
        } ~
        path("solutionAccelerator" / "getDatabase") {
          entity(as[String]) { dbUrl =>
            complete {
              DbSourceService.getDatabase(dbUrl)
            }
          }
        } ~
        path("solutionAccelerator" / "getTables") {
          entity(as[String]) { databaseName =>
            complete {
              DbSourceService.getTables(databaseName)
            }
          }
        } ~
        get {
          path("solutionAccelerator" / "fetchDatabaseConfiguration") {
            parameters('userId) { (userId) =>
              {
                complete {
                  println("-----------------------IN DB CONFIG-------------------------------")
                  DbSourceService.fetchUserDatabaseConfigurationService(userId)
                }
              }
            }

          } ~
            path("solutionAccelerator" / "fetchEmailConfiguration") {
              parameters('userId) { (userId) =>
                complete {
                  println("======USERID FOR EMAIL==========" + userId)
                  println("-----------------------IN EMAIL CONFIG---------------------------")
                  MailSourceService.fetchUserEmailConfigurationService(userId)
                }
              }

            }~
            path("solutionAccelerator" / "fetchTwitterConfiguration") {
              parameters('userId) { (userId) =>
                complete {
                  println("----------------------------IN TWITTER CONFIG--------------------")
                  TwitterSourceService.fetchUserTwitterConfigurationService(userId)
             
                }
              }

            } ~
            path("solutionAccelerator" / "fetchAWSConfiguration") {
              parameters('userId) { (userId) =>
                complete {
                  println("-----------------------------IN AWS CONFIG-----------------------")
                  AwsService.fetchAWSConfigurationService(userId)
                }
              }
            } ~
            path("solutionAccelerator" / "fetchHDFSConfiguration") {
              parameters('userId) { (userId) =>
                complete {
                  println("-----------------------------IN HDFS CONFIG-----------------------")
                  HdfsService.fetchHDFSConfigurationService(userId)
                 
                }
              }
            } ~
            path("solutionAccelerator" / "fetchClusterConfiguration") {
              parameters('userId) { (userId) =>
                complete {
                  println("--------------------------IN CLUSTER CONFIG-----------------------")
                   ClusterService.fetchClusterConfigurationService(userId)
                }
              }
            }
        }
    }
}
