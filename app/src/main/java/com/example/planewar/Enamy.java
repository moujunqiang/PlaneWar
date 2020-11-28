package com.example.planewar;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Enamy {
	
	GameView gameview;
	public float x;
	public float y;
	private Bitmap enemy;
	public Enamy(float x,float y,Bitmap enemy,GameView gameview){
		this.gameview=gameview;
		this.x = x;
		this.y = y;
		this.enemy = enemy;
	}
	public void draw(final Canvas canvas){
		Matrix matrix = new Matrix();
		if(gameview.tm2-gameview.tm1>5000){
			y+=4;
		}
		else if(gameview.tm2-gameview.tm1>10000){
			y+=8;
		}else if(gameview.tm2-gameview.tm1>15000){
			y=y+10;
		}else if(gameview.tm2-gameview.tm1>20000){
			y=y+15;
		}else if(gameview.tm2-gameview.tm1>25000){
			y=y+20;
		}else if(gameview.tm2-gameview.tm1>30000){
			y=y+30;
		}else if(gameview.tm2-gameview.tm1>35000){
			y=y+40;
		}else if(gameview.tm2-gameview.tm1>45000){
			y=y+43;
		}else if(gameview.tm2-gameview.tm1>60000){
			y=y+46;
		}else{
		
		y+=3;}
		matrix.setTranslate(x, y);
		
		canvas.drawBitmap(enemy, matrix, null);
	}
}
