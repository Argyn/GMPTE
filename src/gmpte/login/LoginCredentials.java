/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.login;

import gmpte.Driver;

/**
 *
 * @author mbgm2rm2
 */
public class LoginCredentials {
    
    private final static LoginCredentials loginCredentials = new LoginCredentials();
    
    private Driver driver;
    
    public static LoginCredentials getInstance() {
        return loginCredentials;
    }
    
    public Driver getDriver() {
        return driver;
    }
    
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    
    public boolean isAuthenticated() {
        return driver!=null;
    }
    
    
}
