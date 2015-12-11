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
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;


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
    private Intent intent;

    @Override
    protected void onCreate(final Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.edit);
        save = (Button) findViewById(R.id.save);
        back = (Button) findViewById(R.id.back);
        image = (ImageView)findViewById(R.id.image);
        change = (Button)findViewById(R.id.change);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        r = metrics.widthPixels/5;
        intent = new Intent(EditActivity.this,MainActivity.class);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        change.setOnClickListener(this);
//        image.setLeft(20);
//        image.setTop(20);
//
//        image.getMaxHeight();
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
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                            dx = dx+event.getX()-start.x;
                            dy = dy+event.getY()-start.y;
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
        return (float)Math.sqrt(x*x+y*y);
    }
    private PointF mid(MotionEvent event){
        float x = event.getX(0)+event.getX(1);
        float y = event.getY(0)+event.getY(1);
        return new PointF(x/2,y/2);
    }

    @Override
    public void onClick(View v){
        switch ((v.getId())){
            case R.id.save:
                Bundle bundle = new Bundle();
                bundle.putFloat("scale",scaling);
                bundle.getFloat("x",image.getX());
                bundle.getFloat("y",image.getY());
                bundle.putFloat("dx",dx);
                bundle.putFloat("dy",dy);
                bundle.putFloat("r",r);
                bundle.putInt("id",i);
                intent.putExtras(bundle);
                startActivity(intent);
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
}
