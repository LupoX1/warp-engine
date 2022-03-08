package com.codersdungeon.warp.engine;

import com.codersdungeon.warp.engine.exceptions.InitializationException;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements LifeCycleComponent{
    private static final Logger LOG = LoggerFactory.getLogger(Window.class);

    private String title;
    private int width;
    private int height;
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
        showWindow();
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(glfwWindow);
    }

    public void update() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glfwSwapBuffers(glfwWindow);
        glfwPollEvents();
    }

    public void closeOnEsc(){
        if(KeyboardListener.isKeyPressed(GLFW_KEY_ESCAPE)){
            glfwSetWindowShouldClose(glfwWindow, true);
        }
    }

    @Override
    public void destroy() {
        LOG.debug("Destroy");
        try {
            glfwFreeCallbacks(glfwWindow);
            glfwDestroyWindow(glfwWindow);

            glfwTerminate();
            glfwSetErrorCallback(null).free();
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

    private void resizeWindow() throws InitializationException {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(glfwWindow, width, height);

            GLFWVidMode glfwVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                glfwWindow,
                (glfwVidMode.width() - width.get(0)) / 2,
                (glfwVidMode.height() - height.get(0)) / 2
            );
        }catch (Exception ex){
            throw new InitializationException(ex);
        }
    }

    private void showWindow() {
        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();
        glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
    }
}
