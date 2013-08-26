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
import com.jme3.network.Server;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import networking.UtNetworking.NetworkMessage;
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

    public static void main(String[] args) {
	UtNetworking.initializables();
    }

    public ServerController(SimpleApplication app, Server server) {
	this.app = app;
	this.server = server;

	ipAddresses = new ArrayList();	// List of player IP addresses.

	shipList = new ShipDetails();
	
	// Listen for people joining the server
	this.server.addConnectionListener(new ConnectionListener() {
	    public void connectionAdded(Server server, HostedConnection conn) {
		listBox.addItem("User connected: " + conn.getAddress());
		ipAddresses.add(conn.getAddress());
	    }

	    public void connectionRemoved(Server server, HostedConnection conn) {
		listBox.addItem("User disconnected: " + ipAddresses.get(conn.getId()));
		//throw new UnsupportedOperationException("Not supported yet.");
	    }
	});
    }

    @Override
    public void update(float tpf) {
	listBox.setFocusItemByIndex(listBox.itemCount() - 1);
	server.broadcast(new NetworkMessage("Welcome to my universe! " + tpf));
	server.broadcast(shipList);
    }

    public void bind(Nifty nifty, Screen screen) {
	this.nifty = nifty;
	this.screen = screen;
	listBox = this.screen.findNiftyControl("listBox", ListBox.class);
    }

    public void onStartScreen() {
	System.out.println("Boom!");
	listBox.addItem("Server started...");

	// Generate new universe if it hasn't already

	// Load existing universe
    }

    public void onEndScreen() {
	throw new UnsupportedOperationException("Not supported yet.");
    }
}