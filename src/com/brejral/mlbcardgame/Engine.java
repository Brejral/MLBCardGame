package com.brejral.mlbcardgame;

import android.view.View;

public class Engine
{
	public static final int menuButtonAlpha = 110;
	public static Team[] mlb_teams;
	
	
	public static boolean onExit(View v) {
		try {
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static Team[] makeMlbTeamArray() {
		int i;
		Team[] teams = new Team[30];
		
		for (i = 1; i < 31; i++) {
			teams[i-1] = new Team(i);
		}
		
		return teams;
	}

}
