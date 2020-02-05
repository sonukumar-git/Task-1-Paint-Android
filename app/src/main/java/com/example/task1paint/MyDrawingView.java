package com.example.task1paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MyDrawingView extends View {

    private Path drawPath;
    private Paint drawPaint,canvasPaint;

    private int paintColor=0xff000000;
    private int backColor = 0xffFFFFFF;
    private int previousColor=paintColor;
    private Canvas drawCanvas;
     Bitmap canvasBitmap;
    float touchX ;
    float touchY ;


     int brushSize;

    private boolean erase=false;

    public MyDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUpDrawingTool();
        Log.d("MSG","Constructor");

    }

    private void setUpDrawingTool(){
        drawPath=new Path();
        drawPaint=new Paint();
        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

      //  drawPaint.setXfermode(null);//new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvasPaint=new Paint(Paint.DITHER_FLAG);
        brushSize=20;
        drawPaint.setStrokeWidth(brushSize);
        Log.d("MSG","Setting up tools");


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        drawCanvas=new Canvas(canvasBitmap);
        drawCanvas.drawColor(Color.WHITE);

        Log.d("MSG","objectChanged");



    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap,0,0,drawPaint);
        canvas.drawPath(drawPath,drawPaint);
       //canvas.setBitmap(null);

        Log.d("MSG","onDraw");


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // X and Y position of user touch.
         touchX = event.getX();
        touchY = event.getY();
        Log.d("MSG","onTouch");
        // Draw the path according to the touch event taking place.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
               break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);

                break;
           case MotionEvent.ACTION_UP:
                if (erase){
                    drawPaint.setXfermode(null);//
                    // new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                }
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                drawPaint.setXfermode(null);
                break;
            default:
                return false;
        }
        // invalidate the view so that canvas is redrawn.
        invalidate();
        return true;
    }

    public void startNew()
    {
        drawCanvas.drawColor(0,PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void setEraser(Boolean perform_erase)
    {
        erase=perform_erase;
        if(erase)
        {
            drawPaint.setColor(backColor);


        }
        else
        {
            drawPaint.setColor(paintColor);
            drawPaint.setXfermode(null);
        }
    }

    public void setColor(int newColor){
        // invalidate the view
        invalidate();
        //paintColor = Color.parseColor(newColor);
        paintColor = newColor;
        drawPaint.setColor(paintColor);
        previousColor = paintColor;
    }

    public  void setBackground_Color(int newColor)
    {
        invalidate();
        backColor=newColor;
        drawCanvas.drawColor(newColor);

    }

    public void setsizeofBrush(int newsize)
    {
        if(brushSize+newsize>0)
        brushSize+=newsize;
        drawPaint.setStrokeWidth(brushSize);
    }




}
