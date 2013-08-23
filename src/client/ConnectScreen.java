/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import Utilities.UtNetworking;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import server.ServerMain;

/**
 *
 * @author Tony
 */
public class ConnectScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private SimpleApplication app;
    private Client client;
    private ConcurrentLinkedQueue<String> messageQueue;
    int connectCounter;

    public ConnectScreen(SimpleApplication app) {
	this.app = app;
    }

    // Connect to server button
    public void connectToServer() {
	connectCounter++;
	TextField ServerIPField = screen.findNiftyControl("serverIP", TextField.class);
	Label connectError = screen.findNiftyControl("connectError", Label.class);
	connectError.setText("");
	UtNetworking.initializables();
	messageQueue = new ConcurrentLinkedQueue<String>();
	try {
	    client = Network.connectToServer(ServerIPField.getText(), UtNetworking.PORT);
	    client.start();
	} catch (IOException ex) {
	    connectError.setText("Could not connect to server " + connectCounter + " times.");
	    com.sun.istack.internal.logging.Logger.getLogger(ServerMain.class).log(Level.SEVERE, null, ex);
	}
    }

    public void bind(Nifty nifty, Screen screen) {
	this.nifty = nifty;
	this.screen = screen;
    }

    public void onStartScreen() {
	connectCounter = 0;
	//throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    private class NetworkMessageListener implements MessageListener<Client> {

	public void messageReceived(Client source, Message m) {
	    if (m instanceof UtNetworking.NetworkMessage) {
		UtNetworking.NetworkMessage message = (UtNetworking.NetworkMessage) m;
		messageQueue.add(message.getMessage());
	    }
	}
    }
}
