package com.purdueplanner.purdueplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that takes care of listening/receiving connections on a mobile/Wifi network.
 * Includes an interface w/ methods for us to implement handling (in MainActivity or whatever) if a network is connected or not. {}NetworkStateReceiverListener}
 * Reference: http://stackoverflow.com/questions/6169059/android-event-for-internet-connectivity-state-change
 * Created by menane on 11/27/15.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    protected List<NetworkStateReceiverListener> listeners; //list of associated listeners
    protected Boolean connected; //let's us know if we are connected to a network or not

    public NetworkStateReceiver() { //constructer to init variables
        listeners = new ArrayList<NetworkStateReceiverListener>();
        connected = null;
    }

    public void onReceive(Context context, Intent intent) {
        if(intent == null || intent.getExtras() == null)  //if there's no operations to be performed then just return
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); //starts the connection
        NetworkInfo ni = manager.getActiveNetworkInfo(); //gets the info from the connection

        if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {  //self explanatory
            connected = true;
        } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) { //basically means if the network has been disabled somehow
            connected = false;
        }

        notifyStateToAll(); //notifies the state to all listeners
    }

    private void notifyStateToAll() { //Notifies the state for each listener
        for(NetworkStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    private void notifyState(NetworkStateReceiverListener listener) { //updates the state based on the listener
        if(connected == null || listener == null) //if we aren't connected or we don't have a listener then simply just return
            return;

        if(connected == true) //if we are connected then call the abstract method from the interface..what it does is up to us
            listener.networkAvailable();
        else
            listener.networkUnavailable();
    }

    public void addListener(NetworkStateReceiverListener l) { //adds a listener to a list
        listeners.add(l);
        notifyState(l);
    }

    public void removeListener(NetworkStateReceiverListener l) { //removes a listener to a list. Wondering if this is necessary since the listener will be deleted upon closing the app?
        listeners.remove(l);
    }


    //Interface for listening to connections
    public interface NetworkStateReceiverListener {
        public void networkAvailable();
        public void networkUnavailable();
    }

}