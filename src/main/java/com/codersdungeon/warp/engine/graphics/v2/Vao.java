package com.codersdungeon.warp.engine.graphics.v2;

import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public final class Vao {
    private final int vaoID;
    private final List<Vbo> vbos;
    private Ebo ebo;

    private Vao(int vaoID, List<Vbo> vbos){
        this.vaoID = vaoID;
        this.vbos = vbos;
    }

    private void bind(){
        glBindVertexArray(vaoID);
    }

    private void unbind(){
        glBindVertexArray(0);
    }

    public void enable(){
        bind();
        vbos.forEach(Vbo::enable);
    }

    public void disable(){
        vbos.forEach(Vbo::disable);
        unbind();
    }

    public void delete(){
        glDeleteVertexArrays(vaoID);
        vbos.forEach(Vbo::dispose);
        ebo.delete();
    }

    public static Vao create(List<Vbo> vbos, int[] elementArray){

        int vaoID = glGenVertexArrays();
        Vao vao = new Vao(vaoID, vbos);
        vao.bind();

        Ebo ebo = Ebo.createBuffer(elementArray);
        vao.setEbo(ebo);

        vao.unbind();

        return vao;
    }

    private void setEbo(Ebo ebo) {
        this.ebo = ebo;
    }


}
