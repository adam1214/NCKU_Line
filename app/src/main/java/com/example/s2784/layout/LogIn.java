package com.example.s2784.layout;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Browser;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogIn extends AppCompatActivity {
    private final static int CAMERA_RESULT = 0;
    private final static int SIP_RESULT = 1;
    private Mqtt_Client mqtt;

    private EditText login_et;
    private EditText pw_et;
    private Button scan;
    //SQLite
    //public static boolean LoginOrNot = false;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        SQLiteManager.setContext(this);
        SQLiteManager.DBinit();
        SQLiteManager.createTableForBadge();
        SQLiteManager.createTableForLogin();
        //Change status color

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getColor(R.color.ncku_red_thick));
        }
        else{
            getWindow().setStatusBarColor(getColor(R.color.ncku_red_thick));
        }
        //Change status color
        login_et = findViewById(R.id.login_et);
        pw_et = findViewById(R.id.password);
        scan = findViewById(R.id.btn_login_scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    callCamera();
            }
        });
        Button btn_logIn = findViewById(R.id.btn_logIn);
                btn_logIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mqtt.LoginWithPassword(login_et.getText().toString().trim().toUpperCase(), pw_et.getText().toString().trim());
                        login_et.setText("");
                        pw_et.setText("");
                        /*
                        Intent mainintent = new Intent(LogIn.this,Main.class);
                        startActivity(mainintent);
                        finish();
                        */
                    }
                });

        //StartInterface.LoginOrNot = true;

//        StartInterface.db = openOrCreateDatabase(StartInterface.db_name, Context.MODE_PRIVATE, null);
//        String createTable = "CREATE TABLE IF NOT EXISTS " + StartInterface.tb_name + "name VARCHAR(32), " + "id VARCHAR(9))";

//        Cursor c = StartInterface.db.rawQuery("SELECT * FROM "+StartInterface.tb_name,null);
//        if(c.getCount()==0)
//        {
//            StartInterface.addData("LogIn","22222222");
//        }
//        StartInterface.db.close();

        mqtt = new Mqtt_Client(this.getApplicationContext(), Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        mqtt.Connect();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.USE_SIP) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.USE_SIP ,Manifest.permission.RECORD_AUDIO},1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqtt.disconnect();
    }

    public void callCamera(){
        String[] permissionNeed = {
                Manifest.permission.CAMERA,
        };
        if( hasPermission(permissionNeed)){
            Scanner();
        }else {
            getPermission();
        }
    }

    @TargetApi(23)
    public void getPermission(){
        if(Build.VERSION.SDK_INT>=23) {
            String[] permissionNeed = {
                    Manifest.permission.CAMERA,
            };
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(this, "需要相機權限掃描條碼", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(permissionNeed, CAMERA_RESULT);
        }
    }

    public void Scanner(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("請對準條碼");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }
    private boolean hasPermission(String[] permission) {
        if (canMakeSmores()) {
            for (String permissions : permission) {
                return (ContextCompat.checkSelfPermission(this, permissions) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int [] grantResults ){
        switch (requestCode){
            case CAMERA_RESULT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Scanner();
                } else {
                    Toast.makeText(this,"需要相機權限掃描條碼",Toast.LENGTH_SHORT).show();
                }
                break;
            case SIP_RESULT:{
//                Toast.makeText(this,"SIPSIPSIP",Toast.LENGTH_SHORT).show();
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }}
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                this.finish();
            } else {
                Log.d("MainActivity", "Scanned");

                // send to server check id exists or not
                Toast.makeText(this, "學號:" + result.getContents(), Toast.LENGTH_SHORT).show();
                mqtt.Login(result.getContents());

                // Go student Data page
//                Intent studentDataIntent = new Intent(LogIn.this,StudentData.class);
//                studentDataIntent.putExtra("studentID",result.getContents());
//                startActivity(studentDataIntent);

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }   

    }

    public class Mqtt_Client{
        private static final String MQTT_HOST = "tcp://140.116.82.52:1883";
        private MqttAndroidClient client;
        private MqttConnectOptions options;

        private Context context;
        private String user;

        public Mqtt_Client(Context context, String user) {
            this.context = context;
            this.user = user;
        }

        public void Connect(){
            String clientId = MqttClient.generateClientId();
            client = new MqttAndroidClient(context, MQTT_HOST, clientId);

            options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);

            try {
                IMqttToken token = client.connect(options);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        mqttSub();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        // Something went wrong e.g. connection timeout or firewall problems
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    if(reconnect){
                        mqttSub();
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String[] idf = topic.split("/");
                    String msg[] = new String(message.getPayload()).split(",");
                    switch (idf[1]){
                        case "Login":
                            if(msg[0].equals("True")){
                                // Go Main page
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.success_toast,
                                        (ViewGroup) findViewById(R.id.toast_layout_root));

                                ImageView image = (ImageView) layout.findViewById(R.id.image);
                                image.setImageResource(R.drawable.success);
                                TextView text = (TextView) layout.findViewById(R.id.text);
                                text.setText("登入成功");

                                Toast toast = new Toast(getApplicationContext());
                                toast.setView(layout);
                                toast.show();
                                SQLiteManager.addUser(msg[1]);
                                Intent mainIntent = new Intent(LogIn.this,Tabs.class);
                                mainIntent.putExtra("userID",msg[1]);
                                startActivity(mainIntent);
                                finish();
                            }else if(msg[0].equals("False")){
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.warning_toast,
                                        (ViewGroup) findViewById(R.id.toast_layout_root));

                                ImageView image = (ImageView) layout.findViewById(R.id.image);
                                image.setImageResource(R.drawable.warning);
                                TextView text = (TextView) layout.findViewById(R.id.text);
                                text.setText("登入失敗!");

                                Toast toast = new Toast(getApplicationContext());
                                toast.setView(layout);
                                toast.show();
                            }
                            break;
                        case "LoginWithPassword" :
                            if(msg[0].equals("True")){
                                // Go Main page
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.success_toast,
                                        (ViewGroup) findViewById(R.id.toast_layout_root));

                                ImageView image = (ImageView) layout.findViewById(R.id.image);
                                image.setImageResource(R.drawable.success);
                                TextView text = (TextView) layout.findViewById(R.id.text);
                                text.setText("登入成功");

                                Toast toast = new Toast(getApplicationContext());
                                toast.setView(layout);
                                toast.show();
                                SQLiteManager.addUser(msg[1]);
                                Intent mainIntent = new Intent(LogIn.this,Tabs.class);
                                mainIntent.putExtra("userID",msg[1]);
                                startActivity(mainIntent);
                                finish();
                            }else if(msg[0].equals("False")){
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.warning_toast,
                                        (ViewGroup) findViewById(R.id.toast_layout_root));

                                ImageView image = (ImageView) layout.findViewById(R.id.image);
                                image.setImageResource(R.drawable.warning);
                                TextView text = (TextView) layout.findViewById(R.id.text);
                                text.setText("登入失敗!");

                                Toast toast = new Toast(getApplicationContext());
                                toast.setView(layout);
                                toast.show();
                            }
                            break;
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }
        public void disconnect() {
            if(client != null && client.isConnected()) {
                try {
                    client.unsubscribe("IDF/Login/" + user + "/Re");
                    client.disconnect();
                    client.unregisterResources();
                    client = null;
                    Log.d("TAG","Try Disconnect");
                } catch (MqttException e) {
                    Log.d("TAG","Disconnect Error");
                    e.printStackTrace();
                }
            }
        }

        private void mqttSub() {
            try {
                String topic = "IDF/Login/" + user + "/Re";
                client.subscribe(topic,2);
                topic = "IDF/LoginWithPassword/" + user + "/Re";
                client.subscribe(topic,2);
            }catch (MqttException e) {
                e.printStackTrace();
            }
        }

        public void Login(String id){
            String topic = "IDF/Login/" + user;
            String MSG = id;
            try {
                client.publish(topic,MSG.getBytes(),0,false);
            }catch (MqttException e) {
                e.printStackTrace();
            }
        }

        public void LoginWithPassword(String userID, String password){
            String topic = "IDF/LoginWithPassword/" + user;
            String MSG = userID + "\t" + password;
            try {
                client.publish(topic,MSG.getBytes(),0,false);
            }catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

}
