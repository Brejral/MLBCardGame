package com.brejral.mlbcardgame.game;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.brejral.mlbcardgame.R;

public class GameBoxScoreActivity extends Activity implements OnTabChangeListener {
	public TextView awayTeamText, awayRuns, awayHits, awayInningScore[];
	public TextView homeTeamText, homeRuns, homeHits, homeInningScore[];
	public ImageView scoreboard;
	public static Game game;
	public static int sWidth, sHeight;
	private TabHost tabHost;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.game_box_score);
		
		awayInningScore = new TextView[9];
		homeInningScore = new TextView[9];

		scoreboard = (ImageView)findViewById(R.id.scoreboard);
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
		
		scoreboard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GameBoxScoreActivity.this.finish();
			}
		});
		
		setBoxScore();
		setBoxScoreTabs();
		setTeamBoxScore();
	}
	
	public void setBoxScoreTabs() {
		tabHost = (TabHost)findViewById(R.id.tabhost);
		tabHost.setup();
		tabHost.setOnTabChangedListener(this);
		
		Resources resources = getResources();
		
		TabSpec tabSpecAway = tabHost
				.newTabSpec(game.awayTeam.teamName)
				.setIndicator("", resources.getDrawable(game.awayTeam.logo))
				.setContent(R.id.awayTab);
		
		TabSpec tabSpecHome = tabHost
				.newTabSpec(game.homeTeam.teamName)
				.setIndicator("", resources.getDrawable(game.homeTeam.logo))
				.setContent(R.id.homeTab);
		
		tabHost.addTab(tabSpecAway);
		tabHost.addTab(tabSpecHome);
		
	}
	
	public void setTeamBoxScore() {
		
	}
	
	public void setBoxScore() {
		float ratio = sWidth/1080f;
		
		LayoutParams layoutScoreboard = scoreboard.getLayoutParams();
		layoutScoreboard.width = sWidth;
		layoutScoreboard.height = (int) (ratio*240);
		scoreboard.setLayoutParams(layoutScoreboard);
		
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

	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		
	}

}