package com.example.hidarah42.smartplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends Fragment {

    static String MQTTHOST = "tcp://broker.hivemq.com:1883";
    static String topikNerima = "avianaPub";
    static String topikKirim = "avianaSub";
    private MqttAndroidClient client;
    private String clientId, pesan;

    public Home(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View page = inflater.inflate(R.layout.fragment_home,container,false);

        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getContext(), MQTTHOST, clientId);

        final TextView timeHome = page.findViewById(R.id.tv_timehome);
        final TextView humadityHome = page.findViewById(R.id.tv_humidityhome);

        final ImageView waterNow = page.findViewById(R.id.iv_waternow);
        final ImageView fertilizeNow = page.findViewById(R.id.iv_fertilizenow);

        //MQTT
        MqttConnectOptions options = new MqttConnectOptions();

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    int qos = 1;

                    try {
                        IMqttToken subToken = client.subscribe(topikNerima, qos);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.d("StatusHome", "Konek berhasil");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.d("StatusHome", "Gagal");
                            }
                        });

                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("StatusHome", "GAGAL");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d("StatusHome", "Status Terputus");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                Log.d("isi pesan", "" + message.toString());

                JSONObject jsonObject = new JSONObject(message.toString());
                try {
                    String jam = jsonObject.getString("jam");
                    String sensor = String.valueOf(jsonObject.getInt("sensor"));
                    Log.d("Pesan akhir", "" + jam + "," + sensor);

                    timeHome.setText(jam);
                    humadityHome.setText(sensor + "%");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        waterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();

                Map<String, Integer> sensor = new HashMap<String, Integer>();
                sensor.put("param", 2);
                sensor.put("water_now", 1);
                String jsonnya = gson.toJson(sensor);
                Log.d("Akhir data", jsonnya);

                try {
                    client.publish(topikKirim, jsonnya.getBytes(), 0, false);
                    Log.d("Status", "Berhasil " + jsonnya);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        fertilizeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();


                Map<String, Integer> sensor = new HashMap<String, Integer>();
                sensor.put("param", 6);
                sensor.put("fertiNowBut", 1);
                String jsonnya = gson.toJson(sensor);
                Log.d("Akhir data", jsonnya);

                try {
                    client.publish(topikKirim, jsonnya.getBytes(), 0, false);
                    Log.d("Status", "Berhasil " + jsonnya);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        return page;
    }
}
