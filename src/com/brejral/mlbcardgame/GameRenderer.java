package com.brejral.mlbcardgame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;

public class GameRenderer implements Renderer {
	private static Background background;
	private Context mContext;
	
	private FloatBuffer mCubeTextureCoordinates;
	private int mTextureUniformHandle;
	private int mTextureCoordinateHandle;
	private final int mTextureCoordinateDataSize = 2;
	private int mTextureDataHandle;
	
	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjMatrix = new float[16];
	private final float[] mVMatrix = new float[16];
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		background = new Background(mContext);
	}	
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// Draw Background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		// Set the camera position (View matrix)
		Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0f);
		
		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
		
		// Draw the background
		background.draw(mMVPMatrix);
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width/height;
		
		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	
	}
	
	public static int loadShader(int type, String shaderCode) {
		// Create a Vertex Shader Type Or a Fragment Shader Type (GLES20.GL_VERTEX_SHADER OR GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);
		
		// Add the source code and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		
		return shader;
	}
	
	public GameRenderer(Context context) {
		mContext = context;
	}
	
	class Background {
		
		private final String vertexShaderCode = 
					"attribute vec2 a_TexCoordinate;" +
					"varying vec2 v_TexCoordinate;" +
					"uniform mat4 uMVPMatrix;" +
					"attribute vec4 vPosition;" +
					"void main() {" +
					"	gl_Position = vPosition * uMVPMatrix;" +
					" 	v_TexCoordinate = a_TexCoordinate;" +
					"}";
		
		private final String fragmentShaderCode = 
					"precision mediump float;" +
					"uniform vec4 vColor;" +
					"uniform sampler2D u_Texture;" +
					"varying vec2 v_TexCoordinate;" +
					"void main() {" +
					" 	gl_FragColor = (v_Color * texture2D(u_Texture, v_TexCoordinate));" +
					"}";
		
		private final int shaderProgram;
		private final FloatBuffer vertexBuffer;
		private final ShortBuffer drawListBuffer;
		private int mPositionHandle;
		private int mColorHandle;
		private int mMVPMatrixHandle;
		
		static final int COORDS_PER_VERTEX = 3;
		private final int vertexStride = COORDS_PER_VERTEX * 4;
				
		private Context mContext;
		
		private float vertices[] = {
				0.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,
				1.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f,				
		};
				
		private short drawOrder[] = {
				0,1,2,
				0,2,3,
		};
		
		float color[] = {0f, 0f, 0f, 1f};
		
		public Background(Context context) {
			mContext = context;
			
			ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
			bb.order(ByteOrder.nativeOrder());
			vertexBuffer = bb.asFloatBuffer();
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);
			
			final float[] cubeTextureCoordinateData = {
					-1f,  1f,
					-1f, -1f,
					 1f, -1f,
					 1f,  1f,
			};
			
			mCubeTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mCubeTextureCoordinates.put(cubeTextureCoordinateData).position(0);
			
			ByteBuffer dlb = ByteBuffer.allocateDirect(vertices.length * 2);
			dlb.order(ByteOrder.nativeOrder());
			drawListBuffer = dlb.asShortBuffer();
			drawListBuffer.put(drawOrder);
			drawListBuffer.position(0);
			
			int vertexShader = GameRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
			int fragmentShader = GameRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
			
			shaderProgram = GLES20.glCreateProgram();
			GLES20.glAttachShader(shaderProgram, vertexShader);
			GLES20.glAttachShader(shaderProgram, fragmentShader);
			
			// Texture Code
			GLES20.glBindAttribLocation(shaderProgram, 0, "a_TexCoordinate");
			
			GLES20.glLinkProgram(shaderProgram);
			
			// Load the Texture
			mTextureDataHandle = loadTexture(mContext, R.drawable.baseball_diamond);
		}
		
		public void draw(float[] mvpMatrix) {
			GLES20.glUseProgram(shaderProgram);
			
			mPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
			
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			
			GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
			
			mColorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
			
			GLES20.glUniform4fv(mColorHandle, 1, color, 0);
			
			mTextureUniformHandle = GLES20.glGetAttribLocation(shaderProgram, "u_Texture");
			mTextureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgram, "a_TexCoordinate");
			
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
			
			GLES20.glUniform1i(mTextureUniformHandle, 0);
			
			mCubeTextureCoordinates.position(0);
			GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 0, mCubeTextureCoordinates);
			GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
			
			mMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix");
			
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
			
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
			
			GLES20.glDisableVertexAttribArray(mPositionHandle);			
		}
		
		public int loadTexture(final Context context, final int resourceId) {
			final int[] textureHandle = new int[1];
			
			GLES20.glGenTextures(1, textureHandle, 0);
			
			if(textureHandle[0] != 0) {
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inScaled = false;
				
				final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
				
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
				
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
				
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
				
				bitmap.recycle();
			}
			
			if (textureHandle[0] == 0) {
				throw new RuntimeException("Error loading texture.");
			}
			
			return textureHandle[0];
		}
	}

}
