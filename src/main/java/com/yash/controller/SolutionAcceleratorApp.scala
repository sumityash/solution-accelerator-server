package com.yash.controller

import akka.actor.ActorSystem
import akka.io.IO
import akka.actor.Props
import spray.can.Http
import com.yash.utilities.ConnectionManager
import java.util.List
import java.util.ArrayList

object SolutionAcceleratorApp  {

   def main(args: Array[String]): Unit = {
    
    ConnectionManager.databaseUrl="jdbc:mysql://"+args(0)+":3306/"+args(1)
    println("DATABASE URL "+ConnectionManager.databaseUrl)
    ConnectionManager.databaseUsername=args(2)
    println("DATABASE Username "+ConnectionManager.databaseUsername)
    ConnectionManager.databasePassword=args(3)
    println("DATABASE Password "+ConnectionManager.databasePassword)
    implicit val actorsystem = ActorSystem("solutionAccelerator");
    val controller = actorsystem.actorOf(Props[SolutionAcceleratorRoutingImpl], "solutionAccelerator")
    /**start a new HTTP server on port given with our service actor as handler*/
    IO(Http) ! Http.Bind(controller, interface = "0.0.0.0", port = 8081)
  }

}
