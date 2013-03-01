package com.brejral.mlbcardgame;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GameView extends GLSurfaceView {
	private GameRenderer renderer;
	
	public GameView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		renderer = new GameRenderer(context);
		this.setRenderer(renderer);
	}
	

}
