package com.proinlab.checkingip;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class GridCustomAdapter extends BaseAdapter {

	private LayoutInflater Inflater;
	private ArrayList<String> arAddr;
	private boolean[] arStat;
	private int layout;

	public GridCustomAdapter(Context context, ArrayList<String> arAddr,
			boolean[] arStat) {
		Inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.arAddr = arAddr;
		this.arStat = arStat;
		layout = R.layout.gridlist_contents;
	}

	public int getCount() {
		return arAddr.size();
	}

	public String getItem(int position) {
		return arAddr.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		TextView StatView = (TextView) convertView
				.findViewById(R.id.gridview_content);

		StatView.setText(Integer.toString(position % 100));
		if (arStat[position]) {
			StatView.setBackgroundResource(R.drawable.on);
		} else {
			StatView.setBackgroundResource(R.drawable.off);
		}

		convertView.setTag(position);

		return convertView;
	}

}
