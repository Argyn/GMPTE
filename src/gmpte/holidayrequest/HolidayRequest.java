/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.holidayrequest;

import gmpte.entities.Driver;
import java.util.Date;

/**
 *
 * @author argyn
 */
public class HolidayRequest {
    private final Driver driver;
    private final Date startDate;
    private final Date endDate;
    
    public HolidayRequest(Driver driver, Date startDate, Date endDate) {
        this.driver = driver;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public Driver getDriver() {
        return driver;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
}
