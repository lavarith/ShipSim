/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameplay;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import entities.Planet;
import entities.Ship;
import java.text.DecimalFormat;

/**
 *
 * @author Tony
 */
public class Helm {

    private SimpleApplication app;
    private Nifty nifty;
    private Screen screen;
    // Ship Variables
    float screenWidth, screenHeight, topHeight;
    String warpString, impulseString;
    DecimalFormat dformat = new DecimalFormat("#.##");
    Node pivot;
    Picture helmDirection, shipImg;
    Element midPanel;
    Ship ship;
    Planet planet;

    public Helm(SimpleApplication app, Nifty nifty, Screen screen) {
	this.app = app;
	this.nifty = nifty;
	this.screen = screen;
	screenWidth = this.app.getContext().getSettings().getWidth();
	screenHeight = this.app.getContext().getSettings().getHeight();

	// Create Direction Image
	helmDirection = new Picture("Helm Direction");
	helmDirection.setImage(this.app.getAssetManager(), "Images/helmticks.png", true);
	helmDirection.setPosition(0, 0);

	ship = new Ship(this.app);
	planet = new Planet(this.app, new Vector2f(200, 300), 100);

	//throw new UnsupportedOperationException("Not supported yet.");
    }

    public void StartScreen() {
	// Set Helm Ticks Info
	//topHeight = nifty.getCurrentScreen().findElementByName("helmtop_panel").getHeight();
	midPanel = nifty.getCurrentScreen().findElementByName("helmpanel_mid");
	float helmWidth = midPanel.getHeight() / helmDirection.getLocalScale().y * helmDirection.getLocalScale().x;
	helmDirection.setHeight(midPanel.getHeight());
	helmDirection.setWidth(helmWidth);
	//helmDirection.setPosition(midPanel.getX() + (midPanel.getWidth() - helmWidth) / 2, midPanel.getY() - topHeight);
	helmDirection.setPosition(midPanel.getX() + (midPanel.getWidth() - helmWidth) / 2, midPanel.getY());

	// Add middle panel pivot point
	pivot = ship.getPivot();
	//pivot.setLocalTranslation(screenWidth / 2, screenHeight / 2 - topHeight / 2, 0);
	pivot.setLocalTranslation(screenWidth / 2, screenHeight / 2, 0);
    }

    public void EndScreen() {
	this.app.getGuiNode().detachAllChildren();
    }

    // Do the data update
    public void update(float tpf) {
	// Moves the ship
	ship.move(tpf);
	
	// Updates ship components
	ship.update(screen);
    }

    // Render images
    public void render() {
	// Draw planet if it's close enough
	if (planet.isOnSensors(ship.getSensorSphere(), ship.getLocation())) {
	    planet.drawPlanet(ship.getLocation(), planet.getPivot());
	} else {
	    planet.getPivot().detachAllChildren();
	}

	// Display the compass node over everything
	pivot.attachChild(ship.getShipImg());
	this.app.getGuiNode().attachChild(pivot);
	
	this.app.getGuiNode().attachChild(helmDirection);
    }

    public void getRotateLocation() {
	Vector3f mousePos = new Vector3f(this.app.getInputManager().getCursorPosition().x, this.app.getInputManager().getCursorPosition().y, 0);
	if (mousePos.x >= helmDirection.getLocalTranslation().x && mousePos.x <= helmDirection.getLocalTranslation().x + helmDirection.getLocalScale().x) {
	    if (mousePos.y >= helmDirection.getLocalTranslation().y && mousePos.y <= helmDirection.getLocalTranslation().y + helmDirection.getLocalScale().y) {
		Vector3f topMiddle = new Vector3f(screenWidth / 2, screenHeight, 0);
		ship.setShipTurnTo((float) Math.toDegrees(Math.atan2(mousePos.x - pivot.getLocalTranslation().x, mousePos.y - pivot.getLocalTranslation().y)
			- Math.atan2(topMiddle.x - pivot.getLocalTranslation().x, topMiddle.y - pivot.getLocalTranslation().y)));
		if (ship.getShipTurnTo() < 0) {
		    ship.setShipTurnTo(ship.getShipTurnTo() + 360);
		}
	    }
	}
	ship.setShipTurn(ship.getShipTurnTo() - ship.getShipHeading());
	if ((ship.getShipTurn() > 0 && ship.getShipTurn() < 180) || ship.getShipTurn() < -180) {
	    if (ship.getShipTurn() < -180) {
		ship.setShipTurn((360 - ship.getShipHeading()) + ship.getShipTurnTo());
	    }
	    ship.setTurnClock(true);
	} else {
	    if (ship.getShipTurn() > 180) {
		ship.setShipTurn((360 - ship.getShipTurnTo()) + ship.getShipHeading());
	    }
	    ship.setTurnClock(false);
	}
	ship.setShipTurn(FastMath.abs(ship.getShipTurn()));
    }

    // All Stop Button
    public void onAllStopClick() {
	System.out.println("All Stop!");
	screen.findNiftyControl("warpSlide", Slider.class).setValue(screen.findNiftyControl("warpSlide", Slider.class).getMax());
	screen.findNiftyControl("impulseSlide", Slider.class).setValue(screen.findNiftyControl("impulseSlide", Slider.class).getMax());
	ship.setShipSpeed(0);
    }

    public void initKeys() {
	this.app.getInputManager().addMapping("LMB", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	this.app.getInputManager().addListener(analogListener, "LMB");
	//this.app.getInputManager().addListener(actionListener, "LMB");
    }
    public ActionListener actionListener = new ActionListener() {
	public void onAction(String name, boolean keyPressed, float tpf) {
	    if (name.equals("LMB")) {
		getRotateLocation();
	    }
	}
    };
    public AnalogListener analogListener = new AnalogListener() {
	public void onAnalog(String name, float value, float tpf) {
	    if (name.equals("LMB")) {
		getRotateLocation();
	    }
	}
    };
}
