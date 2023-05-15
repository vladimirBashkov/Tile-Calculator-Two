package com.example.tilecalculatortwo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
                BigDecimal lengthBD = new BigDecimal(length);
                BigDecimal widthBD = new BigDecimal(width);
                BigDecimal tileLengthBD = new BigDecimal(tileLength);
                BigDecimal tileWidthBD = new BigDecimal(tileWidth);
                BigDecimal rowsOfTilesBD = widthBD.divide(tileWidthBD, 0, RoundingMode.UP);
                BigDecimal tilesInRowBD = lengthBD.divide(tileLengthBD, 0, RoundingMode.HALF_UP);
                if(tilesInRowBD.compareTo(lengthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP))<0){
                    tilesInRowBD = tilesInRowBD.add(new BigDecimal("1"));
                }
                BigDecimal centeringStepBD = new BigDecimal("0");
                BigDecimal stepBD = new BigDecimal(step);
                BigDecimal usingRatioBD = new BigDecimal(usingRatio);
                if(centering){
                    centeringStepBD = lengthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP)
                            .subtract(tilesInRowBD.subtract(new BigDecimal("1")))
                            .add(new BigDecimal("1")).divide(new BigDecimal("2"));
                }
                BigDecimal restBD = new BigDecimal("0");
                BigDecimal stepBeforeBD = new BigDecimal("0");
                BigDecimal stepX = tileWidthBD.multiply(usingRatioBD);
                BigDecimal stepY = tileLengthBD.multiply(usingRatioBD);
                for (int i = 0; i < rowsOfTilesBD.intValue(); i++) {
                    stepBeforeBD = stepBeforeBD.add(stepBD);
                    if(i==0){
                        stepBeforeBD = new BigDecimal("0");
                    }
                    if (stepBeforeBD.multiply(tileLengthBD).compareTo(tileLengthBD) > 0){
                        stepBeforeBD = stepBeforeBD
                                .subtract(stepBeforeBD.divide(tileLengthBD, 0, RoundingMode.DOWN));
                    }
                    BigDecimal addForRest = centeringStepBD.add(restBD).add(stepBeforeBD);
                    for (int j = 0; j < tilesInRowBD.intValue(); j++) {
                        int xPointOne = right - stepX.multiply(new BigDecimal(i+1)).intValue();
                        int yPointOne = top + stepY
                                .multiply(addForRest.add(new BigDecimal(j))).intValue();
                        yPointOne = Math.min(yPointOne, bottom);
                        int xPointTwo = right - stepX.multiply(new BigDecimal(i)).intValue();
                        int yPointTwo  = top + stepY
                                .multiply(addForRest.add(new BigDecimal(j+1))).intValue();
                        yPointTwo = Math.min(yPointTwo, bottom);
                        Rect rect = new Rect(xPointOne, yPointOne, xPointTwo, yPointTwo);
                        canvas.drawRect(rect,  p);
                        if (j == 0){
                            int yPointOneRect = top + stepY
                                    .multiply(addForRest).intValue();
                            rect = new Rect(xPointOne, top, xPointTwo, yPointOneRect);
                            canvas.drawRect(rect,  p);
                        }
                    }
                    if(useRest){
                        BigDecimal restLengthBD = tileLengthBD.multiply(tilesInRowBD)
                                .add(restBD.multiply(tileLengthBD)).subtract(lengthBD);
                        if(restLengthBD.compareTo(tileLengthBD)>0){
                            restLengthBD = restLengthBD.subtract(tileLengthBD.multiply(restLengthBD.divide(tileLengthBD,0, RoundingMode.DOWN)));
                        }
                        restBD = restLengthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP);
                    }
                }
                backgroundPainting(canvas, left, top, right, bottom);

            } else {
                BigDecimal lengthBD = new BigDecimal(length);
                BigDecimal widthBD = new BigDecimal(width);
                BigDecimal tileLengthBD = new BigDecimal(tileLength);
                BigDecimal tileWidthBD = new BigDecimal(tileWidth);
                BigDecimal rowsOfTilesBD = lengthBD.divide(tileWidthBD, 0, RoundingMode.UP);
                BigDecimal tilesInRowBD = widthBD.divide(tileLengthBD, 0, RoundingMode.HALF_UP);
                if(tilesInRowBD.compareTo(widthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP))<0){
                    tilesInRowBD = tilesInRowBD.add(new BigDecimal("1"));
                }
                BigDecimal centeringStepBD = new BigDecimal("0");
                BigDecimal stepBD = new BigDecimal(step);
                BigDecimal usingRatioBD = new BigDecimal(usingRatio);
                if(centering){
                    centeringStepBD = widthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP)
                            .subtract(tilesInRowBD.subtract(new BigDecimal("1")))
                            .add(new BigDecimal("1")).divide(new BigDecimal("2"));
                }
                BigDecimal restBD = new BigDecimal("0");
                BigDecimal stepBeforeBD = new BigDecimal("0");
                BigDecimal stepX = tileLengthBD.multiply(usingRatioBD);
                BigDecimal stepY = tileWidthBD.multiply(usingRatioBD);
                for (int i = 0; i < rowsOfTilesBD.intValue(); i++) {
                    stepBeforeBD = stepBeforeBD.add(stepBD);
                    if(i==0){
                        stepBeforeBD = new BigDecimal("0");
                    }
                    if (stepBeforeBD.multiply(tileLengthBD).compareTo(tileLengthBD) > 0){
                        stepBeforeBD = stepBeforeBD
                                .subtract(stepBeforeBD.divide(tileLengthBD, 0, RoundingMode.DOWN));
                    }
                    BigDecimal addForRest = centeringStepBD.add(restBD).add(stepBeforeBD);
                    for (int j = 0; j < tilesInRowBD.intValue(); j++) {
                        int xPointOne = left + stepX.multiply(addForRest.add(new BigDecimal(j))).intValue();
                        int yPointOne = top + stepY
                                .multiply((new BigDecimal(i))).intValue();
                        xPointOne = Math.min(xPointOne, right);
                        int xPointTwo = left + stepX.multiply(addForRest.add(new BigDecimal(j+1))).intValue();
                        int yPointTwo  = top + stepY
                                .multiply(new BigDecimal(i+1)).intValue();
                        xPointTwo = Math.min(xPointTwo, bottom);
                        Rect rect = new Rect(xPointOne, yPointOne, xPointTwo, yPointTwo);
                        canvas.drawRect(rect,  p);
                        if (j == 0){
                            int xPointOneRect = left + stepX
                                    .multiply(addForRest).intValue();
                            rect = new Rect(left, yPointOne, xPointOneRect, yPointOne);
                            canvas.drawRect(rect,  p);
                        }
                    }
                    if(useRest){
                        BigDecimal restLengthBD = tileLengthBD.multiply(tilesInRowBD)
                                .add(restBD.multiply(tileLengthBD)).subtract(widthBD);
                        if(restLengthBD.compareTo(tileLengthBD)>0){
                            restLengthBD = restLengthBD.subtract(tileLengthBD.multiply(restLengthBD.divide(tileLengthBD,0, RoundingMode.DOWN)));
                        }
                        restBD = restLengthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP);
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