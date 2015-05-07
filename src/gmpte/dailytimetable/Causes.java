/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.dailytimetable;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author mbax3jw3
 */
public class Causes 
{
  private ArrayList<String> causes;
  private ArrayList<String> delays;
  private ArrayList<String> cancels;
  public Causes()
  {
    causes = new ArrayList<>();
    delays = new ArrayList<>();
    cancels = new ArrayList<>();
    delays.add("a shortage of water for the windscreen wipers");
    delays.add("a punctured tyre");
    delays.add("heavy traffic");
    delays.add("a street parade");
    cancels.add("a punctured tyre");
    cancels.add("a crash");
    cancels.add("a street parade");
  }
  public String getRandomCause()
  {
    Random random = new Random();
    int causeSize = causes.size();
    int index = random.nextInt(causeSize);
    return causes.get(index);
  } // getRandomCause
  
  public String getRandomDelay()
  {
    Random random = new Random();
    int causeSize = delays.size();
    int index = random.nextInt(causeSize);
    return delays.get(index);
  } // getRandomCause
    
  public String getRandomCancel()
  {
    Random random = new Random();
    int causeSize = cancels.size();
    int index = random.nextInt(causeSize);
    return cancels.get(index);
  } // getRandomCause
}
