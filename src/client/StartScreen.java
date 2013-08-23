/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import Utilities.UtNetworking;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.Client;
import com.jme3.network.Network;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import server.ServerMain;

/**
 *
 * @author Anthony Bissell
 */
public class StartScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private SimpleApplication app;
    HelmScreen helmControl;
    Client client;
    int connectCounter;
    ConcurrentLinkedQueue<String> messageQueue;

    public StartScreen(SimpleApplication app) {
	this.app = app;
	connectCounter = 0;
    }

    // Switch to a particular Screen
    public void gotoScreen(String gotoScreen) {
	System.out.println("Going to " + gotoScreen + "!");
	nifty.gotoScreen(gotoScreen);
    }

    // Quit the Game!
    public void quitGame() {
	app.stop();     // Quit the game;
    }

    public void connectToServer() {
	connectCounter++;
	TextField ServerIPField = screen.findNiftyControl("serverIP", TextField.class);
	Label connectError = screen.findNiftyControl("connectError", Label.class);
	connectError.setText("");
	UtNetworking.initializables();
	messageQueue = new ConcurrentLinkedQueue<String>();
	try {
	    client = Network.connectToServer(ServerIPField.getText(), UtNetworking.PORT);
	    if(client.isConnected()){
		nifty.gotoScreen("launchScreen");
	    }else{
		// Rejected by the server;
	    }
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
	/**
	 * jME update loop!
	 */
    }
}
