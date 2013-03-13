package com.brejral.mlbcardgame.exhibition;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.brejral.mlbcardgame.Engine;
import com.brejral.mlbcardgame.R;
import com.brejral.mlbcardgame.RowItem;
import com.brejral.mlbcardgame.Team;

public class ExhibitionSelectTeam extends Activity implements OnItemClickListener
{
	public final Team[] mlb_teams = Engine.makeMlbTeamArray();
	public static boolean homeTeamSelect = false;
		
	ListView listView;
	List<RowItem> rowItems;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exhibition_select_team);
		
		rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < mlb_teams.length; i++) {
			RowItem item = new RowItem(mlb_teams[i].logo, mlb_teams[i].teamName, mlb_teams[i].overall);
			rowItems.add(item);
		}
		
		listView = (ListView) findViewById(R.id.teamList);
		ExhibitionSelectTeamListViewAdapter adapter = new ExhibitionSelectTeamListViewAdapter(this, R.layout.mlb_team_list_item, rowItems);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
				
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (homeTeamSelect) {
			ExhibitionMenu.homeTeam = new Team(position + 1);
		} else {
			ExhibitionMenu.awayTeam = new Team(position + 1 );
		}
		homeTeamSelect = false;
		ExhibitionSelectTeam.this.finish();
	}

}
