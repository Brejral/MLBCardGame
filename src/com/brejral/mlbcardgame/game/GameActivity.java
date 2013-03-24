package com.brejral.mlbcardgame.game;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brejral.mlbcardgame.R;

public class GameActivity extends Activity {
	@SuppressWarnings("unused")
	private final String TAG = "GameActivity";
	private GameView gameView;
	public GameRenderer gameRenderer;
	public static Game game;
	public TextView resultText, battingOrderText;
	public ImageView scoreboard, lineupBox;
	public int pit;
	public float sHeight, sWidth, sRatio;
	public int sMargin, sLineupMargin;
	public float backgroundRatio = 640f/375f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.game);

		resultText = (TextView)findViewById(R.id.resultText);
		battingOrderText = (TextView)findViewById(R.id.battingOrderText);
		scoreboard = (ImageView)findViewById(R.id.scoreboard);
		lineupBox = (ImageView)findViewById(R.id.lineupbox);
		
		gameView = (GameView)findViewById(R.id.gl_surface_view);
		
		// Check if the system supports OpenGL ES 2.0
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		if (supportsEs2)
		{
			gameView.setEGLContextClientVersion(2);
			
			gameRenderer = new GameRenderer(this, game);
			gameView.setRenderer(gameRenderer, displayMetrics);
		}
		else {
			return;
		}
		setLayoutParameters();			
		setPitchButtons();
		updateView();
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
		LayoutParams layoutBottom = pitchButtons.getLayoutParams();
		((MarginLayoutParams) layoutBottom).setMargins(0, 0, 0, sMargin);
		pitchButtons.setLayoutParams(layoutBottom);
		for(int i = 0; i < game.pitcher.pitches.length; i++) {
			Button btnTag = new Button(this);
			btnTag.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int)sWidth/9));
			btnTag.setText(game.pitcher.pitches[i]);
			btnTag.setId(i);
			pit = i;
			btnTag.setOnClickListener(new OnClickListener() {
				public int p = pit;
				@Override
				public void onClick(View v) {
					game.pitch(p);
					updateView();
					
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

	public void setLayoutParameters() {
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		sHeight = displayMetrics.heightPixels;
		sWidth = displayMetrics.widthPixels;
		sRatio = sHeight/sWidth;
		
		LayoutParams layoutScoreboard = scoreboard.getLayoutParams();
		LayoutParams layoutLineup = battingOrderText.getLayoutParams();
		LayoutParams layoutLineupBox = lineupBox.getLayoutParams();

		layoutScoreboard.width = (int)sWidth;
		layoutScoreboard.height = (int)sWidth*240/1040;
		layoutLineupBox.width = (int) (sWidth*.4f);
		layoutLineupBox.height = (int) (600/720 * sWidth*.4f);
		
		sLineupMargin = (int) ((int)sWidth/1800f * 50f);
		sMargin = (int) ((sHeight - backgroundRatio*sWidth)/2);
		((MarginLayoutParams) layoutLineup).setMargins(5, 0, 0, 0);
		((MarginLayoutParams) layoutScoreboard).setMargins(0, 0, 0, 0);
		((MarginLayoutParams) layoutLineupBox).setMargins(0, 0, 0, 0);
		
		battingOrderText.setLayoutParams(layoutLineup);
		scoreboard.setLayoutParams(layoutScoreboard);
		lineupBox.setLayoutParams(layoutLineupBox);
	}
}
