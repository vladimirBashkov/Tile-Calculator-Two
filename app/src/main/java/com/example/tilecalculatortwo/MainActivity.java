package com.example.tilecalculatortwo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<String> results = new ArrayList<>();
    Button calculate;
    Button history;
    EditText boxSquare;
    EditText tilesInBox;
    EditText searchingSquad;
    TextView result;
    TextView boxCount;
    TextView tileCount;
    TextView oldResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculate = findViewById(R.id.Calculate);
        history = findViewById(R.id.History);
        boxSquare = findViewById(R.id.BoxSquare);
        tilesInBox = findViewById(R.id.TilesInBox);
        searchingSquad = findViewById(R.id.SearchingSquare);
        result = findViewById(R.id.Result);
        oldResults = findViewById(R.id.OldResult);
        boxCount = findViewById(R.id.BoxCount);
        tileCount = findViewById(R.id.TileCount);
        calculate.setOnClickListener(this);
        history.setOnClickListener(this);
        history.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.Calculate){
            calculateSquare();
        }
        if(view.getId() == R.id.History){
            historyText();
        }

    }

    private void calculateSquare(){
        String box = boxSquare.getText().toString();
        String countTiles = tilesInBox.getText().toString();
        String search = searchingSquad.getText().toString();
        if(box.isEmpty() || countTiles.isEmpty() || search.isEmpty()){
            String allChars = box+countTiles+search;
            if(allChars.contains("[a-z]*[A-Z]*[А-Я]*[а-я]*")){
                return;
            }
            return;
        }
        int countTilesInt = Integer.parseInt(countTiles);
        BigDecimal boxB = new BigDecimal(box);
        BigDecimal countTilesB = new BigDecimal(countTiles);
        BigDecimal oneTile = boxB.divide(countTilesB, 20, RoundingMode.HALF_UP);
        BigDecimal searchB = new BigDecimal(search);
        int res = searchB.divide(oneTile, 1).intValue();
        double resD = searchB.divide(oneTile, 10, RoundingMode.HALF_UP).doubleValue();
        if(Double.compare(resD, Double.valueOf(res))!=0){
            res= res+1;
        }
        BigDecimal finB = oneTile.multiply(new BigDecimal(res));
        result.setText(finB.setScale(4,RoundingMode.HALF_UP).toString());
        int boxCountInt = res/countTilesInt;
        boxCount.setText(Integer.toString(boxCountInt));
        int tilesCountInt = res%countTilesInt;
        tileCount.setText(Integer.toString(tilesCountInt));
        results.add("В упаковке - " + box + "м2 - " +
                countTiles + "шт. S = " + search +
                "м2. Итог: " + finB.toString() + "\n");
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