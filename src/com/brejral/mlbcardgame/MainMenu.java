package com.brejral.mlbcardgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		Button exhibitionButton = (Button)findViewById(R.id.exhibitionButton);
		Button seasonButton = (Button)findViewById(R.id.seasonButton);
		Button tournamentButton = (Button)findViewById(R.id.tournamentButton);
		Button settingsButton = (Button)findViewById(R.id.settingsButton);
		Button exitButton = (Button)findViewById(R.id.exitButton);
		
		exhibitionButton.getBackground().setAlpha(Engine.menuButtonAlpha);
		seasonButton.getBackground().setAlpha(Engine.menuButtonAlpha);
		tournamentButton.getBackground().setAlpha(Engine.menuButtonAlpha);
		settingsButton.getBackground().setAlpha(Engine.menuButtonAlpha);
		exitButton.getBackground().setAlpha(Engine.menuButtonAlpha);
		
		exhibitionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent exhibitionMenuIntent = new Intent(getApplicationContext(), ExhibitionMenu.class);
				MainMenu.this.startActivity(exhibitionMenuIntent);
				MainMenu.this.finish();
				
			}
		});
		seasonButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		tournamentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		settingsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		exitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean clean = false;
				clean = Engine.onExit(v);
				if (clean) {
					int pid = android.os.Process.myPid();
					android.os.Process.killProcess(pid);
				}
				
			}
		});
	}
}
