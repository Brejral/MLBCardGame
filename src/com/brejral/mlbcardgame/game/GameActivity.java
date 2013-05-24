package com.brejral.mlbcardgame.game;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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

import com.brejral.mlbcardgame.MainMenu;
import com.brejral.mlbcardgame.R;
import com.brejral.mlbcardgame.exhibition.ExhibitionMenu;

public class GameActivity extends Activity {
	@SuppressWarnings("unused")
	private final String TAG = "GameActivity";
	private GameView gameView;
	public GameRenderer gameRenderer;
	public static Game game;
	public static LinearLayout pitchButtons;
	public static TextView resultText, battingOrderText, inning, outs;
	public static TextView awayTeamText, awayRuns;
	public static TextView homeTeamText, homeRuns;
	public static ImageView scoreboard, lineupBox, resultBox;
	public int pit;
	public int sHeight, sWidth;
	public float sRatio;
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
		awayTeamText = (TextView)findViewById(R.id.awayTeamAbreviation);
		homeTeamText = (TextView)findViewById(R.id.homeTeamAbreviation);
		awayRuns = (TextView)findViewById(R.id.awayRuns);
		homeRuns = (TextView)findViewById(R.id.homeRuns);
		resultBox = (ImageView)findViewById(R.id.resultbox);
		inning = (TextView)findViewById(R.id.inning);
		outs = (TextView)findViewById(R.id.outs);
		gameView = (GameView)findViewById(R.id.gl_surface_view);
		
		scoreboard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GameBoxScoreActivity.game = game;
				Intent gameBoxScoreIntent = new Intent(getApplicationContext(), GameBoxScoreActivity.class);
				GameActivity.this.startActivity(gameBoxScoreIntent);
			}
		});
		
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
		awayTeamText.setText(game.awayTeam.teamAbrev);
		homeTeamText.setText(game.homeTeam.teamAbrev);
		setLayoutParameters();			
		setPitchButtons();
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
		pitchButtons = (LinearLayout)findViewById(R.id.pitchButtons);
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
					checkEndOfGame();
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
		GameBoxScoreActivity.sHeight = sHeight;
		GameBoxScoreActivity.sWidth = sWidth;
		
		LayoutParams layoutScoreboard = scoreboard.getLayoutParams();
		LayoutParams layoutLineup = battingOrderText.getLayoutParams();
		LayoutParams layoutLineupBox = lineupBox.getLayoutParams();
		LayoutParams layoutResultBox = resultBox.getLayoutParams();
		LayoutParams layoutInning = inning.getLayoutParams();
		LayoutParams layoutOuts = outs.getLayoutParams();
		LayoutParams layoutResultText = resultText.getLayoutParams();
		LayoutParams layoutAwayTeamText = awayTeamText.getLayoutParams();
		LayoutParams layoutHomeTeamText = homeTeamText.getLayoutParams();
		LayoutParams layoutAwayScore = awayRuns.getLayoutParams();
		LayoutParams layoutHomeScore = homeRuns.getLayoutParams();

		layoutScoreboard.width = (int) (sWidth*.4f);
		layoutScoreboard.height = (int) (sWidth*.4f*600f/900f);
		layoutInning.width = (int) (layoutScoreboard.width/900f*240f);
		layoutInning.height = (int) (layoutScoreboard.height/600f*255f);
		layoutOuts.width = (int) (layoutScoreboard.width/900f*100f);
		layoutOuts.height = (int) (layoutScoreboard.height/600f*140f);
		layoutAwayScore.width = (int) (layoutScoreboard.width/900f*190f);
		layoutAwayScore.height = (int) (layoutScoreboard.height/600f*207f);
		layoutHomeScore.width = (int) (layoutScoreboard.width/900f*190f);
		layoutHomeScore.height = (int) (layoutScoreboard.height/600f*207f);
		layoutAwayTeamText.width = (int) (layoutScoreboard.width/900f*440f);
		layoutAwayTeamText.height = (int) (layoutScoreboard.height/600f*207f);
		layoutHomeTeamText.width = (int) (layoutScoreboard.width/900f*440f);
		layoutHomeTeamText.height = (int) (layoutScoreboard.height/600f*207f);
		layoutLineupBox.width = (int) (sWidth*.36f);
		layoutLineupBox.height = (int) (600f/720f * sWidth*.4f);
		layoutResultBox.width = (int) (sWidth - layoutScoreboard.width);
		layoutResultBox.height = (int) (layoutScoreboard.height);
		
		sLineupMargin = (int) (sWidth/1800f * 50f);
		sMargin = (int) ((sHeight - backgroundRatio*sWidth)/2f);
		((MarginLayoutParams) layoutLineup).setMargins(5, 0, 0, 2);
		((MarginLayoutParams) layoutScoreboard).setMargins(0, 0, 0, 0);
		((MarginLayoutParams) layoutLineupBox).setMargins(0, 0, 0, 0);
		((MarginLayoutParams) layoutResultText).setMargins(5, 5, 0, 0);
		((MarginLayoutParams) layoutInning).setMargins((int)(layoutScoreboard.width/900f*655f), (int)(layoutScoreboard.height/600f*235f),0,0);
		((MarginLayoutParams) layoutOuts).setMargins((int)(layoutScoreboard.width/900f*555f), (int)(layoutScoreboard.height/600f*5f),0,0);
		((MarginLayoutParams) layoutAwayTeamText).setMargins((int)(layoutScoreboard.width/900f*5f), (int)(layoutScoreboard.height/600f*155f),0,0);
		((MarginLayoutParams) layoutHomeTeamText).setMargins((int)(layoutScoreboard.width/900f*5f), (int)(layoutScoreboard.height/600f*380f),0,0);
		((MarginLayoutParams) layoutAwayScore).setMargins((int)(layoutScoreboard.width/900f*455f), (int)(layoutScoreboard.height/600f*155f),0,0);
		((MarginLayoutParams) layoutHomeScore).setMargins((int)(layoutScoreboard.width/900f*455f), (int)(layoutScoreboard.height/600f*380f),0,0);
		
		
		battingOrderText.setLayoutParams(layoutLineup);
		scoreboard.setLayoutParams(layoutScoreboard);
		lineupBox.setLayoutParams(layoutLineupBox);
		resultBox.setLayoutParams(layoutResultBox);
		outs.setLayoutParams(layoutOuts);
		inning.setLayoutParams(layoutInning);
		resultText.setLayoutParams(layoutResultText);
	}
	
	
	public void updateScoreboard() {
		if (game.topOfInning)
			scoreboard.setImageResource(R.drawable.miniscoreboardtop);
		else
			scoreboard.setImageResource(R.drawable.miniscoreboardbottom);
		inning.setText(Integer.toString(game.inning));
		outs.setText(Integer.toString(game.outs));
		homeRuns.setText(Integer.toString(game.homeScore));
		awayRuns.setText(Integer.toString(game.awayScore));
	}
	
	public void checkEndOfGame() {
		if (game.endOfGame) {
			GameBoxScoreActivity.game = game;
			Intent boxScoreIntent = new Intent(getApplicationContext(), GameBoxScoreActivity.class);
			GameActivity.this.startActivity(boxScoreIntent);
			GameActivity.this.finish();
		}
	}
}
