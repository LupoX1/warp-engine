package com.codersdungeon.warp.engine.util;

import com.codersdungeon.warp.engine.graphics.Image;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Resources {
    private static final Logger LOG = LoggerFactory.getLogger(Resources.class);

    private Resources(){}

    public static ByteBuffer loadResource(String resource){
        try(InputStream inputStream = Resources.class.getClassLoader().getResourceAsStream(resource);
            ReadableByteChannel channel = Channels.newChannel(inputStream)){
            ByteBuffer byteBuffer = BufferUtils.createByteBuffer(inputStream.available());
            channel.read(byteBuffer);
            return byteBuffer;
        } catch (Exception e) {
            LOG.error("Error loading resource '" + resource + "'", e);
            throw new RuntimeException(e);
        }
    }

    public static String loadTextResource(String resource){
        Charset charset = StandardCharsets.UTF_8;
        ByteBuffer textBuffer = loadResource(resource);
        textBuffer.flip();
        String resourceContent = charset.decode(textBuffer).toString();
        textBuffer.clear();
        return resourceContent;
    }

    public static Image loadPng(String imagePath){
        ByteBuffer imageBuffer = loadResource(imagePath);
        imageBuffer.flip();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w    = stack.mallocInt(1);
            IntBuffer h    = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

         /*   if(!STBImage.stbi_info_from_memory(imageBuffer, w, h, channels)){
                throw new RuntimeException("Cannot load information '"+imagePath+"' : " + STBImage.stbi_failure_reason());
            }
*/


            ByteBuffer buffer = STBImage.stbi_load_from_memory(imageBuffer, w, h, channels, 0);
            if (buffer == null) {
                throw new RuntimeException("Failed to load image '"+imagePath+"' : " + STBImage.stbi_failure_reason());
            }

            int width = w.get(0);
            int height = h.get(0);
            int components = channels.get(0);

            return new Image(width, height, components, buffer);
        }finally {
            imageBuffer.clear();
        }
    }
}
