package com.brejral.mlbcardgame.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.brejral.mlbcardgame.R;
import com.brejral.mlbcardgame.common.RawResourceReader;
import com.brejral.mlbcardgame.common.ShaderHelper;
import com.brejral.mlbcardgame.common.TextureHelper;


/**
 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
 * renderer -- the static class GLES20 is used instead.
 */
public class GameRenderer implements GLSurfaceView.Renderer 
{	
	/** Used for debug logs. */
	private static final String TAG = "GameRenderer";

	private final Context mActivityContext;
	
	private Game game; 

	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];

	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	private float[] mViewMatrix = new float[16];

	/** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
	private float[] mProjectionMatrix = new float[16];

	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	private float[] mMVPMatrix = new float[16];

	/** Store our model data in a float buffer. */
	private final FloatBuffer mImagePositions;
	private final FloatBuffer mImageColors;
	private final FloatBuffer mImageNormals;
	private final FloatBuffer mImageTextureCoordinates;

	/** This will be used to pass in the transformation matrix. */
	private int mMVPMatrixHandle;

	/** This will be used to pass in the modelview matrix. */
	private int mMVMatrixHandle;

	/** This will be used to pass in the texture. */
	private int mTextureUniformHandle;

	/** This will be used to pass in model position information. */
	private int mPositionHandle;

	/** This will be used to pass in model texture coordinate information. */
	private int mTextureCoordinateHandle;

	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;	

	/** Size of the position data in elements. */
	private final int mPositionDataSize = 3;	

	/** Size of the texture coordinate data in elements. */
	private final int mTextureCoordinateDataSize = 2;


	/** This is a handle to our image shading program. */
	private int mProgramHandle;

	/** This is a handle to our texture data. */
	private int mBackgroundTextureDataHandle;
	

	/**
	 * Initialize the model data.
	 */
	public GameRenderer(final Context activityContext, Game inputgame)
	{	
		mActivityContext = activityContext;
		game = inputgame;

		// Define points for an image.		

		// X, Y, Z
		final float[] imagePositionData =
		{
				// In OpenGL counter-clockwise winding is default. This means that when we look at a triangle, 
				// if the points are counter-clockwise we are looking at the "front". If not we are looking at
				// the back. OpenGL has an optimization where all back-facing triangles are culled, since they
				// usually represent the backside of an object and aren't visible anyways.

				// Front face
				-1.0f, 1.0f, 0.0f,				
				-1.0f, -1.0f, 0.0f,
				1.0f, 1.0f, 0.0f, 
				-1.0f, -1.0f, 0.0f, 				
				1.0f, -1.0f, 0.0f,
				1.0f, 1.0f, 0.0f,

		};	

		// R, G, B, A
		final float[] imageColorData =
		{				
				// Front face (red)
				0.0f, 0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 0.0f, 1.0f,

		};

		// X, Y, Z
		// The normal is used in light calculations and is a vector which points
		// orthogonal to the plane of the surface. For a image model, the normals
		// should be orthogonal to the points of each face.
		final float[] imageNormalData =
		{												
				// Front face
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,

		};

		// S, T (or X, Y)
		// Texture coordinate data.
		// Because images have a Y axis pointing downward (values increase as you move down the image) while
		// OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
		// What's more is that the texture coordinates are the same for every face.
		final float[] imageTextureCoordinateData =
		{												
				// Front face
				0.0f, 0.0f, 				
				0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f,				

		};

		// Initialize the buffers.
		mImagePositions = ByteBuffer.allocateDirect(imagePositionData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mImagePositions.put(imagePositionData).position(0);		

		mImageColors = ByteBuffer.allocateDirect(imageColorData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mImageColors.put(imageColorData).position(0);

		mImageNormals = ByteBuffer.allocateDirect(imageNormalData.length * mBytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mImageNormals.put(imageNormalData).position(0);

		mImageTextureCoordinates = ByteBuffer.allocateDirect(imageTextureCoordinateData.length * mBytesPerFloat)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mImageTextureCoordinates.put(imageTextureCoordinateData).position(0);
	}

	protected String getVertexShader()
	{
		return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader);
	}

	protected String getFragmentShader()
	{
		return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader);
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) 
	{
		// Set the background clear color to black.
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);

		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		// The below glEnable() call is a holdover from OpenGL ES 1, and is not needed in OpenGL ES 2.
		// Enable texture mapping
		// GLES20.glEnable(GLES20.GL_TEXTURE_2D);

		// Position the eye in front of the origin.
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = 1.706666666667f;

		// We are looking toward the distance
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -5.0f;

		// Set our up vector. This is where our head would be pointing were we holding the camera.
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		// Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);		

		final String vertexShader = getVertexShader();   		
 		final String fragmentShader = getFragmentShader();			

		final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
		final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);		

		mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});								                                							       
                
        // Load the textures
        mBackgroundTextureDataHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.baseball_diamond);
		loadCardTextures();
	}	

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) 
	{
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 10.0f;

		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}	

	@Override
	public void onDrawFrame(GL10 glUnused) 
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);			        
                
        // Set our per-vertex lighting program.
        GLES20.glUseProgram(mProgramHandle);
        
        // Set program handles for image drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBackgroundTextureDataHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);        
                
        // Draw background image.        
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.scaleM(mModelMatrix, 0, 1.0f, 1.7066666667f, 1.0f);
        drawImage();
        
        // Draw the pitcher card.
        if (game.pitcher != null) {
	        Matrix.setIdentityM(mModelMatrix, 0);
	        Matrix.translateM(mModelMatrix, 0, 0f, -.1f, .01f);
	        Matrix.scaleM(mModelMatrix, 0, 0.25f, 0.35f, 1.0f);
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, game.pitcher.textureDataHandle);
	        GLES20.glUniform1i(mTextureUniformHandle, 0);
	        drawImage();
        }

        // Draw the batter card.
        if (game.batter != null) {
	        Matrix.setIdentityM(mModelMatrix, 0);
	        Matrix.translateM(mModelMatrix, 0, 0f, -0.95f, .01f);
	        Matrix.scaleM(mModelMatrix, 0, 0.25f, 0.35f, 1.0f);
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, game.batter.textureDataHandle);
	        GLES20.glUniform1i(mTextureUniformHandle, 0);
	        drawImage();
        }

        // Draw the runner 1 card.
        if (game.runner1 != null) {
	        Matrix.setIdentityM(mModelMatrix, 0);
	        Matrix.translateM(mModelMatrix, 0, .70f, -0.1f, .01f);
	        Matrix.scaleM(mModelMatrix, 0, 0.25f, 0.35f, 1.0f);
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, game.runner1.textureDataHandle);
	        GLES20.glUniform1i(mTextureUniformHandle, 0);
	        drawImage();
        }

        // Draw the runner 2 card.
        if (game.runner2 != null) {
	        Matrix.setIdentityM(mModelMatrix, 0);
	        Matrix.translateM(mModelMatrix, 0, 0f, 0.75f, .01f);
	        Matrix.scaleM(mModelMatrix, 0, 0.25f, 0.35f, 1.0f);
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, game.runner2.textureDataHandle);
	        GLES20.glUniform1i(mTextureUniformHandle, 0);
	        drawImage();
        }

        // Draw the runner 3 card.
        if (game.runner3 != null) {
	        Matrix.setIdentityM(mModelMatrix, 0);
	        Matrix.translateM(mModelMatrix, 0, -.70f, -0.1f, .01f);
	        Matrix.scaleM(mModelMatrix, 0, 0.25f, 0.35f, 1.0f);
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, game.runner3.textureDataHandle);
	        GLES20.glUniform1i(mTextureUniformHandle, 0);
	        drawImage();
        }

	}
	

	/**
	 * Draws an image.
	 */			
	private void drawImage()
	{		
		// Pass in the position information
		mImagePositions.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, mImagePositions);        
                
        GLES20.glEnableVertexAttribArray(mPositionHandle);        
                
        // Pass in the texture coordinate information
        mImageTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mImageTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
                
        // Draw the Image.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);                               
	}	
	
	public void loadCardTextures() {
		
		for (int i = 0; i < game.homeTeam.positions.length; i++) {
			game.homeTeam.positions[i].textureDataHandle = TextureHelper.loadTexture(mActivityContext, game.homeTeam.positions[i].imageId);
			GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		}
		for (int i = 0; i < game.awayTeam.positions.length; i++) {
			game.awayTeam.positions[i].textureDataHandle = TextureHelper.loadTexture(mActivityContext, game.awayTeam.positions[i].imageId);
			GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		}
	}
}