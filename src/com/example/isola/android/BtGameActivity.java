package com.example.isola.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.isola.R;
import com.example.isola.game.DestroyEvent;
import com.example.isola.game.GameEvent;
import com.example.isola.game.GameOverEvent;
import com.example.isola.game.MoveEvent;
import com.example.isola.game.Player;
/**
 * @author Felix Kibellus
 * @version Alpha
 * BtGameActivity is a class for a game about a bluetooth-connection
 * */
public class BtGameActivity extends GameActivity implements OnItemClickListener {

	ArrayAdapter<String> listAdapter;
	ListView listView;
	Button create;
	BluetoothAdapter btAdapter;
	Set<BluetoothDevice> devicesArray;
	ArrayList<String> pairedDevices;
	ArrayList<BluetoothDevice> devices;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	protected static final int SUCCESS_CONNECT = 0;
	protected static final int MESSAGE_READ = 1;
	IntentFilter filter;
	BroadcastReceiver receiver;
	String tag = "debugging";
	ConnectedThread connectedThread;
	
	//game attributes
	private boolean isPlayer1;
	/**
	 * The Handler reacts to a receive of the connected socket
	 * If it is a SUCCESS_CONNECT-receive a game is started
	 * if it is a  MESSAGE_READ-receive the turn is sent to the local game
	 * */
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Log.i(tag, "in handler");
			super.handleMessage(msg);
			switch(msg.what){
			case SUCCESS_CONNECT:
				connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
				Toast.makeText(getApplicationContext(), R.string.toast_connected, 0).show();
				connectedThread.start();
				Log.i(tag, "connected");
				isPlayer1=false; //player 2
				p1 = Player.BTPLAYER;
				startGame();
				runOnUiThread(new Runnable() {				
					@Override
					public void run() {
						setContentView(bv);
					}
				});
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[])msg.obj;
				int x = readBuf[0];
				int y = readBuf[1];			
				game.doTurn(x, y);
				break;
			}
		}
	};
	
	@Override
	public void onBackPressed() {
	    if(connectedThread!=null)
	    	connectedThread.cancel();
	    finish();
	}
	/**
	 * If the local board has changed this method decides whether it is a DESTROY or a MOVE event.
	 * Then the coordinates of the turn are sent to the opponent
	 * */
	@Override
	public void update(Observable board, Object data) {
		//return if the change come from the other device
		if(!(game.isP1Turn()==isPlayer1))
			return;
		GameEvent ev = (GameEvent) data;
		Log.i(tag, "update event");
		byte x;
		byte y;
		byte[] bytes= new byte[2];
		switch (ev.getType()) {
		case DESTROY:
			DestroyEvent de = (DestroyEvent) ev;
			x = (byte) de.getX();
			y = (byte) de.getY();
			bytes[0]=x;
			bytes[1]=y;
			break;
		case MOVE:
			MoveEvent me = (MoveEvent)ev;
			x = (byte) me.getNewX();
			y = (byte) me.getNewY();
			bytes[0]=x;
			bytes[1]=y;
			break;
		case GAMEOVER:
			endGame();
			break;
		}
		connectedThread.write(bytes);
	}
	
	/**
	 * The onCreate-Method runs the constructor of GameActivity and initializes the game
	 * After that it is proved whether bluetooth is enabled, if not it is activated
	 * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_join);
        init();
        if(btAdapter==null){
        	Toast.makeText(getApplicationContext(), "No bluetooth detected", 0).show();
        	finish();
        }
        else{
        	if(!btAdapter.isEnabled()){
        		turnOnBT();
        	}        	
        	getPairedDevices();
        	startDiscovery();
        }
    }

    /**
     * The method starts the search for other bluetooth-devices
     * */
	private void startDiscovery() {
		btAdapter.cancelDiscovery();
		btAdapter.startDiscovery();
	}
	
	/**
	 * Starts the bluetooth-adapter
	 * */
	private void turnOnBT() {
		btAdapter.enable();
	}
	
	/**
	 * searches for paired Devices
	 * */
	private void getPairedDevices() {
		devicesArray = btAdapter.getBondedDevices();
		if(devicesArray.size()>0){
			for(BluetoothDevice device:devicesArray){
				pairedDevices.add(device.getName());
			}
		}
	}
	
	/**
	 * Initializes the attributes
	 * */
	private void init() {
		listView=(ListView)findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,0);
		listView.setAdapter(listAdapter);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		pairedDevices = new ArrayList<String>();
		filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		devices = new ArrayList<BluetoothDevice>();
		receiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				
				if(BluetoothDevice.ACTION_FOUND.equals(action)){
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					devices.add(device);
					String s = "";
					for(int a = 0; a < pairedDevices.size(); a++){
						if(device.getName().equals(pairedDevices.get(a))){
							//append 
							s = "(Paired)";
							break;
						}
					}
			
					listAdapter.add(device.getName()+" "+s+" "+"\n"+device.getAddress());
				}
				
				else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
					// run some code if needed later
				}
				else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
					// run some code if needed later				
				}
				
				else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
					if(btAdapter.getState() == btAdapter.STATE_OFF){
						turnOnBT();
					}
				}
			}
		};
		
		registerReceiver(receiver, filter);
		 filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		registerReceiver(receiver, filter);
		 filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(receiver, filter);
		 filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(receiver, filter);
		
		create = (Button) findViewById(R.id.buttonStartAcc);
		create.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(btAdapter.isEnabled())
				{
					startOpen();
					setContentView(R.layout.activity_bt_create);
				}
			}
		});
	}

	/**
	 * Opens a game (start a acceptThread) and makes the bluetooth-device visible
	 * */
	public void startOpen()
	{
		Intent discoverableIntent = new
				Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				startActivity(discoverableIntent);
		AcceptThread acc = new AcceptThread();
		acc.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}
	/**
	 * shows a warning (Toast) if the user does not want to activate bluetooth
	 * */
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if(resultCode == RESULT_CANCELED){
				Toast.makeText(getApplicationContext(), R.string.bt_must_act, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
		
		/**
		 * If the chosen device is paired and has opened a game the player joins this game
		 * If not the player gets a warning (Toast) that the devices must be paired
		 * */
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			if(btAdapter.isDiscovering()){
				btAdapter.cancelDiscovery();
			}
			if(listAdapter.getItem(arg2).contains("Paired")){
		
				BluetoothDevice selectedDevice = devices.get(arg2);
				ConnectThread connect = new ConnectThread(selectedDevice);
				connect.start();
				Log.i(tag, "in click listener");
			}
			else{
				Toast.makeText(getApplicationContext(), R.string.device_must_paired, 0).show();
			}
		}
		
		/**
		 * As long as there is no connection the Thread waits at mmSocket.connect()
		 * If a connection succeeds the ConnectThread starts the obtainMessage-method 
		 * of the handler and informs it about the connection
		 * */
		private class ConnectThread extends Thread {
		
			private final BluetoothSocket mmSocket;
		    private final BluetoothDevice mmDevice;
		 
		    public ConnectThread(BluetoothDevice device) {
		        // Use a temporary object that is later assigned to mmSocket,
		        // because mmSocket is final
		        BluetoothSocket tmp = null;
		        mmDevice = device;
		        Log.i(tag, "construct");
		        // Get a BluetoothSocket to connect with the given BluetoothDevice
		        try {
		            // MY_UUID is the app's UUID string, also used by the server code
		            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
		        } catch (IOException e) { 
		        	Log.i(tag, "get socket failed");
		        	
		        }
		        mmSocket = tmp;
		    }
		 
		    public void run() {
		        // Cancel discovery because it will slow down the connection
		        btAdapter.cancelDiscovery();
		        Log.i(tag, "connect - run");
		        try {
		            // Connect the device through the socket. This will block
		            // until it succeeds or throws an exception
		            mmSocket.connect();
		            Log.i(tag, "connect - succeeded");
		        } catch (IOException connectException) {	Log.i(tag, "connect failed");
		            // Unable to connect; close the socket and get out
		            try {
		                mmSocket.close();
		            } catch (IOException closeException) { }
		            return;
		        }
		 
		        // Do work to manage the connection (in a separate thread)
		   
		        mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
		    }
		 


			/** Will cancel an in-progress connection, and close the socket */
		    public void cancel() {
		        try {
		            mmSocket.close();
		        } catch (IOException e) { }
		    }
		}
		
		/**
		 * Thread which is interacting with the connected Socket
		 * read and write data to the streams with read(byte[]) and write(byte[])
		 * */
		private class ConnectedThread extends Thread {
		    private final BluetoothSocket mmSocket;
		    private final InputStream mmInStream;
		    private final OutputStream mmOutStream;
		 
		    public ConnectedThread(BluetoothSocket socket) {
		        mmSocket = socket;
		        InputStream tmpIn = null;
		        OutputStream tmpOut = null;
		 
		        // Get the input and output streams, using temp objects because
		        // member streams are final
		        try {
		            tmpIn = socket.getInputStream();
		            tmpOut = socket.getOutputStream();
		        } catch (IOException e) { }
		 
		        mmInStream = tmpIn;
		        mmOutStream = tmpOut;
		    }
		 
		    public void run() {
		        byte[] buffer;  // buffer store for the stream
		        int bytes; // bytes returned from read()
		        
		        
		        // Keep listening to the InputStream until an exception occurs
		        while (true) {
		            try {
			              // Read from the InputStream
			            buffer = new byte[1024];
			            bytes = mmInStream.read(buffer);
			            // Send the obtained bytes to the UI activity
			            mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
			                        .sendToTarget();							
		               
		            } catch (IOException e) {
		                break;
		            }
		        }
		    }
		 
		    /* Call this from the main activity to send data to the remote device */
		    public void write(byte[] bytes) {
		        try {
		            mmOutStream.write(bytes);
		        } catch (IOException e) { }
		    }
		 
		    /* Call this from the main activity to shutdown the connection */
		    public void cancel() {
		        try {
		            mmSocket.close();
		        } catch (IOException e) { }
		    }
		}
		
		/**
		 * Thread to accept a bluetooth-device
		 * The Thread is listening for connection request by calling accept
		 * The Thread ends if a device connects and start a connected Thread
		 * */
		private class AcceptThread extends Thread {
		    private final BluetoothServerSocket mmServerSocket;
		 
		    public AcceptThread() {
		        // Use a temporary object that is later assigned to mmServerSocket,
		        // because mmServerSocket is final
		        BluetoothServerSocket tmp = null;
		        try {
		            // MY_UUID is the app's UUID string, also used by the client code
		            tmp = btAdapter.listenUsingRfcommWithServiceRecord("Felilein", MY_UUID);
		        } catch (IOException e) { }
		        mmServerSocket = tmp;
		    }
		 
		    public void run() {
		        BluetoothSocket socket = null;
		        // Keep listening until exception occurs or a socket is returned
		        while (true) {
		            try {
		                socket = mmServerSocket.accept();
		            } catch (IOException e) {
		                break;
		            }
		            // If a connection was accepted
		            if (socket != null) {
		                // Do work to manage the connection (in a separate thread)
		                manageConnectedSocket(socket);
		                try {
							mmServerSocket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
		                break;
		            }
		        }
		    }
		 
		    private void manageConnectedSocket(BluetoothSocket socket) {
				connectedThread = new ConnectedThread(socket);
				connectedThread.start();
				this.cancel();
				isPlayer1=true;
				p2 = Player.BTPLAYER;
				startGame();
				runOnUiThread(new Runnable() {				
					@Override
					public void run() {
						setContentView(bv);
						Toast.makeText(getApplicationContext(), R.string.toast_connected, 0).show();
					}
				});
			}

			/** Will cancel the listening socket, and cause the thread to finish */
		    public void cancel() {
		        try {
		            mmServerSocket.close();
		        } catch (IOException e) { }
		    }
		}
}
