/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tony
 */
public class Game extends SimpleApplication {

    Float islideY, wslideY;
    Nifty nifty;
    StartScreen startScreen;

    public static void main(String[] args) {
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
	flyCam.setEnabled(false);   // turns off the silly cam follow

	// Load the GUI
	NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
		assetManager, inputManager, audioRenderer, guiViewPort);
	nifty = niftyDisplay.getNifty();
	// Turn on/off random color panels
	nifty.setDebugOptionPanelColors(true);

	// Start Screen Controls
	startScreen = new StartScreen(this);
	nifty.registerScreenController(startScreen);
	nifty.addXml("Interface/StartScreen.xml");
	nifty.addXml("Interface/ConnectScreen.xml");
	nifty.addXml("Interface/LaunchScreen.xml");
	nifty.addXml("Interface/SettingsScreen.xml");
	nifty.gotoScreen("start"); // default: "start" 
	stateManager.attach(startScreen);

	guiViewPort.attachScene(guiNode);
	guiNode.setQueueBucket(Bucket.Gui);

	Camera guiCam = new Camera(settings.getWidth(), settings.getHeight());
	ViewPort guiViewPort2 = renderManager.createPostView("Gui 2 Default", guiCam);
	guiViewPort2.addProcessor(niftyDisplay);
	guiViewPort2.setClearFlags(false, false, false);
    }

    /* Use the main event loop to trigger repeating actions. */
    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    @Override
    public void destroy() {
	if(startScreen.client!=null){
	    if(startScreen.client.isConnected()){
		startScreen.client.close();
	    }
	}
	super.destroy();
    }
}
