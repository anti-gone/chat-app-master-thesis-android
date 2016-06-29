package com.example.charlotte.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by charlotte on 21.06.16.
 */
public class DistanceView extends View {


    public static final float DEFAULT_MARKER_SIZE = 50.0f;
    private  int textsize;
    private  float markerSize=0.0f;
    private int color;
    private  float distance;
    private String distanceAnnotation;
    private Paint paint;
    private Path path;

    public DistanceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DistanceView,
                0, 0);
 distance = Float.parseFloat(a.getString(R.styleable.DistanceView_distanceFraction));
distanceAnnotation=a.getString(R.styleable.DistanceView_distanceAnnotation);
        color=a.getColor(R.styleable.DistanceView_textColor, Color.BLACK);
        markerSize=Float.parseFloat(a.getString(R.styleable.DistanceView_markerSize));
        textsize = a.getInt(R.styleable.DistanceView_textSize,36);



        Log.d("TAG", "distance: "+distance);

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DistanceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setDistanceFraction(float distance)
    {
        this.distance=distance;


    }

    public void setDistanceAnnotation(String distance)
    {
        this.distanceAnnotation=distance;

    }

public void init()
{

    paint = new Paint();
    paint.setColor(getResources().getColor(R.color.colorPrimary));

    int[] attrs = new int[] { android.R.attr.textColorSecondary };
    TypedArray a = getContext().getTheme().obtainStyledAttributes(R.style.AppTheme, attrs);
    color = a.getColor(0, Color.RED);
    a.recycle();




    paint.setStrokeWidth(5.0f);

    path = new Path();
}


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);




        int x = getWidth();
        int y = getHeight();
        float startX=90.0f;
        float imageWidth=(markerSize>0.0f)?markerSize: DEFAULT_MARKER_SIZE;

        float startY=y/2+imageWidth/2;


        //TODO: annotierte Distanz

        float lineLength=distance*(getWidth()-2*startX);




        //path.close();
        //canvas.drawPath(path, paint);

        paint.setColor(getResources().getColor(R.color.colorPrimary));



        float lineEndX=startX+lineLength;

        canvas.drawLine(startX,startY, lineEndX, startY, paint);

        //path.moveTo(lineEndX, lineEndY-imageWidth);
        //path.lineTo(lineEndX, lineEndY+imageWidth);
        //path.lineTo(lineEndX+imageWidth, lineEndY);
        //path.close();

        //paint.setColor(getResources().getColor(R.color.colorPrimary));
        //canvas.drawPath(path, paint);

        paint.setColor(Color.parseColor("#8a000000"));
        paint.setAntiAlias(true);

        paint.setTextSize(textsize);

        float textOffset=20.0f;

        canvas.drawText(distanceAnnotation, lineLength/2+textOffset, startY-textOffset, paint);


        Drawable d = getResources().getDrawable(R.drawable.location_pin);
        if (d!=null) {
            //left, top, right, bottom
          //TODO: mit formel

            d.setBounds((int)(startX-imageWidth/2), (int) (startY-imageWidth), (int)(startX+imageWidth/2), (int)startY);
            Log.d("TAG", "draw canvas");
            d.draw(canvas);
            d.setBounds((int)(lineEndX-imageWidth/2),(int) (startY-imageWidth), (int) (lineEndX+imageWidth/2), (int) startY);
            d.draw(canvas);
        }


    }
}
