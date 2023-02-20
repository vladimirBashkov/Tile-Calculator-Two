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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Calculator calculator;
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
        calculator = new Calculator(result, boxCount, tileCount, packInfo, tilesInfo);

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
        results.add("В упаковке - " + box + "м2 - " +
                countTiles + "шт. S = " + search + "м2. Итог: " +
                calculator.calculateSquareByMeters(box, countTiles, search) + "\n");
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
        results.add("В упаковке - " + box + "м2 - " +
                countTiles + "шт. нужно штук: " + search + "шт. Итог: " +
                calculator.calculateSquareByTiles(box, countTiles, search) + "\n");
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
        results.add("В упаковке - " + box + "м2. S = " + search + "м2. Итог: " +
                calculator.calculateSquareByPack(box, search) + "\n");
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
            oldResults.setText("История: \n" + history);
        }
    }
}