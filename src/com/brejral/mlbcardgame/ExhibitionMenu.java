package com.brejral.mlbcardgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ExhibitionMenu extends Activity
{
	public static Team homeTeam;
	public static Team awayTeam;
	
	public ImageView homeTeamLogo;
	public ImageView awayTeamLogo;
	public TextView homeTeamName;
	public TextView awayTeamName;
	public TextView homeTeamOverall;
	public TextView awayTeamOverall;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exhibition_menu);
		
		homeTeam = new Team(11);
		awayTeam = new Team(27);
		
		Button startButton = (Button)findViewById(R.id.startExhibitionButton);
		Button backButton = (Button)findViewById(R.id.exitExhibitionButton);
		Button homeSelectTeamButton = (Button)findViewById(R.id.homeSelectTeamButton);
		Button awaySelectTeamButton = (Button)findViewById(R.id.awaySelectTeamButton);
		
		RadioButton oneGameButton = (RadioButton)findViewById(R.id.oneGameButton);
		//RadioButton threeGameButton = (RadioButton)findViewById(R.id.threeGameButton);
		//RadioButton fiveGameButton = (RadioButton)findViewById(R.id.fiveGameButton);
		//RadioButton sevenGameButton = (RadioButton)findViewById(R.id.sevenGameButton);
		
		homeTeamLogo = (ImageView)findViewById(R.id.homeTeamLogo);
		awayTeamLogo = (ImageView)findViewById(R.id.awayTeamLogo);
		
		homeTeamName = (TextView)findViewById(R.id.homeTeamName);
		awayTeamName = (TextView)findViewById(R.id.awayTeamName);
		homeTeamOverall = (TextView)findViewById(R.id.homeTeamOverall);
		awayTeamOverall = (TextView)findViewById(R.id.awayTeamOverall);
		//TextView homeTeamControl = (TextView)findViewById(R.id.homeTeamControl);
		//TextView awayTeamControl = (TextView)findViewById(R.id.awayTeamControl);
		
		oneGameButton.setChecked(true);
		
		homeTeamLogo.setImageResource(homeTeam.logo);
		awayTeamLogo.setImageResource(awayTeam.logo);
		homeTeamName.setText(homeTeam.teamName);
		awayTeamName.setText(awayTeam.teamName);
		homeTeamOverall.setText("Overall: " + homeTeam.overall);
		awayTeamOverall.setText("Overall: " + awayTeam.overall);
		
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Game.homeTeam = homeTeam;
				Game.awayTeam = awayTeam;
				Intent gameIntent = new Intent(getApplicationContext(), Game.class);
				ExhibitionMenu.this.startActivity(gameIntent);
				ExhibitionMenu.this.finish();
			}
		});
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
				ExhibitionMenu.this.startActivity(mainMenuIntent);
				ExhibitionMenu.this.finish();
			}
		});
		
		homeSelectTeamButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ExhibitionSelectTeam.homeTeamSelect = true;
				Intent exhibitionSelectTeam = new Intent(getApplicationContext(), ExhibitionSelectTeam.class);
				ExhibitionMenu.this.startActivity(exhibitionSelectTeam);
			}
		});
		
		awaySelectTeamButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent exhibitionSelectTeam = new Intent(getApplicationContext(), ExhibitionSelectTeam.class);
				ExhibitionMenu.this.startActivity(exhibitionSelectTeam);
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		homeTeamLogo.setImageResource(homeTeam.logo);
		awayTeamLogo.setImageResource(awayTeam.logo);
		homeTeamName.setText(homeTeam.teamName);
		awayTeamName.setText(awayTeam.teamName);
		homeTeamOverall.setText("Overall: " + homeTeam.overall);
		awayTeamOverall.setText("Overall: " + awayTeam.overall);
		
	}
	

}
