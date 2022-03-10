package com.codersdungeon.warp.engine.graphics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VertexTemplate {
    private static final Logger LOG = LoggerFactory.getLogger(VertexArray.class);

    private final int index;
    private final int elements;
    private final String description;

    public VertexTemplate(int index, int elements, String description) {
        LOG.debug("new vertex template '{}', '{}', '{}'", index, elements, description);
        this.index = index;
        this.elements = elements;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public int getElements() {
        return elements;
    }

    public String getDescription() {
        return description;
    }
}