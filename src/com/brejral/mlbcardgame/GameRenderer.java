package com.brejral.mlbcardgame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public class GameRenderer implements Renderer {
	@Override
	public void onDrawFrame(GL10 gl) {
		
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0f, 1f, 0f, 1f, -1f, 1f);
	}

	@Override
	public void onSurfaceCreated(GL10 gl,
			javax.microedition.khronos.egl.EGLConfig config) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
	}
	
	class Background {
		
		public Background() {
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
		}
	}

}
