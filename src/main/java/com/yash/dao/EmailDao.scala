package com.yash.dao

import javax.mail.{ Multipart, Part, Session, Folder }
import javax.mail.internet.MimeBodyPart
import java.io.{ File, ByteArrayInputStream }
import org.jsoup.Jsoup
import java.nio.charset.StandardCharsets
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.yash.model._
import spray.http.HttpResponse
import spray.http.StatusCodes._
import java.sql.{ Connection, DriverManager, DatabaseMetaData, ResultSet }
import com.yash.utilities.ConnectionManager

object EmailDao {
  val s3client = new AmazonS3Client()
  val meta = new ObjectMetadata();

  def saveOtherServerAttachments(session: Session, emailObjectReceived: Email, awsSource: AWS): HttpResponse = {
    try {
      val store = session.getStore("pop3");
      store.connect(emailObjectReceived.mailUsername, emailObjectReceived.mailPassword);
      val folderInbox = store.getFolder("INBOX");
      folderInbox.open(Folder.READ_ONLY);

      // fetches new messages from server
      val arrayMessages = folderInbox.getMessages();
      for (i <- arrayMessages) {
        val fromAddress = i.getFrom();
        val from = fromAddress.head.toString();
        val subject = i.getSubject();
        val sentDate = i.getSentDate().toString();

        val contentType = i.getContentType();
        var messageContent = "";
        var attachFiles = "";

        if (contentType.contains("multipart")) {
          // content may contain attachments
          val multiPart = i.getContent().asInstanceOf[Multipart];
          val numberOfParts = multiPart.getCount();
          for (partCount <- numberOfParts until 0) {
            val part = multiPart.getBodyPart(partCount).asInstanceOf[MimeBodyPart];
            if (Part.ATTACHMENT.equalsIgnoreCase(part
              .getDisposition())) {
              // this part is attachment
              val fileName = part.getFileName();
              attachFiles += fileName + ", ";
              part.saveFile(new File("C:\\Users\\sushant.varshney\\Desktop\\OtherAttachments") + File.separator
                + fileName);
            } else {
              // this part may be the message content
              //messageContent = part.getContent().toString();
              // Filtering text contents from html content in body part
              messageContent = Jsoup.parse(part.getContent.toString()).text()
            }
          }

          if (attachFiles.length() > 1) {
            attachFiles = attachFiles.substring(0,
              attachFiles.length() - 2);
          }
        } else if (contentType.contains("text/plain")
          || contentType.contains("text/html")) {
          val content = i.getContent();
          if (content != null) {
            //messageContent = content.toString();
            // Filtering text contents from html content in body part
            messageContent = Jsoup.parse(content.toString()).text()
          }
        }
        // print out details of each message
        val content = from + "\n" + subject + "\n" + sentDate + "\n" + messageContent + "\n" + attachFiles
        val inputstream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        meta.setContentLength(content.length);
        s3client.putObject(awsSource.s3Location, i.getMessageNumber.toString(), inputstream, meta)
        System.out.println("Message #" + i.getMessageNumber);
        System.out.println("\t From: " + from);
        System.out.println("\t Subject: " + subject);
        System.out.println("\t Sent Date: " + sentDate);
        System.out.println("\t Message: " + messageContent);
        System.out.println("\t Attachments: " + attachFiles);
      }
      // disconnect
      folderInbox.close(false);
      store.close();
      HttpResponse(OK, s"Data Successfully saved")
    } catch {
      case e: Exception => HttpResponse(InternalServerError, s"The credentials provided were incorrect")
    }
  }

  def saveExchangeServerAttachments(session: Session, emailObjectReceived: Email, awsSource: AWS): HttpResponse = {
    try {
      val store = session.getStore("imaps")
      // accessing the mail server using the domain user and password
      store.connect(emailObjectReceived.mailHost, emailObjectReceived.mailUsername, emailObjectReceived.mailPassword)
      println("Connected to store")
      // retrieving the inbox folder
      val inbox = store.getFolder(emailObjectReceived.mailFolder.toUpperCase())
      inbox.open(Folder.READ_ONLY)

      // fetches new messages from server
      val arrayMessages = inbox.getMessages()
      println("Message count in Inbox is: " + arrayMessages.length);
      for (i <- arrayMessages) {
        val fromAddress = i.getFrom();
        val from = fromAddress.head.toString();
        val subject = i.getSubject();
        val sentDate = i.getSentDate().toString();

        val contentType = i.getContentType();
        println("Content type is: " + contentType)
        var messageContent = "";

        // store attachment file name, separated by comma
        var attachFiles = "";

        if (contentType.contains("multipart")) {
          // content may contain attachments
          val multiPart = i.getContent().asInstanceOf[Multipart];
          val numberOfParts = multiPart.getCount();
          var partCount = 0
          for (partCount <- numberOfParts until 0) {
            val part = multiPart
              .getBodyPart(partCount).asInstanceOf[MimeBodyPart];
            if (Part.ATTACHMENT.equalsIgnoreCase(part
              .getDisposition())) {
              // this part is attachment
              val fileName = part.getFileName();
              attachFiles += fileName + ", ";
              part.saveFile(new File("C:\\Users\\sushant.varshney\\Desktop\\ExchangeAttachments") + File.separator
                + fileName);
            } else {
              // this part may be the message content
              //messageContent = part.getContent().toString();
              // Filtering text contents from html content in body part
              messageContent = Jsoup.parse(part.getContent.toString()).text()
            }
          }

          if (attachFiles.length() > 1) {
            attachFiles = attachFiles.substring(0,
              attachFiles.length() - 2);
          }
        } else if (contentType.contains("text/plain")
          || contentType.contains("text/html")) {
          val content = i.getContent();
          if (content != null) {
            //messageContent = content.toString();
            // Filtering text contents from html content in body part
            messageContent = Jsoup.parse(content.toString()).text()
          }
        }

        // print out details of each message
        println("Message #" + i.getMessageNumber);
        println("\t From: " + from);
        println("\t Subject: " + subject);
        println("\t Sent Date: " + sentDate);
        println("\t Message: " + messageContent);
        println("\t Attachments: " + attachFiles);
      }

      // disconnect
      inbox.close(false);
      store.close();
      HttpResponse(OK, s"Data Successfully saved")
    } catch {
      case e: Exception => HttpResponse(InternalServerError, s"The credentials provided were incorrect")
    }

  }
  def getUserEmailConfiguration(userId: String): Email =
    {
      var emailSource: Email = null
      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM EmailConfiguration where userId=" + userId)

      try {
        while (resultSet.next()) {
          val mailHost = resultSet.getString("mailHost")
          val mailPort = resultSet.getString("mailPort")
          val mailUsername = resultSet.getString("mailUsername")
          val mailPassword = resultSet.getString("mailPassword")
          val mailFolder = resultSet.getString("mailFolder")
          val isExchangeServer = resultSet.getString("isExchangeServer")
          val userId = resultSet.getInt("userId")
          emailSource = new Email(mailHost, mailPort, mailUsername, mailPassword, mailFolder, isExchangeServer, userId)
        }
      } catch {
        case t: Throwable => emailSource // TODO: handle error
      }
      emailSource
    }

  def insertUserEmailConfigurations(emailSource: Email) = {

    try {
      val statement = ConnectionManager.provideConnection().createStatement()
      val resultSet = statement.executeQuery(("select * from EmailConfiguration where userId = '" + emailSource.userId + "' "))
      if (resultSet.next()) {
        statement.executeUpdate("UPDATE emailconfiguration SET mailHost = '" + emailSource.mailHost + "', " + "mailPort = '" + emailSource.mailPort + "', " + "mailUsername = '" + emailSource.mailUsername + "', " + "mailPassword = '" + emailSource.mailPassword + "', " + "mailFolder = '" + emailSource.mailFolder + "', " + "isExchangeServer = '" + emailSource.isExchangeServer + "' " + "WHERE userId = '" + emailSource.userId + "' ");
        System.out.println("updated in email database...");
        "updated in email database..."
      } else {
        val prep = ConnectionManager.provideConnection().prepareStatement("INSERT INTO EmailConfiguration VALUES (?, ?, ?, ?, ?, ?, ? )")
        prep.setString(1, emailSource.mailHost)
        prep.setString(2, emailSource.mailPort)
        prep.setString(3, emailSource.mailUsername)
        prep.setString(4, emailSource.mailPassword)
        prep.setString(5, emailSource.mailFolder)
        prep.setString(6, emailSource.isExchangeServer)
        prep.setInt(7, emailSource.userId)
        prep.executeUpdate()
        System.out.println("inserted in email database...");
        "Email Configuration's saved"
      }
    } catch {
      case t: Throwable => "Problem Occurred While Saving Email Configuration"
    }
  }
}