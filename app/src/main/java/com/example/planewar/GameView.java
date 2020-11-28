package com.example.planewar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



import com.example.planewar.ProcessView.TutorialThread;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;

public class GameView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {
	int shun=0;
	boolean zhakai=false;
	private int jiguang=10;
	private long second=-1200;
	private long third=-600;
	private long fourth=0;
	 SurfaceHolder sh;
	private PanlewarActivity activity;
	private int width;
	private int height;
	private boolean flag = true;
	Bitmap background4, player;
	private Bitmap explosion;
	public float tm1,tm2;
	private Canvas canvas;
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	private ArrayList<Bullet> ebullets = new ArrayList<Bullet>();
	float x, y;
	int height1 = 0;
	Enamy enemy;
	int[] explodesID = new int[]{//爆炸的所有帧
			R.drawable.explode1,
			R.drawable.explode2,
			R.drawable.explode3,
			R.drawable.explode4,
			R.drawable.explode5,
			R.drawable.explode6,
			R.drawable.explode7,
			R.drawable.explode8,
			R.drawable.explode9,
			R.drawable.explode10,
			R.drawable.explode11,
			R.drawable.explode12,
		};
		Bitmap[] explodes = new Bitmap[explodesID.length];//爆炸的数组
		Bitmap jiguangpt;
	SoundPool soundPool;// 声音
	HashMap<Integer, Integer> soundPoolMap;

	MediaPlayer mMediaPlayer;

	ArrayList<Enamy> enemys = new ArrayList<Enamy>();

	int height2;
	int width1;
	int width2;
	int shoot, time;

	private Paint paint = new Paint();
	Paint paint2;
	private float xu;
	private float yu;
	private float xd;
	private float yd;
	private Bullet bullet;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private int bomb = 10;
	private int ilife = 3;
	private int score;
	Bitmap bulle;
	Bitmap bull;
	Bitmap ene;
	Bitmap xuetiao;
	boolean isSound;
	boolean jikai=false;

	public GameView(PanlewarActivity activity, int width, int height, int bomb,
			int ilife) {
		super(activity);
		sh = this.getHolder();
		sh.addCallback(this);
		initSounds();
		this.isSound = activity.isSound;
		mMediaPlayer = MediaPlayer.create(activity, R.raw.gamestart);
		mMediaPlayer.setLooping(true);
		ene = BitmapFactory.decodeResource(GameView.this.getResources(),
				R.drawable.enemy);
		bulle = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.bullet2);
		background4 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.background);
		bull = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.bullet);
		player = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.myplane);
		jiguangpt=BitmapFactory.decodeResource(this.getResources(),R.drawable.jiguang);
		xuetiao=BitmapFactory.decodeResource(this.getResources(),R.drawable.xuetiao);
	//	explosion = BitmapFactory.decodeResource(this.getResources(),
	//			R.drawable.explosion);
		for(int i=0; i<explodes.length; i++){//初始化爆炸图片
			explodes[i] = BitmapFactory.decodeResource(getResources(), explodesID[i]);
		}
		this.activity = activity;
		this.width = width;
		this.height = height;
		this.height2 = -height;
		this.width1=width/2-jiguangpt.getWidth()/2;
		this.width2=width/2-jiguangpt.getWidth()/2;
		this.bomb = bomb;
		this.ilife = ilife;
		this.x = width / 2 - player.getWidth() / 2;
		this.y = height / 2;
		this.setFocusable(true);
		this.score = 0;
		
		if (isSound) {
			mMediaPlayer.start();
		}
		// TODO Auto-generated constructor stub
	}

	public void onDraw() {
		if (flag) {
			canvas = sh.lockCanvas();
			Rect rect1 = new Rect(0, 0, background4.getWidth(),
					background4.getHeight());

			Rect rect2 = new Rect(0, height1, width, height +10 + height1);
			Rect rect3 = new Rect(0, height2, width, height  +10+ height2);

		
			canvas.drawBitmap(background4, rect1, rect2, null);
			canvas.drawBitmap(background4, rect1, rect3, null);

			height1 += height / 100;
			height2 += height / 100;
			if (height1 >= height) {
				height1 = -height;
			}
			if (height2 >= height) {
				height2 = -height;
			}

			// 绘制子弹
			// 绘制敌人战机 添加子弹
			for (int j = 0; j < enemys.size(); j++) {
				Enamy ene = enemys.get(j);

				if (shoot % 40 == 0) {

					Bullet bull = new Bullet(ene.x + 9, ene.y + 31, bulle, time,this);
					ebullets.add(bull);
				}
				if (ene.y < height) {
					ene.draw(canvas);
				} else {
					enemys.remove(ene);
				}
			}
//绘制爆炸

			 for(int m = 0;m<explosions.size();m++){ 
				 Explosion explo =explosions.get(m); 
				 for(;explo.m<6;explo.m++){ 
					 explo.draw(canvas);
					
                       }
			 explosions.remove(explo); }
			//炸弹效果
			 
			// 敌人子弹与玩家碰撞 绘制敌人子弹
			for (int i = 0; i < ebullets.size(); i++) {
				Bullet bull = ebullets.get(i);
				float ebullx = bull.x;
				float ebully = bull.y;
				if (bull.y < height) {
					bull.draw2(canvas);
				} else {
					ebullets.remove(bull);
				}
				if (ebullx > x+10 & ebullx< x + ene.getWidth()+10
						& ebully > y  & ebully< y + ene.getHeight()
				//		|(ebullx>x+10*ene.getWidth()/46&ebullx<x+36*ene.getWidth()/46&
					//	ebully>y+20*ene.getHeight()/47&ebully<y+34*ene.getHeight()/47)
						) {
					ebullets.remove(bull);

					ilife--;
					Explosion explo = new Explosion(x+9,y+9,explodes);
					 explosions.add(explo);
					 if(isSound){
					 playSound(2,0);
					 }
					if (ilife <= 0) {
						 activity.score=score;
						activity.myhander.sendEmptyMessage(5);
                       
						if (this.mMediaPlayer.isPlaying()) {
							this.mMediaPlayer.stop();// 将游戏背景音乐停止
							 playSound(3,0);
						}
					}
				}
			}
			// 玩家子弹
			for (int i = 0; i < bullets.size(); i++) {
				Bullet bul = bullets.get(i);
				float bullx = bul.x + bull.getWidth() / 2;
				float bully = bul.y;
				for (int k = 0; k < enemys.size(); k++) {

					Enamy eneb = enemys.get(k);
					float enemyx = eneb.x;
					float enemyy = eneb.y;
					if (bullx < (enemyx + 100) & (bullx + 11) > enemyx
							& bully < (enemyy + 100) & (bully + 11 > enemyy)) {
						score += 100;
						enemys.remove(eneb);
						bullets.remove(bul);
						 Explosion explo = new Explosion(enemyx,enemyy,explodes);
						 explosions.add(explo);
						 
						if (isSound) {
							playSound(3, 0);
						}
					}
				}

				if (bul.y > 0) {
					bul.draw1(canvas);
				} else {
					bullets.remove(bul);

				}
			}

			if (ilife <= 0) {
				 for(int m = 0;m<explosions.size();m++){ 
					 Explosion explo =explosions.get(m); 
					 for(;explo.m<6;explo.m++){ 
						 explo.draw(canvas);
						 try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
	                       }
				 explosions.remove(explo); }

			} else {
				canvas.drawBitmap(player, x, y, null);
			}
			//添加炸弹
			if(zhakai){
				  for (int k = 0; k < enemys.size(); k++) {

						Enamy eneb = enemys.get(k);
						float enemyx = eneb.x;
						float enemyy = eneb.y;
						
							score += 100;
							enemys.remove(eneb);
							
							 Explosion explo = new Explosion(enemyx,enemyy,explodes);
							 explosions.add(explo);
					}
				  for (int i = 0; i < ebullets.size(); i++) {
						Bullet bull = ebullets.get(i);
					
						
							ebullets.remove(bull);
						}
				  zhakai=false;
			}
			//绘制激光条
			if(jikai){
				Rect rect4 = new Rect(0, 0, jiguangpt.getWidth(),
						jiguangpt.getHeight());

				Rect rect5 = new Rect(width1, 0, width1+jiguangpt.getWidth(), height);
				Rect rect6 = new Rect(width2, 0, width2+jiguangpt.getWidth(), height);

			
				canvas.drawBitmap(jiguangpt, rect4, rect5, null);
				canvas.drawBitmap(jiguangpt, rect4, rect6, null);
				 for (int k = 0; k < enemys.size(); k++) {
                      Enamy eneb = enemys.get(k);
						float enemyx = eneb.x;
						float enemyy = eneb.y;
						if(width1<enemyx+50&width1>enemyx|width2>enemyx&width2<enemyx+50){
							if (isSound) {
								playSound(3, 0);
							}
					    score += 100;
							enemys.remove(eneb);
							 Explosion explo = new Explosion(enemyx,enemyy,explodes);
							 explosions.add(explo);
						}}
					for (int i = 0; i < ebullets.size(); i++) {
					Bullet bull = ebullets.get(i);
						if(width1<bull.x+50&width1>bull.x|width2>bull.x&width2<bull.x+50){
						ebullets.remove(bull);
						}}
				if(shun==0){
				width1-=10;
				width2+=10;}
				else{
					width1+=10;
					width2-=10;
				}
				if(width1<=0){
					shun=1;
				}
				if(width1>width2&shun==1){
					jikai=false;
					shun=0;
					width1=width/2-jiguangpt.getWidth()/2;
					width2=width1;
				}
				
				
			}
			// 绘制血条、炸弹、分数等状态

			paint.setColor(Color.RED);
            paint.setTextSize(20);
			canvas.drawText("炸弹:" + bomb, 0, 50, paint);
			canvas.drawText("分数:" + score,width*3/5, 50, paint);
			canvas.drawText("血量:", width/3, 50, paint);
			canvas.drawText("激光弹"+jiguang, width/3, 100, paint);
			canvas.drawBitmap(xuetiao,width/3+50,40, paint);
			paint.setColor(Color.BLUE);
			canvas.drawRect(width/3+50,40,width/3+50+ilife*xuetiao.getWidth()/3,
					40+xuetiao.getHeight(),paint);
			//canvas.drawText("激光弹"+jiguang, width/3, 100, paint);
			sh.unlockCanvasAndPost(canvas);

		}

	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	public void initSounds() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap
				.put(1, soundPool.load(getContext(), R.raw.bulletsound1, 1));
		soundPoolMap.put(2, soundPool.load(getContext(), R.raw.explode, 1));

		soundPoolMap.put(3, soundPool.load(getContext(), R.raw.dead, 1));

	}

	public void playSound(int sound, int loop) {
		AudioManager mgr = (AudioManager) getContext().getSystemService(
				Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;

		soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

		setFlag(true);// 设置线程标志位

		new Thread(this).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		try {
			soundPool=null;
			mMediaPlayer=null;
			setFlag(false);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		flag = false;
	}
 
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xu = event.getX();
			yu = event.getY();
			xd = xu - x;
			yd = yu - y;
		
			  second=third;
			  third=fourth;
			  fourth=System.currentTimeMillis();
			  if(fourth-third<500){//炸弹效果
				  
				
				 if(fourth-third<500&third-second<500){
					  //激光的效果//，但是还没有加，以后学的很深入了再加！已经加上
					 
	                jiguang();
				  }else{
					  baozha();
				  }
			  }
			  
			break;
		case MotionEvent.ACTION_MOVE:
			xu = event.getX();
			yu = event.getY();
			x = xu - xd;
			y = yu - yd;
			if (x <= 0) {
				x = 0;
			}
			if (y <= 0) {
				y = 0;
			}
			if (x >= width - player.getWidth()) {
				x = width - player.getWidth();
			}
			if (y >= height - player.getHeight()) {
				y = height - player.getHeight();
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}
	 public boolean onKeyDown(int keyCode,KeyEvent event){
	    	if(keyCode==KeyEvent.KEYCODE_BACK){
	    		this.setFlag(false);
	    	if(mMediaPlayer!=null){
	    		mMediaPlayer=null;
	    	}
	    		activity.myhander.sendEmptyMessage(5);
	    		return true;
	    	}
	    	return super.onKeyDown(keyCode, event);
	    }
	public void setFlag(boolean flag) {// 设置标志位
		this.flag = flag;
	}
	public void jiguang(){
		if(jiguang>0){
			bomb+=1;
			jiguang-=1;
			jikai=true;
		}
	}
public void baozha(){
	 if(bomb>0){
		  bomb-=1;
	  if (isSound) {
			playSound(3, 0);
		}
	
	  zhakai=true;
	  }
}
	public void run() {
		// TODO Auto-generated method stub
		// 不停在不同位置生成敌人
		new Thread(new Runnable() {
			public void run() {
				Random r = new Random();
				  float tm1=0,tm2=0;
				  tm1=System.currentTimeMillis();
				while (flag) {
					int i = r.nextInt(9);
              int shui=1000;
            
					if (i == 0) {
						enemy = new Enamy(player.getWidth()/2-ene.getWidth()/2, 0, ene,GameView.this);
						enemys.add(enemy);
					} else if (i == 1) {
						enemy = new Enamy(width/8, 0, ene,GameView.this);
						enemys.add(enemy);
					} else if (i == 2) {
						enemy = new Enamy(2*width/8, 0, ene,GameView.this);
						enemys.add(enemy);
					} else if (i == 3) {
						enemy = new Enamy(3*width/8, 0, ene,GameView.this);
						enemys.add(enemy);
					} else if (i == 4) {
						enemy = new Enamy(4*width/8, 0, ene,GameView.this);
						enemys.add(enemy);
					} else if (i == 5) {
						enemy = new Enamy(5*width/8, 0, ene,GameView.this);
						enemys.add(enemy);
					} else if (i == 6) {
						enemy = new Enamy(6*width/8- ene.getWidth()/2-player.getWidth()/2, 0, ene,GameView.this);
						enemys.add(enemy);
					} else if (i == 7) {
						enemy = new Enamy(7*width/8- ene.getWidth()/2-player.getWidth()/2, 0, ene,GameView.this);
						enemys.add(enemy);
					} else {
						enemy = new Enamy(width - ene.getWidth()/2-player.getWidth()/2, 0, ene,GameView.this);
						enemys.add(enemy);
					}
					   
                       tm2=System.currentTimeMillis();
                       if(tm2-tm1>5000){   shui=500;}
                       if(tm2-tm1>30000){  shui=400;}
                       if(tm2-tm1>60000){  shui=250;}
      
					try {
						Thread.sleep(shui);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		tm1=System.currentTimeMillis();
		// 循环调用onDraw方法
		while (this.flag) {// 循环
			if (shoot % 5 == 0) {
				bullet = new Bullet(x + player.getWidth() / 2-bull.getWidth()/2, y, bull, 0,this);
				bullets.add(bullet);
			} else if (shoot == 100) {
				shoot = 0;
			}
			shoot += 1;
			tm2=System.currentTimeMillis();
			onDraw();// 调用绘制方法
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
