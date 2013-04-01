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
import android.widget.TextView;

import com.brejral.mlbcardgame.R;

public class GameActivity extends Activity {
	@SuppressWarnings("unused")
	private final String TAG = "GameActivity";
	private GameView gameView;
	public GameRenderer gameRenderer;
	public static Game game;
	public TextView resultText, battingOrderText, inning, outs, inningText, outsText;
	public TextView awayTeamText, awayRuns, awayHits, awayInningScore[];
	public TextView homeTeamText, homeRuns, homeHits, homeInningScore[];
	public ImageView scoreboard, lineupBox, batterBox, resultBox;
	public int pit;
	public float sHeight, sWidth, sRatio;
	public int sMargin, sLineupMargin;
	public float backgroundRatio = 640f/375f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		awayInningScore = new TextView[9];
		homeInningScore = new TextView[9];
		
		setContentView(R.layout.game);

		resultText = (TextView)findViewById(R.id.resultText);
		battingOrderText = (TextView)findViewById(R.id.battingOrderText);
		scoreboard = (ImageView)findViewById(R.id.scoreboard);
		lineupBox = (ImageView)findViewById(R.id.lineupbox);
		awayTeamText = (TextView)findViewById(R.id.awayTeamName);
		homeTeamText = (TextView)findViewById(R.id.homeTeamName);
		awayInningScore[0] = (TextView)findViewById(R.id.awayInning1);
		awayInningScore[1] = (TextView)findViewById(R.id.awayInning2);
		awayInningScore[2] = (TextView)findViewById(R.id.awayInning3);
		awayInningScore[3] = (TextView)findViewById(R.id.awayInning4);
		awayInningScore[4] = (TextView)findViewById(R.id.awayInning5);
		awayInningScore[5] = (TextView)findViewById(R.id.awayInning6);
		awayInningScore[6] = (TextView)findViewById(R.id.awayInning7);
		awayInningScore[7] = (TextView)findViewById(R.id.awayInning8);
		awayInningScore[8] = (TextView)findViewById(R.id.awayInning9);
		awayRuns = (TextView)findViewById(R.id.awayRuns);
		awayHits = (TextView)findViewById(R.id.awayHits);
		homeInningScore[0] = (TextView)findViewById(R.id.homeInning1);
		homeInningScore[1] = (TextView)findViewById(R.id.homeInning2);
		homeInningScore[2] = (TextView)findViewById(R.id.homeInning3);
		homeInningScore[3] = (TextView)findViewById(R.id.homeInning4);
		homeInningScore[4] = (TextView)findViewById(R.id.homeInning5);
		homeInningScore[5] = (TextView)findViewById(R.id.homeInning6);
		homeInningScore[6] = (TextView)findViewById(R.id.homeInning7);
		homeInningScore[7] = (TextView)findViewById(R.id.homeInning8);
		homeInningScore[8] = (TextView)findViewById(R.id.homeInning9);
		homeRuns = (TextView)findViewById(R.id.homeRuns);
		homeHits = (TextView)findViewById(R.id.homeHits);
		batterBox = (ImageView)findViewById(R.id.currentbatterbox);
		resultBox = (ImageView)findViewById(R.id.resultbox);
		inningText = (TextView)findViewById(R.id.inningtext);
		outsText = (TextView)findViewById(R.id.outstext);
		inning = (TextView)findViewById(R.id.inning);
		outs = (TextView)findViewById(R.id.outs);
		
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
		setBoxScoreTextBoxes();
		updateScoreboard();
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
		((MarginLayoutParams) layoutBottom).setMargins(0, 0, 0, 0);
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
					updateScoreboard();
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
				String playerString = (i+1)+" "+game.awayTeam.findPosition(i)+" "+game.awayTeam.battingOrder[i].name;
				if (i != game.awayTeam.battingOrder.length - 1)
					playerString += "\n";
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
				String playerString = (i+1)+" "+game.homeTeam.findPosition(i)+" "+game.homeTeam.battingOrder[i].name;
				if (i != game.homeTeam.battingOrder.length - 1)
					playerString += "\n";
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
		LayoutParams layoutBatterBox = batterBox.getLayoutParams();
		LayoutParams layoutResultBox = resultBox.getLayoutParams();
		LayoutParams layoutInningText = inningText.getLayoutParams();
		LayoutParams layoutOutsText = outsText.getLayoutParams();
		LayoutParams layoutInning = inning.getLayoutParams();
		LayoutParams layoutOuts = outs.getLayoutParams();
		LayoutParams layoutResultText = resultText.getLayoutParams();

		layoutScoreboard.width = (int)sWidth;
		layoutScoreboard.height = (int)sWidth*240/1040;
		layoutLineupBox.width = (int) (sWidth*.36f);
		layoutLineupBox.height = (int) (600f/720f * sWidth*.4f);
		layoutResultBox.width = (int) (sWidth*.36f);
		layoutResultBox.height = (int) (sHeight*.15f);
		layoutBatterBox.width = (int) (sWidth*.36f);
		layoutBatterBox.height = (int) (sHeight*.15f);
		layoutOuts.width = outsText.getMeasuredWidth();
		Log.d(TAG, Integer.toString(outsText.getMeasuredWidth()));
		Log.d(TAG, Integer.toString(inningText.getMeasuredWidth()));
		layoutInning.width = inningText.getMeasuredWidth();
		
		sLineupMargin = (int) ((int)sWidth/1800f * 50f);
		sMargin = (int) ((sHeight - backgroundRatio*sWidth)/2);
		((MarginLayoutParams) layoutLineup).setMargins(5, 0, 0, 2);
		((MarginLayoutParams) layoutScoreboard).setMargins(0, 0, 0, 0);
		((MarginLayoutParams) layoutLineupBox).setMargins(0, 0, 0, 0);
		((MarginLayoutParams) layoutInningText).setMargins((int)(layoutBatterBox.width/720f*10f), (int)(layoutBatterBox.height/600f*5f),0,0);
		((MarginLayoutParams) layoutInning).setMargins((int)(layoutBatterBox.width/720f*100f), (int)(layoutBatterBox.height/600f*120f),0,0);
		((MarginLayoutParams) layoutOutsText).setMargins((int)(layoutBatterBox.width/720f*500f), (int)(layoutBatterBox.height/600f*5f),0,0);
		((MarginLayoutParams) layoutOuts).setMargins((int)(layoutBatterBox.width/720f*580f), (int)(layoutBatterBox.height/600f*120f),0,0);
		((MarginLayoutParams) layoutResultText).setMargins((int)(layoutBatterBox.width/720f*10f), (int)(layoutBatterBox.height/600f*400f),0,0);
		
		
		battingOrderText.setLayoutParams(layoutLineup);
		scoreboard.setLayoutParams(layoutScoreboard);
		lineupBox.setLayoutParams(layoutLineupBox);
		batterBox.setLayoutParams(layoutBatterBox);
		resultBox.setLayoutParams(layoutResultBox);
		outsText.setLayoutParams(layoutOutsText);
		inningText.setLayoutParams(layoutInningText);
		outs.setLayoutParams(layoutOuts);
		inning.setLayoutParams(layoutInning);
		resultText.setLayoutParams(layoutResultText);
		Log.d(TAG, Integer.toString(outsText.getMeasuredWidth()));
		Log.d(TAG, Integer.toString(inningText.getMeasuredWidth()));
	}
	
	public void setBoxScoreTextBoxes() {
		float ratio = sWidth/1080;
		
		LayoutParams layoutParams = awayTeamText.getLayoutParams();
		layoutParams.width = (int) (ratio*350);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(5*ratio), (int)(84*ratio), 0, 0);
		awayTeamText.setLayoutParams(layoutParams);
		awayTeamText.setText(game.awayTeam.teamNickname);

		layoutParams = homeTeamText.getLayoutParams();
		layoutParams.width = (int) (ratio*350);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(5*ratio), (int)(164*ratio), 0, 0);
		homeTeamText.setLayoutParams(layoutParams);
		homeTeamText.setText(game.homeTeam.teamNickname);
		
		layoutParams = awayInningScore[0].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(365*ratio), (int)(84*ratio), 0, 0);
		awayInningScore[0].setLayoutParams(layoutParams);
		awayInningScore[0].setText("0");
		
		layoutParams = awayInningScore[1].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(430*ratio), (int)(84*ratio), 0, 0);
		awayInningScore[1].setLayoutParams(layoutParams);

		layoutParams = awayInningScore[2].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(495*ratio), (int)(84*ratio), 0, 0);
		awayInningScore[2].setLayoutParams(layoutParams);

		layoutParams = awayInningScore[3].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(560*ratio), (int)(84*ratio), 0, 0);
		awayInningScore[3].setLayoutParams(layoutParams);

		layoutParams = awayInningScore[4].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(625*ratio), (int)(84*ratio), 0, 0);
		awayInningScore[4].setLayoutParams(layoutParams);

		layoutParams = awayInningScore[5].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(690*ratio), (int)(84*ratio), 0, 0);
		awayInningScore[5].setLayoutParams(layoutParams);

		layoutParams = awayInningScore[6].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(755*ratio), (int)(84*ratio), 0, 0);
		awayInningScore[6].setLayoutParams(layoutParams);

		layoutParams = awayInningScore[7].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(820*ratio), (int)(84*ratio), 0, 0);
		awayInningScore[7].setLayoutParams(layoutParams);
		
		layoutParams = awayInningScore[8].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(885*ratio), (int)(84*ratio), 0, 0);
		awayInningScore[8].setLayoutParams(layoutParams);

		layoutParams = awayRuns.getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(956*ratio), (int)(84*ratio), 0, 0);
		awayRuns.setLayoutParams(layoutParams);
		awayRuns.setText("0");

		layoutParams = awayHits.getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(1018*ratio), (int)(84*ratio), 0, 0);
		awayHits.setLayoutParams(layoutParams);
		awayHits.setText("0");

		layoutParams = homeInningScore[0].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(365*ratio), (int)(164*ratio), 0, 0);
		homeInningScore[0].setLayoutParams(layoutParams);
		
		layoutParams = homeInningScore[1].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(430*ratio), (int)(164*ratio), 0, 0);
		homeInningScore[1].setLayoutParams(layoutParams);

		layoutParams = homeInningScore[2].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(495*ratio), (int)(164*ratio), 0, 0);
		homeInningScore[2].setLayoutParams(layoutParams);

		layoutParams = homeInningScore[3].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(560*ratio), (int)(164*ratio), 0, 0);
		homeInningScore[3].setLayoutParams(layoutParams);

		layoutParams = homeInningScore[4].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(625*ratio), (int)(164*ratio), 0, 0);
		homeInningScore[4].setLayoutParams(layoutParams);

		layoutParams = homeInningScore[5].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(690*ratio), (int)(164*ratio), 0, 0);
		homeInningScore[5].setLayoutParams(layoutParams);

		layoutParams = homeInningScore[6].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(755*ratio), (int)(164*ratio), 0, 0);
		homeInningScore[6].setLayoutParams(layoutParams);

		layoutParams = homeInningScore[7].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(820*ratio), (int)(164*ratio), 0, 0);
		homeInningScore[7].setLayoutParams(layoutParams);
		
		layoutParams = homeInningScore[8].getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(885*ratio), (int)(164*ratio), 0, 0);
		homeInningScore[8].setLayoutParams(layoutParams);

		layoutParams = homeRuns.getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(956*ratio), (int)(164*ratio), 0, 0);
		homeRuns.setLayoutParams(layoutParams);
		homeRuns.setText("0");

		layoutParams = homeHits.getLayoutParams();
		layoutParams.width = (int) (ratio*55);
		layoutParams.height = (int) (ratio*70);
		((MarginLayoutParams) layoutParams).setMargins((int)(1018*ratio), (int)(164*ratio), 0, 0);
		homeHits.setLayoutParams(layoutParams);
		homeHits.setText("0");
	}
	
	public void updateScoreboard() {
		inning.setText(Integer.toString(game.inning));
		outs.setText(Integer.toString(game.outs));
		homeRuns.setText(Integer.toString(game.homeScore));
		awayRuns.setText(Integer.toString(game.awayScore));
		homeHits.setText(Integer.toString(game.homeHits));
		awayHits.setText(Integer.toString(game.awayHits));
		
		for (int i = 0; i < game.inning; i++) {
			awayInningScore[i].setText(Integer.toString(game.awayInningScores[i]));
			if (i != game.inning - 1)
				homeInningScore[i].setText(Integer.toString(game.homeInningScores[i]));
			else if (game.topOfInning == false)
				homeInningScore[i].setText(Integer.toString(game.homeInningScores[i]));				
		}

	}
}
