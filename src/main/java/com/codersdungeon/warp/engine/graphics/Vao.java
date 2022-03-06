package com.codersdungeon.warp.engine.graphics;

import static org.lwjgl.opengl.GL33.*;

public final class Vao {
    private final int vaoID;
    private Vbo vbo;
    private Ebo ebo;

    private Vao(int vaoID){
        this.vaoID = vaoID;
    }

    private void setVbo(Vbo vbo) {
        this.vbo = vbo;
    }

    private void setEbo(Ebo ebo) {
        this.ebo = ebo;
    }

    private void bind(){
        glBindVertexArray(vaoID);
    }

    private void unbind(){
        glBindVertexArray(0);
    }

    public Ebo getEbo() {
        return ebo;
    }

    public void enable(){
        bind();
        vbo.enableArrays();
    }

    public void disable(){
        vbo.disableArrays();
        unbind();
    }

    public void delete(){
        glDeleteVertexArrays(vaoID);
        vbo.delete();
        ebo.delete();
    }

    public static Vao create(VertexArray vertexArray, int[] elementArray){

        int vaoID = glGenVertexArrays();
        Vao vao = new Vao(vaoID);
        vao.bind();

        Vbo vbo = Vbo.createBuffer(vertexArray);
        vao.setVbo(vbo);

        Ebo ebo = Ebo.createBuffer(elementArray);
        vao.setEbo(ebo);

        vao.unbind();

        return vao;
    }


}
