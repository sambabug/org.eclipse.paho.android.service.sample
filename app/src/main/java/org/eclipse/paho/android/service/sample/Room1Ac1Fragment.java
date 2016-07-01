package org.eclipse.paho.android.service.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.util.Map;


public class Room1Ac1Fragment extends Fragment {
    //publish
    public final static String PUB_AC1_ROOM1_ACTU1_TOPIC = "up/room1/actu1";
    public final static String PUB_AC1_ROOM1_ACTU1_MSG_ON = "1";
    public final static String PUB_AC1_ROOM1_ACTU1_MSG_OFF = "0";
    public final static int PUB_AC1_ROOM1_ACTU1_QOS = ActivityConstants.defaultQos;
    public final static boolean PUB_AC1_ROOM1_ACTU1_RET = false;

    //subscribe
    public final static String SUB_AC1_ROOM1_TEMP_TOPIC = "down/room1/temp1";
    public final static int SUB_AC1_ROOM1_TEMP_QOS = ActivityConstants.defaultQos;

    public final static String SUB_AC1_ROOM1_STATUS_TOPIC = "down/room1/status1";
    public final static int SUB_AC1_ROOM1_STATUS_QOS = ActivityConstants.defaultQos;


    private Button mButtonClientConnections;
    private Button mButtonAC1_Room1_On;
    private Button mButtonAC1_Room1_Off;
    private Button mButtonAC1_Room1_Sub;

    private Context mContext;
    private String mClientConnection;

    private View.OnClickListener mButtonAC1_Room1_On_OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //ON
            publish(mClientConnection, PUB_AC1_ROOM1_ACTU1_TOPIC, PUB_AC1_ROOM1_ACTU1_MSG_ON, PUB_AC1_ROOM1_ACTU1_QOS, PUB_AC1_ROOM1_ACTU1_RET);
        }

    };
    private View.OnClickListener mButtonAC1_Room1_Off_OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //OFF
            publish(mClientConnection, PUB_AC1_ROOM1_ACTU1_TOPIC, PUB_AC1_ROOM1_ACTU1_MSG_OFF, PUB_AC1_ROOM1_ACTU1_QOS, PUB_AC1_ROOM1_ACTU1_RET);
        }

    };

    private View.OnClickListener mButtonAC1_Room1_Sub_OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //subscribe to topics
            subscribe(mClientConnection, SUB_AC1_ROOM1_TEMP_TOPIC, SUB_AC1_ROOM1_TEMP_QOS);
            subscribe(mClientConnection, SUB_AC1_ROOM1_STATUS_TOPIC, SUB_AC1_ROOM1_STATUS_QOS);
        }

    };

    private void publish(String clientConnection, String topic, String message, int qos, boolean retained) {
        String[] args = new String[2];
        args[0] = message;
        args[1] = topic + ";qos:" + qos + ";retained:" + retained;

        try {
            Connections.getInstance(mContext).getConnection(clientConnection).getClient()
                    .publish(topic, message.getBytes(), qos, retained, null, new ActionListener(mContext, ActionListener.Action.PUBLISH, clientConnection, args));
        } catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to publish a messged from the client with the handle " + clientConnection, e);
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to publish a messged from the client with the handle " + clientConnection, e);
        }
    }

    private void subscribe(String clientConnection, String topic, int qos) {
        try {
            String[] topics = new String[1];
            topics[0] = topic;
            Connections.getInstance(mContext).getConnection(clientConnection).getClient()
                    .subscribe(topic, qos, null, new ActionListener(mContext, ActionListener.Action.SUBSCRIBE, clientConnection, topics));
        } catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle " + clientConnection, e);
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle " + clientConnection, e);
        }

    }

    private String getFirstConnection() {
        // get all the available connections
        Map<String, Connection> connections = Connections.getInstance(mContext)
                .getConnections();

        String firstConnection = null;
        if (connections != null) {
            for (String s : connections.keySet()) {
                //arrayAdapter.add(connections.get(s));
                firstConnection = s;
                break;
            }
        }
        return firstConnection;
    }


    /**
     * @see android.support.v4.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();
        mClientConnection = getFirstConnection();

        //mButtonClientConnections = (Button) findViewById(R.id.buttonClientConnections);
        //mButtonClientConnections.setOnClickListener(mButtonClientConnectionsOnClickListener);

        mButtonAC1_Room1_On = (Button) container.getRootView().findViewById(R.id.buttonAC1_Room1_On);
        mButtonAC1_Room1_On.setOnClickListener(mButtonAC1_Room1_On_OnClickListener);

        mButtonAC1_Room1_Off = (Button) container.getRootView().findViewById(R.id.buttonAC1_Room1_Off);
        mButtonAC1_Room1_Off.setOnClickListener(mButtonAC1_Room1_Off_OnClickListener);

        mButtonAC1_Room1_Sub = (Button) container.getRootView().findViewById(R.id.buttonAC_1_Room_1_Sub);
        mButtonAC1_Room1_Sub.setOnClickListener(mButtonAC1_Room1_Sub_OnClickListener);
        return LayoutInflater.from(getActivity()).inflate(R.layout.content_main, null);

    }
}
