/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Tony
 */
@Serializable
public class ServerShip {
    
    String name;
    Vector3f location;
    int shipType;
    
    public ServerShip(){}
    
    public ServerShip(String name, Vector3f location, int shipType){
	this.name = name;
	this.location = location;
	this.shipType = shipType;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Vector3f getLocation() {
	return location;
    }

    public void setLocation(Vector3f location) {
	this.location = location;
    }

    public int getShipType() {
	return shipType;
    }

    public void setShipType(int shipType) {
	this.shipType = shipType;
    }
}
