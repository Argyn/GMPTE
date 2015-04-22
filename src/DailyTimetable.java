/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import gmpte.entities.Roster;
import gmpte.entities.Service;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author mbax2eu2
 */
public class DailyTimetable {
  
    private ArrayList<DailyTimetableNode> timetable;
    
    
    public DailyTimetable() {
        timetable = new ArrayList<DailyTimetableNode>();
    }
    
    public void addtimetableNode(DailyTimetableNode node) {
        timetable.add(node);
    }
    
    public ArrayList<DailyTimetableNode> getTimetable() {
        return timetable;
    }
    
}