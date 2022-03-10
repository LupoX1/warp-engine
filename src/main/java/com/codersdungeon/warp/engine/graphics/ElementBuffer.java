package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL15.*;

public final class ElementBuffer implements Disposable {
    private static final Logger LOG = LoggerFactory.getLogger(ElementBuffer.class);

    private final int eboID;
    private final int size;

    ElementBuffer(int eboID, int size){
        LOG.debug("new element buffer: ID '{}' Size '{}'", eboID, size);
        this.eboID = eboID;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void bind(){
        LOG.debug("bind element buffer '{}'", eboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
    }

    public void unbind(){
        LOG.debug("unbind element buffer '{}'", eboID);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void dispose(){
        LOG.debug("dispose element buffer '{}'", eboID);

        if(eboID != 0){
            unbind();
            glDeleteBuffers(eboID);
        }
    }
}
