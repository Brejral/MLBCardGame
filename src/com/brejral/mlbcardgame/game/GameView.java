package com.brejral.mlbcardgame.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class GameView extends GLSurfaceView {
	@SuppressWarnings("unused")
	private String TAG = "GameView";
	public GameRenderer gameRenderer;
	public float screenWidth;
	public float screenHeight;
	public float screenRatio;
	
	public GameView(Context context, GameActivity gameAct)
	{
		super(context);
	}
	
	public GameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public void setRenderer(GameRenderer renderer, DisplayMetrics metrics) {
		gameRenderer = renderer;
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		screenRatio = screenHeight/screenWidth;
		gameRenderer.screenRatio = screenHeight/screenWidth;
		super.setRenderer(gameRenderer);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event != null) {
			float x = event.getX();
			float y = event.getY();
			float openglX = x/screenWidth * 2f - 1f;
			float openglY = (1f - y/screenHeight) * 2f * screenRatio - screenRatio;
			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				if (openglX >= -1f && openglX <= 1f && openglY >= -1.5f && openglY <= 1.3f && gameRenderer.game.pitcher != null &&
						(gameRenderer.batterZoomed == true || gameRenderer.pitcherZoomed == true || gameRenderer.runner1Zoomed == true ||
						gameRenderer.runner2Zoomed == true || gameRenderer.runner3Zoomed == true)) {
					gameRenderer.batterZoomed = false;
					gameRenderer.pitcherZoomed = false;
					gameRenderer.runner1Zoomed = false;
					gameRenderer.runner2Zoomed = false;
					gameRenderer.runner3Zoomed = false;
				} else if (openglX >= -.25f && openglX <= .25f && openglY >= -.45f && openglY <= .25f && gameRenderer.game.pitcher != null) { 
					gameRenderer.pitcherZoomed = true;
					gameRenderer.zoomTranslate = 0f;
					gameRenderer.zoomScaleX = .25f;
					gameRenderer.zoomScaleY = .35f;

				} else if (openglX >= -.25f && openglX <= .25f && openglY >= -1.3f && openglY <= -.6f && gameRenderer.game.batter != null) {
					gameRenderer.batterZoomed = true;
					gameRenderer.zoomTranslate = -.95f;
					gameRenderer.zoomScaleX = .25f;
					gameRenderer.zoomScaleY = .35f;
					
				} else if (openglX >= .45f && openglX <= .95f && openglY >= -.45f && openglY <= .25f && gameRenderer.game.runner1 != null) {
					gameRenderer.runner1Zoomed = true;
					gameRenderer.zoomTranslate = .7f;
					gameRenderer.zoomScaleX = .25f;
					gameRenderer.zoomScaleY = .35f;
					
				} else if (openglX >= -.25f && openglX <= .25f && openglY >= .4f && openglY <= 1.1f && gameRenderer.game.runner2 != null) {
					gameRenderer.runner2Zoomed = true;
					gameRenderer.zoomTranslate = .75f;
					gameRenderer.zoomScaleX = .25f;
					gameRenderer.zoomScaleY = .35f;
					
				} else if (openglX >= -.95f && openglX <= -.45f && openglY >= -.45f && openglY <= .25f && gameRenderer.game.runner3 != null) {
					gameRenderer.runner3Zoomed = true;
					gameRenderer.zoomTranslate = -.7f;
					gameRenderer.zoomScaleX = .25f;
					gameRenderer.zoomScaleY = .35f;
					
				}
			}
			
			
			return true;
		} else {
			return super.onTouchEvent(event);
		}
	}

}
