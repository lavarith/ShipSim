/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Utilities.UtNetworking;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;
import com.sun.istack.internal.logging.Logger;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author Tony
 */
public class ServerMain extends SimpleApplication {

    private Server server;
    int serverPort;

    public static void main(String[] args) {

	UtNetworking.initializables();

	ServerMain app = new ServerMain();
	app.start(JmeContext.Type.Display); // headless type for servers!
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
}
