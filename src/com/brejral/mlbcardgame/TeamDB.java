package com.brejral.mlbcardgame;

public class TeamDB {
	public static void populateTeamData(Team team) {
		switch (team.teamId) {
		case 1:
			team.teamName = "Los Angeles Angels";
			team.teamNickname = "Angels";
			team.teamAbrev = "LAA";
			team.overall = 0;
			team.logo = R.drawable.angels_logo;
			team.color[0] = 188;
			team.color[1] = 14;
			team.color[2] = 44;
			break;
		case 2:
			team.teamName = "Houston Astros";
			team.teamNickname = "Astros";
			team.teamAbrev = "HOU";
			team.overall = 0;
			team.logo = R.drawable.astros_logo;
			team.color[0] = 4;
			team.color[1] = 46;
			team.color[2] = 100;
			break;
		case 3:
			team.teamName = "Oakland Athletics";
			team.teamNickname = "Athletics";
			team.teamAbrev = "OAK";
			team.overall = 0;
			team.logo = R.drawable.athletics_logo;
			team.color[0] = 0;
			team.color[1] = 72;
			team.color[2] = 58;
			break;
		case 4:
			team.teamName = "Toronto Blue Jays";
			team.teamNickname = "Blue Jays";
			team.teamAbrev = "TOR";
			team.overall = 0;
			team.logo = R.drawable.bluejays_logo;
			team.color[0] = 28;
			team.color[1] = 45;
			team.color[2] = 92;
			break;
		case 5:
			team.teamName = "Atlanta Braves";
			team.teamNickname = "Braves";
			team.teamAbrev = "ATL";
			team.overall = 0;
			team.logo = R.drawable.braves_logo;
			team.color[0] = 188;
			team.color[1] = 14;
			team.color[2] = 44;
			break;
		case 6:
			team.teamName = "Milwaukee Brewers";
			team.teamNickname = "Brewers";
			team.teamAbrev = "MIL";
			team.overall = 0;
			team.logo = R.drawable.brewers_logo;
			team.color[0] = 0;
			team.color[1] = 34;
			team.color[2] = 93;
			break;
		case 7:
			team.teamName = "St. Louis Cardinals";
			team.teamNickname = "Cardinals";
			team.teamAbrev = "STL";
			team.overall = 0;
			team.logo = R.drawable.cardinals_logo;
			team.color[0] = 212;
			team.color[1] = 18;
			team.color[2] = 68;
			break;
		case 8:
			team.teamName = "Chicago Cubs";
			team.teamNickname = "Cubs";
			team.teamAbrev = "CHC";
			team.overall = 0;
			team.logo = R.drawable.cubs_logo;
			team.color[0] = 10;
			team.color[1] = 50;
			team.color[2] = 110;
			break;
		case 9:
			team.teamName = "Arizona Diamondbacks";
			team.teamNickname = "Diamondbacks";
			team.teamAbrev = "ARI";
			team.overall = 0;
			team.logo = R.drawable.diamondbacks_logo;
			team.color[0] = 164;
			team.color[1] = 26;
			team.color[2] = 44;
			break;
		case 10:
			team.teamName = "Los Angeles Dodgers";
			team.teamNickname = "Dodgers";
			team.teamAbrev = "LAD";
			team.overall = 0;
			team.logo = R.drawable.dodgers_logo;
			team.color[0] = 4;
			team.color[1] = 46;
			team.color[2] = 108;
			break;
		case 11:
			team.teamName = "San Fransisco Giants";
			team.teamNickname = "Giants";
			team.teamAbrev = "SF";
			team.overall = 0;
			team.logo = R.drawable.giants_logo;
			team.positions[0] = team.battingOrder[8] = new Card(13011);
			team.positions[1] = new Card(13014);
			team.positions[2] = team.battingOrder[2] = new Card(13007);
			team.positions[3] = team.battingOrder[5] = new Card(13005);
			team.positions[4] = team.battingOrder[1] = new Card(13013);
			team.positions[5] = team.battingOrder[3] = new Card(13017);
			team.positions[6] = team.battingOrder[7] = new Card(13006);
			team.positions[7] = team.battingOrder[6] = new Card(13008);
			team.positions[8] = team.battingOrder[0] = new Card(13003);
			team.positions[9] = team.battingOrder[4] = new Card(13009);
			team.color[0] = 252;
			team.color[1] = 70;
			team.color[2] = 20;
			break;
		case 12:
			team.teamName = "Cleveland Indians";
			team.teamNickname = "Indians";
			team.teamAbrev = "CLE";
			team.overall = 0;
			team.logo = R.drawable.indians_logo;
			team.color[0] = 212;
			team.color[1] = 2;
			team.color[2] = 52;
			break;
		case 13:
			team.teamName = "Seattle Mariners";
			team.teamNickname = "Mariners";
			team.teamAbrev = "SEA";
			team.overall = 0;
			team.logo = R.drawable.mariners_logo;
			team.color[0] = 4;
			team.color[1] = 42;
			team.color[2] = 92;
			break;
		case 14:
			team.teamName = "Miami Marlins";
			team.teamNickname = "Marlins";
			team.teamAbrev = "MIA";
			team.overall = 0;
			team.logo = R.drawable.marlins_logo;
			team.color[0] = 252;
			team.color[1] = 66;
			team.color[2] = 60;
			break;
		case 15:
			team.teamName = "New York Mets";
			team.teamNickname = "Mets";
			team.teamAbrev = "NYM";
			team.overall = 0;
			team.logo = R.drawable.mets_logo;
			team.color[0] = 4;
			team.color[1] = 46;
			team.color[2] = 116;
			break;
		case 16:
			team.teamName = "Washington Nationals";
			team.teamNickname = "Nationals";
			team.teamAbrev = "WAS";
			team.overall = 0;
			team.logo = R.drawable.nationals_logo;
			team.color[0] = 188;
			team.color[1] = 14;
			team.color[2] = 44;
			break;
		case 17:
			team.teamName = "Baltimore Orioles";
			team.teamNickname = "Orioles";
			team.teamAbrev = "BAL";
			team.overall = 0;
			team.logo = R.drawable.orioles_logo;
			team.color[0] = 252;
			team.color[1] = 78;
			team.color[2] = 4;
			break;
		case 18:
			team.teamName = "San Diego Padres";
			team.teamNickname = "Padres";
			team.teamAbrev = "SD";
			team.overall = 0;
			team.logo = R.drawable.padres_logo;
			team.color[0] = 4;
			team.color[1] = 30;
			team.color[2] = 68;
			break;
		case 19:
			team.teamName = "Philadelpia Phillies";
			team.teamNickname = "Phillies";
			team.teamAbrev = "PHI";
			team.overall = 0;
			team.logo = R.drawable.phillies_logo;
			team.color[0] = 4;
			team.color[1] = 82;
			team.color[2] = 156;
			break;
		case 20:
			team.teamName = "Pittsburgh Pirates";
			team.teamNickname = "Pirates";
			team.teamAbrev = "PIT";
			team.overall = 0;
			team.logo = R.drawable.pirates_logo;
			team.color[0] = 255;
			team.color[1] = 196;
			team.color[2] = 35;
			break;
		case 21:
			team.teamName = "Texas Rangers";
			team.teamNickname = "Rangers";
			team.teamAbrev = "TEX";
			team.overall = 0;
			team.logo = R.drawable.rangers_logo;
			team.color[0] = 172;
			team.color[1] = 14;
			team.color[2] = 52;
			break;
		case 22:
			team.teamName = "Tampa Bay Rays";
			team.teamNickname = "Rays";
			team.teamAbrev = "TB";
			team.overall = 0;
			team.logo = R.drawable.rays_logo;
			team.color[0] = 0;
			team.color[1] = 39;
			team.color[2] = 93;
			break;
		case 23:
			team.teamName = "Boston Red Sox";
			team.teamNickname = "Red Sox";
			team.teamAbrev = "BOS";
			team.overall = 0;
			team.logo = R.drawable.redsox_logo;
			team.color[0] = 220;
			team.color[1] = 25;
			team.color[2] = 50;
			break;
		case 24:
			team.teamName = "Cincinnati Reds";
			team.teamNickname = "Reds";
			team.teamAbrev = "CIN";
			team.overall = 0;
			team.logo = R.drawable.reds_logo;
			team.color[0] = 35;
			team.color[1] = 31;
			team.color[2] = 32;
			break;
		case 25:
			team.teamName = "Colorado Rockies";
			team.teamNickname = "Rockies";
			team.teamAbrev = "COL";
			team.overall = 0;
			team.logo = R.drawable.rockies_logo;
			team.color[0] = 36;
			team.color[1] = 18;
			team.color[2] = 92;
			break;
		case 26:
			team.teamName = "Kansas City Royals";
			team.teamNickname = "Royals";
			team.teamAbrev = "KC";
			team.overall = 0;
			team.logo = R.drawable.royals_logo;
			team.color[0] = 10;
			team.color[1] = 50;
			team.color[2] = 125;
			break;
		case 27:
			team.teamName = "Detroit Tigers";
			team.teamNickname = "Tigers";
			team.teamAbrev = "DET";
			team.overall = 0;
			team.logo = R.drawable.tigers_logo;
			team.positions[0] = team.battingOrder[4] = new Card(13020);	//Victor Martinez
			team.positions[1] = new Card(13012);						//Justin Verlander
			team.positions[2] = team.battingOrder[5] = new Card(13001);	//Alex Avila
			team.positions[3] = team.battingOrder[3] = new Card(13018); //Prince Field
			team.positions[4] = team.battingOrder[8] = new Card(13016);	//Omar Infante
			team.positions[5] = team.battingOrder[2] = new Card(13015);	//Miguel Cabrera
			team.positions[6] = team.battingOrder[7] = new Card(13010);	//Jhonny Peralta
			team.positions[7] = team.battingOrder[6] = new Card(13002);	//Andy Dirks
			team.positions[8] = team.battingOrder[0] = new Card(13004);	//Austin Jackson
			team.positions[9] = team.battingOrder[1] = new Card(13019); //Torii Hunter
			team.color[0] = 252;
			team.color[1] = 70;
			team.color[2] = 20;
			break;
		case 28:
			team.teamName = "Minnesota Twins";
			team.teamNickname = "Twins";
			team.teamAbrev = "MIN";
			team.overall = 0;
			team.logo = R.drawable.twins_logo;
			team.color[0] = 180;
			team.color[1] = 18;
			team.color[2] = 52;
			break;
		case 29:
			team.teamName = "Chicago White Sox";
			team.teamNickname = "White Sox";
			team.teamAbrev = "CWS";
			team.overall = 0;
			team.logo = R.drawable.whitesox_logo;
			team.color[0] = 4;
			team.color[1] = 2;
			team.color[2] = 4;
			break;
		case 30:
			team.teamName = "New York Yankees";
			team.teamNickname = "Yankees";
			team.teamAbrev = "NYY";
			team.overall = 0;
			team.logo = R.drawable.yankees_logo;
			team.color[0] = 12;
			team.color[1] = 46;
			team.color[2] = 132;
			break;
		default:
			team.teamName = "";
			team.teamNickname = "";
			team.teamAbrev = "";
			team.overall = 0;
			team.color[0] = 0;
			team.color[1] = 0;
			team.color[2] = 0;
			break;
		}
	}

}
