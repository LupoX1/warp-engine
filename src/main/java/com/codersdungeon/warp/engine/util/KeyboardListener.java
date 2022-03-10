package com.codersdungeon.warp.engine.util;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardListener {

    private final boolean[] keyPressed;

    private static final KeyboardListener listener = new KeyboardListener();

    private KeyboardListener(){
        this.keyPressed = new boolean[350];
        Arrays.fill(this.keyPressed, false);
    }

    public static void onKey(long glfwWindow, int key, int scancode, int action, int mods){
        if(action == GLFW_PRESS){
            listener.keyPressed[key] = true;
        }else if(action == GLFW_RELEASE){
            listener.keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int key){
        return listener.keyPressed[key];
    }

}
