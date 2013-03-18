package com.brejral.mlbcardgame.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

public class GameView extends GLSurfaceView {
	private String TAG = "GameView";
	public GameRenderer gameRenderer;
	public float density;
	public float densityDpi;
	public float screenWidth;
	public float screenHeight;
	public boolean cardZoomed;
	
	public GameView(Context context)
	{
		super(context);
	}
	
	public GameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public void setRenderer(GameRenderer renderer, DisplayMetrics metrics) {
		gameRenderer = renderer;
		density = metrics.density;
		densityDpi = metrics.densityDpi;
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		Log.d(TAG, "density = "+Float.toString(density));
		Log.d(TAG, "densityDpi = "+Float.toString(densityDpi));
		super.setRenderer(renderer);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event != null) {
			float x = event.getX();
			float y = event.getY();
			float openglX = x/screenWidth * 2f - 1f;
			float openglY = y/screenHeight * 3.413333333333f - 1.70666666666666f;
			int action = event.getAction();
			
			if (action == MotionEvent.ACTION_DOWN) {
				if (openglX >= -.12f && openglX <= .12f && openglY >= -.27f && openglY <= .07f) { 
					gameRenderer.zoomPitcherCard();
				}
			}
			
			
			return true;
		} else {
			return super.onTouchEvent(event);
		}
	}

}
