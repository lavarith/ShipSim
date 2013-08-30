/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import networking.UtNetworking;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import networking.UtNetworking.AttributeMessage;
import networking.UtNetworking.NetworkMessage;
import networking.UtNetworking.PlanetDetails;
import networking.UtNetworking.ShipDetails;

/**
 * @author Tony
 */
public class ServerController extends AbstractAppState implements ScreenController {

    Server server;
    SimpleApplication app;
    Nifty nifty;
    Screen screen;
    ListBox<String> listBox;
    int connections, newestconnection;
    ArrayList ipAddresses;
    ShipDetails shipList;
    PlanetDetails planetList;

    public static void main(String[] args) {
	UtNetworking.initializables();
    }

    public ServerController(SimpleApplication app, Server server) {
	this.app = app;
	this.server = server;

	ipAddresses = new ArrayList();	// List of player IP addresses.

	shipList = new ShipDetails();
	
	planetList = new PlanetDetails();

	// Listen for people joining the server
	this.server.addConnectionListener(new ConnectionListener() {
	    public void connectionAdded(Server server, HostedConnection conn) {
		addServerNote("User connected: " + conn.getAddress());
		ipAddresses.add(conn.getAddress());
		conn.send(new NetworkMessage("ip|"+ conn.getAddress()));
	    }

	    public void connectionRemoved(Server server, HostedConnection conn) {
		addServerNote("User disconnected: " + ipAddresses.get(conn.getId()));
		//throw new UnsupportedOperationException("Not supported yet.");
	    }
	});
	
	// Add Data Listeners
	server.addMessageListener(new ServerListener(), NetworkMessage.class);
	server.addMessageListener(new ServerListener(), AttributeMessage.class);
    }

    @Override
    public void update(float tpf) {
	server.broadcast(shipList);
	server.broadcast(planetList);
    }

    public void bind(Nifty nifty, Screen screen) {
	this.nifty = nifty;
	this.screen = screen;
	listBox = this.screen.findNiftyControl("listBox", ListBox.class);
    }

    public void onStartScreen() {
	System.out.println("Boom!");
	addServerNote("Server started...");

	// Generate new universe if it hasn't already

	// Load existing universe

    }

    public void onEndScreen() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addServerNote(String listItem) {
	listBox.addItem(listItem);
	listBox.setFocusItemByIndex(listBox.itemCount() - 1);
    }

    private class ServerListener implements MessageListener<HostedConnection> {

	public void messageReceived(HostedConnection source, Message message) {
	    if (message instanceof NetworkMessage) {
		// do something with the message
		NetworkMessage helloMessage = (NetworkMessage) message;
		addServerNote(helloMessage.getMessage());
	    }
	    
	    if(message instanceof AttributeMessage){
		AttributeMessage attribute = (AttributeMessage) message;
		
		// respond to servernote
		if(attribute.getAttribute().equals("servernote")){
		    addServerNote(attribute.getValue());
		}
	    }
	}
    }
}