/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.network.serializing.Serializable;

/**
 * @author Tony
 */
@Serializable
public class ServerUsers {

    String ipAddress;
    int shipIndex;
    
    public ServerUsers(){
    }

    public ServerUsers(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public int getShipIndex() {
	return shipIndex;
    }

    public void setShipIndex(int shipIndex) {
	this.shipIndex = shipIndex;
    }

    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }
}
