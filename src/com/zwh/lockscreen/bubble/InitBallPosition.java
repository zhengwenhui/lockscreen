package com.zwh.lockscreen.bubble;

import android.graphics.PointF;
import android.graphics.RectF;
/** 
 * 泡泡位置分布的抽象类
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */ 
public abstract class InitBallPosition {
	protected int num;          	//泡泡的数目
	protected int radius;			//泡泡的半径
	protected int xCenter;			//屏幕水平方向上的中点
	protected int yCenter;			//屏幕垂直方向上的中点
	public InitBallPosition(RectF rect, int num, int radius){
		this.num = num;
		this.radius = radius;
		xCenter = (int) ((rect.right - rect.left)/2);
		yCenter = (int) ((rect.bottom - rect.top)/2);
	}
	/** 
	 * 抽象函数
	 * 得到位置坐标
	 * @param  
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	public abstract PointF getPosition( int index);
}
