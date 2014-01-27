package com.example.littleinv_android_battleversion;

import gameLogic.GameContainer;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.littleinv_android_battleversion.gameLogic.Android_Battle_GameContainer;

/**
 *  Android向け実装の、ビュー
 * @author n-dolphin
 * @version 1.00 2014/01/17
 */
public class MainView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {
	
	private int width;
	private SurfaceHolder holder= null;
	private Thread thread= null;

	
	GameContainer gc =null; 
	Integer dpi;

	
	/**
	 * コンストラクタ
	 * @param context 
	 * @param DPI ドット密度
	 */
	public MainView(Context context, Integer DPI) {
		super(context);

		this.dpi = DPI;
		this.holder = getHolder();
		holder.addCallback(this);
		
	   setFocusable(true);
       requestFocus();
	}

	// SurfaceView生成時に呼び出される
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.holder = holder;
		
		gc =  new Android_Battle_GameContainer(this.getWidth(), this.getHeight());	
		thread = new Thread(this);
	}

	// SurfaceView変更時に呼び出される
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (thread != null) {
			 this.width = width;
			thread.start();
		}
	}


	// SurfaceView破棄時に呼び出される
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	// スレッドによるSurfaceView更新処理
    @Override
	public void run() {
    	Integer sleepMiliSec = 10;
    	
		while (thread != null) {
			
			draw();
			try {
				Thread.sleep(sleepMiliSec);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
  
    Integer lastX = null;
    /**
     * 加速度の更新 -自機の移動
     * @param gx 端末の傾き(Roll)
     */
    public void setAcce(int gx) {
	    
		Integer aveX=0;
		
		//端末を真横にしなくても自機が端までとどくよう、Rollの値を増加させる
		gx = (int)((float)gx *2.0);
		
		//センサーのノイズを除去する
		if(lastX ==null){
			lastX = aveX = gx;
		}else{
			aveX = (int)((float)lastX *0.8 + (float)gx *0.2);
			lastX = aveX;
		}
		
		//Rollの値に応じて画面を左右に移動する
		int x = (int)(width/2 + ((float)aveX/90)  *width/2);
		
		//はみ出さないように
		if(x>width){
			x=width;
		}else if (x<0){
			x=0;
		}
		
		if(gc==null){
			return ;
		}
		gc.move(x, 100);
	}
	
	/**
	 * 描画処理
	 */
	synchronized public void draw(){
		Canvas c = holder.lockCanvas();
		
		if (gc != null) {
			gc.update(c);
		}
		
		holder.unlockCanvasAndPost(c);
	}


	// クリック時のイベント　-自ミサイルの発射やボタンの押下など
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			gc.pointerDown((int)event.getX(), (int)event.getY());
			gc.pointerUp((int)event.getX(), (int)event.getY());
			gc.touch((int)event.getX(), (int)event.getY());
		}
		return true;
	}
}