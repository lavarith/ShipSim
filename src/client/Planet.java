/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Tony
 */
public class Planet {

    SimpleApplication app;
    float radius;
    Vector2f position, screenPosition;
    Sphere circle;
    Geometry sphereGeo;
    Node pivot;

    public Planet(SimpleApplication app, Vector2f position, float radius) {
	this.app = app;
	this.position = position;
	this.radius = radius;

	pivot = new Node("Planet");
	pivot.setLocalTranslation(this.app.getContext().getSettings().getWidth() / 2, this.app.getContext().getSettings().getHeight() / 2, 0);

	circle = new Sphere(3, 100, this.radius);
	sphereGeo = new Geometry("Planet", circle);
	Material circleMat = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
	circleMat.setColor("Color", ColorRGBA.Blue);
	sphereGeo.setMaterial(circleMat);
	sphereGeo.setLocalTranslation(position.x, position.y, 0);
    }

    public Node getPivot() {
	return pivot;
    }

    public Geometry getSphereGeo() {
	return sphereGeo;
    }

    public void drawPlanet(Vector2f shipLocation, Node pivot) {
	Vector2f newposition = position.subtract(shipLocation);
	sphereGeo.setLocalTranslation(newposition.x, newposition.y, 0);
	this.app.getGuiNode().attachChild(pivot);
	pivot.attachChild(sphereGeo);
    }

//    public boolean isOnSensors(Vector2f shipLocation, float sensorRange) {
//	if (position.distance(shipLocation) <= sensorRange) {
//	    return true;
//	}else{
//	    return false;
//	}
//    }
    public boolean isOnSensors(BoundingSphere shipSensors, Vector2f shipLocation) {
	Sphere circle2 = new Sphere(100,100,100);
	Geometry sphereGeo2 = new Geometry("Planet 2", circle2);
	Vector2f newposition = position.subtract(shipLocation);
	sphereGeo2.setLocalTranslation(newposition.x, newposition.y, 0);
	CollisionResults collisionResult = new CollisionResults();
	sphereGeo2.collideWith(shipSensors, collisionResult);
	if (collisionResult.size()>0) {
	    return true;
	} else {
	    return false;
	}
    }

    public float getRadius() {
	return radius;
    }

    public Vector2f getPosition() {
	return position;
    }
}
