/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Utilities.UtNetworking;
import client.StartScreen;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.sun.istack.internal.logging.Logger;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author Tony
 */
public class ServerMain extends SimpleApplication implements ScreenController{

    private Server server;
    ServerController serverControl;
    int serverPort;
    Nifty nifty;

    public static void main(String[] args) {
	UtNetworking.initializables();
	ServerMain app = new ServerMain();
	
	AppSettings settings = new AppSettings(true);
	settings.setResolution(640, 480);		// Resolution
	settings.setTitle("Ship Simulator Server");		// Game Title
	settings.setVSync(true);			// VSync
	settings.setFrameRate(60);			// Sets FPS
	app.setDisplayStatView(false);	    // Turn screen stats on/off
	app.setDisplayFps(false);	    // Turn FPS display on/off
	app.setSettings(settings);	    // Apply the settings
	app.setShowSettings(false);	    // Don't show JME settings screen
	
	app.start(JmeContext.Type.Display); // Server info screen
	
	java.util.logging.Logger.getLogger("").setLevel(Level.SEVERE);
    }

    @Override
    public void simpleInitApp() {
	flyCam.setEnabled(false);   // turns off the silly cam follow
	this.pauseOnFocus = false;
	try {
	    server = Network.createServer(UtNetworking.PORT);
	    server.start();
	} catch (IOException ex) {
	    Logger.getLogger(ServerMain.class).log(Level.SEVERE, null, ex);
	}
	
	serverControl = new ServerController(this, this.server);
	
	// Load the GUI
	NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
		assetManager, inputManager, audioRenderer, guiViewPort);
	nifty = niftyDisplay.getNifty();
	// Turn on/off random color panels
	nifty.setDebugOptionPanelColors(true);

	// Start Screen Controls
	nifty.registerScreenController(serverControl);
	nifty.addXml("Interface/ServerMain.xml");
	nifty.gotoScreen("serverMain");
	stateManager.attach(serverControl);
	guiViewPort.addProcessor(niftyDisplay);
    }

    @Override
    public void simpleUpdate(float tpf) {
	if(server.getConnections().size()>0){
	    fpsText.setText("Connected!");
	}else{
	    fpsText.setText("No friends!");
	}
    }

    @Override
    public void destroy() {
	server.close();
	super.destroy();
    }

    public void bind(Nifty nifty, Screen screen) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onStartScreen() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
	throw new UnsupportedOperationException("Not supported yet.");
    }
}
