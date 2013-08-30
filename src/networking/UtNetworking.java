/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import entities.Planet;
import java.util.ArrayList;
import server.ServerPlanet;
import server.ServerShip;
import server.ServerShipList;
import server.ServerUsers;

/**
 *
 * @author Tony
 */
public class UtNetworking {

    public static final int PORT = 6000;

    public static void initializables() {
	Serializer.registerClass(NetworkMessage.class);
	Serializer.registerClass(ServerShip.class);
	Serializer.registerClass(ServerShipList.class);
	Serializer.registerClass(ShipDetails.class);
	Serializer.registerClass(ServerUsers.class);
	Serializer.registerClass(UserDetails.class);
	Serializer.registerClass(ServerPlanet.class);
	Serializer.registerClass(PlanetDetails.class);
	Serializer.registerClass(AttributeMessage.class);
    }

    @Serializable
    public static class NetworkMessage extends AbstractMessage {

	private String message;

	public NetworkMessage() {
	}

	public NetworkMessage(String message) {
	    this.message = message;
	}

	public String getMessage() {
	    return message;
	}
    }

    // Gets the server ship details
    @Serializable
    public static class ShipDetails extends AbstractMessage {

	private ArrayList<ServerShipList> serverShips;

	public ShipDetails() {
	    // Initialize ship names
	    this.serverShips = new ArrayList<ServerShipList>();
	    ServerShipList newship = new ServerShipList();

	    // Add a ship to the record.
	    newship.addShip("Enterprise", new Vector3f(20, 100, 0), 0);
	    serverShips.add(newship);
	}

	public ArrayList<ServerShipList> getServerShips() {
	    return serverShips;
	}
    }

    // Gets the server ship details
    @Serializable
    public static class UserDetails extends AbstractMessage {

	private ArrayList<ServerUsers> serverUsers;

	public UserDetails() {
	    // Initialize ship names
	    this.serverUsers = new ArrayList<ServerUsers>();
	}

	public ArrayList<ServerUsers> getServerShips() {
	    return serverUsers;
	}

	public void addUser(String ipAddress) {
	    serverUsers.add(new ServerUsers(ipAddress));
	}
    }

    // Gets the server ship details
    @Serializable
    public static class AttributeMessage extends AbstractMessage {

	private String attribute;
	private String[] all;

	public AttributeMessage() {
	}
	// Attributes should be in the format attribute|value

	public AttributeMessage(String attribute) {
	    this.attribute = attribute;
	    all = attribute.split("\\|");
	}

	public String getAttribute() {
	    return all[0];
	}

	public String getValue() {
	    return all[1];
	}
    }
    // Gets the server ship details

    @Serializable
    public static class PlanetDetails extends AbstractMessage {

	private ArrayList<ServerPlanet> serverPlanets;

	public PlanetDetails() {
	    // Initialize ship names
	    this.serverPlanets = new ArrayList<ServerPlanet>();
	    
	    // Add test planet
	    serverPlanets.add(new ServerPlanet(new Vector3f(200,300,0), 100));
	}

	public ArrayList<ServerPlanet> getPlanets() {
	    return serverPlanets;
	}
    }
}
