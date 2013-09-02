package com.zwh.lockscreen.bubble;

import com.zwh.lockscreen.addition.Vibrate;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
/** 
 * 泡泡
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */ 
public class Ball {
	private int radius;      	 		//半径
	private Bitmap bitmap;   	 		//图资源
	private Bitmap icon;		 		//图标资源
	private Paint paint;     			//画笔

	private float x = 0;      	 		//圆心x轴坐标
	private float y = 0;      	 		//圆心y轴坐标
	private RectF screenRect;			//圆心位置范围

	private float vx = 0;    	 		//x轴速度
	private float vy = 0;    	 		//y轴速度

	private float mass = 1;   	 		//质量 
	private float bounce = -1.0f;		//弹力
	
	private float MAX_VELOCITY = 5.5f;  //最大速度
	private float MIN_VELOCITY = -5.5f; //最小速度

	//用于表示球碰到了那面墙
	public static enum wall{
		NULL,
		TOP,
		BOTTOM,
		RIGHT,
		LEFT
	}
	/** 
	 * 构造器
	 * @param vibrate 振动；radius 半径,  bitmap 泡泡；paint 画笔；rect 泡泡圆心可移动的范围；  
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	public Ball(Vibrate vibrate, int radius , Bitmap bitmap ,Paint paint, RectF rect) {
		this.radius = radius;
		this.bitmap = bitmap;
		this.paint = paint;
//		this.paint.setAntiAlias(true);
		//this.vibrate = vibrate;
		//设置圆心坐标的范围
		setScreenRect(rect);
	}

	/** 
	 * 绘制球
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	public void onDraw(Canvas canvas) {
		RectF rect = new RectF(x-radius, y-radius, x+radius, y+radius);
		canvas.drawBitmap(bitmap, null, rect, paint);
		
//		canvas.drawBitmap(bitmap, x-radius, y-radius, paint);
		
		if(null!=icon){
			int xd = icon.getWidth()/2;
			int yd = icon.getHeight()/2; 
			canvas.drawBitmap(icon, x-xd, y-yd, paint);
		}
	}

	/** 
	 * 设置圆心坐标的范围
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public void setScreenRect( RectF rect){
		if(null == screenRect){
			screenRect = new RectF();
		}
		float left = rect.left+radius;
		float top = rect.top+radius;
		float right = rect.right - radius;
		float bottom = rect.bottom - radius;

		screenRect.set(left, top, right, bottom);
	}

	/** 
	 * 处理球的越界（调整位置，并设置速度）
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	private wall handleWalls() {
		wall whichWall = wall.NULL;
		//水平方向上
		if(x < screenRect.left){
			//左边界
			x = screenRect.left;
			vx *= bounce;
			whichWall = wall.LEFT;
			// 振动
			//vibrate.vibrate();
		}
		else if(x > screenRect.right){
			//右边界
			x = screenRect.right;
			vx *= bounce;
			whichWall = wall.RIGHT;
			// 振动
			//			vibrate.vibrate();
		}

		//垂直方向上
		if(y < screenRect.top){
			//上边界
			y = screenRect.top;
			vy *= bounce;
			whichWall = wall.TOP;
			// 振动
			//			vibrate.vibrate();
		}
		else if(y > screenRect.bottom){
			//下边界
			y = screenRect.bottom;
			vy *= bounce;
			whichWall = wall.BOTTOM;
			// 振动
			//			vibrate.vibrate();
		}
		return whichWall;
	}

	/** 
	 * 根据速度，计算下一个位置，自动处理越界。
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public wall moveStep(){
		x += vx;
		y += vy;
		return handleWalls();
	}

	/** 
	 * 判断给定的位置是否在圆内
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public boolean contains(float cx, float cy){
		boolean contains = false;

		float disX = x - cx;
		float disY = y - cy;
		//点到圆心的距离
		float dis = (float) Math.sqrt( disX*disX + disY*disY );

		if(dis<radius){
			contains = true;
		}
		return contains;
	}

	/** 
	 * 设置位置，如果越界，自动调整。
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public void setPosition(float x,float y){
		this.x = x;
		this.y = y;
	}

	/** 设置位置，如果越界，自动调整。
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public void setPosition(PointF position){
		this.x = position.x;
		this.y = position.y;
	}

	/** 得到x值
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public float getX(){
		return x;
	}

	/** 
	 * 得到y值
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public float getY(){
		return y;
	}
	/** 
	 * 设置速度
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public boolean setVelocity(float vx,float vy){
		if(vx>MAX_VELOCITY){
			vx = MAX_VELOCITY-0.5f;
		}
		else if(vx<MIN_VELOCITY){
			vx = MIN_VELOCITY+0.5f;
		}
		
		if(vy>MAX_VELOCITY){
			vy = MAX_VELOCITY-0.5f;
		}
		else if(vy<MIN_VELOCITY){
			vy = MIN_VELOCITY+0.5f;
		}
		
		this.vx = vx;
		this.vy = vy;
		return true;
	}
	/** 
	 * 得到x方向的速度值
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public float getVX(){
		return vx;
	}
	/** 
	 * 到y方向的速度值
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public float getVY(){
		return vy;
	}
	/** 
	 * 设置球的质量
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public void setMass(float mass){
		this.mass = mass;
	}
	/** 
	 * 设置球的质量
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public float getMass(){
		return mass;
	}
	/** 
	 * 设置球的半径
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public void setRadius(int radius){
		this.radius = radius;
	}
	/** 
	 * 返回球的半径
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public float getRadius(){
		return radius;
	}
	/** 设置图标
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public void setIcon(Bitmap icon){
		this.icon = icon;
	}
}
