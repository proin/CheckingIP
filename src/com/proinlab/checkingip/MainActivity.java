package com.proinlab.checkingip;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity implements
		ActionBar.OnNavigationListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private Fragment fragment;
	private ActionBar actionBar;
	private ArrayList<String> dropboxList = new ArrayList<String>();
	private ArrayList<String[]> arData = new ArrayList<String[]>();
	private DataBaseHelper mHelper;
	private DatabaseEdit mDBEdit = new DatabaseEdit();
	private int PRE_POSITION = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		mHelper = new DataBaseHelper(this);

	}

	public void onResume() {
		super.onResume();

		arData = mDBEdit.READ(mHelper);
		dropboxList = new ArrayList<String>();
		if (arData.size() == 0) {
			dropboxList.add("No List");
		} else {
			for (int i = 0; i < arData.size(); i++) {
				dropboxList.add(arData.get(i)[2]);
			}
		}
		dropboxList.add("+ Add");

		actionBar.setListNavigationCallbacks(new ArrayAdapter<String>(
				getActionBarThemedContextCompat(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				dropboxList), this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_sync:
			if (dropboxList.get(PRE_POSITION).equals("+ Add")) {
			} else if (dropboxList.get(PRE_POSITION).equals("No List")) {
			} else {
				fragment.onResume();
			}

			return true;
		case R.id.menu_delete:
			DeleteDialog();
		}
		return false;
	}

	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getSupportActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		if (dropboxList.get(position).equals("+ Add")) {
			AddDialog();
		} else if (dropboxList.get(position).equals("No List")) {
			PRE_POSITION = position;
		} else {
			fragment = new GridFragment();
			Bundle args = new Bundle();
			args.putStringArray(GridFragment.ARG_SECTION_DATA,
					arData.get(position));
			fragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).commit();
			PRE_POSITION = position;
		}
		return true;
	}

	private void AddDialog() {
		final LinearLayout linear = (LinearLayout) View.inflate(this,
				R.layout.add_list_dialog, null);
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setView(linear);
		alt_bld.setTitle("Add List")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText Title = (EditText) linear
								.findViewById(R.id.add_dialog_title);
						EditText ip1 = (EditText) linear
								.findViewById(R.id.add_dialog_ip1);
						EditText ip2 = (EditText) linear
								.findViewById(R.id.add_dialog_ip2);
						EditText ip3 = (EditText) linear
								.findViewById(R.id.add_dialog_ip3);
						EditText ip4 = (EditText) linear
								.findViewById(R.id.add_dialog_ip4);
						EditText count = (EditText) linear
								.findViewById(R.id.add_dialog_count);
						String ip = ip1.getText().toString() + "."
								+ ip2.getText().toString() + "."
								+ ip3.getText().toString() + "."
								+ ip4.getText().toString();

						if (Title.getText().length() == 0
								|| ip1.getText().length() == 0
								|| ip2.getText().length() == 0
								|| ip3.getText().length() == 0
								|| ip4.getText().length() == 0
								|| count.getText().length() == 0) {
							AddDialog();
							return;
						}
						
						if (Title.getText().toString().equals("No List")) {
							Toast.makeText(MainActivity.this,
									"추가 할 수 없는 단어입니다.", Toast.LENGTH_LONG)
									.show();
							AddDialog();
							return;
						} else if (Title.getText().toString().equals("+ Add")) {
							Toast.makeText(MainActivity.this,
									"추가 할 수 없는 단어입니다.", Toast.LENGTH_LONG)
									.show();
							AddDialog();
							return;
						}

						mDBEdit.INSERT(mHelper, ip, Title.getText().toString(),
								count.getText().toString());
						MainActivity.this.onResume();
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						actionBar.setSelectedNavigationItem(PRE_POSITION);
					}
				});
		AlertDialog alert = alt_bld.create();
		alert.setCanceledOnTouchOutside(true);
		alert.show();
	}

	private void DeleteDialog() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setTitle("Delete").setMessage("삭제하시겠습니까?")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDBEdit.DELETE(mHelper, arData.get(PRE_POSITION)[0]);
						PRE_POSITION = 0;
						MainActivity.this.onResume();
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		AlertDialog alert = alt_bld.create();
		alert.setCanceledOnTouchOutside(true);
		alert.show();
	}
}
