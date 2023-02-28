package com.example.tilecalculatortwo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Calculator calculator;
    private ArrayList<String> tilesType = new ArrayList<>();
    private ArrayList<String> tilesTypeOptions = new ArrayList<>();
    private ArrayList<String> cersanitType = new ArrayList<>();
    private ArrayList<String> cersanitTypeOptions = new ArrayList<>();
    private ArrayList<String> globaltileTiles = new ArrayList<>();
    private ArrayList<String> globaltileTilesOptions = new ArrayList<>();
    private ArrayList<String> almaCeramicaTiles = new ArrayList<>();
    private ArrayList<String> almaCeramicaTilesOption = new ArrayList<>();
    private ArrayList<String> results = new ArrayList<>();
    private Button calculateByM;
    private Button calculateByTiles;
    private Button calculateByPack;
    private Button history;
    private EditText boxSquare;
    private EditText tilesInBox;
    private EditText searchingSquad;
    private EditText searchingTiles;
    private TextView result;
    private TextView packInfo;
    private TextView tilesInfo;
    private TextView boxCount;
    private TextView tileCount;
    private TextView oldResults;

    Spinner spinner;
    Spinner cersanitSp;
    Spinner globaltileSp;
    Spinner almaCeramicaSp;



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

        addNullableValue(tilesType, "--Не выбрано--", tilesTypeOptions);
        addNullableValue(tilesType, "Cersanit", tilesTypeOptions);
        addNullableValue(tilesType, "Global Tile", tilesTypeOptions);
        addNullableValue(tilesType, "Alma Ceramica", tilesTypeOptions);
        addNullableValue(cersanitType, "Cersanit", cersanitTypeOptions);
        addNullableValue(globaltileTiles, "Global Tile", globaltileTilesOptions);
        addNullableValue(almaCeramicaTiles, "Alma Ceramica", almaCeramicaTilesOption);

        readTilesType(MainActivity.this);

        ArrayAdapter<String> tilesTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tilesType);
        tilesTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) findViewById(R.id.spTilesType);
        spinner.setAdapter(tilesTypeAdapter);

        ArrayAdapter<String> tilesTypeAdapterCersanit = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cersanitType);
        tilesTypeAdapterCersanit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cersanitSp = (Spinner) findViewById(R.id.CersanitSp);
        cersanitSp.setAdapter(tilesTypeAdapterCersanit);

        ArrayAdapter<String> tilesTypeAdapterGlobaltile = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, globaltileTiles);
        tilesTypeAdapterGlobaltile.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        globaltileSp = (Spinner) findViewById(R.id.GlobalTileSp);
        globaltileSp.setAdapter(tilesTypeAdapterGlobaltile);

        ArrayAdapter<String> tilesTypeAdapterAlmaCeramica = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, almaCeramicaTiles);
        tilesTypeAdapterAlmaCeramica.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        almaCeramicaSp = (Spinner) findViewById(R.id.AlmaCeramicaSp);
        almaCeramicaSp.setAdapter(tilesTypeAdapterAlmaCeramica);

        cersanitSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String infoAboutTile = cersanitTypeOptions.get(i);
                    setTileInfo(infoAboutTile);
                } catch (Exception exception){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        globaltileSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String infoAboutTile = globaltileTilesOptions.get(i);
                    setTileInfo(infoAboutTile);
                } catch (Exception exception){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        almaCeramicaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String infoAboutTile = almaCeramicaTilesOption.get(i);
                    setTileInfo(infoAboutTile);
                } catch (Exception exception){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if(i>0 && i<4){
                        if(i == 1){
                            cersanitSp.setVisibility(View.VISIBLE);
                            globaltileSp.setVisibility(View.INVISIBLE);
                            almaCeramicaSp.setVisibility(View.INVISIBLE);
                        }else if(i == 2){
                            cersanitSp.setVisibility(View.INVISIBLE);
                            globaltileSp.setVisibility(View.VISIBLE);
                            almaCeramicaSp.setVisibility(View.INVISIBLE);
                        } else{
                            cersanitSp.setVisibility(View.INVISIBLE);
                            globaltileSp.setVisibility(View.INVISIBLE);
                            almaCeramicaSp.setVisibility(View.VISIBLE);
                        }
                    } else {
                        cersanitSp.setVisibility(View.INVISIBLE);
                        globaltileSp.setVisibility(View.INVISIBLE);
                        almaCeramicaSp.setVisibility(View.INVISIBLE);
                        String infoAboutTile = tilesTypeOptions.get(i);
                        setTileInfo(infoAboutTile);
                    }
                } catch (Exception exception){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

    private void readTilesType(Activity activity){
        try {
            Resources r = activity.getResources();
            InputStream is = r.openRawResource(R.raw.tilestype);
            String value = convertStreamToString(is);
            is.close();
            InputStream isCers = r.openRawResource(R.raw.cersanit);
            String valueCers = convertStreamToString(isCers);
            isCers.close();
            InputStream isGT = r.openRawResource(R.raw.globaltile);
            String valueGT = convertStreamToString(isGT);
            isGT.close();
            InputStream isAC = r.openRawResource(R.raw.almaceramica);
            String valueAC = convertStreamToString(isAC);
            isAC.close();
            writeTilesInfo(value, tilesType, tilesTypeOptions);
            writeTilesInfo(valueCers, cersanitType, cersanitTypeOptions);
            writeTilesInfo(valueGT, globaltileTiles, globaltileTilesOptions);
            writeTilesInfo(valueAC, almaCeramicaTiles, almaCeramicaTilesOption);
        } catch (Exception e) {

        }
    }

    private void addNullableValue(ArrayList<String> tilesType, String name,
                                  ArrayList<String> tilesTypeOptions ) {
        tilesType.add(name);
        tilesTypeOptions.add("0-0");
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

    private void writeTilesInfo(String value, ArrayList<String> listToWrite,
                                ArrayList<String> optionList){
        String[] tilesInfo = value.split("\n");
        for (int i = 0; i < tilesInfo.length; i++) {
            if(tilesInfo[i].contains("-")){
                String[] tilesInfoString = tilesInfo[i].split("-");
                listToWrite.add(tilesInfoString[0].trim().replace(",", "."));
                optionList.add(tilesInfoString[1].trim().replace(",", ".") +
                        " - " + tilesInfoString[2].trim());
            }
        }
    }

    private void setTileInfo(String tileInfo){
        String[] splitInfo = tileInfo.split("-");
        String tilesVolume = splitInfo[0].trim();
        String tilesCount = splitInfo[1].trim();
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