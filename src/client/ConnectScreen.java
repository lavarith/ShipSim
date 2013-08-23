/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Tony
 */
public class ConnectScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private SimpleApplication app;
    
    public ConnectScreen(SimpleApplication app) {
	this.app = app;
    }

    public void bind(Nifty nifty, Screen screen) {
	this.nifty = nifty;
	this.screen = screen;
    }

    public void onStartScreen() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
	throw new UnsupportedOperationException("Not supported yet.");
    }
}
