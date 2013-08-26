/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Tony
 */
@Serializable
public class ServerShipList {
    ArrayList<ServerShip> ships;
    
    public ServerShipList(){
	ships = new ArrayList<ServerShip>();
    }
    
    public void addShip(String name, Vector3f location, int shipType){
	ships.add(new ServerShip(name, location, shipType));
    }

    public ArrayList<ServerShip> getShips() {
	return ships;
    }    
}
