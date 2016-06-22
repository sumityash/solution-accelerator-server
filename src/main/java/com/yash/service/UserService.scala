package com.yash.service

import com.yash.model.User
import com.google.gson.Gson
import com.yash.dao.UserDao._
import com.yash.model.UserForReg

object UserService {
  
    val gson=new Gson
  
  def validateService(userReceived:User)={
      gson.toJson(validateUser(userReceived))
    }
    
  def registerService(userReceived: UserForReg):String={
    registerUser(userReceived)
    }
  
}
  