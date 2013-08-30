/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Tony
 */
@Serializable
public class ServerPlanet {

    Vector3f location;
    int radius;

    public ServerPlanet() {
    }

    public ServerPlanet(Vector3f location, int radius) {
	this.location = location;
	this.radius = radius;
    }

    public int getRadius() {
	return radius;
    }

    public Vector3f getLocation() {
	return location;
    }
}
