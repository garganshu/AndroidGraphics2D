package com.bennyplo.graphics2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;

/**
 * Created by benlo on 09/05/2018.
 */

public class MyView extends View {
    Paint redPaint;
    Paint bluePaint ;
    Path line ;
    Point[] p ;
    //for centroid of polygon
    int xi, yi ;


    public MyView(Context context) {
        super(context, null);
        //Add your initialisation code here
        redPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setStyle(Paint.Style.STROKE);//stroke only no fill
        redPaint.setColor(0xffff0000);//color red
        redPaint.setStrokeWidth(5);//set the line stroke width to 5

        LinearGradient linearGradient = new LinearGradient(500,400,600,300, Color.BLUE, Color.RED, Shader.TileMode.MIRROR) ;

        bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setShader(linearGradient) ;
        //bluePaint.setColor(getResources().getColor(R.color.colorPrimary));
        bluePaint.setStrokeWidth(5);
        line = new Path() ;

        //initialize
        //int[] x = {50,150,180,240,300};
        //int[] y = {300,400,340,420,200};

        int[] x = {500,500,600,600};
        int[] y = {300,400,400,300};

        //center of polygon
        for(int i=0;i<x.length;i++){
            xi+=x[i] ;
            yi+=y[i] ;
        }
        xi = xi/x.length ;
        yi = yi/y.length ;

        p = new Point[x.length] ;
        for(int i=0;i<x.length;i++){
            p[i] = new Point(x[i]-xi,y[i]-yi) ;
        }


    }
    protected void updatePath(Point[] newpoint){
        line.reset();
        line.moveTo(newpoint[0].x,newpoint[0].y);
        for(int i=1;i<newpoint.length;i++){
            line.lineTo(newpoint[i].x, newpoint[i].y);
        }
        line.close();
    }

    protected Point[] AffineTransformation(Point[] oldpoint, double[][] matrix){
        Point[] result = new Point[oldpoint.length] ;

        for(int i=0;i<oldpoint.length;i++){
            int t = (int) (matrix[0][0] * oldpoint[i].x + matrix[0][1] * oldpoint[i].y + matrix[0][2] ) ;
            int u = (int) (matrix[1][0] * oldpoint[i].x + matrix[1][1] * oldpoint[i].y + matrix[1][2] ) ;
            result[i] = new Point(t,u) ;
        }

        return result ;
    }

    protected Point[] translate(Point[] oldpoint, double px, double py){
        double[][] matrix = new double[3][3] ;
        matrix[0][0] = 1;
        matrix[0][1] = 0;
        matrix[0][2] = px;
        matrix[1][0] = 0;
        matrix[1][1] = 1;
        matrix[1][2] = py;
        matrix[2][0] = 0;
        matrix[2][1] = 0;
        matrix[2][2] = 1;

        return AffineTransformation(oldpoint,matrix) ;
    }

    protected Point[] rotate(Point[] oldpoint,double angle){
        double[][] matrix = new double[3][3] ;
        matrix[0][0] = Math.cos(Math.toRadians(angle));
        matrix[0][1] = -1*Math.sin(Math.toRadians(angle));
        matrix[0][2] = 0;
        matrix[1][0] = Math.sin(Math.toRadians(angle));
        matrix[1][1] = Math.cos(Math.toRadians(angle));
        matrix[1][2] = 0;
        matrix[2][0] = 0;
        matrix[2][1] = 0;
        matrix[2][2] = 1;

        return AffineTransformation(oldpoint,matrix) ;
    }

    protected Point[] scale(Point[] oldpoint, double px, double py){
        double[][] matrix = new double[3][3] ;
        matrix[0][0] = px;
        matrix[0][1] = 0;
        matrix[0][2] = 0;
        matrix[1][0] = 0;
        matrix[1][1] = py;
        matrix[1][2] = 0;
        matrix[2][0] = 0;
        matrix[2][1] = 0;
        matrix[2][2] = 1;

        return AffineTransformation(oldpoint,matrix) ;
    }

    protected Point[] shear(Point[] oldpoint, double px, double py){
        double[][] matrix = new double[3][3] ;
        matrix[0][0] = 1;
        matrix[0][1] = px;
        matrix[0][2] = 0;
        matrix[1][0] = py;
        matrix[1][1] = 1;
        matrix[1][2] = 0;
        matrix[2][0] = 0;
        matrix[2][1] = 0;
        matrix[2][2] = 1;

        return AffineTransformation(oldpoint,matrix) ;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Add your drawing code here
        //canvas.drawCircle(500,450,50,redPaint);
        //canvas.drawCircle(300,300,250,bluePaint);

        /*//linear gradient
        int[] x = {50,150,180,240,300};
        int[] y = {300,400,340,420,200};

        //draw the polygon
        line.moveTo(x[0],y[0]);
        line.lineTo(x[1],y[1]);
        line.lineTo(x[2],y[2]);
        line.lineTo(x[3],y[3]);
        line.lineTo(x[4],y[4]);
        line.lineTo(x[0],y[0]);
        //canvas.drawPath(line,bluePaint);
        //canvas.drawPath(line,redPaint);

        int meanx = (50+160+300+380+280+100+160)/7 ;
        int meany = (300+280+380+370+450+390+380)/7 ;
        Log.d("vals",meanx+" "+meany) ;
        //canvas.drawCircle(meanx,meany,250,redPaint);*/

        //do the affine transformations

       /* //canvas.drawPath(line,bluePaint);
        Point[] newpoints = shear(p,2,0) ;
        newpoints = scale(newpoints,0.5,3) ;
        newpoints = rotate(newpoints, 45) ;
        newpoints = translate(newpoints,550,0) ;
        updatePath(newpoints);
        canvas.drawPath(line,bluePaint);*/


       //quiz ques 2
        Point[] rotate = rotate(p,45) ;
        rotate = translate(rotate,xi,yi) ;
        updatePath(rotate);
        canvas.drawPath(line,bluePaint);



    }
}
