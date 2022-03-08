package com.codersdungeon.warp.engine;

import com.codersdungeon.warp.engine.exceptions.InitializationException;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements LifeCycleComponent{
    private static final Logger LOG = LoggerFactory.getLogger(Window.class);

    private final String title;
    private final int width;
    private final int height;
    private long glfwWindow;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    @Override
    public void init() throws InitializationException {
        LOG.debug("Init window LWJGL version {}", Version.getVersion());

        initGlfw();
        createWindow();
        setCallbacks();
        centerWindow();
        showWindow();
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(glfwWindow);
    }

    public void update() {
        glfwSwapBuffers(glfwWindow);
        glfwPollEvents();
    }

    public void closeOnEsc(){
        if(KeyboardListener.isKeyPressed(GLFW_KEY_ESCAPE)){
            glfwSetWindowShouldClose(glfwWindow, true);
        }
    }

    public void setClearColor(float r, float g, float b, float a){
        glClearColor(r, g, b, a);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        LOG.debug("Destroy");
        try {
            glfwFreeCallbacks(glfwWindow);
            glfwDestroyWindow(glfwWindow);

            glfwTerminate();
            Objects.requireNonNull(glfwSetErrorCallback(null)).free();
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
    }

    private void initGlfw() throws InitializationException {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new InitializationException("Cannot initialize GLFW");
        }
    }

    private void setCallbacks() {
        glfwSetCursorPosCallback(glfwWindow, MouseListener::onMouseMove);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::onMouseButton);
        glfwSetScrollCallback(glfwWindow, MouseListener::onMouseScroll);
        glfwSetKeyCallback(glfwWindow, KeyboardListener::onKey);
    }

    private void createWindow() throws InitializationException {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new InitializationException("Cannot create GLFW window");
        }
    }

    private void centerWindow(){
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (videoMode != null) {
            glfwSetWindowPos(
                    glfwWindow,
                    (videoMode.width() - width) / 2,
                    (videoMode.height() - height) / 2
            );
        }
    }

    private void showWindow() {
        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();
    }
}
