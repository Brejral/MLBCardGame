package com.brejral.mlbcardgame;

public class RowItem {
	private int logoId;
	private String teamName;
	private String overall;
	
	public RowItem(int logoId, String teamName, int overall) {
		this.logoId = logoId;
		this.teamName = teamName;
		this.overall = "Overall: " + overall;
	}
	
	public int getLogoId() {
		return logoId;
	}
	
	public void setLogoId(int logoId) {
		this.logoId = logoId;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public String getOverall() {
		return overall;
	}
	
	public void setOverall(int overallnum) {
		this.overall = "Overall: " + overallnum;
	}
	
	@Override
	public String toString() {
		return teamName + "\n" + overall;
	}

}
