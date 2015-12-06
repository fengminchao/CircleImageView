package com.example.fengminchao.circleimageview;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by fengminchao on 15-12-4.
 */
public class MyView extends View{
    public MyView(Context context,AttributeSet set){
        super(context,set);
    }



    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.alpha(Color.WHITE));
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        float viewWidth = this.getWidth();
        canvas.drawCircle(viewWidth/2,viewWidth/2,viewWidth/5,paint);

    }

}
