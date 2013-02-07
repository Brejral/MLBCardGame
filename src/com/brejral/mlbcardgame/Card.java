package com.brejral.mlbcardgame;

public class Card {
	int id;  
	String name;
	int imageId;
	boolean batsR, batsL, batsS, throwsR, throwsL;
	boolean _c, _1b, _2b, _3b, _ss, _lf, _cf, _rf, _dh, _sp, _rp, _cp;
	
	// stats12* - Stats from 2012 
	// [0] - plate appearances
	// [1] - hits
	// [2] - doubles
	// [3] - triples
	// [4] - home runs
	// [5] - walks
	// [6] - strike outs
	int stats12L[] = new int[7]; 
	int stats12R[] = new int[7]; 

	// hitSpread*[] - Percentages of type of hit, ld, gb, fb, iffb
	// [0] - line drive %
	// [1] - ground ball %
	// [2] - fly ball %
	// [3] - infield fly ball %
	int hitSpreadL[] = new int[4]; 
	int hitSpreadR[] = new int[4];
	
	// pitchType[] - Pitches against/used by percentage
	// [0] - fast ball
	// [1] - two seamer
	// [2] - change up
	// [3] - curve ball (knuckle curve)
	// [4] - slider (cutter)
	// [5] - sinker (splitter, forkball)
	// [6] - special (includes all other pitches)
	int pitchType[] = new int[7]; 
	String[] pitches;
	
	// bip*[] - contains three outcomes for a plate appearance, walk, strike out, ball in play
	// [0] - walk
	// [1] - strike out	
	// ball in play not included, else statement is utilized in code
	int bipL[] = new int[2];
	int bipR[] = new int[2];
	
	// babip* - Batting average on balls in play. Used to check for a hit on balls in play
	int babipL;
	int babipR;
	
	// hits*[] - hit chart to see which hit occurs on a hit on a ball in play
	// [0] - hits 
	// [1] - doubles %
	// [2] - triples %
	// home runs % not included. But accounted for in an else statement in code
	int hitsL[] = new int[3];
	int hitsR[] = new int[3];
	
	// *Stats[]
	// [0] = plate appearances
	// [1] - outs (pitcher) (used in ip calculation)
	// [2] - hits
	// [3] - doubles
	// [4] - triples
	// [5] - home runs
	// [6] - walks
	// [7] - strike outs
	// [8] - sacrifices
	// [9] - runs
	// [10] - rbis
	int gameStats[] = new int[11]; 
	int seasonStats[] = new int[11];
	int careerStats[] = new int[11];
	
	
	public Card(int id){
		this.id = id;
		CardDB.populateCard(this);
	}

}