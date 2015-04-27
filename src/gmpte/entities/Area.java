/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gmpte.entities;

/**
 *
 * @author argyn
 */
public class Area {
  
  private int id;
  private String name;
  private String code;
  
  public Area(int id, String name) {
    this.id = id;
    this.name = name;
    code = null;
  }
  
  public Area(int id, String name, String code) {
    this(id, name);
    this.code = code;
  }
  
  public int getID() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  @Override
  public String toString() {
    return name;
  }
  
  @Override
  public boolean equals(Object other) {
    if(other==null)
      return false;
    
    return id == ((Area)other).id;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 47 * hash + this.id;
    return hash;
  }
  
  
}
