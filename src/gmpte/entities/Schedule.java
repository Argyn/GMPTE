/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.ArrayList;

/**
 *
 * @author mbgm2rm2
 */
public class Schedule {
    private ArrayList<Roster> rosters;
    
    
    public Schedule() {
        rosters = new ArrayList<Roster>();
    }
    
    public void addRoster(Roster roster) {
        rosters.add(roster);
    }
    
    public ArrayList<Roster> getRosters() {
        return rosters;
    }
    
}
