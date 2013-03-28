package com.brejral.mlbcardgame.game;

import java.util.Random;

import android.util.Log;

import com.brejral.mlbcardgame.Card;
import com.brejral.mlbcardgame.Team;

public class Game {
	@SuppressWarnings("unused")
	private final String TAG = "Game";
	public Card batter, pitcher, runner1, runner2, runner3;
	public Team homeTeam, awayTeam;
	public String pitch;
	public Random rand = new Random();
	public int inning = 1, outs = 0;
	public boolean topOfInning = true;
	public int homeScore, awayScore;
	public int runnerAdvance;
	public String result = "";
	public int awayInningScores[] = new int[20];
	public int homeInningScores[] = new int[20];
	public int awayHits;
	public int homeHits;
	
	public Game(Team home, Team away) {
		homeTeam = home;
		awayTeam = away;
		pitcher = homeTeam.positions[1];
		batter = awayTeam.battingOrder[awayTeam.battingOrderNum];

	}
		
	public void pitch(int n) {
		pitchRoll(n);
		advanceBatterNum();
		checkEndOfInning();
		updateBatter();
	}
	
	public void pitchRoll(int n) {
		// grabs the index of the pitch and passes it to the chartSelection method
		int i = selectPitch(n);
		boolean batterChart = chartSelection(i);
		checkResult(batterChart);
	}
	
	public int selectPitch(int n) {
		// selects the index of the pitchType array based off the pitch names of the current pitcher
		pitch = pitcher.pitches[n];
		if (pitch == "Fastball")
			n = 0;
		else if (pitch == "Two Seamer")
			n = 1;
		else if (pitch == "Changeup")
			n = 2;
		else if (pitch == "Curveball" || pitch == "Knuckle Curve")
			n = 3;
		else if (pitch == "Slider" || pitch == "Cutter")
			n = 4;
		else if (pitch == "Sinker" || pitch == "Splitter" || pitch == "Forkball")
			n = 5;
		else
			n = 6;
		return n;
	}
	
	public boolean chartSelection(int i) {
		boolean batterChart = false;
		int total = batter.pitchType[i] + pitcher.pitchType[i];
		int chartRoll = rand.nextInt(total - 1) + 1;
		if (chartRoll <= batter.pitchType[i])
			batterChart = true;
		return batterChart; //if true then off the batters chart
	}

	public void checkResult(boolean batterChart) {
		int bipRoll, babipRoll;
		bipRoll = rand.nextInt(1000 - 1) + 1;
		if (batterChart == true) {
			if (pitcher.throwsL == true) {
				if (bipRoll <= batter.bipL[0]) {
					walk();
				} else if (bipRoll <= batter.bipL[1]) {
					strikeOut();
				} else {
					babipRoll = rand.nextInt(1000 - 1) + 1;
					if (babipRoll <= batter.babipL) {
						hitRoll(batter, true);
					} else {
						outRoll(batter, true);
					}
				}
			} else if (pitcher.throwsR == true) {
				if (bipRoll <= batter.bipL[0]) {
					walk();
				} else if (bipRoll <= batter.bipL[1]) {
					strikeOut();
				} else {
					babipRoll = rand.nextInt(1000 - 1) + 1;
					if (babipRoll <= batter.babipL) {
						hitRoll(batter, false);
					} else {
						outRoll(batter, false);
					}					
				}
				
			} else {
				Log.v("PitcherThrow", pitcher.name + " throwing hand not specified.");
			}
			
		} else if (batterChart == false) {
			if (pitcher.throwsL == true) {
				if (batter.batsL == true) {
					if (bipRoll <= pitcher.bipL[0]) {
						walk();
					} else if (bipRoll <= pitcher.bipL[1]) {
						strikeOut();
					} else {
						babipRoll = rand.nextInt(1000 - 1) + 1;
						if (babipRoll <= batter.babipL) {
							hitRoll(pitcher, true);
						} else {
							outRoll(pitcher, true);
						}						
					}
				} else if (batter.batsR == true || batter.batsS == true) {
					if (bipRoll <= pitcher.bipL[0]) {
						walk();
					} else if (bipRoll <= pitcher.bipL[1]) {
						strikeOut();
					} else {
						babipRoll = rand.nextInt(1000 - 1) + 1;
						if (babipRoll <= batter.babipL) {
							hitRoll(pitcher, false);
						} else {
							outRoll(pitcher, false);
						}						
					}
					
				} else {
					Log.v("BatterHit", batter.name + " hitting side not specified.");
				}
			} else if (pitcher.throwsR == true) {
				if (batter.batsL == true || batter.batsS == true) {
					if (bipRoll <= pitcher.bipL[0]) {
						walk();
					} else if (bipRoll <= pitcher.bipL[1]) {
						strikeOut();
					} else {
						babipRoll = rand.nextInt(1000 - 1) + 1;
						if (babipRoll <= batter.babipL) {
							hitRoll(pitcher, true);
						} else {
							outRoll(pitcher, true);
						}						
					}
				} else if (batter.batsR == true) {
					if (bipRoll <= pitcher.bipL[0]) {
						walk();
					} else if (bipRoll <= pitcher.bipL[1]) {
						strikeOut();
					} else {
						babipRoll = rand.nextInt(1000 - 1) + 1;
						if (babipRoll <= batter.babipL) {
							hitRoll(pitcher, false);
						} else {
							outRoll(pitcher, false);
						}						
					}
					
				} else {
					Log.v("BatterHit", batter.name + " hitting side not specified.");
				}
			}			
			
		}				
	}
	
	public void hitRoll(Card card, boolean vsLeft) {
		int hitRoll = rand.nextInt(1000 - 1) + 1;
		if (vsLeft) {
			if (hitRoll <= card.hitsL[0]) {
				singleHit();
			} else if (hitRoll <= card.hitsL[1]) {
				doubleHit();
			} else if (hitRoll <= card.hitsL[2]) {
				tripleHit();
			} else {
				homeRunHit();
			}
		} else {
			if (hitRoll <= card.hitsR[0]) {
				singleHit();
			} else if (hitRoll <= card.hitsR[1]) {
				doubleHit();
			} else if (hitRoll <= card.hitsR[2]) {
				tripleHit();
			} else {
				homeRunHit();
			}			
		}
	}
	
	public void outRoll(Card card, boolean vsLeft) {
		int outRoll = rand.nextInt(1000 - 1) + 1;
		if (vsLeft) {
			if (outRoll <= card.hitSpreadL[0]) {
				lineDriveOut();
			} else if (outRoll <= card.hitSpreadL[1]) {
				groundBallOut();
			} else {
				// check for infield flyball out
				int iffbRoll = rand.nextInt(1000 - 1) + 1;
				if (iffbRoll <= card.hitSpreadL[3])
					infieldFlyBallOut();
				else
					flyBallOut();			
			}
		} else {
			if (outRoll <= card.hitSpreadR[0]) {
				lineDriveOut();
			} else if (outRoll <= card.hitSpreadR[1]) {
				groundBallOut();
			} else {
				int iffbRoll = rand.nextInt(1000 - 1) + 1;
				if (iffbRoll <= card.hitSpreadR[3])
					infieldFlyBallOut();
				else
					flyBallOut();
			}			
		}
	}

	public void strikeOut() {
		result = "Strike Out";
		outs++;
		pitcher.gameStats[1]++;
		pitcher.gameStats[7]++;
		batter.gameStats[7]++;
	}
	
	public void groundBallOut() {
		result = "Ground Out";
		outs++;
	}
	
	public void lineDriveOut() {
		result = "Line Out";
		outs++;
		pitcher.gameStats[1]++;
	}
	
	public void flyBallOut() {
		result = "Fly Out";
		outs++;
	}
	
	public void infieldFlyBallOut() {
		result = "Infield Fly Out";
		outs++;
	}
	
	public void walk() {
		result = "Walk";
		batter.gameStats[6]++;
		pitcher.gameStats[6]++;
		if (runner3 != null && runner2 != null && runner1 != null) {
			runner3.gameStats[9]++;
			pitcher.gameStats[9]++;
			batter.gameStats[10]++;
			runner3 = runner2;
			runner2 = runner1;
			runner1 = batter;
		} else if (runner2 != null && runner1 != null) {
			runner3 = runner2;
			runner2 = runner1;
			runner1 = batter;
		} else if (runner1 != null) {
			runner2 = runner1;
			runner1 = batter;
		} else {
			runner1 = batter;
		}
		
	}
	
	public void singleHit() {
		addHit();
		result = "Single";
		batter.gameStats[2]++;
		pitcher.gameStats[2]++;
		if (runner3 != null) {
			batter.gameStats[10]++;
			pitcher.gameStats[9]++;
			runner3.gameStats[9]++;
			addRun();
			runner3 = null;
		}
		if (runner2 != null) {
			runnerAdvance += 3;
			runner3 = runner2;
			runner2 = null;
		}
		if (runner1 != null) {
			runnerAdvance += 2;
			runner2 = runner1;
			runner1 = null;
		}
		runner1 = batter;
	}
	
	public void doubleHit() {
		addHit();
		result = "Double";
		batter.gameStats[2]++;
		batter.gameStats[3]++;
		pitcher.gameStats[2]++;
		pitcher.gameStats[3]++;
		if (runner3 != null) {
			batter.gameStats[10]++;
			pitcher.gameStats[9]++;
			runner3.gameStats[9]++;
			addRun();
			runner3 = null;			
		}
		if (runner2 != null) {
			batter.gameStats[10]++;
			pitcher.gameStats[9]++;
			runner2.gameStats[9]++;
			addRun();
			runner2 = null;			
		}
		if (runner1 != null) {
			runnerAdvance += 3;
			runner3 = runner1;
			runner1 = null;
		}
		runner2 = batter;
	}
	
	public void tripleHit() {
		addHit();
		result = "Triple";
		batter.gameStats[2]++;
		batter.gameStats[4]++;
		pitcher.gameStats[2]++;
		pitcher.gameStats[4]++;
		if (runner3 != null) {
			batter.gameStats[10]++;
			pitcher.gameStats[9]++;
			runner3.gameStats[9]++;
			addRun();
			runner3 = null;			
		}
		if (runner2 != null) {
			batter.gameStats[10]++;
			pitcher.gameStats[9]++;
			runner2.gameStats[9]++;
			addRun();
			runner2 = null;			
		}
		if (runner1 != null) {
			batter.gameStats[10]++;
			pitcher.gameStats[9]++;
			runner1.gameStats[9]++;
			addRun();
			runner1 = null;			
		}
		runner3 = batter;
	}
	
	public void homeRunHit() {
		addHit();
		result = "Home Run";
		batter.gameStats[2]++;
		batter.gameStats[5]++;
		pitcher.gameStats[2]++;
		pitcher.gameStats[5]++;
		if (runner3 != null) {
			batter.gameStats[10]++;
			pitcher.gameStats[9]++;
			runner3.gameStats[9]++;
			addRun();
			runner3 = null;			
		}
		if (runner2 != null) {
			batter.gameStats[10]++;
			pitcher.gameStats[9]++;
			runner2.gameStats[9]++;
			addRun();
			runner2 = null;			
		}
		if (runner1 != null) {
			batter.gameStats[10]++;
			pitcher.gameStats[9]++;
			runner1.gameStats[9]++;
			addRun();
			runner1 = null;			
		}
		addRun();
		batter.gameStats[10]++;
		pitcher.gameStats[9]++;
		batter.gameStats[9]++;
	}
	
	public void addRun() {
		if (topOfInning) {
			awayScore++;
			awayInningScores[inning - 1]++; 
		}
		else {
			homeScore++;
			homeInningScores[inning - 1]++;
		}
	}
	
	public void advanceBatterNum() {
		if (topOfInning) {
			if (awayTeam.battingOrderNum == 8)
				awayTeam.battingOrderNum = 0;
			else
				awayTeam.battingOrderNum++;
		} else {
			if (homeTeam.battingOrderNum == 8)
				homeTeam.battingOrderNum = 0;
			else
				homeTeam.battingOrderNum++;
		}
	}

	public void updateBatter() {
		if (topOfInning)
			batter = awayTeam.battingOrder[awayTeam.battingOrderNum];
		else
			batter = homeTeam.battingOrder[homeTeam.battingOrderNum];		
	}
	
	public void updatePitcher() {
		if (topOfInning)
			pitcher = homeTeam.positions[1];
		else
			pitcher = awayTeam.positions[1];
	}

	public void checkEndOfInning() {
		if (outs == 3) {
			outs = 0;
			runner1 = null;
			runner2 = null;
			runner3 = null;
			if (topOfInning) 
				topOfInning = false;
			else {
				topOfInning = true;
				inning++;
			}
			updatePitcher();
		}
	}
	
	public void addHit() {
		if (topOfInning)
			awayHits++;
		else
			homeHits++;
	}
}
