/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author mbgm2rm2
 */
public class UIInterfaceHelper {
  public static void onMenuItemHover(final VBox menuItem) {
    menuItem.setOnMouseEntered(new EventHandler<MouseEvent>() {
      
      @Override
      public void handle(MouseEvent t) {
        // apply css styling
        menuItem.getStyleClass().add(GMPTEConstants.MAIN_MENU_ITEM_ON_HOVER_STYLE);
      }
      
    });
    
    menuItem.setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        menuItem.getStyleClass().clear();
        // remove css styling
        menuItem.getStyleClass().add(GMPTEConstants.MAIN_MENU_ITEM_STYLE);
      }
    });       
  }
}
