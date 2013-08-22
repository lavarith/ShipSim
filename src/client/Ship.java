/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

/**
 *
 * @author Tony
 */
public class Ship {

    private SimpleApplication app;
    // Helm Image Vars
    float shipWidth, shipHeight;
    Picture shipImg;
    // Movement Vars
    Node pivot;
    Vector2f location;
    float warp, impulse, shipHeading, shipTurnTo, shipTurnSpeed, shipTurn,
	    shipSpeed, shipMaxSpeed;
    Boolean turnClock;
    // Sensor Vars
    int sensordistance;
    BoundingSphere sensorSphere;
    // Energy Vars
    float energy;
    
    // State Vars
    Boolean shieldsUp;

    public Ship(SimpleApplication app) {
	this.app = app;
	// Ship location/speed details
	shipHeading = 0;
	shipTurnTo = 0;
	shipSpeed = 0;
	shipMaxSpeed = 74948115; // 1/4 speed of light
	shipWidth = 50;
	shipHeight = 50;
	shipTurnSpeed = 100;
	shipTurn = 0;
	turnClock = true;
	shieldsUp = false;

	shipImg = new Picture("Ship Helm");
	shipImg.setImage(this.app.getAssetManager(), "Images/shipHelm.png", true);
	shipImg.setPosition(0, 0);
	shipImg.setHeight(shipWidth);
	shipImg.setWidth(shipHeight);
	shipImg.setLocalTranslation(-shipWidth / 2, -shipHeight / 2, 0);   // center image on pivot point

	pivot = new Node("pivot");
	location = new Vector2f(0, 0);

	// ship Sensor details
	sensordistance = 300;
	sensorSphere = new BoundingSphere(sensordistance, new Vector3f(location.x, location.y, 0));
    }

    public void move(float tpf) {
	if (shipHeading != shipTurnTo) {
	    turnShip(tpf);
	}
	location.set(location.x + ((tpf * shipSpeed) * FastMath.sin(shipHeading * FastMath.DEG_TO_RAD)),
		location.y + ((tpf * shipSpeed) * FastMath.cos(shipHeading * FastMath.DEG_TO_RAD)));
	shipSpeed += impulse / 100;
	// set shipSpeed = max speed if it exceeds maxspeed
	if (shipSpeed > shipMaxSpeed) {
	    shipSpeed = shipMaxSpeed;
	}
	System.out.println("X:" + location.x + " Y:" + location.y + " A:" + shipHeading + " S:" + shipSpeed);
    }

    public void turnShip(float tpf) {
	shipTurn -= shipTurnSpeed * tpf;
	if (turnClock) {
	    shipHeading += shipTurnSpeed * tpf;
	} else {
	    shipHeading -= shipTurnSpeed * tpf;
	}
	if (shipHeading >= 360) {
	    shipHeading -= 360;
	}
	if (shipTurn < 0) {
	    shipTurn = 0;
	    shipHeading = shipTurnTo;
	}
	Quaternion roll045 = new Quaternion(); // create the quaternion
	roll045.fromAngleAxis(-shipHeading * FastMath.DEG_TO_RAD, Vector3f.UNIT_Z); // supply angle and axis as arguments)
	pivot.setLocalRotation(roll045);
	System.out.println("A:" + shipHeading + " B:" + shipTurn);
    }

    public float getSensordistance() {
	return sensordistance;
    }

    public Vector2f getLocation() {
	return location;
    }

    public float getWarp() {
	return warp;
    }

    public void setWarp(float warp) {
	this.warp = warp;
    }

    public float getImpulse() {
	return impulse;
    }

    public void setImpulse(float impulse) {
	this.impulse = impulse;
    }

    public float getShipHeading() {
	return shipHeading;
    }

    public void setShipHeading(float shipHeading) {
	this.shipHeading = shipHeading;
    }

    public float getShipTurnTo() {
	return shipTurnTo;
    }

    public void setShipTurnTo(float shipTurnTo) {
	this.shipTurnTo = shipTurnTo;
    }

    public float getShipTurn() {
	return shipTurn;
    }

    public void setShipTurn(float shipTurn) {
	this.shipTurn = shipTurn;
    }

    public float getShipSpeed() {
	return shipSpeed;
    }

    public void setShipSpeed(float shipSpeed) {
	this.shipSpeed = shipSpeed;
    }

    public Boolean getTurnClock() {
	return turnClock;
    }

    public void setTurnClock(Boolean turnClock) {
	this.turnClock = turnClock;
    }

    public Node getPivot() {
	return pivot;
    }

    public void setPivot(Node pivot) {
	this.pivot = pivot;
    }

    public Picture getShipImg() {
	return shipImg;
    }

    public BoundingSphere getSensorSphere() {
	return sensorSphere;
    }

    public Boolean getShieldsUp() {
	return shieldsUp;
    }

    public void setShieldsUp(Boolean shieldsUp) {
	this.shieldsUp = shieldsUp;
    }
}
