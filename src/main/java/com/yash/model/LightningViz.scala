package com.yash.model
import spray.json.DefaultJsonProtocol
/**
 * @author dishant.mishra
 * DbSource class is working here as a model
 */
case class LightningViz(x:Array[Double],y:Array[Double])

object LightningVizImplicits extends  DefaultJsonProtocol{
   implicit val lightningVizFormat= jsonFormat2(LightningViz.apply)
}
