package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
    private Vector3f postion;
    private Vector3f colour;

    public Light(Vector3f postion, Vector3f colour) {
        this.postion = postion;
        this.colour = colour;
    }

    public Vector3f getPostion() {
        return this.postion;
    }

    public void setPostion(Vector3f postion) {
        this.postion = postion;
    }

    public Vector3f getColour() {
        return this.colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

}
