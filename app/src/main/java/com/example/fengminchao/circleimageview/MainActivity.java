package com.example.fengminchao.circleimageview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    int image[] = new int[]{R.drawable.b,R.drawable.c,R.drawable.d};
Button btn;
    private Bitmap bitmap;
    private int height;
    private int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.btn);
        Bundle b = getIntent().getExtras();
        if(b!=null){
        Log.w("xxx", String.valueOf(b.getFloat("x")));
//        Log.w
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(image[b.getInt("id")]);
        bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        height = bitmap.getHeight();
        width = bitmap.getWidth();
//        bitmap.createBitmap(bitmap,)
        imageView.setImageBitmap(toRoundBitmap(bitmap));}

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,EditActivity.class);
                startActivity(intent);
            }
        });
    }
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = 0;
        if(width > height) {
            r = height;
        } else {
            r = width;
        }
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        canvas.drawRoundRect(rect, r/2, r/2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        return backgroundBmp;
    }


}
