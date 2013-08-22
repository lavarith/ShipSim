/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
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
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.text.DecimalFormat;

/**
 *
 * @author Tony
 */
public class Helm extends AbstractAppState implements ScreenController {

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

    public Helm(SimpleApplication app) {
	this.app = app;
	screenWidth = this.app.getContext().getSettings().getWidth();
	screenHeight = this.app.getContext().getSettings().getHeight();

	// Create Direction Image
	helmDirection = new Picture("Helm Direction");
	helmDirection.setImage(this.app.getAssetManager(), "Images/helmticks.png", true);
	helmDirection.setPosition(0, 0);

	ship = new Ship(this.app);
	planet = new Planet(this.app, new Vector2f(200, 200), 100);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
	super.initialize(stateManager, app);
	this.app = (SimpleApplication) app;
	initKeys();
    }

    public void bind(Nifty nifty, Screen screen) {
	this.nifty = nifty;
	this.screen = screen;
    }

    public void onStartScreen() {
	// Remove random colorization for nifty gui
	this.nifty.setDebugOptionPanelColors(false);

	//throw new UnsupportedOperationException("Not supported yet.");
	topHeight = nifty.getCurrentScreen().findElementByName("panel_top").getHeight();

	// Set Helm Ticks Info
	midPanel = nifty.getCurrentScreen().findElementByName("panel_mid");
	float helmWidth = midPanel.getHeight() / helmDirection.getLocalScale().y * helmDirection.getLocalScale().x;
	helmDirection.setHeight(midPanel.getHeight());
	helmDirection.setWidth(helmWidth);
	helmDirection.setPosition(midPanel.getX() + (midPanel.getWidth() - helmWidth) / 2, midPanel.getY() - topHeight);

	// Add middle panel pivot point
	pivot = ship.getPivot();
	pivot.setLocalTranslation(screenWidth / 2, screenHeight / 2 - topHeight / 2, 0);
    }

    public void onEndScreen() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(float tpf) {
	/**
	 * jME update loop!
	 */
	ship.move(tpf);

	// Draw planet if it's close enough
	if (planet.isOnSensors(ship.getSensorSphere(), ship.getLocation())) {
	    planet.drawPlanet(ship.getLocation(), planet.getPivot());
	} else {
	    planet.getPivot().detachAllChildren();
	}

	// Display the compass node over everything
	this.app.getGuiNode().attachChild(helmDirection);
	pivot.attachChild(ship.getShipImg());
	this.app.getGuiNode().attachChild(pivot);

	// Updates ship components
	ship.update(screen);
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

    // Warp Slider changes
    @NiftyEventSubscriber(id = "warpSlide")
    public void onWarpSliderChange(final String id, final SliderChangedEvent event) {
	ship.setWarp(event.getSlider().getMax() - event.getValue());
	if (ship.getWarp() >= 9) {
	    warpString = "max";
	} else {
	    warpString = dformat.format(ship.getWarp());
	}
	screen.findElementByName("warpText").getRenderer(TextRenderer.class).setText(warpString);
	System.out.println(dformat.format(ship.getWarp()));
    }

    // Impulse Slider changes
    @NiftyEventSubscriber(id = "impulseSlide")
    public void onImpulseSliderChange(final String id, final SliderChangedEvent event) {
	ship.setImpulse(event.getSlider().getMax() - event.getValue());
	impulseString = dformat.format(ship.getImpulse());
	screen.findElementByName("impulseText").getRenderer(TextRenderer.class).setText(impulseString);
	System.out.println(dformat.format(ship.getImpulse()));
    }

    // All Stop Button
    public void onAllStopClick() {
	screen.findNiftyControl("warpSlide", Slider.class).setValue(screen.findNiftyControl("warpSlide", Slider.class).getMax());
	screen.findNiftyControl("impulseSlide", Slider.class).setValue(screen.findNiftyControl("impulseSlide", Slider.class).getMax());
	ship.setShipSpeed(0);
    }

    private void initKeys() {
	this.app.getInputManager().addMapping("LMB", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	this.app.getInputManager().addListener(analogListener, "LMB");
	//this.app.getInputManager().addListener(actionListener, "LMB");
    }
    private ActionListener actionListener = new ActionListener() {
	public void onAction(String name, boolean keyPressed, float tpf) {
	    if (name.equals("LMB")) {
		getRotateLocation();
	    }
	}
    };
    private AnalogListener analogListener = new AnalogListener() {
	public void onAnalog(String name, float value, float tpf) {
	    if (name.equals("LMB")) {
		getRotateLocation();
	    }
	}
    };
}
