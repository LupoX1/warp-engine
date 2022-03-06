package com.codersdungeon.warp.engine.graphics.renderer;

import com.codersdungeon.warp.engine.graphics.Vao;

import static org.lwjgl.opengl.GL33.*;

public final class TriangleRenderer extends Renderer {

    private TriangleRenderer(){

    }

    public static TriangleRenderer create(){
        return new TriangleRenderer();
    }

    @Override
    void render(Vao vao) {
        glDrawElements(GL_TRIANGLES, vao.getEbo().getElementArray().length, GL_UNSIGNED_INT, 0);
    }
}
