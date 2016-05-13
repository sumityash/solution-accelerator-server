package com.yash.service

import com.yash.model.User
import com.google.gson.Gson
import com.yash.dao.UserDao._

object UserService {
  
    val gson=new Gson
  
  def validateService(userReceived:User)={
      gson.toJson(validateUser(userReceived))
    }
    
  def registerService(userReceived: User)={
    registerUser(userReceived)
    }
  
}
  