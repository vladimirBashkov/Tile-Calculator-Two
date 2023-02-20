package com.example.tilecalculatortwo;

import android.widget.TextView;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {
    TextView result;
    TextView boxCount;
    TextView tileCount;
    TextView packInfo;
    TextView tilesInfo;

    Calculator(TextView result, TextView boxCount, TextView tileCount,
               TextView packInfo, TextView tilesInfo){
        this.result = result;
        this.boxCount = boxCount;
        this.tileCount = tileCount;
        this.packInfo = packInfo;
        this.tilesInfo = tilesInfo;
    }

    public String calculateSquareByMeters(String box, String countTiles, String search){
        int countTilesInt = Integer.parseInt(countTiles);
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal countTilesB = new BigDecimal(countTiles);
        BigDecimal oneTile = boxB.divide(countTilesB, 20, RoundingMode.HALF_UP);
        BigDecimal searchB = new BigDecimal(search);
        long res = searchB.divide(oneTile, 20, RoundingMode.HALF_UP)
                .longValue();
        double resD = searchB.divide(oneTile, 20, RoundingMode.HALF_UP)
                .doubleValue();
        if(Double.compare(resD, Double.valueOf(res))!=0){
            res= res+1;
        }
        BigDecimal finB = oneTile.multiply(new BigDecimal(res))
                .setScale(4,RoundingMode.HALF_UP)
                .stripTrailingZeros();
        result.setText(finB.toString());
        if(res > 99999){
            setBoxInformation(100000, 1);
        } else {
            int resI = Integer.parseInt(Long.toString(res));
            setBoxInformation(resI, 1);
        }
        return finB.toString();
    }

    public String calculateSquareByTiles(String box, String countTiles, String search){
        int countTilesInt = Integer.parseInt(countTiles);
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal countTilesB = new BigDecimal(countTiles);
        BigDecimal oneTile = boxB.divide(countTilesB, 20, RoundingMode.HALF_UP);
        int searchingTiles = Integer.parseInt(search);
        BigDecimal finB = oneTile.multiply(new BigDecimal(searchingTiles))
                .setScale(4,RoundingMode.HALF_UP)
                .stripTrailingZeros();
        result.setText(finB.toString());
        setBoxInformation(searchingTiles, countTilesInt);
        return finB.toString();
    }

    public String calculateSquareByPack(String box, String search){
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal searchB = new BigDecimal(search);
        BigDecimal searchingBox = searchB.divide(boxB, 20, RoundingMode.HALF_UP);
        long res = searchingBox.intValue();
        double resD = searchingBox.doubleValue();
        if(Double.compare(resD, Double.valueOf(res))!=0){
            res= res+1;
        }
        BigDecimal finB = boxB.multiply(new BigDecimal(res))
                .setScale(4,RoundingMode.HALF_UP)
                .stripTrailingZeros();
        result.setText(finB.toString());
        if(res > 99999){
            setBoxInformation(100000, 1);
        } else {
            int resI = Integer.parseInt(Long.toString(res));
            setBoxInformation(resI, 1);
        }
        return finB.toString();
    }

    private void setBoxInformation(int allTiles, int tilesInPack){
        int boxCountInt = allTiles/tilesInPack;
        if(boxCountInt > 99999 || boxCountInt < 0){
            boxCount.setText("МНОГО");
            packInfo.setText("УПАКОВОК");
            tileCount.setText("0");
            tilesInfo.setText("ШТУК");
            return;
        }
        boxCount.setText(boxCountInt);
        int tilesCountInt = allTiles%tilesInPack;
        if (boxCountInt%100 >= 11 && boxCountInt%100 <= 19) {
            packInfo.setText("УПАКОВОК");
        } else {
            if (boxCountInt%10 == 1){
                packInfo.setText("УПАКОВКА");
            } else if (boxCountInt%10 > 1 && boxCountInt%10 <= 4){
                packInfo.setText("УПАКОВКИ");
            } else {
                packInfo.setText("УПАКОВОК");
            }
        }
        tileCount.setText(tilesCountInt);
        if (tilesCountInt%100 >= 11 && tilesCountInt%100 <= 19) {
            tilesInfo.setText("ШТУК");
        } else {
            if (tilesCountInt%10 == 1){
                tilesInfo.setText("ШТУКА");
            } else if (tilesCountInt%10 > 1 && tilesCountInt%10 <= 4){
                tilesInfo.setText("ШТУКИ");
            } else {
                tilesInfo.setText("ШТУК");
            }
        }
    }
}
