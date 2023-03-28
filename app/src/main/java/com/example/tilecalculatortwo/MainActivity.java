package com.example.tilecalculatortwo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int READ_REQUEST_CODE = 42;
    private static final String FILE_NAME = "tilestype.txt";
    String SEPARATOR = "~";
    private Calculator calculator;
    private TreeMap<Integer, String> tilesMap = new TreeMap<Integer, String>();
    private FillingData fillingData;
    private Button searchFromTreeMap;
    private Button searchByName;
    private Button calculateByM;
    private Button calculateByTiles;
    private Button calculateByPack;
    private Button download;
    private Button showHistory;
    private EditText searchingArticle;
    private EditText searchingText;
    private EditText boxSquare;
    private EditText tilesInBox;
    private EditText searchingSquad;
    private EditText searchingTiles;
    private TextView infoAboutTile;
    private TextView result;
    private TextView packInfo;
    private TextView tilesInfoName;
    private TextView boxCount;
    private TextView tileCount;
    private Spinner selectedSpinner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        searchFromTreeMap = findViewById(R.id.SearchFromTreeMap);
        searchByName = findViewById(R.id.SearchByName);
        calculateByM = findViewById(R.id.CalculateByM);
        calculateByTiles = findViewById(R.id.CalculateByPieces);
        calculateByPack = findViewById(R.id.CalculateByPack);
        download = findViewById(R.id.DownloadButton);
        showHistory = findViewById(R.id.ShowHistory);
        searchingArticle = findViewById(R.id.SearchingArticle);
        searchingText = findViewById(R.id.SearchingText);
        boxSquare = findViewById(R.id.BoxSquare);
        tilesInBox = findViewById(R.id.TilesInBox);
        searchingSquad = findViewById(R.id.SearchingSquare);
        searchingTiles = findViewById(R.id.SearchingTiles);
        infoAboutTile = findViewById(R.id.InfoAboutTile);
        result = findViewById(R.id.Result);
        boxCount = findViewById(R.id.BoxCount);
        packInfo = findViewById(R.id.PackInfo);
        tileCount = findViewById(R.id.TileCount);
        tilesInfoName = findViewById(R.id.TilesInfoName);
        selectedSpinner = findViewById(R.id.SelectedSpinner);
        TextView tileSquare = findViewById(R.id.TileSquare);
        TextView packagingBox = findViewById(R.id.PackagingBox);
        TextView history = findViewById(R.id.History);
        calculator = new Calculator(result, boxCount, tileCount,
                packInfo, tilesInfoName, infoAboutTile);
        fillingData = new FillingData(MainActivity.this, tilesMap, calculator,
                searchingArticle, searchingSquad,searchingTiles, infoAboutTile,
                boxSquare, tilesInBox, SEPARATOR, searchingText, selectedSpinner,
                tileSquare, result, packagingBox, boxCount, packInfo,
                tileCount, tilesInfoName, history);

        readTilesType();

        searchFromTreeMap.setOnClickListener(this);
        searchByName.setOnClickListener(this);
        calculateByTiles.setOnClickListener(this);
        calculateByM.setOnClickListener(this);
        calculateByPack.setOnClickListener(this);
        download.setOnClickListener(this);
        showHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.SearchFromTreeMap){
            fillingData.searchFromMap();
        }
        if(view.getId() == R.id.SearchByName){
            fillingData.searchByName();
        }
        if(view.getId() == R.id.CalculateByM){
            fillingData.calculateSquareByM();
        }
        if(view.getId() == R.id.CalculateByPieces){
            fillingData.calculateSquareByPieces();
        }
        if(view.getId() == R.id.CalculateByPack){
            fillingData.calculateSquareByPack();
        }
        if(view.getId() == R.id.DownloadButton){
           readFile();
        }
        if(view.getId() == R.id.ShowHistory){
            fillingData.showHistory();
        }
    }

    private void readTilesType(){
        File file = new File(getApplicationContext().getFilesDir(),FILE_NAME);
        if(file.exists()){
            writeTilesInfoToMap(openText());
            fillingData.setTilesMap(tilesMap);
        } else {
            Toast.makeText(this, "Нет файла для чтения данных", Toast.LENGTH_SHORT).show();
        }
    }

    public String openText(){
        FileInputStream fin = null;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String (bytes);
            return text;
        } catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return "";
    }

    private void writeTilesInfoToMap(String value){
        String[] tilesInfo = value.split("\n");
        for (int i = 0; i < tilesInfo.length; i++) {
            if(tilesInfo[i].contains(SEPARATOR)){
                String[] tilesInfoString = tilesInfo[i].split(SEPARATOR);
                int article = Integer.parseInt(tilesInfoString[0].trim());
                String tileInfo = tilesInfoString[1].trim() + SEPARATOR +
                        tilesInfoString[2].trim().replace(",", ".") +
                        SEPARATOR + tilesInfoString[3].trim();
                tilesMap.put(article, tileInfo);
            }
        }
    }

    public void readFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(data != null){
                Uri uri = data.getData();
                String content = "";
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    for (String line; (line = r.readLine()) != null; ) {
                        total.append(line).append('\n');
                    }
                    content = total.toString();
                    saveText(content);
                } catch (Exception e) {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveText(String text){
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
            readTilesType();
        } catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally{
            try{
                if(fos!=null)
                    fos.close();
            } catch(IOException ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}