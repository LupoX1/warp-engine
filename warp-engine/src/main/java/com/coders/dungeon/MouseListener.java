package com.coders.dungeon;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private double mouseX, mouseY, lastX, lastY, scrollX, scrollY;

    private final boolean[] mousePressed;
    private boolean mouseDrag;
    private int mouseButtonsPressed;

    private static final MouseListener listener = new MouseListener();

    private MouseListener(){
        this.mouseX = 0;
        this.mouseY = 0;
        this.lastX = 0;
        this.lastY = 0;
        this.scrollX = 0;
        this.scrollY = 0;
        this.mousePressed = new boolean[20];
        this.mouseButtonsPressed = 0;
        this.mouseDrag = false;
        Arrays.fill(this.mousePressed, false);
    }

    public static void onMouseMove(long window, double xPos, double yPos){
        listener.lastX = listener.mouseX;
        listener.lastY = listener.mouseY;
        listener.mouseX = xPos;
        listener.mouseY = yPos;
        listener.mouseDrag = listener.mouseButtonsPressed > 0;
    }

    public static void onMouseScroll(long window, double xOffset, double yOffset){
        listener.scrollX = xOffset;
        listener.scrollY = yOffset;
    }

    public static void onMouseButton(long window, int button, int action, int mods){
        if(action == GLFW_PRESS){
            listener.mouseButtonsPressed++;
            listener.mousePressed[button] = true;
        }else if(action == GLFW_RELEASE){
            listener.mouseButtonsPressed--;
            listener.mousePressed[button] = false;
            listener.mouseDrag = false;
        }
    }

    public static void endFrame(){
        listener.lastX = listener.mouseX;
        listener.lastY = listener.mouseY;
        listener.scrollX = 0;
        listener.scrollY = 0;
    }

    public static double getX(){
        return listener.mouseX;
    }

    public static double getY(){
        return listener.mouseY;
    }

    public static double getDx(){
        return listener.lastX - listener.mouseX;
    }

    public static double getDy(){
        return listener.lastY - listener.mouseY;
    }

    public static double getScrollX(){
        return listener.scrollX;
    }

    public static double getScrollY(){
        return listener.scrollY;
    }

    public static boolean isMousePressed(int button){
        return listener.mousePressed[button];
    }

    public static boolean isDragging(){
        return listener.mouseDrag;
    }

}
