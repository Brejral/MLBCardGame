package com.brejral.mlbcardgame.game;

import java.util.Random;

import android.content.Intent;
import android.util.Log;

import com.brejral.mlbcardgame.Card;
import com.brejral.mlbcardgame.MainMenu;
import com.brejral.mlbcardgame.Team;
import com.brejral.mlbcardgame.exhibition.ExhibitionMenu;

public class Game {
	@SuppressWarnings("unused")
	private final String TAG = "Game";
	public Card batter, pitcher, runner1, runner2, runner3;
	public Team homeTeam, awayTeam;
	public String pitch;
	public Random rand = new Random();
	public int inning = 1, outs = 0;
	public boolean topOfInning = true, endOfGame, topOfInningEOGCheck;
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
		checkEndOfGame();
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
		result = "strikes out";
		outs++;
		pitcher.gameStats[1]++;
		pitcher.gameStats[7]++;
		batter.gameStats[7]++;
		hitLocation();
	}
	
	public void groundBallOut() {
		result = "grounds out";
		outs++;
		hitLocation();
	}
	
	public void lineDriveOut() {
		result = "lines out";
		outs++;
		pitcher.gameStats[1]++;
		hitLocation();
	}
	
	public void flyBallOut() {
		result = "flies out";
		outs++;
		hitLocation();
	}
	
	public void infieldFlyBallOut() {
		result = "Infield flies out";
		outs++;
		hitLocation();
	}
	
	public void walk() {
		result = "walks";
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
		hitLocation();
	}
	
	public void singleHit() {
		addHit();
		result = "singles";
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
		hitLocation();
	}
	
	public void doubleHit() {
		addHit();
		result = "doubles";
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
		hitLocation();
	}
	
	public void tripleHit() {
		addHit();
		result = "triples";
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
		hitLocation();
	}
	
	public void homeRunHit() {
		addHit();
		result = "homers";
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
		hitLocation();
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
			if (topOfInning) {
				topOfInning = false;
				topOfInningEOGCheck = false;
			} else {
				topOfInning = true;
				inning++;
			}
			updatePitcher();
		}
	}
	
	public void checkEndOfGame() {
		if (inning >= 9 && topOfInning == false && homeScore > awayScore) {
			endOfGame = true;
		} else if (inning > 9 && topOfInning == true && topOfInningEOGCheck == false && awayScore > homeScore) {
			endOfGame = true;
		} else if (inning > 9 && topOfInning == true && topOfInningEOGCheck == false) {
			topOfInningEOGCheck = true;
		}
		
	}
	
	public void addHit() {
		if (topOfInning)
			awayHits++;
		else
			homeHits++;
	}
	
	public void hitLocation() {
		int locationRoll = rand.nextInt(1000 - 1) + 1;
		if (pitch == "Fastball") {
			if (result == "grounds out") {
				if (locationRoll <= 20) {
					result += " to catcher";
				} else if (locationRoll <= 100) {
					result += " to pitcher";
				} else if (locationRoll <= 265) {
					result += " to first";
				} else if (locationRoll <= 510) {
					result += " to second";
				} else if (locationRoll <= 755) {
					result += " to short";
				} else {
					result += " to third";
				}
			} else if (result == "lines out") {
				if (locationRoll <= 20) {
					result += " to pitcher";
				} else if (locationRoll <= 40) {
					result += " to first";
				} else if (locationRoll <= 60) {
					result += " to second";
				} else if (locationRoll <= 80) {
					result += " to short";
				} else if (locationRoll <= 100){
					result += " to third";
				} else if (locationRoll <= 400) {
					result += " to right";
				} else if (locationRoll <= 700) {
					result += " to center";
				} else {
					result += " to left";
				}				
			} else if (result == "flies out") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			} else if (result == "Infield flies out") {
				if (locationRoll <= 100) {
					result = "flies out to catcher";
				} else if (locationRoll <= 200) {
					result = "flies out to pitcher";
				} else if (locationRoll <= 400) {
					result = "flies out to first";
				} else if (locationRoll <= 600) {
					result = "flies out to second";
				} else if (locationRoll <= 800) {
					result = "flies out to short";
				} else {
					result = "flies out to third";
				}
			} else if (result == "singles") {
				if (locationRoll <= 10) {
					result += " to catcher";
				} else if (locationRoll <= 20) {
					result += " to pitcher";
				} else if (locationRoll <= 40) {
					result += " to first";
				} else if (locationRoll <= 60) {
					result += " to second";
				} else if (locationRoll <= 80) {
					result += " to short";
				} else if (locationRoll <= 100){
					result += " to third";
				} else if (locationRoll <= 400) {
					result += " to right";
				} else if (locationRoll <= 700) {
					result += " to center";
				} else {
					result += " to left";
				}				
			} else if (result == "doubles") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			} else if (result == "triples") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			} else if (result == "homers") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			}
		} else if (pitch == "Two Seamer") {
			if (batter.batsR) {
				if (result == "grounds out") {
					if (locationRoll <= 20) {
						result += " to catcher";
					} else if (locationRoll <= 100) {
						result += " to pitcher";
					} else if (locationRoll <= 350) {
						result += " to first";
					} else if (locationRoll <= 600) {
						result += " to second";
					} else if (locationRoll <= 800) {
						result += " to short";
					} else {
						result += " to third";
					}
				} else if (result == "lines out") {
					if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 45) {
						result += " to first";
					} else if (locationRoll <= 70) {
						result += " to second";
					} else if (locationRoll <= 85) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 500) {
						result += " to right";
					} else if (locationRoll <= 800) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "flies out") {
					if (locationRoll <= 400) {
						result += " to right";
					} else if (locationRoll <= 700) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "Infield flies out") {
					if (locationRoll <= 100) {
						result = "flies out to catcher";
					} else if (locationRoll <= 200) {
						result = "flies out to pitcher";
					} else if (locationRoll <= 450) {
						result = "flies out to first";
					} else if (locationRoll <= 700) {
						result = "flies out to second";
					} else if (locationRoll <= 850) {
						result = "flies out to short";
					} else {
						result = "flies out to third";
					}
				} else if (result == "singles") {
					if (locationRoll <= 10) {
						result += " to catcher";
					} else if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 45) {
						result += " to first";
					} else if (locationRoll <= 70) {
						result += " to second";
					} else if (locationRoll <= 85) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 500) {
						result += " to right";
					} else if (locationRoll <= 800) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "doubles") {
					if (locationRoll <= 400) {
						result += " to right";
					} else if (locationRoll <= 700) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "triples") {
					if (locationRoll <= 400) {
						result += " to right";
					} else if (locationRoll <= 700) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "homers") {
					if (locationRoll <= 400) {
						result += " to right";
					} else if (locationRoll <= 700) {
						result += " to center";
					} else {
						result += " to left";
					}
				}

			} else { //batsL for Two-Seamer
				if (result == "grounds out") {
					if (locationRoll <= 20) {
						result += " to catcher";
					} else if (locationRoll <= 100) {
						result += " to pitcher";
					} else if (locationRoll <= 300) {
						result += " to first";
					} else if (locationRoll <= 500) {
						result += " to second";
					} else if (locationRoll <= 750) {
						result += " to short";
					} else {
						result += " to third";
					}
				} else if (result == "lines out") {
					if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 35) {
						result += " to first";
					} else if (locationRoll <= 50) {
						result += " to second";
					} else if (locationRoll <= 75) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 300) {
						result += " to right";
					} else if (locationRoll <= 600) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "flies out") {
					if (locationRoll <= 300) {
						result += " to right";
					} else if (locationRoll <= 600) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "Infield flies out") {
					if (locationRoll <= 100) {
						result = "flies out to catcher";
					} else if (locationRoll <= 200) {
						result = "flies out to pitcher";
					} else if (locationRoll <= 350) {
						result = "flies out to first";
					} else if (locationRoll <= 500) {
						result = "flies out to second";
					} else if (locationRoll <= 750) {
						result = "flies out to short";
					} else {
						result = "flies out to third";
					}
				} else if (result == "singles") {
					if (locationRoll <= 10) {
						result += " to catcher";
					} else if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 35) {
						result += " to first";
					} else if (locationRoll <= 50) {
						result += " to second";
					} else if (locationRoll <= 75) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 300) {
						result += " to right";
					} else if (locationRoll <= 600) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "doubles") {
					if (locationRoll <= 300) {
						result += " to right";
					} else if (locationRoll <= 600) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "triples") {
					if (locationRoll <= 300) {
						result += " to right";
					} else if (locationRoll <= 600) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "homers") {
					if (locationRoll <= 300) {
						result += " to right";
					} else if (locationRoll <= 600) {
						result += " to center";
					} else {
						result += " to left";
					}
				}				
			}
		} else if (pitch == "Changeup") {
			if (batter.batsR) {
				if (result == "grounds out") {
					if (locationRoll <= 20) {
						result += " to catcher";
					} else if (locationRoll <= 100) {
						result += " to pitcher";
					} else if (locationRoll <= 200) {
						result += " to first";
					} else if (locationRoll <= 400) {
						result += " to second";
					} else if (locationRoll <= 700) {
						result += " to short";
					} else {
						result += " to third";
					}
				} else if (result == "lines out") {
					if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 30) {
						result += " to first";
					} else if (locationRoll <= 50) {
						result += " to second";
					} else if (locationRoll <= 75) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 200) {
						result += " to right";
					} else if (locationRoll <= 500) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "flies out") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "Infield flies out") {
					if (locationRoll <= 100) {
						result = "flies out to catcher";
					} else if (locationRoll <= 200) {
						result = "flies out to pitcher";
					} else if (locationRoll <= 300) {
						result = "flies out to first";
					} else if (locationRoll <= 500) {
						result = "flies out to second";
					} else if (locationRoll <= 750) {
						result = "flies out to short";
					} else {
						result = "flies out to third";
					}
				} else if (result == "singles") {
					if (locationRoll <= 10) {
						result += " to catcher";
					} else if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 30) {
						result += " to first";
					} else if (locationRoll <= 50) {
						result += " to second";
					} else if (locationRoll <= 75) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 200) {
						result += " to right";
					} else if (locationRoll <= 500) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "doubles") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "triples") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "homers") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				}

			} else { //batsL for Change Up
				if (result == "grounds out") {
					if (locationRoll <= 20) {
						result += " to catcher";
					} else if (locationRoll <= 100) {
						result += " to pitcher";
					} else if (locationRoll <= 400) {
						result += " to first";
					} else if (locationRoll <= 700) {
						result += " to second";
					} else if (locationRoll <= 900) {
						result += " to short";
					} else {
						result += " to third";
					}
				} else if (result == "lines out") {
					if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 45) {
						result += " to first";
					} else if (locationRoll <= 70) {
						result += " to second";
					} else if (locationRoll <= 90) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 300) {
						result += " to right";
					} else if (locationRoll <= 600) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "flies out") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "Infield Fly Out") {
					if (locationRoll <= 100) {
						result = "flies out to catcher";
					} else if (locationRoll <= 200) {
						result = "flies out to pitcher";
					} else if (locationRoll <= 450) {
						result = "flies out to first";
					} else if (locationRoll <= 700) {
						result = "flies out to second";
					} else if (locationRoll <= 900) {
						result = "flies out to short";
					} else {
						result = "flies out to third";
					}
				} else if (result == "singles") {
					if (locationRoll <= 10) {
						result += " to catcher";
					} else if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 45) {
						result += " to first";
					} else if (locationRoll <= 70) {
						result += " to second";
					} else if (locationRoll <= 90) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 600) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "doubles") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "triples") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "homers") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				}				
			} //end else for Change Up
		} else if (pitch == "Curveball" || pitch == "Knuckle Curve") {
			if (pitcher.throwsL) {
				if (result == "grounds out") {
					if (locationRoll <= 20) {
						result += " to catcher";
					} else if (locationRoll <= 100) {
						result += " to pitcher";
					} else if (locationRoll <= 200) {
						result += " to first";
					} else if (locationRoll <= 400) {
						result += " to second";
					} else if (locationRoll <= 700) {
						result += " to short";
					} else {
						result += " to third";
					}
				} else if (result == "lines out") {
					if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 30) {
						result += " to first";
					} else if (locationRoll <= 50) {
						result += " to second";
					} else if (locationRoll <= 75) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 200) {
						result += " to right";
					} else if (locationRoll <= 500) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "flies out") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "Infield flies out") {
					if (locationRoll <= 100) {
						result = "flies out to catcher";
					} else if (locationRoll <= 200) {
						result = "flies out to pitcher";
					} else if (locationRoll <= 300) {
						result = "flies out to first";
					} else if (locationRoll <= 500) {
						result = "flies out to second";
					} else if (locationRoll <= 750) {
						result = "flies out to short";
					} else {
						result = "flies out to third";
					}
				} else if (result == "singles") {
					if (locationRoll <= 10) {
						result += " to catcher";
					} else if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 30) {
						result += " to first";
					} else if (locationRoll <= 50) {
						result += " to second";
					} else if (locationRoll <= 75) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 200) {
						result += " to right";
					} else if (locationRoll <= 500) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "doubles") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "triples") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "homers") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				}

			} else { //batsL for Change Up
				if (result == "grounds out") {
					if (locationRoll <= 20) {
						result += " to catcher";
					} else if (locationRoll <= 100) {
						result += " to pitcher";
					} else if (locationRoll <= 400) {
						result += " to first";
					} else if (locationRoll <= 700) {
						result += " to second";
					} else if (locationRoll <= 900) {
						result += " to short";
					} else {
						result += " to third";
					}
				} else if (result == "lines out") {
					if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 45) {
						result += " to first";
					} else if (locationRoll <= 70) {
						result += " to second";
					} else if (locationRoll <= 90) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 300) {
						result += " to right";
					} else if (locationRoll <= 600) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "flies out") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "Infield Fly Out") {
					if (locationRoll <= 100) {
						result = "flies out to catcher";
					} else if (locationRoll <= 200) {
						result = "flies out to pitcher";
					} else if (locationRoll <= 450) {
						result = "flies out to first";
					} else if (locationRoll <= 700) {
						result = "flies out to second";
					} else if (locationRoll <= 900) {
						result = "flies out to short";
					} else {
						result = "flies out to third";
					}
				} else if (result == "singles") {
					if (locationRoll <= 10) {
						result += " to catcher";
					} else if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 45) {
						result += " to first";
					} else if (locationRoll <= 70) {
						result += " to second";
					} else if (locationRoll <= 90) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 600) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "doubles") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "triples") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "homers") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				}				
			} //end else for Curveball
		} else if (pitch == "Slider" || pitch == "Cutter") {
			if (pitcher.throwsL) {
				if (result == "grounds out") {
					if (locationRoll <= 20) {
						result += " to catcher";
					} else if (locationRoll <= 100) {
						result += " to pitcher";
					} else if (locationRoll <= 200) {
						result += " to first";
					} else if (locationRoll <= 400) {
						result += " to second";
					} else if (locationRoll <= 700) {
						result += " to short";
					} else {
						result += " to third";
					}
				} else if (result == "lines out") {
					if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 30) {
						result += " to first";
					} else if (locationRoll <= 50) {
						result += " to second";
					} else if (locationRoll <= 75) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 200) {
						result += " to right";
					} else if (locationRoll <= 500) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "flies out") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "Infield flies out") {
					if (locationRoll <= 100) {
						result = "flies out to catcher";
					} else if (locationRoll <= 200) {
						result = "flies out to pitcher";
					} else if (locationRoll <= 300) {
						result = "flies out to first";
					} else if (locationRoll <= 500) {
						result = "flies out to second";
					} else if (locationRoll <= 750) {
						result = "flies out to short";
					} else {
						result = "flies out to third";
					}
				} else if (result == "singles") {
					if (locationRoll <= 10) {
						result += " to catcher";
					} else if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 30) {
						result += " to first";
					} else if (locationRoll <= 50) {
						result += " to second";
					} else if (locationRoll <= 75) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 200) {
						result += " to right";
					} else if (locationRoll <= 500) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "doubles") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "triples") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "homers") {
					if (locationRoll <= 100) {
						result += " to right";
					} else if (locationRoll <= 450) {
						result += " to center";
					} else {
						result += " to left";
					}
				}

			} else { //batsL for Change Up
				if (result == "grounds out") {
					if (locationRoll <= 20) {
						result += " to catcher";
					} else if (locationRoll <= 100) {
						result += " to pitcher";
					} else if (locationRoll <= 400) {
						result += " to first";
					} else if (locationRoll <= 700) {
						result += " to second";
					} else if (locationRoll <= 900) {
						result += " to short";
					} else {
						result += " to third";
					}
				} else if (result == "lines out") {
					if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 45) {
						result += " to first";
					} else if (locationRoll <= 70) {
						result += " to second";
					} else if (locationRoll <= 90) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 300) {
						result += " to right";
					} else if (locationRoll <= 600) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "flies out") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "Infield Fly Out") {
					if (locationRoll <= 100) {
						result = "flies out to catcher";
					} else if (locationRoll <= 200) {
						result = "flies out to pitcher";
					} else if (locationRoll <= 450) {
						result = "flies out to first";
					} else if (locationRoll <= 700) {
						result = "flies out to second";
					} else if (locationRoll <= 900) {
						result = "flies out to short";
					} else {
						result = "flies out to third";
					}
				} else if (result == "singles") {
					if (locationRoll <= 10) {
						result += " to catcher";
					} else if (locationRoll <= 20) {
						result += " to pitcher";
					} else if (locationRoll <= 45) {
						result += " to first";
					} else if (locationRoll <= 70) {
						result += " to second";
					} else if (locationRoll <= 90) {
						result += " to short";
					} else if (locationRoll <= 100){
						result += " to third";
					} else if (locationRoll <= 600) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}				
				} else if (result == "doubles") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "triples") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				} else if (result == "homers") {
					if (locationRoll <= 550) {
						result += " to right";
					} else if (locationRoll <= 900) {
						result += " to center";
					} else {
						result += " to left";
					}
				}				
			} //end else for Slider
		} else if (pitch == "Sinker" || pitch == "Splitter" || pitch == "Forkball") {
			if (result == "grounds out") {
				if (locationRoll <= 20) {
					result += " to catcher";
				} else if (locationRoll <= 100) {
					result += " to pitcher";
				} else if (locationRoll <= 265) {
					result += " to first";
				} else if (locationRoll <= 510) {
					result += " to second";
				} else if (locationRoll <= 755) {
					result += " to short";
				} else {
					result += " to third";
				}
			} else if (result == "lines out") {
				if (locationRoll <= 20) {
					result += " to pitcher";
				} else if (locationRoll <= 40) {
					result += " to first";
				} else if (locationRoll <= 60) {
					result += " to second";
				} else if (locationRoll <= 80) {
					result += " to short";
				} else if (locationRoll <= 100){
					result += " to third";
				} else if (locationRoll <= 400) {
					result += " to right";
				} else if (locationRoll <= 700) {
					result += " to center";
				} else {
					result += " to left";
				}				
			} else if (result == "flies out") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			} else if (result == "Infield flies out") {
				if (locationRoll <= 100) {
					result = "flies out to catcher";
				} else if (locationRoll <= 200) {
					result = "flies out to pitcher";
				} else if (locationRoll <= 400) {
					result = "flies out to first";
				} else if (locationRoll <= 600) {
					result = "flies out to second";
				} else if (locationRoll <= 800) {
					result = "flies out to short";
				} else {
					result = "flies out to third";
				}
			} else if (result == "singles") {
				if (locationRoll <= 10) {
					result += " to catcher";
				} else if (locationRoll <= 20) {
					result += " to pitcher";
				} else if (locationRoll <= 40) {
					result += " to first";
				} else if (locationRoll <= 60) {
					result += " to second";
				} else if (locationRoll <= 80) {
					result += " to short";
				} else if (locationRoll <= 100){
					result += " to third";
				} else if (locationRoll <= 400) {
					result += " to right";
				} else if (locationRoll <= 700) {
					result += " to center";
				} else {
					result += " to left";
				}				
			} else if (result == "doubles") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			} else if (result == "triples") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			} else if (result == "homers") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			}
		} else {
			if (result == "grounds out") {
				if (locationRoll <= 20) {
					result += " to catcher";
				} else if (locationRoll <= 100) {
					result += " to pitcher";
				} else if (locationRoll <= 265) {
					result += " to first";
				} else if (locationRoll <= 510) {
					result += " to second";
				} else if (locationRoll <= 755) {
					result += " to short";
				} else {
					result += " to third";
				}
			} else if (result == "lines out") {
				if (locationRoll <= 20) {
					result += " to pitcher";
				} else if (locationRoll <= 40) {
					result += " to first";
				} else if (locationRoll <= 60) {
					result += " to second";
				} else if (locationRoll <= 80) {
					result += " to short";
				} else if (locationRoll <= 100){
					result += " to third";
				} else if (locationRoll <= 400) {
					result += " to right";
				} else if (locationRoll <= 700) {
					result += " to center";
				} else {
					result += " to left";
				}				
			} else if (result == "flies out") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			} else if (result == "Infield flies out") {
				if (locationRoll <= 100) {
					result = "flies out to catcher";
				} else if (locationRoll <= 200) {
					result = "flies out to pitcher";
				} else if (locationRoll <= 400) {
					result = "flies out to first";
				} else if (locationRoll <= 600) {
					result = "flies out to second";
				} else if (locationRoll <= 800) {
					result = "flies out to short";
				} else {
					result = "flies out to third";
				}
			} else if (result == "singles") {
				if (locationRoll <= 10) {
					result += " to catcher";
				} else if (locationRoll <= 20) {
					result += " to pitcher";
				} else if (locationRoll <= 40) {
					result += " to first";
				} else if (locationRoll <= 60) {
					result += " to second";
				} else if (locationRoll <= 80) {
					result += " to short";
				} else if (locationRoll <= 100){
					result += " to third";
				} else if (locationRoll <= 400) {
					result += " to right";
				} else if (locationRoll <= 700) {
					result += " to center";
				} else {
					result += " to left";
				}				
			} else if (result == "doubles") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			} else if (result == "triples") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			} else if (result == "homers") {
				if (locationRoll <= 333) {
					result += " to right";
				} else if (locationRoll <= 667) {
					result += " to center";
				} else {
					result += " to left";
				}
			}
		}
		result = batter.lastName + " " + result;
	}
}
