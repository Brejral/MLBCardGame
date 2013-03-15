package com.brejral.mlbcardgame.game;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brejral.mlbcardgame.R;

public class GameActivity extends Activity {
	private GameView gameView;
	public GameRenderer gameRenderer;
	public static Game game;
	public TextView resultText, battingOrderText;
	public int pit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.game);
		
		gameView = (GameView)findViewById(R.id.gl_surface_view);
		
		// Check if the system supports OpenGL ES 2.0
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		if (supportsEs2)
		{
			gameView.setEGLContextClientVersion(2);
			
			
			gameRenderer = new GameRenderer(this, game);
			gameView.setRenderer(gameRenderer);
		}
		else {
			return;
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		gameView.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		gameView.onPause();
	}
	
	public void setPitchButtons() {
		LinearLayout pitchButtons = (LinearLayout)findViewById(R.id.pitchButtons);
		for(int i = 0; i < game.pitcher.pitches.length; i++) {
			Button btnTag = new Button(this);
			btnTag.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 60));
			btnTag.setText(game.pitcher.pitches[i]);
			btnTag.setId(i);
			pit = i;
			btnTag.setOnClickListener(new OnClickListener() {
				public int p = pit;
				@Override
				public void onClick(View v) {
					game.pitch(p);
					updateView();
					gameRenderer.loadCardTextures();
				}
			});
			pitchButtons.addView(btnTag);
		}
	}

	public void updateView() {
		resultText.setText(game.result);
		updateBattingOrder();
	}

	public void updateBattingOrder() {
		battingOrderText.setText("");
		if (game.topOfInning) {
			for (int i = 0; i < game.awayTeam.battingOrder.length; i++) {
				String playerString = (i+1)+" "+game.awayTeam.findPosition(i)+" "+game.awayTeam.battingOrder[i].name+"\n";
				if (game.awayTeam.battingOrderNum == i) {
					SpannableString playerSpannableString = new SpannableString(playerString);
					playerSpannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, playerString.length(), 0);
					battingOrderText.append(playerSpannableString);
				} else {
					battingOrderText.append(playerString);
				}
			}
		} else {
			for (int i = 0; i < game.homeTeam.battingOrder.length; i++) {
				String playerString = (i+1)+" "+game.homeTeam.findPosition(i)+" "+game.homeTeam.battingOrder[i].name+"\n";
				if (game.homeTeam.battingOrderNum == i) {
					SpannableString playerSpannableString = new SpannableString(playerString);
					playerSpannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, playerString.length(), 0);
					battingOrderText.append(playerSpannableString);
				} else {
					battingOrderText.append(playerString);
				}	
			}			
		}
	}

}
