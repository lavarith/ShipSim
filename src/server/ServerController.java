/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Utilities.UtNetworking;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.network.Server;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Tony
 */
public class ServerController extends AbstractAppState implements ScreenController{

    Server server;
    SimpleApplication app;
    Nifty nifty;
    Screen screen;
    
    public static void main(String[] args) {
	UtNetworking.initializables();
    }
    
    public ServerController(SimpleApplication app, Server server){
	this.app = app;
	this.server = server;
    }
    
    public void bind(Nifty nifty, Screen screen) {
	this.nifty = nifty;
	this.screen = screen;
    }

    public void onStartScreen() {
	System.out.println("Boom!");
    }

    public void onEndScreen() {
	throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
