package com.yash.controller

import akka.actor.ActorContext
import akka.actor.Actor
/**
 * @author dishant.mishra
 *This is SparkServices class which is extending trait SparkSevice which have all routes
 */
class SolutionAcceleratorRoutingImpl extends Actor with SolutionAcceleratorRoutingTrait {
  /**
   * actorRefFactory is unimplemented method of HttpService trait
   * where we need to define the context on which our service will run
   * @return implicit val context of ActorContext type
   */
  def actorRefFactory: ActorContext = context

  /**This actor is used to run routes defined in SparkService Trait*/
  def receive: Actor.Receive = runRoute(sparkRoutes)

}