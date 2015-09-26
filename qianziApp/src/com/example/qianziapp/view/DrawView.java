package com.example.qianziapp.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.preference.PreferenceManager.OnActivityStopListener;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DrawView extends View {

	Paint mPaint;// ����
	Path path;// ·��
	Bitmap cacheBitmap;// ����λͼ
	Canvas cacheCanvas;// ���滭��

//	private int clr_bg, clr_fg;

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);

//		clr_bg = Color.WHITE;
//		clr_fg = Color.CYAN;
		// ��ʼ������
		mPaint = new Paint();
		// Dither��ͼ��Ķ���������ÿ����ɫֵ�Ե���8λ��ʾʱ����Ӧͼ���������������ʵ���ڿ���ʾ��ɫ�����Ƚϵͣ�����256ɫ��ʱ�����ֽϺõ���ʾЧ����
		mPaint.setDither(true);
		mPaint.setAntiAlias(true);// �����
		mPaint.setStyle(Paint.Style.STROKE);// ������
		mPaint.setStrokeWidth(3);// �������
		mPaint.setAlpha(255);

		path = new Path();
		// ����λͼ�����������ɫ
		cacheBitmap = Bitmap.createBitmap(480, 800, Config.ARGB_8888);
		cacheCanvas = new Canvas(cacheBitmap);
		
		photoPath=new Path();
	}

	public DrawView(Context context) {
		super(context);
	}

	/**
	 * invalidateʱ������
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ������һ�Σ���������
		// canvas.
		// ��Ϊ��ָ̧��������path,������Ҫ�Ȼ����ϴα����״̬
		canvas.drawBitmap(cacheBitmap, 0, 0, null);
		// ����·��
		canvas.drawPath(path, mPaint);
	}

	/**
	 * ��ջ���
	 */
	public void clear() {
		// ����
		Paint p = new Paint();
		p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		cacheCanvas.drawPaint(p);
		// p.setXfermode(new PorterDuffXfermode(Mode.SRC));
		// path.reset();
		// cacheBitmap = Bitmap.createBitmap(480, 800, Config.ARGB_8888);
		// cacheCanvas.drawColor(clr_bg);
		// cacheCanvas.drawColor(Color.TRANSPARENT);
		photoPath.reset();
		invalidate();
	}

	private float curX, curY;
	public boolean isMoving;
	
	Path photoPath;
	float top=0.0f;//�϶�ͼƬ�Ĵ�ֱ�ƶ�ֵ
	float left=0.0f;//�϶�ͼƬ��ˮƽ�ƶ�ֵ
	float scale=1.0f;//ͼƬ������ű���
	float Dx=0f;//ͼƬ���ң���Ե��view�����ң���Ե֮��ľ���
	float Dy=0f;//ͼƬ�ϣ��£���Ե��view���ϣ��£���Ե֮��ľ���
	float picScale=1f;//ͼƬ��ʾʱ����Ӧview��С�����ű���
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			curX = x;
			curY = y;
			path.moveTo(x, y);
			isMoving = true;
			//
			photoPath.moveTo((left+x-Dx)/scale*picScale, (top+y-Dy)/scale*picScale);
//			photoPath.moveTo(left+x, top+y);
//			Log.i("����", "���꣺("+x+","+y+")");
			break;
		case MotionEvent.ACTION_MOVE:
			// if (!isMoving) {
			// break;
			// }
			path.quadTo(curX, curY, x, y);
//			Log.i("�ƶ�", "���꣺("+x+","+y+")");
			// Ч��һ���ķ���
			// path.lineTo(x, y);
			photoPath.quadTo((left+curX-Dx)/scale*picScale, (top+curY-Dy)/scale*picScale,(left+x-Dx)/scale*picScale, (top+y-Dy)/scale*picScale);
//			photoPath.quadTo(left+curX, top+curY,left+x, top+y);
			curX = x;
			curY = y;
			
			break;
		case MotionEvent.ACTION_UP:
			// ��������״̬
			cacheCanvas.drawPath(path, mPaint);
			// ���û���
			path.reset();
//			Log.i("����", "���꣺("+(left+x/scale)+","+(top+y/scale)+")");
			isMoving = false;
			break;
		default:
			break;
		}
		invalidate();
		return true;
	}

	/**
	 * ����ͼƬ����
	 * 
	 * @param filename
	 */
	public void saveToFile(String filename) throws FileNotFoundException {
		File file = new File(filename);
		if (file.exists()) {
			throw new RuntimeException("�ļ�:" + filename + "�Ѿ�����");
		}
		FileOutputStream fos = new FileOutputStream(new File(filename));
		cacheBitmap.compress(CompressFormat.PNG, 50, fos);
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ���ò���
	 * @param top���ϱ߾�
	 * @param left����߾�
	 * @param scale��photo������ű���
	 * @param Dx��������ƫ����
	 * @param Dy��������ƫ����
	 * @param picScale:ͼƬ��ʾ��photo�ϵ����ű���
	 */
	public void setPathPoint(float top,float left,float scale,float Dx,float Dy,float picScale){
		this.top=-top;
		this.left=-left;
		this.scale=scale;
		this.Dx=Dx;
		this.Dy=Dy;
		this.picScale=picScale;
		Log.i("������д����Ĳ���", "���꣺("+-top+","+-left+");scale:"+scale+";Dx:"+Dx+";"+";Dy:"+Dy+";picScale:"+picScale);
	}

	public Bitmap getPathBitmap() {
		return cacheBitmap;
	}
	
	public Path getPhotoPath(){
		return photoPath;
	}
	
	public Paint getPaint(){
		return mPaint;
	}
}
