package com.yash.model
import spray.json.DefaultJsonProtocol

/**
 * @author dishant.mishra
 * DbSource class is working here as a model
 */
case class AWS(s3Location:String,awsAccessKey:String,awsSecretKey:String,userId:Int,aliasName:String,partitionColumn:String)

/**JsonImplicits object is to provide marshalling  and unmarshalling of Employee object in Json format*/
object AWSJsonImplicits  extends  DefaultJsonProtocol
{
  implicit val awsSourceFormat= jsonFormat6(AWS.apply)
}