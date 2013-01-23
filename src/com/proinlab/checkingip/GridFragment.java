package com.proinlab.checkingip;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class GridFragment extends SherlockFragment {
	public static final String ARG_SECTION_NUMBER = "section_number";

	private GridCustomAdapter mAdapter;
	private GridView mGridView;
	private TextView mTextView;
	private ArrayList<String> arAddr;
	private boolean[] arStat;
	private int COUNT_ON = 0;

	public GridFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

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

		arAddr = new ArrayList<String>();
		for (int i = 0; i < 50; i++)
			arAddr.add("172.30.1." + Integer.toString(i));
		
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
		COUNT_ON = 0;

		for (int i = 0; i < arAddr.size(); i++) {
			arStat[i] = false;
			ping(i);
		}

		mAdapter.notifyDataSetChanged();
		mTextView.setText("Activate : " + Integer.toString(COUNT_ON) + " / "
				+ Integer.toString(arAddr.size()));
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 0)
				arStat[msg.what] = false;
			else {
				arStat[msg.what] = true;
				COUNT_ON++;
			}
			mTextView.setText("Activate : " + Integer.toString(COUNT_ON)
					+ " / " + Integer.toString(arAddr.size()));
			// mTextView.setText(str);
			mAdapter.notifyDataSetChanged();
		}
	};

	// private String str = "";

	private void ping(final int position) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Process mProcess;
				int stat = 0;
				try {
					String ipaddr = arAddr.get(position);
					mProcess = new ProcessBuilder()
							.command("/system/bin/ping", "-c 1", ipaddr)
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
