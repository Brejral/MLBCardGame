package com.brejral.mlbcardgame;

public class TeamDB {
	public static void populateTeamData(Team team) {
		switch (team.teamId) {
		case 1:
			team.teamName = "Los Angeles Angels";
			team.teamNickname = "Angels";
			team.overall = 0;
			team.logo = R.drawable.angels_logo;
			break;
		case 2:
			team.teamName = "Houston Astros";
			team.teamNickname = "Astros";
			team.overall = 0;
			team.logo = R.drawable.astros_logo;
			break;
		case 3:
			team.teamName = "Oakland Athletics";
			team.teamNickname = "Athletics";
			team.overall = 0;
			team.logo = R.drawable.athletics_logo;
			break;
		case 4:
			team.teamName = "Toronto Blue Jays";
			team.teamNickname = "Blue Jays";
			team.overall = 0;
			team.logo = R.drawable.bluejays_logo;
			break;
		case 5:
			team.teamName = "Atlanta Braves";
			team.teamNickname = "Braves";
			team.overall = 0;
			team.logo = R.drawable.braves_logo;
			break;
		case 6:
			team.teamName = "Milwaukee Brewers";
			team.teamNickname = "Brewers";
			team.overall = 0;
			team.logo = R.drawable.brewers_logo;
			break;
		case 7:
			team.teamName = "St. Louis Cardinals";
			team.teamNickname = "Cardinals";
			team.overall = 0;
			team.logo = R.drawable.cardinals_logo;
			break;
		case 8:
			team.teamName = "Chicago Cubs";
			team.teamNickname = "Cubs";
			team.overall = 0;
			team.logo = R.drawable.cubs_logo;
			break;
		case 9:
			team.teamName = "Arizona Diamondbacks";
			team.teamNickname = "Diamondbacks";
			team.overall = 0;
			team.logo = R.drawable.diamondbacks_logo;
			break;
		case 10:
			team.teamName = "Los Angeles Dodgers";
			team.teamNickname = "Dodgers";
			team.overall = 0;
			team.logo = R.drawable.dodgers_logo;
			break;
		case 11:
			team.teamName = "San Fransisco Giants";
			team.teamNickname = "Giants";
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
			break;
		case 12:
			team.teamName = "Cleveland Indians";
			team.teamNickname = "Indians";
			team.overall = 0;
			team.logo = R.drawable.indians_logo;
			break;
		case 13:
			team.teamName = "Seattle Mariners";
			team.teamNickname = "Mariners";
			team.overall = 0;
			team.logo = R.drawable.mariners_logo;
			break;
		case 14:
			team.teamName = "Miami Marlins";
			team.teamNickname = "Marlins";
			team.overall = 0;
			team.logo = R.drawable.marlins_logo;
			break;
		case 15:
			team.teamName = "New York Mets";
			team.teamNickname = "Mets";
			team.overall = 0;
			team.logo = R.drawable.mets_logo;
			break;
		case 16:
			team.teamName = "Washington Nationals";
			team.teamNickname = "Nationals";
			team.overall = 0;
			team.logo = R.drawable.nationals_logo;
			break;
		case 17:
			team.teamName = "Baltimore Orioles";
			team.teamNickname = "Orioles";
			team.overall = 0;
			team.logo = R.drawable.orioles_logo;
			break;
		case 18:
			team.teamName = "San Diego Padres";
			team.teamNickname = "Padres";
			team.overall = 0;
			team.logo = R.drawable.padres_logo;
			break;
		case 19:
			team.teamName = "Philadelpia Phillies";
			team.teamNickname = "Phillies";
			team.overall = 0;
			team.logo = R.drawable.phillies_logo;
			break;
		case 20:
			team.teamName = "Pittsburgh Pirates";
			team.teamNickname = "Pirates";
			team.overall = 0;
			team.logo = R.drawable.pirates_logo;
			break;
		case 21:
			team.teamName = "Texas Rangers";
			team.teamNickname = "Rangers";
			team.overall = 0;
			team.logo = R.drawable.rangers_logo;
			break;
		case 22:
			team.teamName = "Tampa Bay Rays";
			team.teamNickname = "Rays";
			team.overall = 0;
			team.logo = R.drawable.rays_logo;
			break;
		case 23:
			team.teamName = "Boston Red Sox";
			team.teamNickname = "Red Sox";
			team.overall = 0;
			team.logo = R.drawable.redsox_logo;
			break;
		case 24:
			team.teamName = "Cincinnati Reds";
			team.teamNickname = "Reds";
			team.overall = 0;
			team.logo = R.drawable.reds_logo;
			break;
		case 25:
			team.teamName = "Colorado Rockies";
			team.teamNickname = "Rockies";
			team.overall = 0;
			team.logo = R.drawable.rockies_logo;
			break;
		case 26:
			team.teamName = "Kansas City Royals";
			team.teamNickname = "Royals";
			team.overall = 0;
			team.logo = R.drawable.royals_logo;
			break;
		case 27:
			team.teamName = "Detroit Tigers";
			team.teamNickname = "Tigers";
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
			break;
		case 28:
			team.teamName = "Minnesota Twins";
			team.teamNickname = "Twins";
			team.overall = 0;
			team.logo = R.drawable.twins_logo;
			break;
		case 29:
			team.teamName = "Chicago White Sox";
			team.teamNickname = "White Sox";
			team.overall = 0;
			team.logo = R.drawable.whitesox_logo;
			break;
		case 30:
			team.teamName = "New York Yankees";
			team.teamNickname = "Yankees";
			team.overall = 0;
			team.logo = R.drawable.yankees_logo;
			break;
		default:
			team.teamName = "";
			team.teamNickname = "";
			team.overall = 0;
			break;
		}
	}

}
