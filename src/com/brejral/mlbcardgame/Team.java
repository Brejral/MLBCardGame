package com.brejral.mlbcardgame;


public class Team
{
	public int teamId;
	public String teamName;
	public int overall;
	public int logo;
	public Card battingOrder[] = new Card[9];
	public Card positions[] = new Card[10];
	int battingOrderNum = 0;
	
	public Team() {
		
	}
	
	public Team(int teamId) {
		this.teamId = teamId;
		TeamDB.populateTeamData(this);
	}
	
	public int getTeamId() {
		return this.teamId;
	}
	
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	
	public int getLogo() {
		return this.logo;
	}
	
	public void setLogo(int logo) {
		this.logo = logo;
	}
	
	public String getTeamName() {
		return this.teamName;
	}
	
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public int getOverall() {
		return this.overall;
	}
	
	public void setOverall(int overall) {
		this.overall = overall;
	}
	
	public String findPosition(int i) {
		int j;
		for (j = 0; j < this.positions.length; j++) {
			if (this.battingOrder[i].name == this.positions[j].name) {
				break;
			}
		}
		switch (j) {
		case 0:
			return "DH";
		case 1:
			return " P";
		case 2:
			return " C";
		case 3:
			return "1B";
		case 4:
			return "2B";
		case 5:
			return "3B";
		case 6:
			return "SS";
		case 7:
			return "LF";
		case 8:
			return "CF";
		case 9:
			return "RF";
		}
		return "";
	}
}
