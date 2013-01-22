package com.proinlab.checkingip;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class GetIpFragment extends SherlockFragment {
	public static final String ARG_SECTION_NUMBER = "section_number";

	private TextView mText;
	
//	private PingTask mTask;

	public GetIpFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mText = new TextView(getActivity());
		mText.setGravity(Gravity.CENTER);
		mText.setText(Integer.toString(getArguments()
				.getInt(ARG_SECTION_NUMBER)));
		return mText;
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		mTask = new PingTask();
//		switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
//		case 1:
//			mTask.execute("172.30.1.20");
//			break;
//		case 2:
//			mTask.execute("172.30.1.1");
//			break;
//		case 3:
//			mTask.execute("172.30.1.19");
//			break;
//		}
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		if (!mTask.isCancelled())
//			mTask.stop();
//	}
//
//	private class PingTask extends AsyncTask<String, Void, Void> {
//		private PipedOutputStream mPOut;
//		private PipedInputStream mPIn;
//		private LineNumberReader mReader;
//		private Process mProcess;
//
//		@Override
//		protected void onPreExecute() {
//			mPOut = new PipedOutputStream();
//			try {
//				mPIn = new PipedInputStream(mPOut);
//				mReader = new LineNumberReader(new InputStreamReader(mPIn));
//			} catch (IOException e) {
//				cancel(true);
//			}
//		}
//
//		public void stop() {
//			Process p = mProcess;
//			if (p != null)
//				p.destroy();
//			cancel(true);
//		}
//
//		@Override
//		protected Void doInBackground(String... params) {
//			try {
//				mProcess = new ProcessBuilder()
//						.command("/system/bin/ping", params[0])
//						.redirectErrorStream(true).start();
//				try {
//					InputStream in = mProcess.getInputStream();
//					OutputStream out = mProcess.getOutputStream();
//					byte[] buffer = new byte[1024];
//					int count;
//
//					if ((count = in.read(buffer)) != -1) {
//						count = in.read(buffer);
//						mPOut.write(buffer, 0, count);
//						publishProgress();
//					}
//
//					out.close();
//					in.close();
//					mPOut.close();
//					mPIn.close();
//				} catch (Exception e) {
//				} finally {
//					mProcess.destroy();
//					mProcess = null;
//					stop();
//				}
//			} catch (IOException e) {
//			}
//			return null;
//		}
//
//		@Override
//		protected void onProgressUpdate(Void... values) {
//			try {
//				if (mReader.ready()) {
//					mText.setText(mReader.readLine());
//				}
//			} catch (IOException t) {
//			}
//		}
//	}

}
