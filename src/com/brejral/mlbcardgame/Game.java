package com.brejral.mlbcardgame;

import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Game extends Activity {
	private GameView gameView;
	public Card batter, pitcher, runner1, runner2, runner3;
	public static Team homeTeam, awayTeam;
	public ImageView pitcherCard, batterCard, runner1Card, runner2Card, runner3Card;
	public TextView resultText, battingOrderText;
	public String pitch;
	public int pit;
	public Random rand = new Random();
	public int inning = 1, outs;
	public boolean topOfInning = true;
	public int homeScore, awayScore;
	public int runnerAdvance;
	public String result;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameView = new GameView(this);
		setContentView(R.layout.game);
		
		final Animation zoomCardAnimation = AnimationUtils.loadAnimation(this, R.animator.card_zoom_animation);
		
		resultText = (TextView)findViewById(R.id.resultText);
		battingOrderText = (TextView)findViewById(R.id.battingOrderText);
		
		pitcherCard = (ImageView)findViewById(R.id.pitcherCard);
		batterCard = (ImageView)findViewById(R.id.batterCard);
		runner1Card = (ImageView)findViewById(R.id.runner1Card);
		runner2Card = (ImageView)findViewById(R.id.runner2Card);
		runner3Card = (ImageView)findViewById(R.id.runner3Card);

		pitcher = homeTeam.positions[1];
		batter = awayTeam.battingOrder[awayTeam.battingOrderNum];
		
		pitcherCard.setImageResource(pitcher.imageId);
		batterCard.setImageResource(batter.imageId);
		
		setPitchButtons();
		
		pitcherCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				pitcherCard.startAnimation(zoomCardAnimation);
			}
		});
		
		updateView();
	}
	
	public void setPitchButtons() {
		LinearLayout pitchButtons = (LinearLayout)findViewById(R.id.pitchButtons);
		for(int i = 0; i < pitcher.pitches.length; i++) {
			Button btnTag = new Button(this);
			btnTag.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 60));
			btnTag.setText(pitcher.pitches[i]);
			btnTag.setId(i);
			pit = i;
			btnTag.setOnClickListener(new OnClickListener() {
				public int p = pit;
				@Override
				public void onClick(View v) {
					pitch(p);
				}
			});
			pitchButtons.addView(btnTag);
		}
	}
	
	public void pitch(int n) {
		pitchRoll(n);
		advanceBatterNum();
		checkEndOfInning();
		updateBatter();
		updateView();
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
		if (topOfInning) 
			awayScore++;
		else
			homeScore++;
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

	public void updateView() {
		resultText.setText(result);
		pitcherCard.setImageResource(pitcher.imageId);
		batterCard.setImageResource(batter.imageId);
		if (runner1 != null)
			runner1Card.setImageResource(runner1.imageId);
		else
			runner1Card.setImageResource(0);
		if (runner2 != null)
			runner2Card.setImageResource(runner2.imageId);
		else
			runner2Card.setImageResource(0);			
		if (runner3 != null)
			runner3Card.setImageResource(runner3.imageId);
		else
			runner3Card.setImageResource(0);
		updateBattingOrder();
	}
	
	public void updateBattingOrder() {
		battingOrderText.setText("");
		if (topOfInning) {
			for (int i = 0; i < awayTeam.battingOrder.length; i++) {
				String playerString = (i+1)+" "+awayTeam.findPosition(i)+" "+awayTeam.battingOrder[i].name+"\n";
				if (awayTeam.battingOrderNum == i) {
					SpannableString playerSpannableString = new SpannableString(playerString);
					playerSpannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, playerString.length(), 0);
					battingOrderText.append(playerSpannableString);
				} else {
					battingOrderText.append(playerString);
				}
			}
		} else {
			for (int i = 0; i < homeTeam.battingOrder.length; i++) {
				String playerString = (i+1)+" "+homeTeam.findPosition(i)+" "+homeTeam.battingOrder[i].name+"\n";
				if (homeTeam.battingOrderNum == i) {
					SpannableString playerSpannableString = new SpannableString(playerString);
					playerSpannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, playerString.length(), 0);
					battingOrderText.append(playerSpannableString);
				} else {
					battingOrderText.append(playerString);
				}	
			}			
		}
	}
}
