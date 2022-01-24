package com.codersdungeon.warp.engine;

import com.codersdungeon.warp.engine.scenes.SceneManager2D;
import com.codersdungeon.warp.engine.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Properties;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    private int width, height;
    private String title;
    private long glfwWindow;

    public Application(){
        LOG.debug("new");
    }

    public void run() {
        LOG.debug("Run");
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties")){
            load(inputStream);
            init();
            loop();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        } finally {
            destroy();
        }
    }

    private void load(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);

        this.width = Integer.parseInt(properties.getProperty("window.width"));
        this.height = Integer.parseInt(properties.getProperty("window.height"));
        this.title = properties.getProperty("window.title");
    }

    private void init() {
        LOG.debug("Init application LWJGL version {}", Version.getVersion());

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Cannot initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (glfwWindow == NULL) {
            throw new RuntimeException("Cannot create GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::onMouseMove);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::onMouseButton);
        glfwSetScrollCallback(glfwWindow, MouseListener::onMouseScroll);
        glfwSetKeyCallback(glfwWindow, KeyboardListener::onKey);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(glfwWindow, width, height);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    glfwWindow,
                    (vidmode.width() - width.get(0)) / 2,
                    (vidmode.height() - height.get(0)) / 2
            );

            glfwMakeContextCurrent(glfwWindow);

            glfwSwapInterval(1);

            glfwShowWindow(glfwWindow);
        }
    }

    private void loop() {
        LOG.debug("Main loop");

        GL.createCapabilities();

        glClearColor(1.0f, 1.0f, 0.0f, 0.0f);

        long startTime = Time.getNanoTime();

        SceneManager2D.currentScene().enter();

        while (!glfwWindowShouldClose(glfwWindow)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);

            glfwPollEvents();

            glfwSetWindowShouldClose(glfwWindow, KeyboardListener.isKeyPressed(GLFW_KEY_ESCAPE));

            long currentTime = Time.getNanoTime();
            long deltaTime = currentTime - startTime;
            startTime = currentTime;
            double fps = 1E9 / deltaTime;

            SceneManager2D.currentScene().update(deltaTime);
            SceneManager2D.currentScene().render(deltaTime);
        }

        SceneManager2D.currentScene().exit();
    }

    private void destroy() {
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
}
