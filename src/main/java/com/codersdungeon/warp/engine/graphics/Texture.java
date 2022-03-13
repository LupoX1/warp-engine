package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.Disposable;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture implements Disposable {
    private final int textureId;

    public Texture(int textureId){
        this.textureId = textureId;
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void setParameters(){
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    public void setData(Image image){
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, image.getWidth(), image.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, image.getBuffer());
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void enable() {
        glActiveTexture(GL_TEXTURE0);
        bind();
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void dispose() {
        glDeleteTextures(textureId);
    }
}
