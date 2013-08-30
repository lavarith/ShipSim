/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameplay;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import networking.UtNetworking;
import server.ServerPlanet;
import server.ServerShip;
import server.ServerShipList;

/**
 * @author Tony
 */
public class MainViewer extends AbstractAppState implements ScreenController {

    SimpleApplication app;
    private Nifty nifty;
    private Screen screen;
    Client client;
    // Ship Vars
    int shipIndex;
    ArrayList<ServerShipList> shipList;
    ServerShip myShip;
    ConcurrentLinkedQueue<ArrayList> shipQueue;
    // Planets Vars
    ArrayList<ServerPlanet> Planets;
    ConcurrentLinkedQueue<ArrayList> planetQueue;
    // Helm Information
    Helm helm;
    String currentScreen;

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

	// Planet Details
	Planets = new ArrayList<ServerPlanet>();
	planetQueue = new ConcurrentLinkedQueue<ArrayList>();
    }

    public void bind(Nifty nifty, Screen screen) {
	this.nifty = nifty;
	this.screen = screen;
	this.screen.setDefaultFocusElement("MainScreenButton");
    }

    public void onStartScreen() {
	shipList = shipQueue.poll();
	if (shipList != null) {
	    myShip = shipList.get(0).getShips().get(shipIndex);
	    System.out.println(myShip.getName() + " Main Viewer");
	}
	currentScreen = "mainviewscreen"; // Initialize the start screen;
	nifty.setDebugOptionPanelColors(false);
	// Helm Details
	helm = new Helm(app, nifty, screen);
	//throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
	//throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(float tpf) {
	helm.update(tpf, Planets);

	renderScreen();
    }
    //====================== SERVER CLIENT MESSAGES ======================//

    private class ShipDetailsListener implements MessageListener<Client> {
	// Get list of active ships

	public void messageReceived(Client source, Message m) {
	    if (m instanceof UtNetworking.ShipDetails) {
		UtNetworking.ShipDetails shipDetails = (UtNetworking.ShipDetails) m;
		shipQueue.add(shipDetails.getServerShips());
	    }
	    if (m instanceof UtNetworking.PlanetDetails) {
		UtNetworking.PlanetDetails planetDetails = (UtNetworking.PlanetDetails) m;
		Planets = planetDetails.getPlanets();
	    }
	}
    }
    //====================== STATION CONTROLS ======================//
    // This layer goes to the station layer indicated.

    public void gotoLayer(String layername) {
	currentScreen = layername;
	// Set all screen layers to invisible that aren't layername
	String[] layers = {"helmscreen", "mainviewscreen", "weaponsscreen", "engineerscreen", "commscreen", "sciencescreen", "starchartscreen"};
	for (String layer : layers) {
	    if (!layer.equals(layername)) {
		screen.findElementByName(layer).setVisible(false);
		if (layer.equals("helmscreen")) {
		    helm.EndScreen();
		}
	    }
	}
	// Set layername to visible
	screen.findElementByName(layername).setVisible(true);
	if (layername.equals("helmscreen")) {
	    helm.StartScreen();
	    helm.initKeys();
	}
    }

    public void renderScreen() {
	if (currentScreen.equals("helmscreen")) {
	    helm.render();
	}
    }

    //====================== HELM CONTROLS ======================//
    public void allStop() {
	helm.onAllStopClick();
    }
    // Warp Slider changes

    @NiftyEventSubscriber(id = "warpSlide")
    public void onWarpSliderChange(final String id, final SliderChangedEvent event) {
	helm.ship.setWarp(event.getSlider().getMax() - event.getValue());
	if (helm.ship.getWarp() >= 9) {
	    helm.warpString = "max";
	} else {
	    helm.warpString = helm.dformat.format(helm.ship.getWarp());
	}
	screen.findElementByName("warpText").getRenderer(TextRenderer.class).setText(helm.warpString);
	System.out.println(helm.dformat.format(helm.ship.getWarp()));
    }

    // Impulse Slider changes
    @NiftyEventSubscriber(id = "impulseSlide")
    public void onImpulseSliderChange(final String id, final SliderChangedEvent event) {
	helm.ship.setImpulse(event.getSlider().getMax() - event.getValue());
	helm.impulseString = helm.dformat.format(helm.ship.getImpulse());
	screen.findElementByName("impulseText").getRenderer(TextRenderer.class).setText(helm.impulseString);
	System.out.println(helm.dformat.format(helm.ship.getImpulse()));
    }
}
