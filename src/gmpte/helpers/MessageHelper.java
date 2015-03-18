/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.helpers;

import gmpte.GMPTEConstants;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author argyn
 */
public class MessageHelper {
  
  public static String prepareRosterGeneratedMessage(Date from, Date to) {
    DateFormat dateFormat = new SimpleDateFormat(GMPTEConstants.DATE_FORMAT);
    String date1 = dateFormat.format(from);
    String date2 = dateFormat.format(to);
    
    String returnString = GMPTEConstants.ROSTER_GENERATED_LABEL;
    returnString = returnString.replaceFirst("\\{from\\}", date1);
    returnString = returnString.replaceFirst("\\{to\\}", date2);
    return returnString;
  }
  
}
