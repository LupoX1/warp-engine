package com.codersdungeon.warp.engine.graphics.renderer;

import com.codersdungeon.warp.engine.graphics.ShaderProgram;
import com.codersdungeon.warp.engine.graphics.Vao;

public abstract class Renderer {

    Renderer(){}

    public void render(float v, ShaderProgram shaderProgram, Vao vao) {
        shaderProgram.bind();
        vao.enable();
        this.render(vao);
        vao.disable();
        shaderProgram.unbind();
    }

    abstract void render(Vao vao);
}
