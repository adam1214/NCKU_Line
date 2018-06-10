package com.example.s2784.layout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class StudentData extends AppCompatActivity {

    private String studentID;
    private ImageView img_proPic;
    private TextView txv_studentID;
    private TextView txv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data);

        Intent intentFromLogin = getIntent();
        studentID = intentFromLogin.getStringExtra("studentID");

        img_proPic = findViewById(R.id.proPic);
        txv_studentID = findViewById(R.id.studentID);
        txv_name = findViewById(R.id.name);


        getDataAndDisplay();
    }

    private void getDataAndDisplay(){

    }

    public void onClick(View v){

        finish();
    }



    class StudentInfo{
        Bitmap bpProPic;
        String studentName;
        String studentID;

    }
}
