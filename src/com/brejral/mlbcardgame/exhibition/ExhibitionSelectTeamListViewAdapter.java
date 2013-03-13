package com.brejral.mlbcardgame.exhibition;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brejral.mlbcardgame.R;
import com.brejral.mlbcardgame.RowItem;

public class ExhibitionSelectTeamListViewAdapter extends ArrayAdapter<RowItem> {
	
	Context context;
	
	public ExhibitionSelectTeamListViewAdapter(Context context, int resourceId, List<RowItem> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	private class ViewHolder {
		ImageView logoView;
		TextView teamName;
		TextView overall;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowItem rowItem = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.mlb_team_list_item, null);
			holder = new ViewHolder();
			holder.logoView = (ImageView) convertView.findViewById(R.id.teamLogo);
			holder.teamName = (TextView) convertView.findViewById(R.id.teamNameLabel);
			holder.overall = (TextView) convertView.findViewById(R.id.teamOverallLabel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.logoView.setImageResource(rowItem.getLogoId());
		holder.teamName.setText(rowItem.getTeamName());
		holder.overall.setText(rowItem.getOverall());
		
		return convertView;
	}
}
