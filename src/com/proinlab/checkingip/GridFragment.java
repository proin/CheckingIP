package com.proinlab.checkingip;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class GridFragment extends SherlockFragment {
	public static final String ARG_SECTION_DATA = "section_data";

	private GridCustomAdapter mAdapter;
	private GridView mGridView;
	private TextView mTextView;
	private ArrayList<String> arAddr;
	private boolean[] arStat;
	private int COUNT_ON = 0;

	private String[] arg_data = null;

	public GridFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		arg_data = getArguments().getStringArray(ARG_SECTION_DATA);

		LinearLayout linear = new LinearLayout(getActivity());
		linear.setOrientation(LinearLayout.VERTICAL);

		mTextView = new TextView(getActivity());
		mGridView = new GridView(getActivity());
		mGridView.setNumColumns(10);

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(5, 10, 5, 10);
		mTextView.setLayoutParams(llp);
		mTextView.setTextSize(15);
		linear.addView(mTextView);
		linear.addView(mGridView);

		int start = 0;
		if (arg_data[1].lastIndexOf(".") != -1)
			start = Integer.parseInt(arg_data[1].substring(arg_data[1]
					.lastIndexOf(".") + 1));
		else
			start = 0;
		arAddr = new ArrayList<String>();
		for (int i = start; i < start + Integer.parseInt(arg_data[3]); i++)
			arAddr.add(arg_data[1].substring(0,
					arg_data[1].lastIndexOf(".") + 1) + Integer.toString(i));

		arStat = new boolean[arAddr.size()];
		for (int i = 0; i < arAddr.size(); i++)
			arStat[i] = false;

		mAdapter = new GridCustomAdapter(getActivity(), arAddr, arStat);
		mGridView.setAdapter(mAdapter);

		return linear;
	}

	public void onResume() {
		super.onResume();
		System.gc();
		arg_data = getArguments().getStringArray(ARG_SECTION_DATA);

		COUNT_ON = 0;

		for (int i = 0; i < arAddr.size(); i++) {
			arStat[i] = false;
			ping(i);
		}

		mAdapter.notifyDataSetChanged();
		mTextView.setText("[ " + arg_data[1] + "* ] [ Activate : "
				+ Integer.toString(COUNT_ON) + " / "
				+ Integer.toString(arAddr.size()) + " ]");
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 0)
				arStat[msg.what] = false;
			else {
				arStat[msg.what] = true;
				COUNT_ON++;
			}
			mTextView.setText("[ " + arg_data[1] + "* ] [ Activate : "
					+ Integer.toString(COUNT_ON) + " / "
					+ Integer.toString(arAddr.size()) + " ]");
			mAdapter.notifyDataSetChanged();
		}
	};

	private void ping(final int position) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Process mProcess;
				int stat = 0;
				try {
					String ipaddr = arAddr.get(position);
					mProcess = new ProcessBuilder()
							.command("/system/bin/ping", "-c 1", "-W 1", ipaddr)
							.redirectErrorStream(true).start();

					InputStream in = mProcess.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					while (reader.read() != -1) {
						if (reader.readLine().contains("1 received"))
							stat = 1;
					}

					in.close();
					mProcess.destroy();
					mProcess = null;

				} catch (Exception e) {
				}

				final int statmsg = stat;
				mHandler.post(new Runnable() {
					public void run() {
						Message msg = new Message();
						msg.arg1 = statmsg;
						msg.what = position;
						mHandler.sendMessage(msg);
					}
				});

			}
		});
		thread.start();
	}
}
