package com.example.tilecalculatortwo;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String separator = "-";
    private Calculator calculator;
    private TreeMap<Integer, String> tilesMap = new TreeMap<Integer, String>();
    private Button searchFromTreeMap;
    private Button calculateByM;
    private Button calculateByTiles;
    private Button calculateByPack;
    private EditText searchingArticle;
    private EditText boxSquare;
    private EditText tilesInBox;
    private EditText searchingSquad;
    private EditText searchingTiles;
    private TextView infoAboutTile;
    private TextView result;
    private TextView packInfo;
    private TextView tilesInfo;
    private TextView boxCount;
    private TextView tileCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchFromTreeMap = findViewById(R.id.SearchFromTreeMap);
        calculateByM = findViewById(R.id.CalculateByM);
        calculateByTiles = findViewById(R.id.CalculateByPieces);
        calculateByPack = findViewById(R.id.CalculateByPack);
        searchingArticle = findViewById(R.id.SearchingArticle);
        boxSquare = findViewById(R.id.BoxSquare);
        tilesInBox = findViewById(R.id.TilesInBox);
        searchingSquad = findViewById(R.id.SearchingSquare);
        searchingTiles = findViewById(R.id.SearchingTiles);
        infoAboutTile = findViewById(R.id.InfoAboutTile);
        result = findViewById(R.id.Result);
        boxCount = findViewById(R.id.BoxCount);
        packInfo = findViewById(R.id.PackInfo);
        tileCount = findViewById(R.id.TileCount);
        tilesInfo = findViewById(R.id.TilesInfo);
        calculateByM.setOnClickListener(this);

        calculator = new Calculator(result, boxCount, tileCount, packInfo, tilesInfo);

        readTilesType();

        searchFromTreeMap.setOnClickListener(this);
        calculateByTiles.setOnClickListener(this);
        calculateByM.setOnClickListener(this);
        calculateByPack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.SearchFromTreeMap){
            searchFromMap();
        }
        if(view.getId() == R.id.CalculateByM){
            calculateSquareByM();
        }
        if(view.getId() == R.id.CalculateByPieces){
            calculateSquareByTiles();
        }
        if(view.getId() == R.id.CalculateByPack){
            calculateSquareByPack();
        }
    }

    private void searchFromMap() {
        String article = searchingArticle.getText().toString().trim();
        if(article.length() == 8){
            int art = Integer.parseInt(article);
            if(tilesMap.containsKey(art)){
                String info = tilesMap.get(art);
                setTileInfo(info);
            }
        }
    }

    private void readTilesType(){
        try {
            InputStream is = getResources().openRawResource(R.raw.tilestype);
            String value = convertStreamToString(is);
            is.close();
            writeTilesInfo(value);
        } catch (Exception e) {

        }
    }

    private String  convertStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = is.read();
        while( i != -1)
        {
            baos.write(i);
            i = is.read();
        }
        return  baos.toString();
    }

    private void writeTilesInfo(String value){
        String[] tilesInfo = value.split("\n");
        for (int i = 0; i < tilesInfo.length; i++) {
            if(tilesInfo[i].contains(separator)){
                String[] tilesInfoString = tilesInfo[i].split(separator);
                int article = Integer.parseInt(tilesInfoString[0].trim());
                String tileInfo = tilesInfoString[1].trim() + separator +
                        tilesInfoString[2].trim().replace(",", ".") +
                        separator + tilesInfoString[3].trim();
                tilesMap.put(article, tileInfo);
            }
        }
    }

    private void setTileInfo(String tileInfo){
        String[] splitInfo = tileInfo.split("-");
        String tileName = splitInfo[0];
        infoAboutTile.setText(tileName);
        String tilesVolume = splitInfo[1];
        String tilesCount = splitInfo[2];
        boxSquare.setText(tilesVolume);
        tilesInBox.setText(tilesCount);
    }

    private void calculateSquareByM(){
        int buttonColor = Color.parseColor("#75EC7A");
        setBackgroundEditText(buttonColor,
                buttonColor, buttonColor,0);
        if(!checkData(searchingSquad)){
            return;
        }
        String box = boxSquare.getText().toString();
        int countTiles = Integer.parseInt(tilesInBox.getText().toString());
        String search = searchingSquad.getText().toString();
        searchingTiles.setText("");
        calculator.calculateSquareByMeters(box, countTiles, search);
    }

    private void calculateSquareByTiles(){
        int buttonColor = Color.parseColor("#FBEB59");
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

    private void calculateSquareByPack(){
        int buttonColor = Color.parseColor("#79B9EC");
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
            Toast.makeText(getApplicationContext(), "Не все поля заполненны, " +
                        "расчет невозможен", Toast.LENGTH_LONG).show();
            return false;
        }
        int countT = Integer.parseInt(countTiles);
        if (countT == 0){
            Toast.makeText(getApplicationContext(), "В заводской пачке" +
                    " не бывает 0 плиток!!!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkBoxAndSquare(EditText editText){
        String box = boxSquare.getText().toString();
        String search = editText.getText().toString();
        if(box.isEmpty() || search.isEmpty()){
            Toast.makeText(getApplicationContext(), "Не все поля заполненны, " +
                    "расчет невозможен", Toast.LENGTH_LONG).show();
            return false;
        }
        double boxCheck = Double.parseDouble(box);
        if(Double.compare(boxCheck, 0D) == 0){
            Toast.makeText(getApplicationContext(), "В заводской пачке" +
                    " не бывает 0 м2!!!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}