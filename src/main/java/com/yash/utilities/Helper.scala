package com.yash.utilities

import java.util.Calendar
import com.yash.model.AWS

object Helper {
    def getPrefix(awsSource: AWS): String = {
    var now = Calendar.getInstance();
    var year = now.get(Calendar.YEAR);
    var month = now.get(Calendar.MONTH) + 1;
    var day = now.get(Calendar.DAY_OF_MONTH);
    var hour = now.get(Calendar.HOUR_OF_DAY);
    var CurrentTimeMillis = System.currentTimeMillis
    var prefix = awsSource.s3Location + year + "/" + month + "/" + day + "/" + hour + "/" + CurrentTimeMillis
    println("Final Prefix is: " + prefix)
    prefix
  }
}