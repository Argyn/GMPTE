/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author mbgm2rm2
 */
public class ListHelper {
  
  public static <T> List<T> intersect(List<T> l1, List<T> l2) {
    List<T> list = new ArrayList<T>();

    for (T t : l1) {
        if(l2.contains(t)) {
            list.add(t);
        }
    }

    return list;
  }
  
}
