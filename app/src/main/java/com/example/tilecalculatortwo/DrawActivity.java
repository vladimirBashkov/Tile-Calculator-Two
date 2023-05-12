package com.example.tilecalculatortwo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

public class DrawActivity extends AppCompatActivity {
    DrawingInformation dI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this,  dI));
        Bundle arguments = getIntent().getExtras();
        DrawingInformation drawingInformation;
        if(arguments!=null){
            drawingInformation = (DrawingInformation) arguments.getSerializable(DrawingInformation.class.getSimpleName());
            dI = drawingInformation;
        }
    }

    class DrawView extends View {
        Paint p;
        Rect rect;
        DrawingInformation di;

        public DrawView(Context context, DrawingInformation drawingInformation) {
            super(context);
            p = new Paint();
            // настройка кисти
            p.setColor(Color.BLACK);
            p.setStrokeWidth(5);
            dI = drawingInformation;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if(dI == null){
                canvas.drawRGB( 255, 255, 255);
            } else{
                canvas.drawRGB( 255, 255, 255);
                drowning(canvas);
            }
        }

        private void drowning(Canvas canvas){
            double length = dI.getLength();
            double width = dI.getWidth();
            double tileLength = dI.getTileLength();
            double tileWidth = dI.getTileWidth();
            double step = dI.getOffset();
            boolean centering = dI.isCentering();
            boolean useRest = dI.isUsingRemnant();
            boolean alongLongSide = dI.isAlongLongSide();
            int deviceWidth = getWidth();
            int deviceHeight = getHeight();
            boolean heightGreaterThanLength = deviceHeight >= deviceWidth;

            if(heightGreaterThanLength){
                double usingRatio = searchRatio(deviceHeight, deviceWidth, length, width);
                int widthRect = (int) ( usingRatio * width);
                int heightRect = (int) ( usingRatio * length);
                int offsetX = (deviceWidth-widthRect)/2;
                int offsetY = (deviceHeight-heightRect)/2;
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(offsetX,offsetY, widthRect + offsetX,
                        heightRect + offsetY, p);
                if(centering){
                    fillingSquareWithCentering(canvas, alongLongSide, usingRatio,
                            width, length, tileLength, tileWidth,
                            offsetX,offsetY, widthRect + offsetX,
                            heightRect + offsetY, 0.0, true, false);
                } else if(useRest){
                    fillingSquareWithCentering(canvas, alongLongSide, usingRatio,
                            width, length, tileLength, tileWidth,
                            offsetX,offsetY, widthRect + offsetX,
                            heightRect + offsetY, 0.0, false, true);
                } else {
                    fillingSquareWithCentering(canvas, alongLongSide, usingRatio,
                            width, length, tileLength, tileWidth,
                            offsetX,offsetY, widthRect + offsetX,
                            heightRect + offsetY, step, false, false);
                }

            } else{
                double usingRatio = searchRatio(deviceWidth, deviceHeight, length, width);
                int widthRect = (int) ( usingRatio * length);
                int heightRect = (int) ( usingRatio * width);
                int offsetX = (deviceWidth-widthRect)/2;
                int offsetY = (deviceHeight-heightRect)/2;
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(offsetX,offsetY, widthRect + offsetX,
                        heightRect + offsetY, p);

                if(centering){
                    fillingSquareWithCentering(canvas, !alongLongSide, usingRatio,
                            length, width, tileLength, tileWidth,
                            offsetX, offsetY, widthRect + offsetX,
                            heightRect + offsetY, 0.0, true, false);
                } else if(useRest){
                    fillingSquareWithCentering(canvas, !alongLongSide, usingRatio,
                            length, width, tileLength, tileWidth,
                            offsetX, offsetY, widthRect + offsetX,
                            heightRect + offsetY, 0.0, false, true);
                } else {
                    fillingSquareWithCentering(canvas, !alongLongSide, usingRatio,
                            length, width, tileLength, tileWidth,
                            offsetX, offsetY, widthRect + offsetX,
                            heightRect + offsetY, step, false, false);
                }
            }
        }

        private double searchRatio(int deviceLongestSide, int deviceShortSide,
                                   double longestSide, double shortSide){
            double lengthRatio = deviceShortSide/shortSide;
            double widthRatio = deviceLongestSide/longestSide;
            if (lengthRatio >= widthRatio){
                return widthRatio;
            } else {
                return lengthRatio;
            }
        }

        private void fillingSquareWithCentering(Canvas canvas, boolean alongLongSide, double usingRatio,
                                                double width, double length, double tileLength, double tileWidth,
                                                int left, int top, int right, int bottom, Double step, boolean centering, boolean useRest) {
            if(alongLongSide){
                int rowsOfTiles = (int)(width/tileWidth);
                double row = length/tileLength;
                int piecesInRow = (int) row;
                double centeringStep = 0;
                if(centering && row-piecesInRow != 0.0){
                    centeringStep = (1.0 + row-piecesInRow)/2;
                }
                double rest = 0;
                int stepBefore = 0;
                for (int i = 0; i <= rowsOfTiles + 1; i++) {
                    for (int j = 0; j <= piecesInRow + 1; j++) {
                        if(j == 0){
                            stepBefore = stepBefore + (int) (step*tileLength*usingRatio);
                            if(i==0){
                                stepBefore =0;
                            }
                            int xPointOne = right - (int) (tileWidth*usingRatio*(i+1));
                            int xPointTwo = right - (int) (tileWidth*usingRatio*(i));
                            int yPointOne = top + (int) ((tileLength*(j + centeringStep) + rest)*usingRatio) + stepBefore;
                            Rect rect = new Rect(xPointOne, top, xPointTwo, yPointOne);
                            canvas.drawRect(rect,  p);
                        }

                        int xPointOne = right - (int) (tileWidth*usingRatio*(i+1));
                        int yPointOne = top + (int) ((tileLength*(j + centeringStep) + rest)*usingRatio) + stepBefore;
                        int xPointTwo = right - (int) (tileWidth*usingRatio*(i));
                        int yPointTwo  = top + (int) ((tileLength*(j + 1 + centeringStep) + rest)*usingRatio)+ stepBefore;

                        Rect rect = new Rect(xPointOne, yPointOne, xPointTwo, yPointTwo);
                        canvas.drawRect(rect,  p);
                        if(useRest && j==piecesInRow){
                            rest = ((int) ((length - rest)/tileLength)) * tileLength + rest - length;
                        }
                    }
                    if(stepBefore > (int) (tileLength*usingRatio*0.95)){
                        stepBefore = stepBefore - (int) (tileLength*usingRatio);
                    }
                }
                backgroundPainting(canvas, left, top, right, bottom);

            } else {
                int piecesInRow = (int)(width/tileLength);
                double row = width/tileLength;
                int rowsOfTiles = (int)(length/tileWidth);
                double centeringStep = 0;
                if(centering && row-piecesInRow != 0.0){
                    centeringStep = (1.0 + row-piecesInRow)/2;
                }
                double rest = 0;
                int stepBefore = 0;
                for (int i = 0; i <= rowsOfTiles + 1; i++) {
                    for (int j = 0; j <= piecesInRow + 1; j++) {
                        if(j == 0){
                            stepBefore = stepBefore + (int) (step*tileLength*usingRatio);
                            if(i==0){
                                stepBefore =0;
                            }
                            int yPointOne = top + (int) (tileWidth*usingRatio*(i));
                            int xPointTwo = left + (int) ((tileLength*(j + 1 + centeringStep) + rest)*usingRatio) + stepBefore;
                            int yPointTwo = top + (int) (tileWidth*usingRatio*(i+1)) ;
                            Rect rect = new Rect(left, yPointOne, xPointTwo, yPointTwo);
                            canvas.drawRect(rect, p);
                        }
                        int xPointOne = left + (int) ((tileLength*(j + centeringStep) + rest)*usingRatio) + stepBefore;
                        int yPointOne = top + (int) (tileWidth*usingRatio*(i));
                        int xPointTwo = left + (int) ((tileLength*(j + 1 + centeringStep) + rest)*usingRatio) + stepBefore;
                        int yPointTwo  = top + (int) (tileWidth*usingRatio*(i+1));
                        Rect rect = new Rect(xPointOne, yPointOne, xPointTwo, yPointTwo);
                        canvas.drawRect(rect, p);
                        if(useRest && j==piecesInRow){
                            rest = ((int) ((width - rest)/tileLength)) * tileLength + rest - width;
                        }
                    }
                    if(stepBefore > (int) (tileLength*usingRatio*0.95)){
                        stepBefore = stepBefore - (int) (tileLength*usingRatio);
                    }
                }
                backgroundPainting(canvas, left, top, right, bottom);
            }
        }
        private void backgroundPainting(Canvas canvas, int left, int top, int right, int bottom){
            int deviceWidth = canvas.getWidth();
            int deviceHeight = canvas.getHeight();
            p.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, deviceWidth, top,  p);
            canvas.drawRect(0, 0, left, deviceHeight,  p);
            canvas.drawRect(right, 0, deviceWidth, deviceHeight,  p);
            canvas.drawRect(0, bottom, deviceWidth, deviceHeight,  p);
        }
    }
}