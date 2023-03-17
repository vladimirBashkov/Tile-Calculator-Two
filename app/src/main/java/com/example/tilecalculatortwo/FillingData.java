package com.example.tilecalculatortwo;

import android.app.Activity;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.TreeMap;

public class FillingData extends Activity {
    String BUTTON_COLOR_CALCULATE_BY_M = "#75EC7A";
    String BUTTON_COLOR_CALCULATE_BY_PIECES = "#FBEB59";
    String BUTTON_COLOR_CALCULATE_BY_PACK = "#79B9EC";
    String SEPARATOR;
    Activity activity;
    private TreeMap<Integer, String> tilesMap;
    private EditText searchingArticle;
    private EditText searchingSquad;
    private EditText searchingTiles;
    private Calculator calculator;
    private TextView infoAboutTile;
    private EditText boxSquare;
    private EditText tilesInBox;

    public FillingData(Activity activity, TreeMap<Integer, String> tilesMap,
                       Calculator calculator, EditText searchingArticle,
                       EditText searchingSquad, EditText searchingTiles,
                       TextView infoAboutTile, EditText boxSquare,
                       EditText tilesInBox, String SEPARATOR) {
        this.activity = activity;
        this.tilesMap = tilesMap;
        this.searchingArticle = searchingArticle;
        this.searchingSquad = searchingSquad;
        this.searchingTiles = searchingTiles;
        this.calculator = calculator;
        this.infoAboutTile = infoAboutTile;
        this.boxSquare = boxSquare;
        this.tilesInBox = tilesInBox;
        this.SEPARATOR = SEPARATOR;
    }

    public void searchFromMap() {
        String article = searchingArticle.getText().toString().trim();
        if(article.length() > 0){
            int art = Integer.parseInt(article);
            if(tilesMap.containsKey(art)){
                String info = tilesMap.get(art);
                setTileInfo(info);
            } else {
                Toast.makeText(activity, "Такого артикула нет в файле", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setTileInfo(String tileInfo){
        String[] splitInfo = tileInfo.split(SEPARATOR);
        String tileName = splitInfo[0];
        infoAboutTile.setText(tileName);
        String tilesVolume = splitInfo[1];
        String tilesCount = splitInfo[2];
        boxSquare.setText(tilesVolume);
        tilesInBox.setText(tilesCount);
    }

    public void calculateSquareByM(){
        int buttonColor = Color.parseColor(BUTTON_COLOR_CALCULATE_BY_M);
        setBackgroundEditText(buttonColor,
                buttonColor, buttonColor,0);
        if(!checkData(searchingSquad)){
            return;
        }
        String box = boxSquare.getText().toString();
        int countTiles = Integer.parseInt(tilesInBox.getText().toString());
        String search = searchingSquad.getText().toString();
        calculator.calculateSquareByMeters(box, countTiles, search, searchingTiles);
    }

    public void calculateSquareByPieces(){
        int buttonColor = Color.parseColor(BUTTON_COLOR_CALCULATE_BY_PIECES);
        setBackgroundEditText(buttonColor, buttonColor,
                0,buttonColor);
        if(!checkData(searchingTiles)){
            return;
        }
        String box = boxSquare.getText().toString();
        int countTiles = Integer.parseInt(tilesInBox.getText().toString());
        String search = searchingTiles.getText().toString();
        searchingSquad.setText("");
        calculator.calculateSquareByTiles(box, countTiles, search);
    }

    public void calculateSquareByPack(){
        int buttonColor = Color.parseColor(BUTTON_COLOR_CALCULATE_BY_PACK);
        setBackgroundEditText(buttonColor, 0,
                buttonColor,0);
        if(!checkBoxAndSquare(searchingSquad)){
            return;
        }
        String box = boxSquare.getText().toString();
        String search = searchingSquad.getText().toString();
        searchingTiles.setText("");
        calculator.calculateSquareByPack(box, search);
    }

    private void setBackgroundEditText(int boxSq, int tilesInB,
                                       int searchingSq, int searchingTi){
        boxSquare.setBackgroundColor(boxSq);
        tilesInBox.setBackgroundColor(tilesInB);
        searchingSquad.setBackgroundColor(searchingSq);
        searchingTiles.setBackgroundColor(searchingTi);
    }

    private boolean checkData(EditText editText){
        String countTiles = tilesInBox.getText().toString();
        if(!checkBoxAndSquare(editText)){
            return false;
        } else if(countTiles.isEmpty()){
            Toast.makeText(activity, "Не все поля заполненны, " +
                    "расчет невозможен", Toast.LENGTH_LONG).show();
            return false;
        }
        int countT = Integer.parseInt(countTiles);
        if (countT == 0){
            Toast.makeText(activity, "В заводской пачке" +
                    " не бывает 0 плиток!!!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkBoxAndSquare(EditText editText){
        String box = boxSquare.getText().toString();
        String search = editText.getText().toString();
        if(box.isEmpty() || search.isEmpty()){
            Toast.makeText(activity, "Не все поля заполненны, " +
                    "расчет невозможен", Toast.LENGTH_LONG).show();
            return false;
        }
        double boxCheck = Double.parseDouble(box);
        if(Double.compare(boxCheck, 0D) == 0){
            Toast.makeText(activity, "В заводской пачке" +
                    " не бывает 0 м2!!!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void setTilesMap(TreeMap<Integer, String> tilesMap) {
        this.tilesMap = tilesMap;
    }
}
