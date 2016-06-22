package com.yash.service
 import com.yash.model.LightningViz
import com.yash.dao.LightningDao._

object LightningService {
  def getGraphService(lightningviz: LightningViz)={
    getGraph(lightningviz)
  }
  
}