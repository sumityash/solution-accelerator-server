package com.yash.model

import spray.json.DefaultJsonProtocol

/**
 * @author dishant.mishra
 * DbSource class is working here as a model
 */
case class Cluster(masterNodeUrl:String,userId:Int)

/**JsonImplicits object is to provide marshalling  and unmarshalling of Employee object in Json format*/
object ClusterJsonImplicits  extends  DefaultJsonProtocol
{
  implicit val clusterSourceFormat= jsonFormat2(Cluster.apply)
}