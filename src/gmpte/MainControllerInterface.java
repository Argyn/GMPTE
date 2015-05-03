/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

import java.io.IOException;

/**
 *
 * @author mbgm2rm2
 */
public interface MainControllerInterface {
    void onSuccessfullLogin() throws Exception;
    
    void onLogOut() throws Exception;
    
    void showHolidayRequestPage();
    
    void showLoginPage();
    
    void showControllerInterface();
    
    void showMainPage();
    
    void showDriverInterface();
    
    void showRosterView();
    
    void showDriverRosterView();
    
    void showPassengerInterface();
    
    void showJourneyPlanner();
    
    void showDailyTimetable();
}
