package com.example.tilecalculatortwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TileCalculatorActivity extends AppCompatActivity implements View.OnClickListener{
    private boolean centering;
    private boolean usingRemnant;
    private boolean alongLongSide;
    private EditText areaLengthInsert;
    private EditText areaWidthInsert;
    private EditText tileLengthInsert;
    private EditText tileWidthInsert;
    private EditText offsetInsert;
    private TextView searchTilesResult;
    Switch centeringSwitch;
    Switch usingRemnantSwitch;
    Switch alongLongSideSwitch;

    DrawingInformation dI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_calculator);
        areaLengthInsert = findViewById(R.id.AreaLengthInsert);
        areaWidthInsert = findViewById(R.id.AreaWidthInsert);
        tileLengthInsert = findViewById(R.id.TileLengthInsert);
        tileWidthInsert = findViewById(R.id.TileWidthInsert);
        offsetInsert = findViewById(R.id.OffsetInsert);
        searchTilesResult = findViewById(R.id.SearchTilesResult);
        centeringSwitch = findViewById(R.id.CenteringSwitch);
        centeringSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    centering = true;
                    usingRemnantSwitch.setChecked(false);
                    usingRemnant = false;
                    offsetInsert.setVisibility(View.INVISIBLE);
                    offsetInsert.setText("");
                } else{
                    centering = false;
                    if(!usingRemnant){
                        offsetInsert.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        usingRemnantSwitch = findViewById(R.id.UsingRemnantSwitch);
        usingRemnantSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    usingRemnant = true;
                    centering = false;
                    centeringSwitch.setChecked(false);
                    offsetInsert.setVisibility(View.INVISIBLE);
                    offsetInsert.setText("");
                } else{
                    usingRemnant = false;
                    if(!centering){
                        offsetInsert.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        alongLongSideSwitch = findViewById(R.id.AlongLongSideSwitch);
        alongLongSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                alongLongSide = b;
            }
        });
        Button searchTilesByAreaButton = findViewById(R.id.SearchTilesByAreaButton);
        searchTilesByAreaButton.setOnClickListener(this);
        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(this);
        Button showPlanButton = findViewById(R.id.ShowPlanButton);
        showPlanButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.BackButton){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if(view.getId() == R.id.ShowPlanButton){
            dI = getDI();
            if(dI != null){
                Intent intent = new Intent(this, DrawActivity.class);
                intent.putExtra(DrawingInformation.class.getSimpleName(), dI);
                startActivity(intent);
            }
        }
        if(view.getId() == R.id.SearchTilesByAreaButton){
            dI = getDI();
            if(dI != null){
                long r = calculatePieces(dI);
                if(r != 0){
                    String res = String.valueOf(r);
                    searchTilesResult.setText(res);
                } else{
                    searchTilesResult.setText(R.string.UnknownData);
                }
            }
        }
    }

    private long calculatePieces(DrawingInformation dI) {
        double length = dI.getLength();
        double width = dI.getWidth();
        double tileLength = dI.getTileLength();
        double tileWidth = dI.getTileWidth();
        double offset = dI.getOffset();
        if(dI.isCentering()){
            if(dI.isAlongLongSide()){
                BigDecimal lengthBD = new BigDecimal(length);
                BigDecimal widthBD = new BigDecimal(width);
                BigDecimal tileLengthBD = new BigDecimal(tileLength);
                BigDecimal tileWidthBD = new BigDecimal(tileWidth);
                BigDecimal divisionL = lengthBD.divide(tileLengthBD, 0, RoundingMode.HALF_DOWN);
                BigDecimal divisionW = widthBD.divide(tileWidthBD, 0, RoundingMode.HALF_DOWN);

                if(divisionL.compareTo(lengthBD.divide(tileLengthBD, 9, RoundingMode.HALF_DOWN)) < 0){
                    divisionL = divisionL.add(new BigDecimal("1.0"));
                }
                if(divisionW.compareTo(widthBD.divide(tileWidthBD, 9, RoundingMode.HALF_DOWN)) < 0){
                    divisionW = divisionW.add(new BigDecimal("1.0"));
                }
                return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
            } else{
                BigDecimal lengthBD = new BigDecimal(length);
                BigDecimal widthBD = new BigDecimal(width);
                BigDecimal tileLengthBD = new BigDecimal(tileLength);
                BigDecimal tileWidthBD = new BigDecimal(tileWidth);
                BigDecimal divisionL = widthBD.divide(tileLengthBD, 0,RoundingMode.HALF_UP);
                BigDecimal divisionW = lengthBD.divide(tileWidthBD, 0, RoundingMode.HALF_UP);
                if(divisionL.compareTo(widthBD.divide(tileLengthBD, 9, RoundingMode.HALF_DOWN)) < 0){
                    divisionL = divisionL.add(new BigDecimal("1.0"));
                }
                if(divisionW.compareTo(lengthBD.divide(tileWidthBD, 9, RoundingMode.HALF_DOWN)) < 0){
                    divisionW = divisionW.add(new BigDecimal("1.0"));
                }
                return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
            }
        } else if(dI.isUsingRemnant()){
            if(dI.isAlongLongSide()){
                BigDecimal lengthBD = new BigDecimal(length);
                BigDecimal widthBD = new BigDecimal(width);
                BigDecimal tileLengthBD = new BigDecimal(tileLength);
                BigDecimal tileWidthBD = new BigDecimal(tileWidth);
                BigDecimal divisionL = lengthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP);
                BigDecimal divisionW = widthBD.divide(tileWidthBD, 0, RoundingMode.HALF_UP);
                if(divisionW.compareTo(widthBD.divide(tileWidthBD, 9, RoundingMode.HALF_UP)) < 0){
                    divisionW = divisionW.add(new BigDecimal("1.0"));
                }
                return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
            } else{
                BigDecimal lengthBD = new BigDecimal(length);
                BigDecimal widthBD = new BigDecimal(width);
                BigDecimal tileLengthBD = new BigDecimal(tileLength);
                BigDecimal tileWidthBD = new BigDecimal(tileWidth);
                BigDecimal divisionL = widthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP);
                BigDecimal divisionW = lengthBD.divide(tileWidthBD, 0, RoundingMode.HALF_UP);
                if(divisionW.compareTo(lengthBD.divide(tileWidthBD, 9, RoundingMode.HALF_DOWN)) < 0){
                    divisionW = divisionW.add(new BigDecimal("1.0"));
                }
                return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
            }
        } else{
            if(dI.isAlongLongSide()){
                BigDecimal offsetBD = new BigDecimal(offset);
                BigDecimal lengthBD = new BigDecimal(length);
                BigDecimal widthBD = new BigDecimal(width);
                BigDecimal tileLengthBD = new BigDecimal(tileLength);
                BigDecimal tileWidthBD = new BigDecimal(tileWidth);
                BigDecimal divisionL = lengthBD.divide(tileLengthBD, 0, RoundingMode.HALF_UP);
                BigDecimal divisionW = widthBD.divide(tileWidthBD, 0, RoundingMode.HALF_UP);
                if (offsetBD.compareTo(new BigDecimal("0")) == 0){
                    if(divisionL.compareTo(lengthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP)) < 0){
                        divisionL = divisionL.add(new BigDecimal("0.5"));
                    }
                    if(divisionW.compareTo(widthBD.divide(tileWidthBD, 9, RoundingMode.HALF_DOWN)) < 0){
                        divisionW = divisionW.add(new BigDecimal("1.0"));
                    }
                    return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
                } else {
                    if(divisionL.compareTo(lengthBD.divide(tileLengthBD, 3, RoundingMode.HALF_UP)) < 0){
                        divisionL = divisionL.add(new BigDecimal("1.0"));
                    }
                    if(divisionW.compareTo(widthBD.divide(tileWidthBD, 3, RoundingMode.HALF_UP)) < 0){
                        divisionW = divisionW.add(new BigDecimal("1.0"));
                    }
                    if(divisionL.multiply(tileLengthBD).compareTo(lengthBD) <= 0 ){
                        return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
                    } else{
                        BigDecimal tileRest = (divisionL.multiply(tileLengthBD).subtract(lengthBD))
                                .divide(tileLengthBD, 9, RoundingMode.HALF_UP);
                        if(offsetBD.compareTo(new BigDecimal("0.5")) > 0){
                            offsetBD = new BigDecimal("1").subtract(offsetBD);
                        }
                        if(tileRest.compareTo(offsetBD) >= 1){
                            BigDecimal repeatRows = new BigDecimal("1")
                                    .divide(offsetBD, 9, RoundingMode.HALF_UP);
                            BigDecimal repeat = divisionW.divide(repeatRows, 0, RoundingMode.HALF_UP);
                            if(repeat.compareTo(divisionW.divide(repeatRows, 0, RoundingMode.HALF_UP)) > 0){
                                repeat = repeat.subtract(new BigDecimal("1.0"));
                            }
                            return divisionL.multiply(divisionW).subtract(repeat).setScale(0, RoundingMode.UP).longValue();
                        }
                        return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
                    }
                }
            } else{
                BigDecimal offsetBD = new BigDecimal(offset);
                BigDecimal lengthBD = new BigDecimal(length);
                BigDecimal widthBD = new BigDecimal(width);
                BigDecimal tileLengthBD = new BigDecimal(tileLength);
                BigDecimal tileWidthBD = new BigDecimal(tileWidth);
                BigDecimal divisionL = widthBD.divide(tileLengthBD, 0,RoundingMode.HALF_DOWN);
                BigDecimal divisionW = lengthBD.divide(tileWidthBD, 0, RoundingMode.HALF_UP);
                if (offsetBD.compareTo(new BigDecimal("0")) == 0){
                    if(divisionL.compareTo(widthBD.divide(tileLengthBD, 9, RoundingMode.HALF_DOWN)) < 0){
                        divisionL = divisionL.add(new BigDecimal("0.5"));
                    }
                    if(divisionW.compareTo(lengthBD.divide(tileWidthBD, 9, RoundingMode.HALF_DOWN)) < 0){
                        divisionW = divisionW.add(new BigDecimal("1.0"));
                    }
                    return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
                } else{
                    if(divisionL.compareTo(widthBD.divide(tileLengthBD, 3, RoundingMode.HALF_DOWN)) < 0){
                        divisionL = divisionL.add(new BigDecimal("1.0"));
                    }
                    if(divisionW.compareTo(lengthBD.divide(tileWidthBD, 3, RoundingMode.HALF_DOWN)) < 0){
                        divisionW = divisionW.add(new BigDecimal("1.0"));
                    }
                    if(divisionL.multiply(tileLengthBD).compareTo(widthBD) <= 0 ){
                        return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
                    } else{
                        BigDecimal tileRest = divisionL.multiply(tileLengthBD).subtract(widthBD)
                                .divide(tileLengthBD, 9, RoundingMode.HALF_UP);
                        if(offsetBD.compareTo(new BigDecimal("0.5")) > 0){
                            offsetBD = new BigDecimal("1").subtract(offsetBD);
                        }
                        if(tileRest.compareTo(offsetBD) >= 1){
                            BigDecimal repeatRows = new BigDecimal("1")
                                    .divide(offsetBD, 9, RoundingMode.HALF_UP);
                            BigDecimal repeat = divisionW.divide(repeatRows, 0, RoundingMode.HALF_UP);
                            if(repeat.compareTo(divisionW.divide(repeatRows, 0, RoundingMode.HALF_UP)) > 0){
                                repeat = repeat.subtract(new BigDecimal("1.0"));
                            }
                            return divisionL.multiply(divisionW).subtract(repeat).setScale(0, RoundingMode.UP).longValue();
                        }
                        return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
                    }
                }
            }
        }
    }

    private DrawingInformation getDI(){
        String areaLengthS = areaLengthInsert.getText().toString().trim();
        String areaWidthS = areaWidthInsert.getText().toString().trim();
        String tileLengthS = tileLengthInsert.getText().toString().trim();
        String tileWidthS = tileWidthInsert.getText().toString().trim();
        String offsetS = offsetInsert.getText().toString().trim();
        if(!checkDrowningInformation(areaLengthS, areaWidthS, tileLengthS, tileWidthS)){
            Toast.makeText(this, getResources().getString(R.string.InvalidData), Toast.LENGTH_SHORT).show();
            return null;
        }
        double areaLength = Double.parseDouble(areaLengthS);
        double areaWidth = Double.parseDouble(areaWidthS);
        if(areaLength < areaWidth){
            double revers = areaLength;
            areaLength = areaWidth;
            areaWidth = revers;
        }
        double tileLength = Double.parseDouble(tileLengthS)/100;
        double tileWidth = Double.parseDouble(tileWidthS)/100;
        if(tileLength < tileWidth){
            double revers = tileLength;
            tileLength = tileWidth;
            tileWidth = revers;
        }
        double offset;
        if(offsetS.isEmpty()){
            offset = 0;
        } else {
            int i = Integer.parseInt(offsetS);
            if( i == 33){
                offset = 0.334;
            } else if( i == 66){
                offset = 0.667;
            } else {
                offset = i/100.0;
            }
        }
        return new DrawingInformation(areaLength, areaWidth,
                tileLength, tileWidth, offset, usingRemnant, centering, alongLongSide);
    }

    private boolean checkDrowningInformation(String areaLengthS, String areaWidthS,
                                             String tileLengthS, String tileWidthS) {
        if (areaLengthS.isEmpty() || areaWidthS.isEmpty() || tileLengthS.isEmpty() || tileWidthS.isEmpty()){
            return false;
        }
        double areaLength = Double.parseDouble(areaLengthS);
        double areaWidth = Double.parseDouble(areaWidthS);
        double tileLength = Double.parseDouble(tileLengthS);
        double tileWidth = Double.parseDouble(tileWidthS);
        if (areaLength == 0 ||  areaWidth == 0 || tileLength == 0 || tileWidth == 0){
            return false;
        }
        return true;
    }
}