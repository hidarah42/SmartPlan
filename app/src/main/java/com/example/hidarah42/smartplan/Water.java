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

public class Water extends Fragment {

    static String MQTTHOST = "tcp://broker.hivemq.com:1883";
    static String topikNerima = "avianaPub";
    static String topikKirim = "avianaSub";
    private MqttAndroidClient client;
    private String clientId, pesan, stringWaterAt, stringWaterEvery;


    public Water(){}

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View Page = inflater.inflate(R.layout.fragment_water,container,false);

        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getContext(), MQTTHOST, clientId);

        final EditText waterAt = Page.findViewById(R.id.tv_wateratwater);
        final EditText waterEvery = Page.findViewById(R.id.tv_watereverywater);

        final ImageView imageViewWaterAt = Page.findViewById(R.id.iv_wateratyes);
        final ImageView imageViewWaterEvery = Page.findViewById(R.id.iv_watereveryyes);

        //MQTT
        MqttConnectOptions options = new MqttConnectOptions();

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("StatusWater", "Status Konek");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("StatusWater", "Status Tidak Konek");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d("StatusWater", "Status Terputus");
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

        imageViewWaterAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringWaterAt = waterAt.getText().toString();
                int lala = Integer.valueOf(stringWaterAt);
                Gson gson = new Gson();
//                String total = "{\"param
//                Log.d("waterAT",""+lala);

                Map<String, Integer> sensor = new HashMap<String, Integer>();
                sensor.put("param", 3);
                sensor.put("wateringH", lala);
                String jsonnya = gson.toJson(sensor);
                Log.d("Akhir data", jsonnya);
            }
        });

        return Page;
    }

    private void methonAtYes() {

    }
}
