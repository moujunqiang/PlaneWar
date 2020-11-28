package com.example.planewar;



import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ListAdapter;

public class HelpView extends SurfaceView implements SurfaceHolder.Callback {
	PanlewarActivity activity;
	private TutorialThread thread;//刷帧的线程
	Paint paint;
	Bitmap background3;
	int width=768,height=900;
	Bitmap ok;
	Cursor cur;
	String[] name={"1","2","3","4","5"};
	int[] score={0,0,0,0,0};
	public HelpView(PanlewarActivity activity,int width,int height,Cursor cur) {//构造器 
		super(activity);
	     this.cur=cur;
		this.activity = activity;

        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);
        initBitmap();//初始化图片资源
        this.width=width;
        this.height=height;
	}
	public void initBitmap(){//初始化图片资源的方法
		paint = new Paint(); 
		
		background3 = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		ok = BitmapFactory.decodeResource(getResources(), R.drawable.sure);
	}
	public void onDraw(Canvas canvas){//自己写的绘制方法
		//画的内容是z轴的，后画的会覆盖前面画的
		canvas.drawColor(Color.WHITE);//将屏幕刷成白色 
		Rect rect1 = new Rect(0,0,background3.getWidth(),background3.getHeight());
		Rect rect2 = new Rect(0,0,width,height);
		canvas.drawBitmap(background3,rect1,rect2, null);
        paint.setTextSize(20);
        paint.setColor(Color.BLUE);
	    canvas.drawText("英雄排行榜", width*2/5, height/20, paint);
	    canvas.drawText("战斗名次", width/4, height/10, paint);
	    canvas.drawText("第五", width/4, 6*height/10, paint);
	    canvas.drawText("第四", width/4, 5*height/10, paint);
	    canvas.drawText("第三", width/4, 4*height/10, paint);
	    canvas.drawText("第二", width/4, 3*height/10, paint);
	    canvas.drawText("第一", width/4, 2*height/10, paint);
		 canvas.drawText("英雄姓名", width/2, height/10, paint);
		 for(int i=0;i<5;i++){
				switch(i){
				case 0:
					 canvas.drawText(name[i], width/2,6*height/10, paint);
					break;
					case 1:
						 canvas.drawText(name[i], width/2,5*height/10, paint);
				    break;
					case 2:
						 canvas.drawText(name[i], width/2,4*height/10, paint);
				  
				    break;
					case 3:
						 canvas.drawText(name[i], width/2,3*height/10, paint);
			     	break;
					case 4:
						 canvas.drawText(name[i], width/2,2*height/10, paint);
				   
				    break;
					
				}
			}
		 canvas.drawText("战斗分数", width*3/4, height/10, paint);
		 for(int i=0;i<5;i++){
				switch(i){
				case 0:
					 canvas.drawText(""+score[i], width*3/4, 6*height/10, paint);
					break;
					case 1:
						 canvas.drawText(""+score[i], width*3/4, 5*height/10, paint);
				    break;
					case 2:
						 canvas.drawText(""+score[i], width*3/4, 4*height/10, paint);
				  
				    break;
					case 3:
						 canvas.drawText(""+score[i], width*3/4, 3*height/10, paint);
			     	break;
					case 4:
						 canvas.drawText(""+score[i], width*3/4, 2*height/10, paint);
				   
				    break;
					
				}
			}
		canvas.drawBitmap(ok, width/2-ok.getWidth()/2, height*7/10, paint);  
 
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	public void surfaceCreated(SurfaceHolder holder) {
        this.thread.setFlag(true);
        this.thread.start();
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
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
	@Override
	public boolean onTouchEvent(MotionEvent event) {//屏幕监听
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(event.getX()>width/2-ok.getWidth()/2 && event.getX()<width/2-ok.getWidth()/2+ok.getWidth()
					&& event.getY()>height*7/10 && event.getY()<height*7/10+ok.getHeight()){//点击了确定按钮
				send();
				 
			
			}  
		}
		return super.onTouchEvent(event);
	}
	public void send(){
		activity.myhander.sendEmptyMessage(7);
	}
	public HelpView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	class TutorialThread extends Thread{//刷帧线程
		private int span = 100;//睡眠的 毫秒数 
		private SurfaceHolder surfaceHolder;
		private HelpView helpView;
		private boolean flag = false;
        public TutorialThread(SurfaceHolder surfaceHolder, HelpView helpView) {//构造器
            this.surfaceHolder = surfaceHolder;
            this.helpView = helpView;
        }
        public void setFlag(boolean flag) {
        	this.flag = flag;
        }
		@Override
		public void run() {
			if(cur!=null&&cur.getCount()>=0){
				int i=0;
				if(cur.moveToFirst()){
					do{
					name[i]=cur.getString(cur.getColumnIndex("name"));
						score[i]=cur.getInt(cur.getColumnIndex("score"));
						
					
						i++;
					 }while(cur.moveToNext());
					i=0;
				}
			}
			Canvas c;
            while (this.flag) {
                c = null;
                try {
                	// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {
                    	helpView.onDraw(c);
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
