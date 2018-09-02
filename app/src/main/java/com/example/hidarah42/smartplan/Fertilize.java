package com.example.hidarah42.smartplan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

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
    private Integer harinya;

    public Fertilize(){}

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View page = inflater.inflate(R.layout.fragment_fertilize,container,false);

        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getContext(), MQTTHOST, clientId);

        final EditText fertilizeAt = page.findViewById(R.id.tv_fertilizeatwater);
        final ImageView imageViewFertilizeAt = page.findViewById(R.id.iv_fertilizeatyes);
        final ImageView imageViewFertilizeEvery = page.findViewById(R.id.iv_fertilizeeveryyes);
        final Spinner spinnerHari = page.findViewById(R.id.spinner_hari);
        final Switch switchFertilize = page.findViewById(R.id.switch_fertilize);

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

        final String[] hariArray = new String[]{
                "Sunday", "Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday"
        };

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.itemlist, R.id.textviewnya, hariArray);
        spinnerAdapter.setDropDownViewResource(R.layout.itemlist);
        spinnerHari.setAdapter(spinnerAdapter);

        spinnerHari.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    harinya = 0;
                } else if (position == 1) {
                    harinya = 1;
                } else if (position == 2) {
                    harinya = 2;
                } else if (position == 3) {
                    harinya = 3;
                } else if (position == 4) {
                    harinya = 4;
                } else if (position == 5) {
                    harinya = 5;
                } else if (position == 6) {
                    harinya = 6;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        imageViewFertilizeEvery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();

                Map<String, Integer> sensor = new HashMap<String, Integer>();
                sensor.put("param", 8);
                sensor.put("fertiEv", harinya);
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

        switchFertilize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchFertilize.isChecked()) {
                    Gson gson = new Gson();

                    Map<String, Integer> sensor = new HashMap<String, Integer>();
                    sensor.put("param", 5);
                    sensor.put("toggleferti", 1);
                    String jsonnya = gson.toJson(sensor);
                    Log.d("Akhir data", jsonnya);

                    try {
                        client.publish(topikKirim, jsonnya.getBytes(), 0, false);
                        Log.d("Status", "Berhasil " + jsonnya);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                } else if (!switchFertilize.isChecked()) {
                    Gson gson = new Gson();

                    Map<String, Integer> sensor = new HashMap<String, Integer>();
                    sensor.put("param", 5);
                    sensor.put("toogleferti", 0);
                    String jsonnya = gson.toJson(sensor);
                    Log.d("Akhir data", jsonnya);

                    try {
                        client.publish(topikKirim, jsonnya.getBytes(), 0, false);
                        Log.d("Status", "Berhasil " + jsonnya);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return page;
    }
}
