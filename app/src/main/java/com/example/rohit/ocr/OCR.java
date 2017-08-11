package com.example.rohit.ocr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class OCR extends AppCompatActivity implements View.OnClickListener{
    SurfaceView cameraView;
    TextView textView;
    String name,number;
    Button continuetonext;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    TextView savename,savenumber,rst;
    @Override
    public void  onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case RequestCameraPermissionID:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        savename=(TextView) findViewById(R.id.saveName);
        savename.setOnClickListener(this);

        continuetonext=(Button)findViewById(R.id.continueToNext);
        continuetonext.setVisibility(View.INVISIBLE);

        continuetonext.setOnClickListener(this);

        savenumber=(TextView)findViewById(R.id.saveNumber);
        savenumber.setVisibility(View.INVISIBLE);
        savenumber.setOnClickListener(this);

        rst=(TextView) findViewById(R.id.reset);
        rst.setOnClickListener(this);

        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        textView = (TextView) findViewById(R.id.text_view);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        /*if (!textRecognizer.isOperational()) {
            Log.w("OCR", "Detector dependencies are not yet available");
        } else {*/
        cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(OCR.this,
                                    new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                     cameraSource.stop();
                }

            });
        //yaha ho skti h peoblem
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items= detections.getDetectedItems();
                    if(items.size()!=0)
                    {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder  stringBuilder=new StringBuilder();
                                for(int i=0;i<items.size();++i)
                                {
                                    TextBlock item=items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                textView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });

        }

    @Override
    public void onClick(View view) {
        if(view==savename)
        {
         name=textView.getText().toString();
            textView.setText("Text");
            Toast.makeText(getApplicationContext(),"Name saved is::"+name,Toast.LENGTH_SHORT).show();
            savenumber.setVisibility(View.VISIBLE);
            savename.setVisibility(View.INVISIBLE);
        }
        if(view==savenumber)
        {
         number=textView.getText().toString();
            textView.setText("All values saved");
            Toast.makeText(getApplicationContext(),"Number saved is::"+number,Toast.LENGTH_SHORT).show();
            savenumber.setVisibility(View.INVISIBLE);
            continuetonext.setVisibility(View.VISIBLE);
        }
        if(view==rst)
        {
            name=null;
            number=null;
            savename.setVisibility(View.VISIBLE);
            savenumber.setVisibility(View.INVISIBLE);
           continuetonext.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"all values are cleared",Toast.LENGTH_SHORT).show();
        }
        if(view==continuetonext)
        {
            values.setName(name);
            values.setNumber(number);
            startActivity(new Intent(getApplicationContext(),AddToPhonebook.class));
        }
    }
    //}

}
