package com.codersdungeon.warp.engine.scenes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class SceneManager2D {
    private static final Logger LOG = LoggerFactory.getLogger(SceneManager2D.class);
    private static final SceneManager2D manager = new SceneManager2D();

    private final Stack<Scene2D> scenes;

    private SceneManager2D(){
        scenes = new Stack<>();
    }

    public static Scene2D popScene(){
        return manager.scenes.pop();
    }

    public static void pushScene(Scene2D scene){
        manager.scenes.push(scene);
    }
}
