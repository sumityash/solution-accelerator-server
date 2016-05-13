package com.yash.service
import java.util.Properties
import javax.mail.{ Session, Part }
import org.jsoup.Jsoup
import spray.http.StatusCodes._
import com.yash.model.{Email,AWS}
import com.yash.dao.EmailDao._
import spray.http.HttpResponse
import com.google.gson.Gson

object MailSourceService {
  val gson=new Gson

  def process(emailObjectReceived: Email,awsSource:AWS): HttpResponse = {
    val isExchangeServer = emailObjectReceived.isExchangeServer
    // Logic to connect for exchange servers and other servers
    if (isExchangeServer.equalsIgnoreCase("no")) {
      connectOtherServer(emailObjectReceived,awsSource)
    } else if (isExchangeServer.equalsIgnoreCase("yes")) {
      connectExchangeServer(emailObjectReceived,awsSource)
    } else {
      HttpResponse(InternalServerError, s"Invalid input for isExchangeServer")
    }
  }
  def connectOtherServer(emailObjectReceived: Email,awsSource:AWS): HttpResponse = {
    val mailhost = emailObjectReceived.mailHost
    val mailport = emailObjectReceived.mailPort
    // server setting
    val properties = new Properties()
    properties.put("mail.pop3.host", mailhost)
    properties.put("mail.pop3.port", mailport)
    // SSL setting
    properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
    properties.setProperty("mail.pop3.socketFactory.fallback", "false")
    properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(mailport))
    val session = Session.getDefaultInstance(properties);
  saveOtherServerAttachments(session, emailObjectReceived,awsSource)
  }

  def connectExchangeServer(emailObjectReceived: Email,awsSource:AWS): HttpResponse = {
    val properties = System.getProperties
    properties.setProperty("mail.imap.port", "993")
    val session = Session.getInstance(properties, null)
   saveExchangeServerAttachments(session, emailObjectReceived,awsSource)

  }
  
   def fetchUserEmailConfigurationService(userId:String)={
      gson.toJson(getUserEmailConfiguration(userId))
    }
   
   def insertEmailConfigurationService(emailSource: Email)={
      insertUserEmailConfigurations(emailSource)
    }

}