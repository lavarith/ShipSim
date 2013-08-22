/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Anthony Bissell
 */
public class StartScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private SimpleApplication app;
    Helm helmControl;

    public StartScreen(SimpleApplication app) {
	this.app = app;
    }

    public void startGame(String screen) {
	helmControl = new Helm(this.app);
	nifty.registerScreenController(helmControl);
	nifty.addXml("Interface/HelmScreen.xml");
	this.app.getStateManager().attach(helmControl);
	nifty.gotoScreen(screen);
    }

    public void quitGame() {
	app.stop();     // Quit the game;
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
