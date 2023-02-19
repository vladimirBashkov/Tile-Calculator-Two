package com.example.tilecalculatortwo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<String> results = new ArrayList<>();
    Button calculateByM;
    Button calculateByTiles;
    Button calculateByPack;
    Button history;
    EditText boxSquare;
    EditText tilesInBox;
    EditText searchingSquad;
    EditText searchingTiles;
    TextView result;
    TextView packInfo;
    TextView tilesInfo;
    TextView boxCount;
    TextView tileCount;
    TextView oldResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculateByM = findViewById(R.id.CalculateByM);
        calculateByTiles = findViewById(R.id.CalculateByPieces);
        calculateByPack = findViewById(R.id.CalculateByPack);
        history = findViewById(R.id.History);
        boxSquare = findViewById(R.id.BoxSquare);
        tilesInBox = findViewById(R.id.TilesInBox);
        searchingSquad = findViewById(R.id.SearchingSquare);
        searchingTiles = findViewById(R.id.SearchingTiles);
        result = findViewById(R.id.Result);
        oldResults = findViewById(R.id.OldResult);
        boxCount = findViewById(R.id.BoxCount);
        packInfo = findViewById(R.id.PackInfo);
        tileCount = findViewById(R.id.TileCount);
        tilesInfo = findViewById(R.id.TilesInfo);
        calculateByM.setOnClickListener(this);
        history.setOnClickListener(this);
        history.setMovementMethod(new ScrollingMovementMethod());

        calculateByTiles.setOnClickListener(this);
        calculateByM.setOnClickListener(this);
        calculateByPack.setOnClickListener(this);
        history.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.CalculateByM){
            calculateSquareByM();
        }
        if(view.getId() == R.id.CalculateByPieces){
            calculateSquareByTiles();
        }
        if(view.getId() == R.id.CalculateByPack){
            calculateSquareByPack();
        }
        if(view.getId() == R.id.History){
            historyText();
        }
    }

    private void calculateSquareByM(){
        setBackgroundEditText(Color.parseColor("#75EC7A"),
                Color.parseColor("#75EC7A"),
                Color.parseColor("#75EC7A"),0);
        if(!checkData(searchingSquad)){
            return;
        }
        String box = boxSquare.getText().toString();
        String countTiles = tilesInBox.getText().toString();
        String search = searchingSquad.getText().toString();
        searchingTiles.setText("");
        int countTilesInt = Integer.parseInt(countTiles);
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal countTilesB = new BigDecimal(countTiles);
        BigDecimal oneTile = boxB.divide(countTilesB, 20, RoundingMode.HALF_UP);
        BigDecimal searchB = new BigDecimal(search);
        int res = searchB.divide(oneTile, 20, RoundingMode.HALF_UP)
                .intValue();
        double resD = searchB.divide(oneTile, 20, RoundingMode.HALF_UP)
                .doubleValue();
        if(Double.compare(resD, Double.valueOf(res))!=0){
            res= res+1;
        }
        BigDecimal finB = oneTile.multiply(new BigDecimal(res));
        result.setText(finB.setScale(4,RoundingMode.HALF_UP).toString());
        setBoxInformation(res, countTilesInt);
        results.add("В упаковке - " + box + "м2 - " +
                countTiles + "шт. S = " + search +
                "м2. Итог: " + finB.toString() + "\n");
    }

    private void calculateSquareByTiles(){
        setBackgroundEditText(Color.parseColor("#FBEB59"),
                Color.parseColor("#FBEB59"),
                0,Color.parseColor("#FBEB59"));
        if(!checkData(searchingTiles)){
            return;
        }
        String box = boxSquare.getText().toString();
        String countTiles = tilesInBox.getText().toString();
        String search = searchingTiles.getText().toString();
        searchingSquad.setText("");
        int countTilesInt = Integer.parseInt(countTiles);
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal countTilesB = new BigDecimal(countTiles);
        BigDecimal oneTile = boxB.divide(countTilesB, 20, RoundingMode.HALF_UP);
        int searchingTiles = Integer.parseInt(search);
        BigDecimal finB = oneTile.multiply(new BigDecimal(searchingTiles));
        result.setText(finB.setScale(4,RoundingMode.HALF_UP).toString());
        setBoxInformation(searchingTiles, countTilesInt);
        results.add("В упаковке - " + box + "м2 - " +
                countTiles + "шт. нужно штук: " + search +
                "шт. Итог: " + finB.toString() + "\n");
    }

    private void calculateSquareByPack(){
        setBackgroundEditText(Color.parseColor("#79B9EC"), 0,
                Color.parseColor("#79B9EC"),0);
        if(!checkBoxAndSquare(searchingSquad)){
            return;
        }
        String box = boxSquare.getText().toString();
        String search = searchingSquad.getText().toString();
        searchingTiles.setText("");
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal searchB = new BigDecimal(search);
        BigDecimal searchingBox = searchB.divide(boxB, 20, RoundingMode.HALF_UP);
        int res = searchingBox.intValue();
        double resD = searchingBox.doubleValue();
        if(Double.compare(resD, Double.valueOf(res))!=0){
            res= res+1;
        }
        BigDecimal finB = boxB.multiply(new BigDecimal(res));
        result.setText(finB.setScale(4,RoundingMode.HALF_UP).toString());
        setBoxInformation(res, 1);
        results.add("В упаковке - " + box + "м2. S = " +
                search + "м2. Итог: " + finB + "\n");
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
        return true;
    }

    private void setBoxInformation(int allTiles, int tilesInPack){
        int boxCountInt = allTiles/tilesInPack;
        boxCount.setText(Integer.toString(boxCountInt));
        int tilesCountInt = allTiles%tilesInPack;
        if (boxCountInt%100 >= 11 && boxCountInt%100 <= 19) {
            packInfo.setText("УПАКОВОК");
        } else {
            if (boxCountInt%10 == 1){
                packInfo.setText("УПАКОВКА");
            } else if (boxCountInt%10 > 1 && boxCountInt%10 <= 4){
                packInfo.setText("УПАКОВКИ");
            } else if ((boxCountInt%10 > 4) ||
                    boxCountInt%10 == 0){
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
            } else if ((tilesCountInt%10 > 4) ||
                    tilesCountInt%10 == 0){
                tilesInfo.setText("ШТУК");
            }
        }
    }

    private void historyText(){
        StringBuilder history = new StringBuilder();
        if(!results.isEmpty()){
            if(results.size()>3){
                for (int i = results.size()-1; i > results.size() -4; i--) {
                    history.append(results.get(i));
                }
            }else {
                for (String text : results) {
                    history.append(text);
                }
            }
        }
        oldResults.setText("История: \n" + history);
    }
}