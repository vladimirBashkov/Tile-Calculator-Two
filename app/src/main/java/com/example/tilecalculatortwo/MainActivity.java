package com.example.tilecalculatortwo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tilecalculatortwo.db.Box;
import com.example.tilecalculatortwo.db.DatabaseAdapter;
import com.example.tilecalculatortwo.historypack.HistoryAndAdd;
import com.example.tilecalculatortwo.historypack.HistoryArray;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String SEPARATOR = "~";
    private static final String LOGS_FILE = "TCTLogs.txt";
    private static final String BUTTON_COLOR_CALCULATE_BY_M = "#8BC34A";
    private static final String BUTTON_COLOR_CALCULATE_BY_PIECES = "#FFEB3B";
    private static final String BUTTON_COLOR_CALCULATE_BY_PACK = "#999EFF";
    private Spinner selectedSpinner;
    private EditText searchingArticle;
    private EditText searchingSquad;
    private EditText searchingTiles;
    private TextView infoAboutTile;
    private EditText boxSquare;
    private EditText tilesInBox;
    private EditText searchingText;
    Calculator calculator;
    HistoryArray historyArray;
    private DatabaseAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        adapter = new DatabaseAdapter(this);
        searchingArticle = findViewById(R.id.SearchingArticle);
        searchingText = findViewById(R.id.SearchingText);
        boxSquare = findViewById(R.id.BoxSquare);
        tilesInBox = findViewById(R.id.TilesInBox);
        searchingSquad = findViewById(R.id.SearchingSquare);
        searchingTiles = findViewById(R.id.SearchingTiles);
        selectedSpinner = findViewById(R.id.SelectedSpinner);
        historyArray = new HistoryArray();
        infoAboutTile = findViewById(R.id.InfoAboutTile);
        TextView result = findViewById(R.id.Result);
        TextView boxCount = findViewById(R.id.BoxCount);
        TextView tileCount = findViewById(R.id.TileCount);
        String firstMessage = getResources().getString(R.string.HistoryMessageFirst);
        String messageByM = getResources().getString(R.string.HistoryMessageByM);
        String messageByPackages = getResources().getString(R.string.HistoryMessageByPackages);
        String messageByPieces = getResources().getString(R.string.HistoryMessageByPieces);
        String secondMessage = getResources().getString(R.string.HistoryMessageSecond);
        String messagePack = getResources().getString(R.string.HistoryMessagePack);
        String messagePcs = getResources().getString(R.string.HistoryMessagePcs);
        String messagePackByPack = getResources().getString(R.string.HistoryMessagePackByPack);
        calculator = new Calculator(result, boxCount, tileCount, infoAboutTile,
                historyArray, firstMessage, messageByM, messageByPackages,
                messageByPieces, secondMessage, messagePack, messagePcs,
                messagePackByPack);
        Button searchFromTreeMap = findViewById(R.id.SearchFromTreeMap);
        Button searchByName = findViewById(R.id.SearchByName);
        Button calculateByM = findViewById(R.id.CalculateByM);
        Button calculateByTiles = findViewById(R.id.CalculateByPieces);
        Button calculateByPack = findViewById(R.id.CalculateByPack);
        Button showHistory = findViewById(R.id.ShowHistory);
        searchFromTreeMap.setOnClickListener(this);
        searchByName.setOnClickListener(this);
        calculateByTiles.setOnClickListener(this);
        calculateByM.setOnClickListener(this);
        calculateByPack.setOnClickListener(this);
        showHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.SearchFromTreeMap){
            searchFromDbByArticle();
        }
        if(view.getId() == R.id.SearchByName){
            searchByName();
        }
        if(view.getId() == R.id.CalculateByM){
            calculateSquareByM();
        }
        if(view.getId() == R.id.CalculateByPieces){
            calculateSquareByPieces();
        }
        if(view.getId() == R.id.CalculateByPack){
            calculateSquareByPack();
        }
        if(view.getId() == R.id.ShowHistory){
            Intent intent = new Intent(this, HistoryAndAdd.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra(HistoryArray.class.getSimpleName(), historyArray);
            startActivity(intent);
        }
    }

    public void searchFromDbByArticle() {
        String article = searchingArticle.getText().toString().trim();
        if(article.length() > 0){
            int art = Integer.parseInt(article);
            adapter.open();
            if(adapter.boxIsExists(art)){
                Box box = adapter.getBox(art);
                selectedSpinner.setVisibility(View.INVISIBLE);
                infoAboutTile.setVisibility(View.VISIBLE);
                infoAboutTile.setText(box.getName());
                boxSquare.setText(Double.toString(box.getVolume()));
                tilesInBox.setText(Integer.toString(box.getPiecesInPack()));
            } else {
                Toast.makeText(this, getResources().getString(R.string.EmptySearchByArticleMessage), Toast.LENGTH_SHORT).show();
            }
            adapter.close();
        }
    }

    public void searchByName(){
        String name = searchingText.getText().toString();
        int nameLength = name.length();
        if(nameLength > 0){
            adapter.open();
            ArrayList<Box> boxList = adapter.getBox(name);
            if(boxList.size() > 0){
                ArrayList<String> selectedString = new ArrayList<>();
                ArrayList<String> tilesInfo = new ArrayList<>();
                for (int i = 0; i < boxList.size(); i++) {
                    Box box = boxList.get(i);
                    selectedString.add(box.getName());
                    tilesInfo.add(box.getVolume() + SEPARATOR + box.getPiecesInPack());
                }
                if (!selectedString.isEmpty()) {
                    setSpinner(selectedString, tilesInfo);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.EmptySearchByNameMessage), Toast.LENGTH_LONG).show();
                }
            }
            adapter.close();
        }
    }

    private void setSpinner(ArrayList<String> selectedString, ArrayList<String> tilesInfo){
        selectedSpinner.setVisibility(View.VISIBLE);
        infoAboutTile.setVisibility(View.INVISIBLE);
        ArrayAdapter<String> tilesTypeAdapter = new ArrayAdapter<>(this,
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
                } catch (Exception ex){
                    writeLog(ex.getMessage());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
            Toast.makeText(this, getResources().getString(R.string.EmptyFieldsMessage),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        int countT = Integer.parseInt(countTiles);
        if (countT == 0){
            Toast.makeText(this, getResources().getString(R.string.ZeroValueInPiecesMessage),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkBoxAndSquare(EditText editText){
        String box = boxSquare.getText().toString();
        String search = editText.getText().toString();
        if(box.isEmpty() || search.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.EmptyFieldsMessage),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        double boxCheck = Double.parseDouble(box);
        if(Double.compare(boxCheck, 0D) == 0){
            Toast.makeText(this, getResources().getString(R.string.ZeroValueInVolumeMessage),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void writeLog(String text){
        try {
            File logFile = new File(getExternalFilesDir(null), LOGS_FILE);
            if(logFile.exists()){
                FileOutputStream fos = new FileOutputStream(logFile, true);
                fos.write(text.getBytes());
            } else {
                FileOutputStream fos = new FileOutputStream(logFile);
                fos.write(text.getBytes());
            }
        } catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}