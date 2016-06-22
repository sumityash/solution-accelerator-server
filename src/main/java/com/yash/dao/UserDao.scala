package com.yash.dao

import com.yash.model.User
import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }
import com.yash.utilities.ConnectionManager
import com.yash.model.UserForReg

object UserDao {

  val driver = "com.mysql.jdbc.Driver"

  def validateUser(userReceived: User): User =
    {
        var userFetched: User = null
      Class.forName(driver)
      val statement = ConnectionManager.provideConnection().createStatement()

      println("QUERY EXECUTED " + "select * from UserDetails where emailId='" + userReceived.emailId + "'")

      val resultSet = statement.executeQuery(("select * from UserDetails where emailId='" + userReceived.emailId + "'"))

      try {
        while (resultSet.next()) {
          val userId = resultSet.getInt("userId")
          val name = resultSet.getString("name")
          val emailId = resultSet.getString("emailId")
          val contactNumber = resultSet.getLong("contactNumber")
          val companyName = resultSet.getString("companyName")
          val password = resultSet.getString("password")

          if (userReceived.password == password)
            userFetched = new User(userId, name, emailId, contactNumber, companyName, password);
          else
            userFetched
        }
      } catch {
        case t: Throwable => userFetched
      }
      userFetched
    }

  def registerUser(user: UserForReg): String =
    {
      println("USER FOR REGISTRATION " + user)

      /* val connection = ConnectionManager.provideConnection()*/
      var status: String = null
      val checkEmail = ConnectionManager.provideConnection().prepareStatement("select * from UserDetails where emailId=?")
      checkEmail.setString(1, user.emailId)

      val existingEmail = checkEmail.executeQuery()
      try {

        if (existingEmail.next())
          status = "Email Id already Exists"
        else {
          val prep = ConnectionManager.provideConnection().prepareStatement("INSERT INTO UserDetails(name,emailId,contactNumber,companyName,password) VALUES (?, ?, ?, ?, ?)")
          prep.setString(1, user.name)
          prep.setString(2, user.emailId)
          prep.setLong(3, user.contactNumber)
          prep.setString(4, user.companyName)
          prep.setString(5, user.password)

          prep.executeUpdate()

          status = "User Regsitered Successfully"
        }
        status
      } catch {
        case t: Throwable => "Error While Registration"
      }
    }
}