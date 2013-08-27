/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameplay;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import networking.UtNetworking;
import server.ServerShip;
import server.ServerShipList;

/**
 *
 * @author Tony
 */
public class MainViewer implements ScreenController {

    SimpleApplication app;
    private Nifty nifty;
    private Screen screen;
    Client client;
    int shipIndex;
    ArrayList<ServerShipList> shipList;
    ServerShip myShip;
    ConcurrentLinkedQueue<ArrayList> shipQueue;

    public MainViewer(SimpleApplication app, int shipIndex, Client client) {
	UtNetworking.initializables();
	this.app = app;

	// Client Details
	this.client = client;
	client.addMessageListener(new ShipDetailsListener()); // Add message listener for ship details

	// Ship Details
	this.shipIndex = shipIndex;
	shipQueue = new ConcurrentLinkedQueue<ArrayList>();
	shipList = new ArrayList<ServerShipList>();
	myShip = new ServerShip();
    }

    public void bind(Nifty nifty, Screen screen) {
	this.nifty = nifty;
	this.screen = screen;
    }

    public void onStartScreen() {
	shipList = shipQueue.poll();
	if (shipList != null) {
	    myShip = shipList.get(0).getShips().get(shipIndex);
	    System.out.println(myShip.getName() + " Main Viewer");
	    screen.findNiftyControl("mainscreenlabel", Label.class).setText(myShip.getName() + " Main Viewer");
	}
	//throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
	//throw new UnsupportedOperationException("Not supported yet.");
    }

    private class ShipDetailsListener implements MessageListener<Client> {
	// Get list of active ships

	public void messageReceived(Client source, Message m) {
	    if (m instanceof UtNetworking.ShipDetails) {
		UtNetworking.ShipDetails shipDetails = (UtNetworking.ShipDetails) m;
		shipQueue.add(shipDetails.getServerShips());
	    }
	}
    }
}
