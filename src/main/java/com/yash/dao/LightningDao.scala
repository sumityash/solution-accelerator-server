package com.yash.dao

import org.viz.lightning._
import scala.util.Random
import com.yash.model.LightningViz

object LightningDao{
  
def getGraph(lightningviz: LightningViz)={


  val lgn = Lightning(host="http://public.lightning-viz.org")
  
  val x = lightningviz.x
  val y = lightningviz.y
/*  val group = Array.fill(100)(Random.nextDouble() * 5).map(_.toDouble)
  val size = Array.fill(100)(Random.nextDouble() * 20 + 5)*/
  
  val viz = lgn.scatter(x, y)
  viz.getIframeLink
}

}