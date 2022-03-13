package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.Disposable;
import org.lwjgl.stb.STBImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class Image implements Disposable {
    private static final Logger LOG = LoggerFactory.getLogger(Image.class);

    private final int width;
    private final int height;
    private final int components;
    private final ByteBuffer buffer;

    public Image(int width, int height, int components, ByteBuffer buffer) {
        LOG.debug("new image w:'{}' h:'{}' c:'{}'", width, height, components);
        this.width = width;
        this.height = height;
        this.components = components;
        this.buffer = buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getComponents() {
        return components;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void dispose() {
        LOG.debug("dispose image");
        STBImage.stbi_image_free(buffer);
    }
}
