package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public final class VertexArray implements Disposable {
    private static final Logger LOG = LoggerFactory.getLogger(VertexArray.class);

    private final int vaoID;

    VertexArray(int vaoID){
        LOG.debug("new vertex array: ID '{}'", vaoID);
        this.vaoID = vaoID;
    }

    public void bind(){
        LOG.debug("bind vertex array: ID '{}'", vaoID);
        glBindVertexArray(vaoID);
    }

    public void unbind(){
        LOG.debug("unbind vertex array: ID '{}'", vaoID);
        glBindVertexArray(0);
    }

    @Override
    public void dispose(){
        LOG.debug("dispose vertex array: ID '{}'", vaoID);
        if(vaoID != 0){
            unbind();
            glDeleteVertexArrays(vaoID);
        }
    }
}
