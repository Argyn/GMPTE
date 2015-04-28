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
  public Causes()
  {
    causes = new ArrayList<>();
    causes.add("a shortage of water for the windscreen wipers");
    causes.add("a punctured tyre");
    causes.add("a crash");
    causes.add("heavy traffic");
    causes.add("a street parade");
  }
  public String getRandomCause()
  {
    Random random = new Random();
    int causeSize = causes.size();
    int index = random.nextInt(causeSize);
    return causes.get(index);
  } // getRandomCause
}
