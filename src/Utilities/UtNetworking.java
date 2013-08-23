/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

/**
 *
 * @author Tony
 */
public class UtNetworking {

    public static final int PORT = 6000;

    public static void initializables() {
	Serializer.registerClass(NetworkMessage.class);
    }

    @Serializable
    public static class NetworkMessage extends AbstractMessage {

	private String message;

	public NetworkMessage() {
	}

	public NetworkMessage(String message) {
	    this.message = message;
	}

	public String getMessage() {
	    return message;
	}
    }
}
