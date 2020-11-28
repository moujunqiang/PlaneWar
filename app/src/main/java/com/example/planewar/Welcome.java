package com.example.planewar;

import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Welcome extends SurfaceView implements SurfaceHolder.Callback {

	private PanlewarActivity activity;
	private TutorialThread thread;// 刷帧的线程
	SoundPool soundPool;// 声音
	int status = 1;// 当前的状态值
	Bitmap background2;// 背景图片
	Bitmap startGame;// 开始游戏菜单
	Bitmap result;// 帮助菜单
	Bitmap openSound;// 打开声音菜单
	Bitmap closeSound;// 关闭声音菜单
	Bitmap exit;// 退出菜单
	HashMap<Integer, Integer> soundPoolMap;
	int width, height;

	Paint paint2;// 正常绘制

	public Welcome(PanlewarActivity activity, int width, int height) {// 构造器
		super(activity);
		this.activity = activity;// 得到activity的引用
    
		this.width = width;
		this.height = height;
		getHolder().addCallback(this);
		this.thread = new TutorialThread(getHolder(), this);
		// this.welcomeThread = new WelcomeViewThread(this);
//		initSounds();// 初始化声音
	//	playSound(1);
		initBitmap();// 初始化图片资源
	}

	private void playSound(int i) {
		// TODO Auto-generated method stub
		AudioManager mgr = (AudioManager) getContext().getSystemService(
				Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 设置最大音量
		float volume = streamVolumeCurrent / streamVolumeMax; // 设备的音量
		soundPool.play(soundPoolMap.get(i), volume, volume, 1, 0, 1f);// 播放
	}

	private void initBitmap() {
		// TODO Auto-generated method stub

		paint2 = new Paint();
		startGame = BitmapFactory.decodeResource(getResources(),
				R.drawable.startgame);// 初始化开始游戏
		background2 = BitmapFactory.decodeResource(getResources(),R.drawable.background);
		result = BitmapFactory
				.decodeResource(getResources(), R.drawable.result);// 帮助
		openSound = BitmapFactory.decodeResource(getResources(),
				R.drawable.opensound);// 打开声音
		closeSound = BitmapFactory.decodeResource(getResources(),
				R.drawable.closesound);// 关闭声音
		exit = BitmapFactory.decodeResource(getResources(), R.drawable.exit);// 退出游戏
	}

	private void initSounds() {
		// TODO Auto-generated method stub
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);// 初始化SoundPool
		soundPoolMap = new HashMap<Integer, Integer>();// 初始化 HashMap
		soundPoolMap.put(1, soundPool.load(getContext(), R.raw.welcome1, 1));
	}

	public void onDraw(Canvas canvas) {

		canvas.drawColor(Color.WHITE);// 背景色
		Rect rect1 = new Rect(0, 0, width, height);
		Rect rect2 = new Rect(0, 0, background2.getWidth(),
				background2.getHeight());
		canvas.drawBitmap(startGame, width/2-startGame.getWidth()/2, 12*(height-4*startGame.getHeight())/20, paint2);// 绘制开始游戏按钮

	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		this.thread.setFlag(true);// 设置循环标记位
		this.thread.start();// 启动绘制线程
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		boolean retry = true;// 循环标记
		thread.setFlag(false);
		while (retry) {
			try {
				thread.join();// 等待线程的结束
				retry = false;// 设置循环标记停止循环
			} catch (InterruptedException e) {
			}// 不断地循环，直到刷帧线程结束
		}
	}

	public boolean onTouchEvent(MotionEvent event) {// 屏幕监听
		if (event.getAction() == MotionEvent.ACTION_DOWN) {// 屏幕被按下
			/*
			 * if(this.status != 4){//当不是菜单状态时返回 return false; }
			 */
			double x = event.getX();// 得到X坐标
			double y = event.getY();// 得到Y坐标
			if (x > width/2-startGame.getWidth()/2 && 
					x < width/2-startGame.getWidth()/2 + openSound.getWidth() && y > 
			12*(height-4*startGame.getHeight())/20
					&& y<12*(height-4*startGame.getHeight())/20 +
					openSound.getHeight()) {// 点击了开始按钮
				activity.myhander.sendEmptyMessage(2);// 发送消息
			}
		}
		return super.onTouchEvent(event);// 调用基类的方法
	}

	class TutorialThread extends Thread {// 刷帧线程
		private int span = 100;// 睡眠的毫秒数
		private SurfaceHolder surfaceHolder;
		private Welcome welcomeView;// 欢迎界面的引用
		private boolean flag = false;

		public TutorialThread(SurfaceHolder surfaceHolder, Welcome welcomeView) {// 构造器
			this.surfaceHolder = surfaceHolder;// SurfaceHolder的引用
			this.welcomeView = welcomeView;// 欢迎界面的引用
		}

		public void setFlag(boolean flag) {// 设置标准位
			this.flag = flag;
		}

		public void run() {// 重写的run方法
			Canvas c;
			while (this.flag) {// 循环
				c = null;
				try {
					// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
					c = this.surfaceHolder.lockCanvas(null);
					synchronized (this.surfaceHolder) {// 同步
						welcomeView.onDraw(c);// 调用绘制方法
					}
				} finally {// 用finally保证一定被执行
					if (c != null) {
						// 更新屏幕显示内容
						this.surfaceHolder.unlockCanvasAndPost(c);
					}
				}
				try {
					Thread.sleep(span);// 睡眠指定毫秒数
				} catch (Exception e) {// 捕获异常
					e.printStackTrace();// 打印异常信息
				}
			}
		}
	}
}
