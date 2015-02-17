/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author argyn
 */
public class DateHelper {
    
    public static boolean isValidDateString(String stringDate, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        // making more strict
        format.setLenient(false);
        // try to convert it to Date, if successfull
        try {
            // means dateString corresponds to an actual Date
            Date date = format.parse(stringDate);
            return true;
        } catch(ParseException exception) {
            return false;
        }   
    }
    public static Date getDateFromString(String stringDate) 
                                                        throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
        
        Date date = format.parse(stringDate);

        return date;
    }
}
