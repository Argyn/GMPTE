/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.helpers;

import java.util.ArrayList;

/**
 *
 * @author mbgm2rm2
 */
public class SQLQueryFilter {
  public ArrayList<String> wheres;
  public ArrayList<String> values;
  
  public ArrayList<String> orderBy;
  public ArrayList<String> order;
  
  public enum ORDER {
    ASC, DESC
  }
  
  public SQLQueryFilter() {
    wheres = new ArrayList<String>();
    values = new ArrayList<String>();
    orderBy = new ArrayList<String>();
    order = new ArrayList<String>();
  }
  
  public void where(String where, String value) {
    wheres.add(where);
    values.add(value);
  }
  
  public void order(String orderBy, ORDER order) {
    this.orderBy.add(orderBy);
    
    switch(order) {
      case ASC:
        this.order.add("ASC");
        break;
      case DESC:
        this.order.add("DESC");
        break;
    }
  }
  
  public String[] getOrderBy() {
    return orderBy.toArray(new String[orderBy.size()]);
  }
  
  public String[] getOrder() {
    return order.toArray(new String[order.size()]);
  }
  
  public String[] getWhereFields() {
    return wheres.toArray(new String[wheres.size()]);
  }
  
  public String[] getWhereValues() {
    return values.toArray(new String[values.size()]);
  }
  
}
