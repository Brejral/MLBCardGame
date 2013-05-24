package com.brejral.mlbcardgame.game;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.brejral.mlbcardgame.Card;
import com.brejral.mlbcardgame.R;
import com.brejral.mlbcardgame.Team;

public class GameBoxScoreActivity extends Activity implements OnTabChangeListener {
	public TextView awayTeamText, awayRuns, awayHits, awayInningScore[];
	public TextView homeTeamText, homeRuns, homeHits, homeInningScore[];
	public ImageView scoreboard;
	public static Game game;
	public static int sWidth, sHeight;
	private TabHost tabHost;
	private TabSpec tabSpecAway, tabSpecHome;
	private TableLayout awayTable, homeTable;

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
		awayTable = (TableLayout)findViewById(R.id.awayTableLayout);
		homeTable = (TableLayout)findViewById(R.id.homeTableLayout);
		
		
		scoreboard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GameBoxScoreActivity.this.finish();
			}
		});
		
		setBoxScore();
		setBoxScoreTabs();
		setTeamBoxScores();
		updateScoreboard();
	}
	
	public void setBoxScoreTabs() {
		tabHost = (TabHost)findViewById(R.id.tabhost);
		tabHost.setup();
		tabHost.setOnTabChangedListener(this);
				
		tabSpecAway = tabHost
				.newTabSpec(game.awayTeam.teamName)
				.setIndicator(game.awayTeam.teamName, getResources().getDrawable(game.awayTeam.logo))
				.setContent(R.id.scrollViewAway);

		tabSpecHome = tabHost
				.newTabSpec(game.homeTeam.teamName)
				.setIndicator(game.homeTeam.teamName, getResources().getDrawable(game.homeTeam.logo))
				.setContent(R.id.scrollViewHome);
				
		tabHost.addTab(tabSpecAway);
		tabHost.addTab(tabSpecHome);
		
	}
	
	public void setTeamBoxScores() {
		TextView awayHitting = (TextView)findViewById(R.id.awayTableHeaderTeamNameHitting);
		awayHitting.setText(game.awayTeam.teamNickname+" Hitting");
		TextView abText = (TextView)findViewById(R.id.abTableHeader);
		TextView rText = (TextView)findViewById(R.id.rTableHeader);
		TextView hText = (TextView)findViewById(R.id.hTableHeader);
		TextView hrText = (TextView)findViewById(R.id.hrTableHeader);
		TextView bbText = (TextView)findViewById(R.id.bbTableHeader);
		TextView soText = (TextView)findViewById(R.id.soTableHeader);
		TextView rbiText = (TextView)findViewById(R.id.rbiTableHeader);
		TextView avgText = (TextView)findViewById(R.id.avgTableHeader);
		
		for (int i = 0; i < game.awayTeam.battingOrder.length; i++) {
			Card card = game.awayTeam.battingOrder[i];
			TableRow row = new TableRow(this);
			
			TextView name = new TextView(this);
			LayoutParams namep = awayHitting.getLayoutParams();
			name.setText(card.name + ", " + game.awayTeam.findPosition(i));
			name.setLayoutParams(namep);
			row.addView(name);
			
			TextView ab = new TextView(this);
			LayoutParams abp = abText.getLayoutParams();
			int abnum = card.gameStats[0]-card.gameStats[6]-card.gameStats[8];
			ab.setText(Integer.toString(abnum));
			ab.setLayoutParams(abp);
			row.addView(ab);
			
			TextView r = new TextView(this);
			LayoutParams rp = rText.getLayoutParams();
			r.setText(Integer.toString(card.gameStats[9]));
			r.setLayoutParams(rp);
			row.addView(r);

			TextView h = new TextView(this);
			LayoutParams hp = hText.getLayoutParams();
			h.setText(Integer.toString(card.gameStats[2]));
			h.setLayoutParams(hp);
			row.addView(h);

			TextView hr = new TextView(this);
			LayoutParams hrp = hrText.getLayoutParams();
			hr.setText(Integer.toString(card.gameStats[5]));
			hr.setLayoutParams(hrp);
			row.addView(hr);

			TextView rbi = new TextView(this);
			LayoutParams rbip = rbiText.getLayoutParams();
			rbi.setText(Integer.toString(card.gameStats[10]));
			rbi.setLayoutParams(rbip);
			row.addView(rbi);

			TextView bb = new TextView(this);
			LayoutParams bbp = bbText.getLayoutParams();
			bb.setText(Integer.toString(card.gameStats[6]));
			bb.setLayoutParams(bbp);
			row.addView(bb);

			TextView so = new TextView(this);
			LayoutParams sop = soText.getLayoutParams();
			so.setText(Integer.toString(card.gameStats[7]));
			so.setLayoutParams(sop);
			row.addView(so);

			TextView avg = new TextView(this);
			LayoutParams avgp = avgText.getLayoutParams();
			avg.setText(card.averageGame());
			avg.setLayoutParams(avgp);
			row.addView(avg);
			
			awayTable.addView(row);
		}

		TableRow tr = new TableRow(this);
		TextView tv = new TextView(this);
		tv.setText(" ");
		tr.addView(tv);
		awayTable.addView(tr);
		
		addPitcherHeader(awayTable, game.awayTeam);
		
		
		for (int i = 0; i < game.awayTeam.pitchersUsed.length; i++) {
			Card card = game.awayTeam.pitchersUsed[i];
			TableRow row = new TableRow(this);
			
			TextView name = new TextView(this);
			LayoutParams namep = awayHitting.getLayoutParams();
			name.setText(card.name);
			name.setLayoutParams(namep);
			row.addView(name);
			
			TextView ip = new TextView(this);
			LayoutParams ipp = abText.getLayoutParams();
			ip.setText(card.ipGame());
			ip.setLayoutParams(ipp);
			row.addView(ip);
			
			TextView h = new TextView(this);
			LayoutParams hp = hText.getLayoutParams();
			h.setText(Integer.toString(card.gameStats[2]));
			h.setLayoutParams(hp);
			row.addView(h);

			TextView er = new TextView(this);
			LayoutParams erp = hrText.getLayoutParams();
			er.setText(Integer.toString(card.gameStats[9]));
			er.setLayoutParams(erp);
			row.addView(er);

			TextView bb = new TextView(this);
			LayoutParams bbp = bbText.getLayoutParams();
			bb.setText(Integer.toString(card.gameStats[6]));
			bb.setLayoutParams(bbp);
			row.addView(bb);

			TextView so = new TextView(this);
			LayoutParams sop = soText.getLayoutParams();
			so.setText(Integer.toString(card.gameStats[7]));
			so.setLayoutParams(sop);
			row.addView(so);

			TextView hr = new TextView(this);
			LayoutParams hrp = hrText.getLayoutParams();
			hr.setText(Integer.toString(card.gameStats[5]));
			hr.setLayoutParams(hrp);
			row.addView(hr);

			TextView era = new TextView(this);
			LayoutParams erap = avgText.getLayoutParams();
			era.setText(card.eraGame());
			era.setLayoutParams(erap);
			row.addView(era);
			
			TextView whip = new TextView(this);
			LayoutParams whipp = avgText.getLayoutParams();
			whip.setText(card.whipGame());
			whip.setLayoutParams(whipp);
			row.addView(whip);

			awayTable.addView(row);			
		}

		TextView homeHitting = (TextView)findViewById(R.id.homeTableHeaderTeamNameHitting);
		homeHitting.setText(game.homeTeam.teamNickname+" Hitting");	
		
		for (int i = 0; i < game.homeTeam.battingOrder.length; i++) {
			Card card = game.homeTeam.battingOrder[i];
			TableRow row = new TableRow(this);
			
			TextView name = new TextView(this);
			LayoutParams namep = homeHitting.getLayoutParams();
			name.setText(card.name + ", " + game.homeTeam.findPosition(i));
			name.setLayoutParams(namep);
			row.addView(name);
			
			TextView ab = new TextView(this);
			LayoutParams abp = abText.getLayoutParams();
			int abnum = card.gameStats[0]-card.gameStats[6]-card.gameStats[8];
			ab.setText(Integer.toString(abnum));
			ab.setLayoutParams(abp);
			row.addView(ab);
			
			TextView r = new TextView(this);
			LayoutParams rp = rText.getLayoutParams();
			r.setText(Integer.toString(card.gameStats[9]));
			r.setLayoutParams(rp);
			row.addView(r);

			TextView h = new TextView(this);
			LayoutParams hp = hText.getLayoutParams();
			h.setText(Integer.toString(card.gameStats[2]));
			h.setLayoutParams(hp);
			row.addView(h);

			TextView hr = new TextView(this);
			LayoutParams hrp = hrText.getLayoutParams();
			hr.setText(Integer.toString(card.gameStats[5]));
			hr.setLayoutParams(hrp);
			row.addView(hr);

			TextView rbi = new TextView(this);
			LayoutParams rbip = rbiText.getLayoutParams();
			rbi.setText(Integer.toString(card.gameStats[10]));
			rbi.setLayoutParams(rbip);
			row.addView(rbi);

			TextView bb = new TextView(this);
			LayoutParams bbp = bbText.getLayoutParams();
			bb.setText(Integer.toString(card.gameStats[6]));
			bb.setLayoutParams(bbp);
			row.addView(bb);

			TextView so = new TextView(this);
			LayoutParams sop = soText.getLayoutParams();
			so.setText(Integer.toString(card.gameStats[7]));
			so.setLayoutParams(sop);
			row.addView(so);

			TextView avg = new TextView(this);
			LayoutParams avgp = avgText.getLayoutParams();
			avg.setText(card.averageGame());
			avg.setLayoutParams(avgp);
			row.addView(avg);
			
			homeTable.addView(row);
		}

		TableRow tr2 = new TableRow(this);
		TextView tv2 = new TextView(this);
		tv2.setText(" ");
		tr2.addView(tv2);
		homeTable.addView(tr2);
		
		addPitcherHeader(homeTable, game.homeTeam);		
		
		for (int i = 0; i < game.homeTeam.pitchersUsed.length; i++) {
			Card card = game.homeTeam.pitchersUsed[i];
			TableRow row = new TableRow(this);
			
			TextView name = new TextView(this);
			LayoutParams namep = homeHitting.getLayoutParams();
			name.setText(card.name);
			name.setLayoutParams(namep);
			row.addView(name);
			
			TextView ip = new TextView(this);
			LayoutParams ipp = abText.getLayoutParams();
			ip.setText(card.ipGame());
			ip.setLayoutParams(ipp);
			row.addView(ip);
			
			TextView h = new TextView(this);
			LayoutParams hp = hText.getLayoutParams();
			h.setText(Integer.toString(card.gameStats[2]));
			h.setLayoutParams(hp);
			row.addView(h);

			TextView er = new TextView(this);
			LayoutParams erp = hrText.getLayoutParams();
			er.setText(Integer.toString(card.gameStats[9]));
			er.setLayoutParams(erp);
			row.addView(er);

			TextView bb = new TextView(this);
			LayoutParams bbp = bbText.getLayoutParams();
			bb.setText(Integer.toString(card.gameStats[6]));
			bb.setLayoutParams(bbp);
			row.addView(bb);

			TextView so = new TextView(this);
			LayoutParams sop = soText.getLayoutParams();
			so.setText(Integer.toString(card.gameStats[7]));
			so.setLayoutParams(sop);
			row.addView(so);

			TextView hr = new TextView(this);
			LayoutParams hrp = hrText.getLayoutParams();
			hr.setText(Integer.toString(card.gameStats[5]));
			hr.setLayoutParams(hrp);
			row.addView(hr);

			TextView era = new TextView(this);
			LayoutParams erap = avgText.getLayoutParams();
			era.setText(card.eraGame());
			era.setLayoutParams(erap);
			row.addView(era);

			TextView whip = new TextView(this);
			LayoutParams whipp = avgText.getLayoutParams();
			whip.setText(card.whipGame());
			whip.setLayoutParams(whipp);
			row.addView(whip);

			homeTable.addView(row);			
		}
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
	
	public void updateScoreboard() {
		if (game.homeScore > 9)
			homeRuns.setTextSize(16f);
		homeRuns.setText(Integer.toString(game.homeScore));
		if (game.awayScore > 9)
			awayRuns.setTextSize(16f);
		awayRuns.setText(Integer.toString(game.awayScore));
		if (game.homeHits > 9)
			homeHits.setTextSize(16f);
		homeHits.setText(Integer.toString(game.homeHits));
		if (game.awayHits > 9)
			awayHits.setTextSize(16f);
		awayHits.setText(Integer.toString(game.awayHits));
		
		for (int i = 0; i < game.inning; i++) {
			if (game.awayInningScores[i] > 9)
				awayInningScore[i].setTextSize(16f);
			awayInningScore[i].setText(Integer.toString(game.awayInningScores[i]));
			if (game.topOfInning == false || i != game.inning - 1) {
				if (game.homeInningScores[i] > 9)
					homeInningScore[i].setTextSize(16f);
				homeInningScore[i].setText(Integer.toString(game.homeInningScores[i]));	
			}
		}
	}
		
			

	@Override
	public void onTabChanged(String tabId) {
		
	}
	
	private void addPitcherHeader(TableLayout table, Team team) {
		TableRow row = new TableRow(this);
		TextView hitting = (TextView)findViewById(R.id.awayTableHeaderTeamNameHitting);
		TextView ab = (TextView)findViewById(R.id.abTableHeader);

		
		TextView pitching = new TextView(this);
		pitching.setText(team.teamNickname + " Pitching");
		pitching.setLayoutParams(hitting.getLayoutParams());
		pitching.setTypeface(null, Typeface.BOLD);
		row.addView(pitching);

		TextView ip = new TextView(this);
		ip.setText("IP");
		ip.setLayoutParams(ab.getLayoutParams());
		ip.setTypeface(null, Typeface.BOLD);
		row.addView(ip);

		TextView h = new TextView(this);
		h.setText("H");
		h.setLayoutParams(ab.getLayoutParams());
		h.setTypeface(null, Typeface.BOLD);
		row.addView(h);

		TextView er = new TextView(this);
		er.setText("ER");
		er.setLayoutParams(ab.getLayoutParams());
		er.setTypeface(null, Typeface.BOLD);
		row.addView(er);

		TextView bb = new TextView(this);
		bb.setText("BB");
		bb.setLayoutParams(ab.getLayoutParams());
		bb.setTypeface(null, Typeface.BOLD);
		row.addView(bb);

		TextView so = new TextView(this);
		so.setText("SO");
		so.setLayoutParams(ab.getLayoutParams());
		so.setTypeface(null, Typeface.BOLD);
		row.addView(so);

		TextView hr = new TextView(this);
		hr.setText("HR");
		hr.setLayoutParams(ab.getLayoutParams());
		hr.setTypeface(null, Typeface.BOLD);
		row.addView(hr);

		TextView era = new TextView(this);
		era.setText("ERA");
		era.setLayoutParams(ab.getLayoutParams());
		era.setTypeface(null, Typeface.BOLD);
		row.addView(era);

		TextView whip = new TextView(this);
		whip.setText("WHIP");
		whip.setLayoutParams(ab.getLayoutParams());
		whip.setTypeface(null, Typeface.BOLD);
		row.addView(whip);
		
		table.addView(row);
	}

}
