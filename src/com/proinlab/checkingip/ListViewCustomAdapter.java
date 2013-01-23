package com.proinlab.checkingip;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class ListViewCustomAdapter extends BaseAdapter {

	private LayoutInflater Inflater;
	private ArrayList<String> arAddr;
	private boolean[] arStat;
	private int layout;

	public ListViewCustomAdapter(Context context, ArrayList<String> arAddr,
			boolean[] arStat) {
		Inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.arAddr = arAddr;
		this.arStat = arStat;
		layout = R.layout.listview_contents;
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

		ImageView StatView = (ImageView) convertView
				.findViewById(R.id.listview_content_stat);
		TextView ipaddress = (TextView) convertView
				.findViewById(R.id.listview_content_ipaddress);

		if (arStat[position])
			StatView.setImageResource(R.drawable.on);
		else
			StatView.setImageResource(R.drawable.off);
		ipaddress.setText(arAddr.get(position));

		convertView.setTag(position);

		return convertView;
	}

}
