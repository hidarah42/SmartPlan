package com.example.hidarah42.smartplan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Water extends Fragment {

    static String MQTTHOST = "tcp://mqtt.dioty.co:1883";
    static String USERNAME = "lazul.azmi60@gmail.com";
    static String PASSWORD = "4ef3fc28";
    static String topik = "/lazul.azmi60@gmail.com/";
    static String topikAlat = "/lazul.azmi60@gmail.com/manual";
    private MqttAndroidClient client;
    private String clientId, pesan;

    public Water(){}

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View Page = inflater.inflate(R.layout.fragment_water,container,false);

        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getContext(), MQTTHOST, clientId);

        //MQTT
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("Status konek", "Status Konek");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("Status konek", "Status Tidak Konek");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d("Status konek", "Status Terputus");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {

                //Mengambil pesan yang dikirim dari alat dan di log
                pesan = new String(message.getPayload());
                Log.d("Status konek", "" + pesan);

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        return Page;
    }
}
