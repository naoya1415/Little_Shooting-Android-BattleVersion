package com.example.littleinv_android_battleversion;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;

/**
 *  *  Android向け実装の、エントリーポイント
 * @author n-dolphin
 * @version 1.00 2014/01/17
 *
 */
public class MainActivity extends Activity implements SensorEventListener {
	
	private SensorManager sensorManager;
	private MainView view;
	
	public final static String TAG = "SensorTest2";
	protected final static double RAD2DEG = 180/Math.PI;

	float[] rotationMatrix = new float[9];
	float[] gravity = new float[3];
	float[] geomagnetic = new float[3];
	float[] attitude = new float[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 画面を縦表示で固定
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);


		view = new MainView(this, metrics.densityDpi);
		setContentView(view);

		// センサーマネージャの取得
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	}

	// アプリの開始
	@Override
	protected void onResume() {
		// アプリの開始
		super.onResume();
		sensorManager.registerListener(
				this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
			sensorManager.registerListener(
				this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);
	}

	// アプリの停止
	@Override
	protected void onStop() {
		super.onStop();
		// センサーの処理の停止
//		sensorManager.unregisterListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	// 精度変更イベントの処理
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	// センサーリスナーの処理
	@Override
	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()){
		case Sensor.TYPE_MAGNETIC_FIELD:
			geomagnetic = event.values.clone();
			break;
		case Sensor.TYPE_ACCELEROMETER:
			gravity = event.values.clone();
			break;
		}

		if(geomagnetic != null && gravity != null){
			
			SensorManager.getRotationMatrix(
				rotationMatrix, null, 
				gravity, geomagnetic);
			
			SensorManager.getOrientation(
				rotationMatrix, 
				attitude);
			
			
			view.setAcce((int)(attitude[2] * RAD2DEG));
		}
	}

}
