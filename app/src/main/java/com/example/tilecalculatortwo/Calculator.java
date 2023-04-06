package com.example.tilecalculatortwo;

import android.widget.EditText;
import android.widget.TextView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Calculator {
    Integer SCALE = 6;
    private final TextView result;
    private final TextView boxCount;
    private final TextView tileCount;
    private final TextView packInfo;
    private final TextView tilesInfo;
    private final TextView infoAboutTile;
    ArrayList<String> history = new ArrayList<>();

    Calculator(TextView result, TextView boxCount, TextView tileCount,
               TextView packInfo, TextView tilesInfo, TextView infoAboutTile){
        this.result = result;
        this.boxCount = boxCount;
        this.tileCount = tileCount;
        this.packInfo = packInfo;
        this.tilesInfo = tilesInfo;
        this.infoAboutTile = infoAboutTile;
    }

    public void calculateSquareByMeters(String box, int countTiles, String search, EditText searchingTiles){
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal countTilesB = new BigDecimal(countTiles);
        BigDecimal oneTile = boxB.divide(countTilesB, 20, RoundingMode.HALF_UP);
        BigDecimal searchB = new BigDecimal(search);
        long res = searchB.divide(oneTile, 20, RoundingMode.HALF_UP)
                .longValue();
        double resD = searchB.divide(oneTile, 20, RoundingMode.HALF_UP)
                .doubleValue();
        if(Double.compare(resD, res)!=0){
            res= res+1;
        }
        searchingTiles.setText(Long.toString(res));
        BigDecimal finB = oneTile.multiply(new BigDecimal(res))
                .setScale(SCALE,RoundingMode.HALF_UP)
                .stripTrailingZeros();
        result.setText(finB.toString());
        int boxResult = Integer.parseInt(Long.toString(res))/countTiles;
        int tiles = Integer.parseInt(Long.toString(res))%countTiles;
        String name = infoAboutTile.getText().toString();
        history.add(name + ". Для " + search + " м2., необходимо " + finB +
                " м2. Это " + boxResult + " уп. " + tiles + " шт.");
        if(res/countTiles > 99999){
            setBoxInformation(100000, 1);
        } else {
            int resI = Integer.parseInt(Long.toString(res));
            setBoxInformation(resI, countTiles);
        }
    }

    public void calculateSquareByTiles(String box, int countTiles, String search){
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal countTilesB = new BigDecimal(countTiles);
        BigDecimal oneTile = boxB.divide(countTilesB, 20, RoundingMode.HALF_UP);
        int searchingTiles = Integer.parseInt(search);
        BigDecimal finB = oneTile.multiply(new BigDecimal(searchingTiles))
                .setScale(SCALE,RoundingMode.HALF_UP)
                .stripTrailingZeros();
        result.setText(finB.toString());
        setBoxInformation(searchingTiles, countTiles);
        int boxResult = searchingTiles/countTiles;
        int tiles = searchingTiles%countTiles;
        String name = infoAboutTile.getText().toString();
        history.add(name + ". Для " + search + " шт., необходимо " + finB +
                " м2. Это " + boxResult + " уп. " + tiles + " шт.");
    }

    public void calculateSquareByPack(String box, String search){
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal searchB = new BigDecimal(search);
        BigDecimal searchingBox = searchB.divide(boxB, 20, RoundingMode.HALF_UP);
        long res = searchingBox.longValue();
        double resD = searchingBox.doubleValue();
        if(Double.compare(resD, res)!=0){
            res= res+1;
        }
        BigDecimal finB = boxB.multiply(new BigDecimal(res))
                .setScale(SCALE,RoundingMode.HALF_UP)
                .stripTrailingZeros();
        result.setText(finB.toString());
        String name = infoAboutTile.getText().toString();
        history.add(name + ". Для " + search + " м2 кратно пачкам, необходимо " + finB +
                " м2. Это " + res + " уп.");
        if(res > 99999){
            setBoxInformation(100000, 1);
        } else {
            int resI = Integer.parseInt(Long.toString(res));
            setBoxInformation(resI, 1);
        }
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
        boxCount.setText(Integer.toString(boxCountInt));
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
        tileCount.setText(Integer.toString(tilesCountInt));
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

    public ArrayList<String> getHistory() {
        return history;
    }
}
