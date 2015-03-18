/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

/**
 *
 * @author argyn
 */
public class RosterGenerationRunnable implements Runnable {

  @Override
  public void run() {
    RosterController controller = new RosterController();
    controller.addRoute(65);
    controller.addRoute(66);
    controller.addRoute(67);
    controller.addRoute(68);

    controller.generateRoster();
  }
  
}
