/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package launcher;

import gameplay.HelmScreen;
import server.ServerShipList;
import networking.UtNetworking;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import gameplay.MainViewer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.regex.Pattern;
import networking.UtNetworking.AttributeMessage;
import networking.UtNetworking.NetworkMessage;
import networking.UtNetworking.ShipDetails;
import server.ServerMain;
import server.ServerShip;

/**
 *
 * @author Anthony Bissell
 */
public class StartScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private SimpleApplication app;
    Client client;
    HelmScreen helmControl;
    int connectCounter;
    TextField serverIPField;
    Label connectError;
    Boolean connected;
    ConcurrentLinkedQueue<String> messageQueue;
    ConcurrentLinkedQueue<ArrayList> shipQueue;
    MainViewer mainViewer;
    ListBox<String> listBox;
    ArrayList<ServerShip> ShipList;
    ArrayList<ServerShipList> shipDetailsLocal;
    String myipAddress;

    public StartScreen(SimpleApplication app) {
	UtNetworking.initializables();
	this.app = app;
	connectCounter = 0;
	connected = false;
	messageQueue = new ConcurrentLinkedQueue<String>();
	shipQueue = new ConcurrentLinkedQueue<ArrayList>();
    }

    public static void main(String[] args) {
    }

    public void bind(Nifty nifty, Screen screen) {
	this.nifty = nifty;
	this.screen = screen;
    }

    public void onStartScreen() {
	connectScreen();    // If return to the connection screen, disconnect from server.
	launchScreen();
    }

    public void onEndScreen() {
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
	super.initialize(stateManager, app);
	this.app = (SimpleApplication) app;
    }

    @Override
    public void update(float tpf) {
	checkDisconnect();  // Return to connect screen if server crashes or connection is lost.
    }

    public void showMessage() {
	if ("launchScreen".equals(nifty.getCurrentScreen().getScreenId())) {
	    // Ship ListBox Details
	    listBox.clear();

	    // Add List of ships to the Ship ListBox
	    shipDetailsLocal = shipQueue.poll();
	    if (shipDetailsLocal != null) {
		ShipList = shipDetailsLocal.get(0).getShips();
		// List all available ships
		for (ServerShip ship : ShipList) {
		    listBox.addItem(ship.getName());
		}
	    } else {
		// If there are no ships available
		listBox.addItem("No Ships");
	    }
	}
    }
    // Switch to a particular Screen

    public void gotoScreen(String gotoScreen) {
	System.out.println("Going to " + gotoScreen + "!");
	// If screen is the Screen is the main viewer, pass the application along
	if ("mainviewer".equals(gotoScreen)) {
	    int shipSelected = listBox.getFocusItemIndex();
	    ServerShip joinShip = ShipList.get(shipSelected);
	    client.send(new AttributeMessage("servernote|" + myipAddress + " joined the " + joinShip.getName()));
	    joinShip.addCrew(myipAddress);
	    mainViewer = new MainViewer(app, shipSelected, client);
	    nifty.registerScreenController(mainViewer);
	    nifty.addXml("Interface/MainViewer.xml");
	    nifty.gotoScreen(gotoScreen);
	} else {
	    nifty.gotoScreen(gotoScreen);
	}
    }

    // Quit the Game!
    public void quitGame() {
	app.stop();     // Quit the game;
    }

    public void connectToServer() {
	connectError.setText("Connecting...");
	connected = true;
	try {
	    client = Network.connectToServer(serverIPField.getText(), UtNetworking.PORT);
	    client.start();
	    client.addMessageListener(new NetworkMessageListener());
	    client.addMessageListener(new ShipDetailsListener());
	    connectError.setText("");
	    gotoScreen("launchScreen");
	} catch (IOException ex) {
	    connectCounter++;
	    connectError.setText("Could not connect to server " + connectCounter + " times.");
	    com.sun.istack.internal.logging.Logger.getLogger(ServerMain.class).log(Level.SEVERE, null, ex);
	}
    }

    public void checkDisconnect() {
	// Return to connect screen if the server crashes or loses connection.
	if (connected) {
	    if (!client.isConnected()) {
		gotoScreen("connectScreen");
	    }
	}
    }

    public void connectScreen() {
	// method to run when connecting to connectScreen
	System.out.println("Connect button");
	if ("connectScreen".equals(nifty.getCurrentScreen().getScreenId())) {
	    connected = false;
	    serverIPField = screen.findNiftyControl("serverIP", TextField.class);
	    connectError = screen.findNiftyControl("connectError", Label.class);
	    if (client != null) {
		if (client.isConnected()) {
		    client.close();
		}
	    }
	}
    }

    public void launchScreen() {
	if ("launchScreen".equals(nifty.getCurrentScreen().getScreenId())) {
	    listBox = this.screen.findNiftyControl("listBox", ListBox.class);
	    if (client != null) {
		showMessage();
	    }
	}
    }

    private class ShipDetailsListener implements MessageListener<Client> {
	// Get list of active ships

	public void messageReceived(Client source, Message m) {
	    if (m instanceof ShipDetails) {
		ShipDetails shipDetails = (ShipDetails) m;
		shipQueue.add(shipDetails.getServerShips());
	    }
	}
    }

    private class NetworkMessageListener implements MessageListener<Client> {

	public void messageReceived(Client source, Message m) {
	    if (m instanceof NetworkMessage) {
		NetworkMessage message = (NetworkMessage) m;
		String[] all = message.getMessage().split("\\|");
		// IP Address Attribute
		if ("ip".equals(all[0])) {
		    myipAddress = all[1];
		}
	    }
	}
    }
}
