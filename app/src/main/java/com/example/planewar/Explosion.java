package com.example.planewar;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Explosion {

	float x;
	float y;
//	int n;
	int m = 0;
	private ArrayList<Bitmap> frame = new ArrayList<Bitmap>();
	public Explosion(float x,float y,Bitmap[] explosion){
		this.x = x;
		this.y = y;
		for(int i =0;i<12;i++){
			frame.add (explosion[i]);
		//	frame.add(explosion[i]);
		}
		
	}
	//每刷次图则换张爆炸图片
	public void draw(Canvas canvas){
		
		canvas.drawBitmap(frame.get(m), x, y, null);
		/*if(m%4!=0){
			canvas.drawBitmap(frame.get(n), x, y,null);
		}else{
			canvas.drawBitmap(frame.get(n++), x, y,null);
		}*/
		
	              
	}
}
