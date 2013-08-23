/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import Utilities.UtNetworking;
import Utilities.UtNetworking.NetworkMessage;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ServerMain;

/**
 *
 * @author Tony
 */
public class Game extends SimpleApplication {

    private Client client;
    Float islideY, wslideY;
    private ConcurrentLinkedQueue<String> messageQueue;

    public static void main(String[] args) {
	UtNetworking.initializables();
	Game app = new Game();
	
	AppSettings settings = new AppSettings(true);
	settings.setResolution(1280, 720);		// Resolution
	settings.setTitle("Ship Simulator");		// Game Title
	settings.setVSync(true);			// VSync
	settings.setFrameRate(60);			// Sets FPS
	app.setDisplayStatView(false);	    // Turn screen stats on/off
	app.setDisplayFps(false);	    // Turn FPS display on/off
	app.setSettings(settings);	    // Apply the settings
	app.setShowSettings(false);	    // Don't show JME settings screen

	app.start();			    // Start the app
	Logger.getLogger("").setLevel(Level.SEVERE);
    }

    // Initialize the application
    @Override
    public void simpleInitApp() {
	messageQueue = new ConcurrentLinkedQueue<String>();
	try {
	    client = Network.connectToServer("127.0.0.1", UtNetworking.PORT);
	    client.start();
	} catch (IOException ex) {
	    com.sun.istack.internal.logging.Logger.getLogger(ServerMain.class).log(Level.SEVERE, null, ex);
	}
	
	
	flyCam.setEnabled(false);   // turns off the silly cam follow

	// Load the GUI
	NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
		assetManager, inputManager, audioRenderer, guiViewPort);
	Nifty nifty = niftyDisplay.getNifty();
	// Turn on/off random color panels
	nifty.setDebugOptionPanelColors(true);

	// Set the first screen to the "start" screen
	StartScreen startScreen = new StartScreen(this);
	nifty.registerScreenController(startScreen);
	nifty.addXml("Interface/StartScreen.xml");
	nifty.gotoScreen("start");
	stateManager.attach(startScreen);

	guiViewPort.attachScene(guiNode);
	guiNode.setQueueBucket(Bucket.Gui);

	Camera guiCam = new Camera(settings.getWidth(), settings.getHeight());
	ViewPort guiViewPort2 = renderManager.createPostView("Gui 2 Default", guiCam);
	guiViewPort2.addProcessor(niftyDisplay);
	guiViewPort2.setClearFlags(false, false, false);
	//guiViewPort.addProcessor(niftyDisplay);
    }

    /* Use the main event loop to trigger repeating actions. */
    @Override
    public void simpleUpdate(float tpf) {
	fpsText.setText(INPUT_MAPPING_EXIT);
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
    
    private class NetworkMessageListener implements MessageListener<Client>{

	public void messageReceived(Client source, Message m) {
	    if(m instanceof NetworkMessage){
		NetworkMessage message = (NetworkMessage) m;
		messageQueue.add(message.getMessage());
	    }
	}
	
    }
    
    @Override
    public void destroy(){
	client.close();
	super.destroy();
    }
}
