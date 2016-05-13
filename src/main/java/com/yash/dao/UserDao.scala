package com.yash.dao

import com.yash.model.User
import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }
import com.yash.utilities.ConnectionManager

object UserDao {
  var userFetched: User = null
  val driver = "com.mysql.jdbc.Driver"

  def validateUser(userReceived: User): User =
    {
      Class.forName(driver)
      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery(("select * from UserDetails where emailId='"+ userReceived.emailId+"'"))

      try {
        while (resultSet.next()) {
          val userId = resultSet.getInt("userId")
          val name = resultSet.getString("name")
          val emailId = resultSet.getString("emailId")
          val contactNumber = resultSet.getLong("contactNumber")
          val companyName = resultSet.getString("companyName")
          val password = resultSet.getString("password")

          if (userReceived.password==password)
            userFetched = new User(userId, name, emailId, contactNumber, companyName, password);
          else
            userFetched
        }
      } catch {
        case t: Throwable => userFetched
      }

      println("USER SENT " + userFetched)
      userFetched
    }

  def registerUser(user: User) =
    {
      val prep = ConnectionManager.provideConnection().prepareStatement("INSERT INTO UserDetails(name,emailId,contactNumber,companyName,password) VALUES (?, ?, ?, ?, ?)")
      prep.setString(1, user.name)
      prep.setString(2, user.emailId)
      prep.setLong(3, user.contactNumber)
      prep.setString(4, user.companyName)
      prep.setString(5, user.password)

      prep.executeUpdate()
      System.out.println("inserted in UserDetails database...");
      "inserted in given database..."
    }

}