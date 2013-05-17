package com.pacwar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Lobby extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);

		final ListView listview = (ListView) findViewById(R.id.listview);

		ArrayList<String> list = null;
		try {
//			list = ServerMethods.getHostsList();
			list =new ArrayList<String>();
			list.add("7amada");
			list.add("7amo");
			list.add("gad el 5arouf");
		} catch (Exception e) {
			System.out.println("gad el 5arouf");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("------------"+list);

		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);

		MyAdapterView adapt = new MyAdapterView(this);
		adapt.setParameters(list, adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lobby, menu);
		return true;
	}

	private class MyAdapterView extends AdapterView implements
			OnItemClickListener {
		ArrayList<String> list;
		StableArrayAdapter adapter;

		public MyAdapterView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public void setParameters(ArrayList<String> ls,
				StableArrayAdapter adapter) {
			this.list = ls;
			this.adapter = adapter;
		}

		@Override
		public Adapter getAdapter() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public View getSelectedView() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setAdapter(Adapter adapter) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setSelection(int position) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onItemClick(AdapterView<?> parent, final View view,
				int position, long id) {
			final String item = (String) parent.getItemAtPosition(position);
			view.animate().setDuration(2000).alpha(0)
					.withEndAction(new Runnable() {
						@Override
						public void run() {
							list.remove(item);
							adapter.notifyDataSetChanged();
							view.setAlpha(1);
						}
					});
		}

	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}
}
