package com.proinlab.checkingip;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class ListViewCustomAdapter extends BaseAdapter {

	private LayoutInflater Inflater;
	private ArrayList<String> arSrc;
	private int layout;
	private ImageView[] StatView;

	public ListViewCustomAdapter(Context context, ArrayList<String> arSrc) {
		Inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.arSrc = arSrc;
		layout = R.layout.listview_contents;
	}

	public int getCount() {
		return arSrc.size();
	}

	public String getItem(int position) {
		return arSrc.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		StatView[position] = (ImageView) convertView
				.findViewById(R.id.listview_content_stat);
		TextView ipaddress = (TextView) convertView
				.findViewById(R.id.listview_content_ipaddress);

		StatView[position].setImageResource(R.drawable.off);
		ipaddress.setText(arSrc.get(position));

		convertView.setTag(position);

		PingTask mTask = new PingTask();
		mTask.execute(Integer.toString(position));
		
		return convertView;
	}

	private class PingTask extends AsyncTask<String, Void, Void> {
		private PipedOutputStream mPOut;
		private PipedInputStream mPIn;
		private LineNumberReader mReader;
		private Process mProcess;
		
		private int position;

		@Override
		protected void onPreExecute() {
			mPOut = new PipedOutputStream();
			try {
				mPIn = new PipedInputStream(mPOut);
				mReader = new LineNumberReader(new InputStreamReader(mPIn));
			} catch (IOException e) {
				cancel(true);
			}
		}

		public void stop() {
			Process p = mProcess;
			if (p != null)
				p.destroy();
			cancel(true);
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				position = Integer.parseInt(params[0]);
				String ipaddr = arSrc.get(position);
				mProcess = new ProcessBuilder()
						.command("/system/bin/ping", ipaddr)
						.redirectErrorStream(true).start();
				try {
					InputStream in = mProcess.getInputStream();
					OutputStream out = mProcess.getOutputStream();
					byte[] buffer = new byte[1024];
					int count;

					if ((count = in.read(buffer)) != -1) {
						count = in.read(buffer);
						mPOut.write(buffer, 0, count);
						publishProgress();
					}

					out.close();
					in.close();
					mPOut.close();
					mPIn.close();
				} catch (Exception e) {
				} finally {
					mProcess.destroy();
					mProcess = null;
					stop();
				}
			} catch (IOException e) {
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			try {
				if (mReader.ready()) {
					StatView[position].setImageResource(R.drawable.on);
				}
			} catch (IOException t) {
			}
		}
	}

}
