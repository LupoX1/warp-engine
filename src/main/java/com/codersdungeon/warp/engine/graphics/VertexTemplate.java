package com.codersdungeon.warp.engine.graphics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public final class VertexTemplate implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(VertexArray.class);

    private int index;
    private int elements;
    private String description;

    public VertexTemplate(){

    }

    public VertexTemplate(int index, int elements, String description) {
        LOG.debug("new vertex template '{}', '{}', '{}'", index, elements, description);
        this.index = index;
        this.elements = elements;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getElements() {
        return elements;
    }

    public void setElements(int elements) {
        this.elements = elements;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}