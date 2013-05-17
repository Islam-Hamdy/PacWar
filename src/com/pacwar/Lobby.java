package com.pacwar;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Lobby extends Activity {
	ArrayList<String> list = null;
	Lobby t = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);

		final ListView listview = (ListView) findViewById(R.id.listview);

		try {
			DownloadFilesTask d = new DownloadFilesTask();
			done = false;
			d.execute(0);
			while (!done)
				;

			// list =new ArrayList<String>();
			// list.add("7amada");
			// list.add("7amo");
			// list.add("gad el 5arouf");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);

		// MyAdapterView adapt = new MyAdapterView(this);
		// adapt.setParameters(list, adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				System.out.println("+++++ " + position);
				final String item = (String) parent.getItemAtPosition(position);
				System.out.println(" _____  " + item);
				view.animate().setDuration(2000).alpha(0)
						.withEndAction(new Runnable() {
							@Override
							public void run() {
								adapter.notifyDataSetChanged();
								view.setAlpha(1);
							}
						});
				hostName = item + "";
				DownloadFilesTask d = new DownloadFilesTask();
				d.execute(connect);
				startIt();
			}
		});
	}

	public void startIt() {
		Intent myIntent = new Intent(this, MainActivity.class);
		startActivity(myIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lobby, menu);
		return true;
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

	byte[] sharedBuffer = new byte[1000];
	boolean done;
	String hostName;
	static String message;
	static final int getHostList = 0;
	static final int connect = 1;
	static final int disconnect = 2;
	static final int sendMessage = 3;
	static final int host = 4;
	static boolean joined = false;

	public class DownloadFilesTask extends AsyncTask<Integer, Integer, Long> {
		protected Long doInBackground(Integer... f) {
			try {
				url = new URL("http://myserver.herokuapp.com");
				userName = Login.name;
				if (f[0] == getHostList)
					getList();
				else {
					unregister();
					register();
					if (f[0] == connect) {
						connect();
					} else if (f[0] == disconnect)
						disconnect();
					else if (f[0] == sendMessage) {
						sendMessage();
					} else if (f[0] == host) {
						host();
					}
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			return 0L;
		}

		String password = "androidelteety ";
		byte[] sharedBuffer = new byte[1000];
		URL url;
		String userName;

		public void getList() throws Exception {
			done = false;
			URL url = new URL("http://myserver.herokuapp.com/?show=hosts");
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			// httpConn.setDoOutput(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			InputStream in = httpConn.getInputStream();
			int read;
			String tempstr = "";
			while ((read = in.read(sharedBuffer)) != -1)
				tempstr += new String(sharedBuffer, 0, read);
			in.close();
			Scanner myScanner = new Scanner(tempstr);
			myScanner.nextLine();
			ArrayList<String> res = new ArrayList<String>();
			while (myScanner.hasNext())
				res.add(myScanner.next());
			myScanner.close();
			list = res;
			done = true;
		}

		public InputStream reconnect() throws Exception {
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			httpConn.connect();
			OutputStream os = httpConn.getOutputStream();
			os.write((password + "reconnect " + userName).getBytes());
			os.close();
			System.out.println("jhabsndjasdd");
			return httpConn.getInputStream();
		}

		public boolean disconnect() throws Exception {
			connected = false;
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			httpConn.connect();
			OutputStream os = httpConn.getOutputStream();
			os.write((password + "disconnect " + userName).getBytes());
			os.close();
			InputStream in = httpConn.getInputStream();
			int read;
			String tempstr = "";
			while ((read = in.read(sharedBuffer)) != -1)
				tempstr += new String(sharedBuffer, 0, read);
			in.close();
			return tempstr.contains("succsessfully");
		}

		public boolean connect() throws Exception {
			connected = true;
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			httpConn.connect();
			OutputStream os = httpConn.getOutputStream();
			os.write((password + "connect " + userName + " " + hostName)
					.getBytes());
			os.close();
			InputStream in = httpConn.getInputStream();
			byte[] buffer = new byte[1000];
			int read;
			String tmpstr = "";
			while (connected) {
				while ((read = in.read(buffer)) != -1) {
					tmpstr = new String(buffer, 0, read);
					decode(tmpstr);
					MainActivity.model
							.SceneTouch(x, y, 1 - GameState.curPlayer);
				}
				in.close();
				in = reconnect();
			}
			in.close();
			return tmpstr.contains("succsessfully");
		}

		public boolean sendMessage() throws Exception {
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			httpConn.connect();
			OutputStream os = httpConn.getOutputStream();
			os.write((password + "message " + userName + " " + message)
					.getBytes());
			os.close();
			InputStream in = httpConn.getInputStream();
			byte[] buffer = new byte[1000];
			int read;
			String tempstr = "";
			while ((read = in.read(buffer)) != -1)
				tempstr += new String(buffer, 0, read);
			System.out.println(tempstr);
			in.close();
			return tempstr.contains("succsessfully");
		}

		public boolean register() throws Exception {
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			httpConn.connect();
			OutputStream os = httpConn.getOutputStream();
			os.write((password + "register " + userName).getBytes());
			os.close();
			InputStream in = httpConn.getInputStream();
			int read;
			String tempstr = "";
			while ((read = in.read(sharedBuffer)) != -1)
				tempstr += new String(sharedBuffer, 0, read);
			in.close();
			return tempstr.contains("succsessfully");
		}

		public boolean unregister() throws Exception {
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			httpConn.connect();
			OutputStream os = httpConn.getOutputStream();
			os.write((password + "unregister " + userName).getBytes());
			os.close();
			InputStream in = httpConn.getInputStream();
			int read;
			String tempstr = "";
			while ((read = in.read(sharedBuffer)) != -1)
				tempstr += new String(sharedBuffer, 0, read);
			in.close();
			return tempstr.contains("succsessfully");
		}

		public void host() throws Exception {
			connected = true;
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			httpConn.connect();
			OutputStream os = httpConn.getOutputStream();
			os.write((password + "host " + userName).getBytes());
			os.close();

			InputStream in = httpConn.getInputStream();
			byte[] buffer = new byte[1000];
			int read;
			String tmpstr = "";
			while (connected) {
				while ((read = in.read(buffer)) != -1) {
					tmpstr = new String(buffer, 0, read);
					if (tmpstr.contains("connected")) {
						joined = true;
					} else if(joined){
						decode(tmpstr);
						MainActivity.model.SceneTouch(x, y,
								1 - GameState.curPlayer);
					}
					// TODO message received from remote
					// TODO do something
				}
				in.close();
				in = reconnect();
			}
			in.close();
		}

		private float x, y;

		private void decode(String msg) {
			byte[] xx = new byte[4];
			for (int i = 0; i < 4; i++)
				xx[i] = (byte) msg.charAt(i);
			x = ByteBuffer.wrap(xx).order(ByteOrder.LITTLE_ENDIAN).getFloat();
			for (int i = 0; i < 4; i++)
				xx[i] = (byte) msg.charAt(i + 4);
			y = ByteBuffer.wrap(xx).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		}

		boolean connected = false;

		protected void onProgressUpdate(Integer... progress) {
		}

		protected void onPostExecute(Long result) {
		}
	}
}
