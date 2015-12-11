package com.example.fengminchao.circleimageview;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;


/**
 * Created by fengminchao on 15-12-4.
 */
public class EditActivity extends Activity implements View.OnClickListener {
    private Button save,back,change;
    private ImageView image;
    int [] imageId = new int[]{R.drawable.b,R.drawable.c,R.drawable.d};
    int i =0;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private final static int NONE = 0;
    private final static int DRAG = 1;
    private final static int ZOOM = 2;
    private float oldDistance = 0f;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float dx = 0;
    private float dy = 0;
    private float scaling = 1;
    private float r;
    Bitmap bitmap;
    float values [];
    private PointF left,right;
    private FrameLayout frameLayout;
    private int width,height;

    @Override
    protected void onCreate(final Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.edit);
        save = (Button) findViewById(R.id.save);
        back = (Button) findViewById(R.id.back);
        image = (ImageView)findViewById(R.id.image);
        change = (Button)findViewById(R.id.change);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        values = new float[9];

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        change.setOnClickListener(this);
        image.setImageResource(imageId[i]);
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                switch (event.getAction()&MotionEvent.ACTION_MASK){
                    case (MotionEvent.ACTION_DOWN):
                        matrix.set(view.getImageMatrix());
                        savedMatrix.set(matrix);
                        start.set(event.getX(),event.getY());
                        mode = DRAG;
                        break;
                    case (MotionEvent.ACTION_POINTER_DOWN):
                        oldDistance = distance(event);
                        if(oldDistance > 10f){
                            mode = ZOOM;
                            savedMatrix.set(matrix);
                            mid = mid(event);
                        }
                        break;
                    case (MotionEvent.ACTION_UP):
                    case (MotionEvent.ACTION_POINTER_UP):
                        mode = NONE;
                        break;
                    case (MotionEvent.ACTION_MOVE):
                        if(mode == DRAG){
                            left = getLeftPointF();
                            right = getRightPointF();

                            if(left.x>0 && left.y>0 && right.x<frameLayout.getWidth()&& right.y<frameLayout.getHeight())
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                            dx = dx+event.getX()-start.x;
                            dy = dy+event.getY()-start.y;
                        }
                       if (left.x<=0){
                            matrix.set((savedMatrix));
                            matrix.postTranslate((event.getX()-start.x)>0?(event.getX()-start.x):0,event.getY() - start.y);
                        }
                       if(right.x>=frameLayout.getWidth()){
                           matrix.set((savedMatrix));
                           matrix.postTranslate(0,event.getY() - start.y);
                       }
                        if(right.y>=frameLayout.getHeight()){
                            matrix.set((savedMatrix));
                            matrix.postTranslate(event.getX()-start.x,0);
                        }
                        if(left.y<0){
                            matrix.set((savedMatrix));
                            matrix.postTranslate(event.getX()-start.x,0);
                        }
                        else if(mode == ZOOM){
                            float newDistance = distance(event);
                            if(oldDistance > 10f){
                                matrix.set(savedMatrix);
                                float scale = newDistance/oldDistance;
                                scaling = scaling*scale;
                                matrix.postScale(scale,scale,mid.x,mid.y);
                            }
                        }
                        break;
                }
                view.setImageMatrix(matrix);
                return true;
            }
        });
//        Bitmap images = ((BitmapDrawable)image.getDrawable()).getBitmap();
//        image.setImageBitmap(toRoundBitmap(images));
//        Bitmap

    }
    private float distance(MotionEvent event){
        float x = event.getX(0)-event.getX(1);
        float y = event.getY(0)-event.getY(1);
        return (float)Math.sqrt(x*x+y* y);
    }

    private PointF mid(MotionEvent event) {
        float x = event.getX(0)+event.getX(1);
        float y = event.getY(0)+event.getY(1);
        return new PointF(x/2,y/2);
    }
    //获取图片的上坐标
    private PointF getLeftPointF()
    {
        Rect rectTemp = image.getDrawable().getBounds();
        float[] values = new float[9];
        matrix.getValues(values);
        float leftX=values[2];
        float leftY=values[5];
        return new PointF(leftX,leftY);
    }
    //获取图片的下坐标
    private PointF getRightPointF()
    {
        Rect rectTemp = image.getDrawable().getBounds();
        float[] values = new float[9];
        matrix.getValues(values);
        float leftX= values[2]+rectTemp.width()*values[0];
        float leftY=values[5]+rectTemp.height()*values[4];
        return new PointF(leftX,leftY);
    }
    @Override
    public void onClick(View v){
        switch ((v.getId())){
            case R.id.save:
                Intent intent = new Intent();
                //此处出错
                bitmap = Bitmap.createBitmap(((BitmapDrawable) image.getDrawable()).getBitmap(),
                         (324),
                         (324),
                         (432),
                         (432));

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    intent.putExtra("bitmap", bytes);
                    setResult(RESULT_OK, intent);
                    finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.change:
                if(i >= 2){
                    i = 0;
                }
                else i++;
                image.setImageResource(imageId[i]);
                break;
        }
    }
    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
}
