package com.example.sensor;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

    ImageView image;
    TextView text;
    float currentDegree = 0f;
    private SensorManager sensorManager;
    private Sensor sensor1;
    private Sensor sensor2;
    private Boolean line;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView)findViewById(R.id.image);
        text = (TextView)findViewById(R.id.text1);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this,sensor1,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,sensor2,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ORIENTATION:
                float degree = sensorEvent.values[0];   //x转过的角度
                RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setDuration(100);
                image.startAnimation(ra);
                if(Math.abs(currentDegree+degree)>0.1){
                    line=false;
                }else{
                    line=true;
                }
                currentDegree = -degree;
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                float acc = sensorEvent.values[0];   //x转过的加速度
                if (Math.abs(acc) > 0.5) {
                    if(line==true){
                        text.setText("直线运动");

                    }else{
                        text.setText("非直线运动");
                    }
                } else
                    text.setText("静止");
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}