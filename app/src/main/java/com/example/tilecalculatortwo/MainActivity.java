package com.example.tilecalculatortwo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
    private static final String LOGS_FILE = "TCTLogs.txt";
    String SEPARATOR = "~";
    private TreeMap<Integer, String> tilesMap = new TreeMap<>();
    private FillingData fillingData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        Button searchFromTreeMap = findViewById(R.id.SearchFromTreeMap);
        Button searchByName = findViewById(R.id.SearchByName);
        Button calculateByM = findViewById(R.id.CalculateByM);
        Button calculateByTiles = findViewById(R.id.CalculateByPieces);
        Button calculateByPack = findViewById(R.id.CalculateByPack);
        Button download = findViewById(R.id.DownloadButton);
        Button showHistory = findViewById(R.id.ShowHistory);
        EditText searchingArticle = findViewById(R.id.SearchingArticle);
        EditText searchingText = findViewById(R.id.SearchingText);
        EditText boxSquare = findViewById(R.id.BoxSquare);
        EditText tilesInBox = findViewById(R.id.TilesInBox);
        EditText searchingSquad = findViewById(R.id.SearchingSquare);
        EditText searchingTiles = findViewById(R.id.SearchingTiles);
        TextView infoAboutTile = findViewById(R.id.InfoAboutTile);
        TextView result = findViewById(R.id.Result);
        TextView boxCount = findViewById(R.id.BoxCount);
        TextView packInfo = findViewById(R.id.PackInfo);
        TextView tileCount = findViewById(R.id.TileCount);
        TextView tilesInfoName = findViewById(R.id.TilesInfoName);
        Spinner selectedSpinner = findViewById(R.id.SelectedSpinner);
        TextView tileSquare = findViewById(R.id.TileSquare);
        TextView packagingBox = findViewById(R.id.PackagingBox);
        TextView history = findViewById(R.id.History);

        Calculator calculator = new Calculator(result, boxCount, tileCount,
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
        } else {
            Toast.makeText(this, "Нет файла для чтения данных", Toast.LENGTH_SHORT).show();
        }
    }

    private String openText(){
        FileInputStream fin = null;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            return new String (bytes);
        } catch(IOException ex) {
            writeLog(ex.getMessage());
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
        int newStrings = 0;
        boolean mapIsEmpty = tilesMap.isEmpty();
        for (String s : tilesInfo) {
            if (s.contains(SEPARATOR)) {
                String[] tilesInfoString = s.split(SEPARATOR);
                int article = Integer.parseInt(tilesInfoString[0].trim());
                String tileInfo = tilesInfoString[1].trim() + SEPARATOR +
                        tilesInfoString[2].trim().replace(",", ".") +
                        SEPARATOR + tilesInfoString[3].trim();
                if (!tilesMap.containsKey(article)) {
                    newStrings++;
                    tilesMap.put(article, tileInfo);
                } else {
                    String oldValue = tilesMap.replace(article, tileInfo);
                    writeLog(article + " replaced " + oldValue + " -> " + tileInfo + "\n");
                }
            }
        }
        if(!mapIsEmpty){
            writeLog("При добавлении новых данных: \n новых артикулов: " + newStrings + "\n");
        }
    }

    private void readFile(){
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
                String content;
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    for (String line; (line = r.readLine()) != null; ) {
                        total.append(line).append('\n');
                    }
                    content = total.toString();
                    writeTilesInfoToMap(content);
                    saveMapToTxt();
                } catch (Exception e) {
                    writeLog(e.getMessage());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveMapToTxt(){
        String text;
        StringBuilder sb = new StringBuilder();
        tilesMap.keySet().forEach(key ->{
            String value = tilesMap.get(key);
            sb.append(key);
            sb.append(SEPARATOR);
            sb.append(value);
            sb.append("\n");
        });
        text = sb.toString();
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
            Toast.makeText(this, "Данные добавлены", Toast.LENGTH_SHORT).show();
            fos.close();
        } catch(IOException ex) {
            writeLog(ex.getMessage());
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try{
                if(fos!=null)
                    fos.close();
            } catch(IOException ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
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