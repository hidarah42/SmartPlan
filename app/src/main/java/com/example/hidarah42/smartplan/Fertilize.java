package com.example.hidarah42.smartplan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

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

import java.util.HashMap;
import java.util.Map;

public class Fertilize extends Fragment {

    static String MQTTHOST = "tcp://broker.hivemq.com:1883";
    static String topikNerima = "avianaPub";
    static String topikKirim = "avianaSub";
    private MqttAndroidClient client;
    private String clientId, pesan, stringFertilizeAt, stringWaterEvery;

    public Fertilize(){}

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View page = inflater.inflate(R.layout.fragment_fertilize,container,false);

        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getContext(), MQTTHOST, clientId);

        final EditText fertilizeAt = page.findViewById(R.id.tv_fertilizeatwater);
        final ImageView imageViewFertilizeAt = page.findViewById(R.id.iv_fertilizeatyes);

        //MQTT
        MqttConnectOptions options = new MqttConnectOptions();

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("StatusFertilize", "Status Konek");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("StatusFertilize", "Status Tidak Konek");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d("StatusFertilize", "Status Terputus");
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

        imageViewFertilizeAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringFertilizeAt = fertilizeAt.getText().toString();
                int fertilizeInt = Integer.valueOf(stringFertilizeAt);
                Gson gson = new Gson();

                Map<String, Integer> sensor = new HashMap<String, Integer>();
                sensor.put("param", 7);
                sensor.put("fertiHour", fertilizeInt);
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
