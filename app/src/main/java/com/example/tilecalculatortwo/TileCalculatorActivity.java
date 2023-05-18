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
    BigDecimal oneBD = new BigDecimal("1.0");
    BigDecimal halfOneBD = new BigDecimal("0.5");
    BigDecimal zeroBD = new BigDecimal("0");
    private boolean centering;
    private boolean usingRemnant;
    private boolean alongLongSide;
    private boolean isLaminate;
    private EditText areaLengthInsert;
    private EditText areaWidthInsert;
    private EditText tileLengthInsert;
    private EditText tileWidthInsert;
    private EditText offsetInsert;
    private TextView searchTilesResult;
    Switch centeringSwitch;
    Switch usingRemnantSwitch;
    Switch alongLongSideSwitch;
    Switch tileOrLaminateSwitch;

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
        tileOrLaminateSwitch = findViewById(R.id.TileOrLaminateSwitch);
        tileOrLaminateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isLaminate = b;
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
        BigDecimal lengthBD = BigDecimal.valueOf(dI.getLength());
        BigDecimal widthBD = BigDecimal.valueOf(dI.getWidth());
        BigDecimal tileLengthBD = BigDecimal.valueOf(dI.getTileLength());
        BigDecimal tileWidthBD = BigDecimal.valueOf(dI.getTileWidth());
        BigDecimal offsetBD = BigDecimal.valueOf(dI.getOffset());
        if(dI.isCentering()){
            return calculateCentering(lengthBD, widthBD, tileLengthBD, tileWidthBD);
        } else if(dI.isUsingRemnant()){
            return calculateUsingRemnant(lengthBD, widthBD, tileLengthBD, tileWidthBD);
        } else{
            return calculateUseOffset(lengthBD, widthBD, tileLengthBD, tileWidthBD, offsetBD);
        }
    }

    private long calculateCentering(BigDecimal lengthBD, BigDecimal widthBD,
                                      BigDecimal tileLengthBD, BigDecimal tileWidthBD){
        BigDecimal divisionL;
        BigDecimal divisionW;
        if(dI.isAlongLongSide()){
            divisionL = checkRoundingDivisionLength(lengthBD, tileLengthBD);
            divisionW = checkRoundingDivisionWidth(widthBD, tileWidthBD);
        } else{
            divisionL = checkRoundingDivisionLength(widthBD, tileLengthBD);
            divisionW = checkRoundingDivisionWidth(lengthBD, tileWidthBD);
        }
        return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
    }

    private long calculateUsingRemnant(BigDecimal lengthBD, BigDecimal widthBD,
                                         BigDecimal tileLengthBD, BigDecimal tileWidthBD){
        BigDecimal divisionL;
        BigDecimal divisionW;
        if(dI.isAlongLongSide()){
            divisionL = lengthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP);
            divisionW = checkRoundingDivisionWidth(widthBD, tileWidthBD);
        } else{
            divisionL = widthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP);
            divisionW = checkRoundingDivisionWidth(lengthBD, tileWidthBD);
        }
        return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
    }

    private long calculateUseOffset(BigDecimal lengthBD, BigDecimal widthBD,
                                    BigDecimal tileLengthBD, BigDecimal tileWidthBD, BigDecimal offsetBD){
        if(dI.isAlongLongSide()){
            BigDecimal divisionL = checkRoundingDivisionLength(lengthBD, tileLengthBD);
            BigDecimal divisionW = checkRoundingDivisionWidth(widthBD, tileWidthBD);
            return continueCalculateUseOffset(divisionL, divisionW, offsetBD, lengthBD, tileLengthBD);
        } else{
            BigDecimal divisionL = checkRoundingDivisionLength(widthBD, tileLengthBD);
            BigDecimal divisionW = checkRoundingDivisionWidth(lengthBD, tileWidthBD);
            return continueCalculateUseOffset(divisionL, divisionW, offsetBD, widthBD, tileLengthBD);
        }
    }

    private long continueCalculateUseOffset(BigDecimal divisionL, BigDecimal divisionW,
                                                   BigDecimal offsetBD, BigDecimal lengthBD, BigDecimal tileLengthBD){
        BigDecimal tileRest = divisionL.multiply(tileLengthBD)
                .subtract(lengthBD)
                .divide(tileLengthBD, 9, RoundingMode.HALF_UP);
        if (offsetBD.compareTo(zeroBD) == 0){
            if(!isLaminate) {
                boolean restMoorHalf = tileRest
                        .compareTo(halfOneBD) >= 0;
                if(restMoorHalf){
                    divisionL = divisionL.subtract(halfOneBD);
                }
            }
        } else {
            if (divisionL.multiply(tileLengthBD).compareTo(lengthBD) > 0) {
                if (offsetBD.compareTo(halfOneBD) > 0) {
                    offsetBD = oneBD.subtract(offsetBD);
                }
                if (tileRest.compareTo(offsetBD) >= 1) {
                    BigDecimal repeatRows = oneBD
                            .divide(offsetBD, 9, RoundingMode.HALF_UP);
                    BigDecimal repeat = divisionW.divide(repeatRows, 0, RoundingMode.HALF_UP);
                    if (repeat.compareTo(divisionW.divide(repeatRows, 9, RoundingMode.HALF_UP)) > 0) {
                        repeat = repeat.subtract(oneBD);
                    }
                    return divisionL.multiply(divisionW).subtract(repeat).setScale(0, RoundingMode.UP).longValue();
                }
            }
        }
        return divisionL.multiply(divisionW).setScale(0, RoundingMode.UP).longValue();
    }

    private BigDecimal checkRoundingDivisionWidth(BigDecimal widthBD, BigDecimal tileWidthBD){
        BigDecimal divisionW = widthBD.divide(tileWidthBD, 0, RoundingMode.HALF_UP);
        if(divisionW.compareTo(widthBD.divide(tileWidthBD, 9, RoundingMode.HALF_UP)) < 0){
            if(isLaminate){
                return divisionW.add(oneBD);
            } else {
                return divisionW.add(halfOneBD);
            }
        }
        return divisionW;
    }

    private BigDecimal checkRoundingDivisionLength(BigDecimal lengthBD, BigDecimal tileLengthBD){
        BigDecimal divisionL = lengthBD.divide(tileLengthBD, 0, RoundingMode.HALF_UP);
        if(divisionL.compareTo(lengthBD.divide(tileLengthBD, 9, RoundingMode.HALF_UP)) < 0){
            return divisionL.add(oneBD);
        }
        return divisionL;
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
                offset = 0.33334;
            } else if( i == 66){
                offset = 0.66667;
            } else {
                offset = i/100.0;
            }
        }
        return new DrawingInformation(areaLength, areaWidth,
                tileLength, tileWidth, offset, usingRemnant, centering, alongLongSide, isLaminate);
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
        return areaLength != 0 && areaWidth != 0 && tileLength != 0 && tileWidth != 0;
    }
}