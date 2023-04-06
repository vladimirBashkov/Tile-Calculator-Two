package com.example.tilecalculatortwo;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FillingData extends Activity {
    private static final String BUTTON_COLOR_CALCULATE_BY_M = "#8BC34A";
    private static final String BUTTON_COLOR_CALCULATE_BY_PIECES = "#FFEB3B";
    private static final String BUTTON_COLOR_CALCULATE_BY_PACK = "#999EFF";
    private final String SEPARATOR;
    private final Activity activity;
    private TreeMap<Integer, String> tilesMap;
    private final Spinner selectedSpinner;
    private final EditText searchingArticle;
    private final EditText searchingSquad;
    private final EditText searchingTiles;
    private final Calculator calculator;
    private final TextView infoAboutTile;
    private final EditText boxSquare;
    private final EditText tilesInBox;
    private final EditText searchingText;
    private final TextView tileSquare;
    private final TextView result;
    private final TextView packagingBox;
    private final TextView boxCount;
    private final TextView packInfo;
    private final TextView tileCount;
    private final TextView tilesInfoName;
    private final TextView history;


    public FillingData(Activity activity, TreeMap<Integer, String> tilesMap,
                       Calculator calculator, EditText searchingArticle,
                       EditText searchingSquad, EditText searchingTiles,
                       TextView infoAboutTile, EditText boxSquare,
                       EditText tilesInBox, String SEPARATOR,
                       EditText searchingText, Spinner selectedSpinner,
                       TextView tileSquare, TextView result,
                       TextView packagingBox, TextView boxCount,
                       TextView packInfo, TextView tileCount,
                       TextView tilesInfoName, TextView history) {
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
        this.searchingText = searchingText;
        this.selectedSpinner = selectedSpinner;
        this.tileSquare = tileSquare;
        this.result = result;
        this.packagingBox = packagingBox;
        this.boxCount = boxCount;
        this.packInfo = packInfo;
        this.tileCount = tileCount;
        this.tilesInfoName = tilesInfoName;
        this.history = history;
    }

    public void searchFromMap() {
        String article = searchingArticle.getText().toString().trim();
        if(article.length() > 0){
            int art = Integer.parseInt(article);
            if(tilesMap.containsKey(art)){
                selectedSpinner.setVisibility(View.INVISIBLE);
                infoAboutTile.setVisibility(View.VISIBLE);
                String info = tilesMap.get(art);
                setTileInfo(info);
            } else {
                Toast.makeText(activity, "Такого артикула нет в файле", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void searchByName(){
        String name = searchingText.getText().toString();
        int nameLength = name.length();
        if(nameLength > 0){
            ArrayList<String> selectedString = new ArrayList<>();
            ArrayList<String> tilesInfo = new ArrayList<>();
            tilesMap.values().forEach(text ->{
                Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(text);
                if(matcher.find()){
                    String[] info = text.split(SEPARATOR);
                    selectedString.add(info[0]);
                    tilesInfo.add(info[1] + SEPARATOR + info[2]);
                }
            });
            if (!selectedString.isEmpty()) {
                selectedSpinner.setVisibility(View.VISIBLE);
                infoAboutTile.setVisibility(View.INVISIBLE);
                ArrayAdapter<String> tilesTypeAdapter = new ArrayAdapter<>(activity,
                        android.R.layout.simple_spinner_item, selectedString);
                tilesTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectedSpinner.setAdapter(tilesTypeAdapter);
                selectedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            setInfoAboutTile(selectedString.get(i));
                            String[] info = tilesInfo.get(i).split(SEPARATOR);
                            String boxVolume = info[0];
                            String tilesCount = info[1];
                            setBoxSquareAndTilesInBox(boxVolume, tilesCount);
                        } catch (Exception exception){

                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            } else {
                Toast.makeText(activity, "По данному запросу " +
                        "результатов нет", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showHistory(){
        if(!calculator.getHistory().isEmpty()){
            ArrayList<String> historyList = calculator.getHistory();
            StringBuilder sb = new StringBuilder();
            if(historyList.size() > 5){
                for (int i = historyList.size()-1; i >= historyList.size()-5 ; i--) {
                    sb.append(historyList.get(i));
                    sb.append("\n");
                }
            } else {
                for (int i = historyList.size()-1; i >= 0; i--) {
                    sb.append(historyList.get(i));
                    sb.append("\n");
                }
            }
            history.setText(sb);
            showVisibleHistory(true);
        }

    }

    private void showVisibleHistory(Boolean isShow){
        if (isShow){
            tileSquare.setVisibility(View.INVISIBLE);
            result.setVisibility(View.INVISIBLE);
            packagingBox.setVisibility(View.INVISIBLE);
            boxCount.setVisibility(View.INVISIBLE);
            packInfo.setVisibility(View.INVISIBLE);
            tileCount.setVisibility(View.INVISIBLE);
            tilesInfoName.setVisibility(View.INVISIBLE);
            history.setVisibility(View.VISIBLE);
        } else {
            tileSquare.setVisibility(View.VISIBLE);
            result.setVisibility(View.VISIBLE);
            packagingBox.setVisibility(View.VISIBLE);
            boxCount.setVisibility(View.VISIBLE);
            packInfo.setVisibility(View.VISIBLE);
            tileCount.setVisibility(View.VISIBLE);
            tilesInfoName.setVisibility(View.VISIBLE);
            history.setVisibility(View.INVISIBLE);
        }

    }

    private void setTileInfo(String tileInfo){
        String[] splitInfo = tileInfo.split(SEPARATOR);
        String tileName = splitInfo[0];
        setInfoAboutTile(tileName);
        String boxVolume = splitInfo[1];
        String tilesCount = splitInfo[2];
        setBoxSquareAndTilesInBox(boxVolume, tilesCount);
    }

    private void setInfoAboutTile(String name){
        infoAboutTile.setText(name);
    }

    public void setBoxSquareAndTilesInBox(String boxVolume, String tilesCount){
        boxSquare.setText(boxVolume);
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
        showVisibleHistory(false);
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
        showVisibleHistory(false);
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
        showVisibleHistory(false);
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
