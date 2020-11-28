package com.example.planewar;



import com.example.planewar.HelpView.TutorialThread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class reStartView extends SurfaceView implements SurfaceHolder.Callback{
	PanlewarActivity activity;
	int width,height;
	Paint paint=new Paint();
	Bitmap restart,backgroundre,turnH;
	private TutorialThread thread;//刷帧的线程
	public reStartView(PanlewarActivity activity,int width,int height) {
		super(activity);
		this.activity=activity;
		this.width=width;
		this.height=height;
		 getHolder().addCallback(this);
	        this.thread = new TutorialThread(getHolder(), this);
	        initBitmap();//初始化图片资源
		// TODO Auto-generated constructor stub
	}
	public void onDraw(Canvas canvas){//自己写的绘制方法
		//画的内容是z轴的，后画的会覆盖前面画的
		canvas.drawColor(Color.WHITE);//将屏幕刷成白色 
		Rect rect1 = new Rect(0,0,backgroundre.getWidth(),backgroundre.getHeight());
		Rect rect2 = new Rect(0,0,width,height);
		canvas.drawBitmap(backgroundre,rect1,rect2, null);
	    
	    
		canvas.drawText("排行榜：", 0, 0, paint);
		
		canvas.drawBitmap(restart,width/2-restart.getWidth()/2, height/3+(height*2/3-2*restart.getHeight())/3, null);  
		canvas.drawBitmap(turnH, width/2-restart.getWidth()/2, height-(height*2/3-2*restart.getHeight())/3-turnH.getHeight(), null);  
	}
	private void initBitmap() {
		// TODO Auto-generated method stub
		 restart=BitmapFactory.decodeResource(getResources(), R.drawable.restartgame);
		 backgroundre=BitmapFactory.decodeResource(getResources(), R.drawable.restart);
		 turnH=BitmapFactory.decodeResource(getResources(), R.drawable.returnh);
	}
	
	public boolean onTouchEvent(MotionEvent event) {//屏幕监听
		if(event.getAction() == MotionEvent.ACTION_DOWN){//屏幕被按下
			/*if(this.status != 4){//当不是菜单状态时返回
				return false;
			}*/
			double x = event.getX();//得到X坐标
			double y = event.getY();//得到Y坐标
			if(x>width/2-restart.getWidth()/2&& x<width/2-restart.getWidth()/2 + restart.getWidth()
					&& y>height/3+(height*2/3-2*restart.getHeight())/3 && y<height/3+(height*2/3-2*restart.getHeight())/3 + restart.getHeight()){//点击了开始有些按钮
				activity.myhander.sendEmptyMessage(6);//发送消息
			}
			else if(x>width/2-restart.getWidth()/2 && x<width/2-restart.getWidth()/2 + turnH.getWidth()
					&& y>height-(height*2/3-2*restart.getHeight())/3-turnH.getHeight() && y<height-(height*2/3-2*restart.getHeight())/3-turnH.getHeight() + turnH.getHeight()){//点击了帮助按钮
				activity.myhander.sendEmptyMessage(8);//发送消息
			}
			
		}
		return super.onTouchEvent(event);//调用基类的方法
	}
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		 this.thread.setFlag(true);
	        this.thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		 boolean retry = true;
	        thread.setFlag(false);
	        while (retry) {
	            try {
	                thread.join();
	                retry = false;
	            } 
	            catch (InterruptedException e) {//不断地循环，直到刷帧线程结束
	            }
	        }
	}
	class TutorialThread extends Thread{//刷帧线程
		private int span = 100;//睡眠的 毫秒数 
		private SurfaceHolder surfaceHolder;
		private reStartView restart;
		private boolean flag = false;
        public TutorialThread(SurfaceHolder surfaceHolder, reStartView restart) {//构造器
            this.surfaceHolder = surfaceHolder;
            this.restart = restart;
        }
        public void setFlag(boolean flag) {
        	this.flag = flag;
        }
		@Override
		public void run() {
			Canvas c;
            while (this.flag) {
                c = null;
                try {
                	// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {
                    	restart.onDraw(c);
                    }
                } finally {
                    if (c != null) {
                    	//更新屏幕显示内容
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try{
                	Thread.sleep(span);
                }
                catch(Exception e){
                	e.printStackTrace();
                }
            }
		}
	}
}
