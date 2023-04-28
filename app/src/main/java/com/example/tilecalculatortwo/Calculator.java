package com.example.tilecalculatortwo;

import android.widget.EditText;
import android.widget.TextView;
import com.example.tilecalculatortwo.historypack.HistoryArray;
import com.example.tilecalculatortwo.historypack.HistoryEntry;
import com.example.tilecalculatortwo.historypack.SearchingType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Calculator {
    private final Integer FIRST_SCALE = 20;
    private final Integer FINAL_SCALE = 3;
    private final TextView result;
    private final TextView boxCount;
    private final TextView tileCount;
    private final TextView infoAboutTile;
    HistoryArray historyArray;
    private final String firstMessage;
    private final String messageByM;
    private final String messageByPackages;
    private final String messageByPieces;
    private final String secondMessage;
    private final String messagePack;
    private final String messagePcs;
    private final String messagePackByPack;
    DecimalFormat decimalFormat;

    public Calculator(TextView result, TextView boxCount,
                      TextView tileCount, TextView infoAboutTile,
                      HistoryArray historyArray, String firstMessage,
                      String messageByM, String messageByPackages,
                      String messageByPieces, String secondMessage,
                      String messagePack, String messagePcs, String messagePackByPack) {
        this.result = result;
        this.boxCount = boxCount;
        this.tileCount = tileCount;
        this.infoAboutTile = infoAboutTile;
        this.historyArray = historyArray;
        this.firstMessage = firstMessage;
        this.messageByM = messageByM;
        this.messageByPackages = messageByPackages;
        this.messageByPieces = messageByPieces;
        this.secondMessage = secondMessage;
        this.messagePack = messagePack;
        this.messagePcs = messagePcs;
        this.messagePackByPack = messagePackByPack;
        decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(0);
        decimalFormat.setMaximumFractionDigits(10);
    }

    public void calculateSquareByMeters(String box, int countTiles, String search, EditText searchingTiles){
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal countTilesB = new BigDecimal(countTiles);
        BigDecimal oneTile = boxB.divide(countTilesB, FIRST_SCALE, RoundingMode.HALF_UP);
        BigDecimal searchB = new BigDecimal(search);
        long res = searchB.divide(oneTile, FIRST_SCALE, RoundingMode.HALF_UP)
                .longValue();
        double resD = searchB.divide(oneTile, FIRST_SCALE, RoundingMode.HALF_UP)
                .doubleValue();
        if(Double.compare(resD, res)!=0){
            res= res+1;
        }
        searchingTiles.setText(Long.toString(res));
        BigDecimal finB = oneTile.multiply(new BigDecimal(res))
                .setScale(FINAL_SCALE,RoundingMode.HALF_UP);
        result.setText(decimalFormat.format(finB));
        int boxResult = Integer.parseInt(Long.toString(res))/countTiles;
        int tiles = Integer.parseInt(Long.toString(res))%countTiles;
        HistoryEntry entry = new HistoryEntry(firstMessage, messageByM,
                secondMessage, messagePack, messagePcs, SearchingType.BY_METER,
                readBoxName(), search, decimalFormat.format(finB), boxResult, tiles);
        historyArray.addEntry(entry);
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
        BigDecimal oneTile = boxB.divide(countTilesB, FIRST_SCALE, RoundingMode.HALF_UP);
        int searchingTiles = Integer.parseInt(search);
        BigDecimal finB = oneTile.multiply(new BigDecimal(searchingTiles))
                .setScale(FINAL_SCALE,RoundingMode.HALF_UP)
                .stripTrailingZeros();
        result.setText(decimalFormat.format(finB));
        setBoxInformation(searchingTiles, countTiles);
        int boxResult = searchingTiles/countTiles;
        int tiles = searchingTiles%countTiles;
        HistoryEntry entry = new HistoryEntry(firstMessage, messageByPieces,
                secondMessage, messagePack, messagePcs, SearchingType.BY_PIECES,
                readBoxName(), search, decimalFormat.format(finB), boxResult, tiles);
        historyArray.addEntry(entry);
    }

    public void calculateSquareByPack(String box, String search){
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal searchB = new BigDecimal(search);
        BigDecimal searchingBox = searchB.divide(boxB, FIRST_SCALE, RoundingMode.HALF_UP);
        long res = searchingBox.longValue();
        double resD = searchingBox.doubleValue();
        if(Double.compare(resD, res)!=0){
            res= res+1;
        }
        BigDecimal finB = boxB.multiply(new BigDecimal(res))
                .setScale(FINAL_SCALE,RoundingMode.HALF_UP)
                .stripTrailingZeros();
        result.setText(decimalFormat.format(finB));
        HistoryEntry entry = new HistoryEntry(firstMessage, messageByPackages,
                secondMessage, messagePackByPack, "", SearchingType.BY_METER_AND_BY_PACK,
                readBoxName(), search, decimalFormat.format(finB), res, 0);
        historyArray.addEntry(entry);
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
            boxCount.setText("0");
            tileCount.setText("0");
            return;
        }
        boxCount.setText(Integer.toString(boxCountInt));
        int tilesCountInt = allTiles%tilesInPack;
        tileCount.setText(Integer.toString(tilesCountInt));
    }

    private String readBoxName(){
        String name = infoAboutTile.getText().toString();
        infoAboutTile.setText("");
        return name;
    }
}
