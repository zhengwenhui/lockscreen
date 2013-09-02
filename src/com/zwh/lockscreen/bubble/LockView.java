package com.zwh.lockscreen.bubble;

import com.zwh.lockscreen.set.PreferenceInfo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.ImageView;
/** 
 * 锁屏界面View
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */ 
public class LockView extends ImageView implements Runnable {
	private static final int MESSAGE_RUN = 0x101;	//刷新画布的消息
	public static final int MESSAGE_FINISH = 0x102;	
	private Paint paint;       						//定义画笔   
	private VelocityTracker vTracker;  				//
	private Handler handler;						//结束的消息处理
	private RefreshHandler mHandler;  				//刷新画布的消息处理
	private BallGroup bubble;						//Ball Group
	private boolean run = false;
	private boolean draw = false;
	private Context context;
	PreferenceInfo info = null;						//程序设置信息
	
	public LockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public LockView(Context context) {
		super(context,null);
		this.context = context;
	}
	public LockView(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated constructor stub
		super(context, attrs, defStyle);
		this.context = context;
	}
	public void setHandler(Handler handler){
		this.handler = handler;
		mHandler = new RefreshHandler();
		setFocusable(true); 
		//启动线程
		new Thread(this).start();
		run = true;
	}
	
	/** 
	 * 初始化
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	private void init(){
		initPaint();
		float width = getWidth();
		float height = getHeight();

		float xDivisor = width/480; 
		float yDivisor = height/800;
		float divisor = xDivisor<yDivisor ? xDivisor:yDivisor;	//缩放因子

		RectF rect = new RectF(0, 0, width, height);

		bubble = new BallGroup(context, getResources(), paint, rect,divisor);
		info = new PreferenceInfo(context);
		vTracker = VelocityTracker.obtain();
	}
	/** 
	 * 初始化画笔
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	private void initPaint(){
		paint = new Paint();  
		//设置消除锯齿   
		paint.setAntiAlias(true);  
	}  
	
	//True if the event was handled, false otherwise.
	@Override
	public boolean onTouchEvent(MotionEvent event){
		boolean result = true;
		float cx,cy;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			cx =  event.getX();
			cy =  event.getY();
			//是否点中了球
			bubble.contains(cx, cy);
			if(null!=bubble.ballBeClick){
				//点中了球
				vTracker.clear();
				//设置点中球的速度为0，防止溜掉。
				bubble.setClickBallVelocity(0, 0);
				vTracker.addMovement(event);
			}
			else{
				//没有点中球
				result = false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			cx = event.getX();
			cy = event.getY();
			if(bubble.setClickBallPosition(cx, cy)){
				vTracker.addMovement(event);
				vTracker.computeCurrentVelocity(10);
				bubble.setClickBallVelocity(vTracker.getXVelocity(), vTracker.getYVelocity());
			}
			break;  
		case MotionEvent.ACTION_UP:
			cx = event.getX();
			cy = event.getY();
			if(bubble.setClickBallPosition(cx, cy)){
				vTracker.addMovement(event);
				vTracker.computeCurrentVelocity(10);
				bubble.setClickBallVelocity(vTracker.getXVelocity(), vTracker.getYVelocity());
				unlock();
			}
			bubble.freeClickBall();
			break;
		default:
			break;
		}  
		return result;  
	} 
	/** 
	 * 解屏，发送广播
	 * @param 
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	private void unlock(){
		Message message = new Message();
		message.what = MESSAGE_FINISH;
		message.arg1 = bubble.getClickIndex();
		handler.sendMessage(message);
		/*if(info.showBattery()){
			battery.unregisterReceiver(context);
		}*/
		run = false;
	}

	public void run() {
		// TODO Auto-generated method stub
		while( !Thread.currentThread().isInterrupted() && run){
			//通过发送消息更新界面
			Message message = new Message();
			message.what = MESSAGE_RUN ;
			mHandler.sendMessage(message);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		//绘制小圆作为小球
		if(!draw){
			draw = true;
			init();
		}
		bubble.moveStep(); 				//移动球
		//paint.setTextSize(36);
		bubble.onDraw(canvas);
	}
	
	/** 
	 * 更新界面处理器
	 * @param
	 * @author 郑文辉
	 * @Date   2012年1月15日
	 */ 
	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message message){
			if( MESSAGE_RUN  == message.what ){
				LockView.this.invalidate();
			}
			super.handleMessage(message);
		}
	}
}
