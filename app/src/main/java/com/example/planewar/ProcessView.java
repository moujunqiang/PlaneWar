package com.example.planewar;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class ProcessView extends SurfaceView 
implements SurfaceHolder.Callback{
	Bitmap processbackground;
	
	private TutorialThread thread;//刷帧的线程
	Bitmap process;
	int proces=0;
	int startx,starty;
	private Paint paint;
	private boolean flag=true;
	private PanlewarActivity activity;
	int scren_width=400;
	int scren_heigt=800;
	private int type;
public ProcessView(PanlewarActivity activity, int type,int screenwidth,int screenheight) {
	   super(activity);
	   this.activity = activity;//得到activity的引用
       getHolder().addCallback(this);
       this.thread = new TutorialThread(getHolder(), this);//初始化重绘线程
       this.type = type;
       paint = new Paint();//创建画笔
   /* */
       this.scren_width=screenwidth;
       this.scren_heigt=screenheight;
       
		paint.setTextSize(12);//设置字体大小
		process = BitmapFactory.decodeResource(getResources(), R.drawable.process);
		processbackground = BitmapFactory.decodeResource(getResources(), R.drawable.background_prg);
		this.startx=scren_width/2-process.getWidth()/2;
		this.starty=scren_heigt/2-process.getHeight()/2-process.getHeight()/4;
		
	}
public void onDraw(Canvas canvas){
	
	Rect rect1 = new Rect(0,0,processbackground.getWidth(),processbackground.getHeight());
	Rect rect2 = new Rect(0,0,scren_width,scren_heigt);
	canvas.drawBitmap(processbackground,rect1,rect2, null);
	canvas.drawBitmap(process,startx,starty, paint);
	canvas.drawRect(startx+proces*(process.getWidth()/100),starty,startx+process.getWidth(),
			starty+process.getHeight(),paint);
	canvas.drawText("加载中……"+ scren_width+"+"+scren_heigt
			,startx+(process.getWidth()/2)-20,
			starty+(process.getHeight()+20),paint);
}


private WindowManager getWindowManager() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	// TODO Auto-generated method stub
	
}

@Override
public void surfaceCreated(SurfaceHolder arg0) {
	// TODO Auto-generated method stub
	 this.thread.setFlag(true);//设置线程标志位
     this.thread.start();//启动线程
}

@Override
public void surfaceDestroyed(SurfaceHolder arg0) {
	// TODO Auto-generated method stub
	 boolean retry = true;//循环标志位
     thread.setFlag(false);//设置循环标志位
     while (retry) {
         try {
             thread.join();//等待线程结束
             retry = false;
         } 
         catch (InterruptedException e) {}//不断地循环，直到刷帧线程结束
     }
}
class TutorialThread extends Thread{//刷帧线程
	private int span = 400;//睡眠的毫秒数 
	private SurfaceHolder surfaceHolder;
	private ProcessView processView;//processView引用
	private boolean flag = false;//循环标志位
    public TutorialThread(SurfaceHolder surfaceHolder, ProcessView processView) {//构造器
        this.surfaceHolder = surfaceHolder;
        this.processView = processView;//得到加载界面
    }
    public void setFlag(boolean flag) {//设置标志位
    	this.flag = flag;
    }
	public void run() {//重写的run方法
		Canvas c;//画布
        while (this.flag) {//循环
            c = null;
            try {
            	// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {
                	processView.onDraw(c);//调用绘制方法
                }
            } finally {//使用finally语句保证下面代码一定被执行
                if (c != null) {
                	//更新屏幕显示内容
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
          
            try{
            	Thread.sleep(span);//睡眠指定毫秒数
            }
            catch(Exception e){//捕获异常信息
            	e.printStackTrace();//打印异常信息
            }
            proces+=20;
            if(proces==100){
            	if(processView.type == 1){//切到WelcomeView
            		processView.activity.myhander.sendEmptyMessage(4);//向主activity发送Handler消息
            	}
            	
            }
        }
	}
}


		
}
