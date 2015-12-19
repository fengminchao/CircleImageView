package com.example.fengminchao.circleimageview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.ImageView;

/**
 * Created by fengminchao on 15-12-19.
 */
public class CropView extends ImageView {
 public CropView(Context context){
     super(context);
     setWillNotDraw(false);
     setBackgroundColor(Color.BLACK);
     setAlpha(0.5f);
 }
    @Override
    public void onDraw(Canvas canvas){
        Paint paint_for_transparent = new Paint();
        paint_for_transparent.setAntiAlias(false);
        paint_for_transparent.setAntiAlias(true);
        paint_for_transparent.setStyle(Paint.Style.FILL_AND_STROKE);
        paint_for_transparent.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth()/5, paint_for_transparent);
    }
}
