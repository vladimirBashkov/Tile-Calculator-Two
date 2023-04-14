package com.example.tilecalculatortwo.historypack;

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
import android.widget.TextView;
import android.widget.Toast;
import com.example.tilecalculatortwo.MainActivity;
import com.example.tilecalculatortwo.R;
import com.example.tilecalculatortwo.db.Box;
import com.example.tilecalculatortwo.db.DatabaseAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HistoryAndAdd extends AppCompatActivity  implements View.OnClickListener{
    String SEPARATOR = "~";
    private static final int READ_REQUEST_CODE = 42;
    TextView articleTextView;
    TextView descriptionTextView;
    TextView boxVolumeTextView;
    TextView tilesCounterTextView;
    EditText articleAdd;
    EditText descriptionAdd;
    EditText boxVolumeAdd;
    EditText tilesCounterAdd;
    TextView historyText;
    private static final String FILE_NAME = "tilesType.txt";
    private static final String LOGS_FILE = "TCTLogs.txt";
    HistoryArray historyArray;
    private DatabaseAdapter adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_and_add);
        Bundle arguments = getIntent().getExtras();
        HistoryArray history;
        if(arguments!=null){
            history = (HistoryArray) arguments.getSerializable(HistoryArray.class.getSimpleName());
            historyArray = history;
        }
        ActivityCompat.requestPermissions(HistoryAndAdd.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        adapter = new DatabaseAdapter(this);
        historyText = findViewById(R.id.HistoryText);
        articleTextView = findViewById(R.id.ArticleTextView);
        descriptionTextView = findViewById(R.id.DescriptionTextView);
        boxVolumeTextView = findViewById(R.id.BoxVolumeTextView);
        tilesCounterTextView = findViewById(R.id.TilesCounterTextView);
        articleAdd = findViewById(R.id.ArticleAdd);
        descriptionAdd = findViewById(R.id.DescriptionAdd);
        boxVolumeAdd = findViewById(R.id.BoxVolumeAdd);
        tilesCounterAdd = findViewById(R.id.TilesCounterAdd);
        Button backToMainScreenButton = findViewById(R.id.BackToMainScreen);
        Button showHistoryButton = findViewById(R.id.ShowHistoryButton);
        Button addArticleButton = findViewById(R.id.AddArticleButton);
        Button download = findViewById(R.id.DownloadButton);
        Button showHelpButton = findViewById(R.id.ShowHelpButton);
        Button uploadDataButton = findViewById(R.id.UploadDataButton);
        backToMainScreenButton.setOnClickListener(this);
        showHistoryButton.setOnClickListener(this);
        addArticleButton.setOnClickListener(this);
        download.setOnClickListener(this);
        showHelpButton.setOnClickListener(this);
        uploadDataButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.BackToMainScreen){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if(view.getId() == R.id.ShowHistoryButton){
            showHistory();
        }
        if(view.getId() == R.id.AddArticleButton){
            addArticle();
        }
        if(view.getId() == R.id.DownloadButton){
            readFile();
        }
        if(view.getId() == R.id.ShowHelpButton){
            showHelp();
        }
        if(view.getId() == R.id.UploadDataButton){
            saveDBToTxt();
        }
    }

    private void showHistory(){
        ArrayList<HistoryEntry> history = historyArray.getHistory();
        if(!history.isEmpty()){
            setHistoryVisible(true);
            StringBuilder sb = new StringBuilder();
            if(history.size() > 10){
                for (int i = history.size()-1; i >= history.size()-5 ; i--) {
                    sb.append(history.get(i));
                    sb.append("\n");
                }
            } else {
                for (int i = history.size()-1; i >= 0; i--) {
                    sb.append(history.get(i));
                    sb.append("\n");
                }
            }
            historyText.setText(sb);
        } else {
            Toast.makeText(this, getResources().getString(R.string.EmptyHistoryMessage), Toast.LENGTH_SHORT).show();
        }
    }

    private void setHistoryVisible(Boolean historyIsVisible){
        if(historyIsVisible){
            historyText.setVisibility(View.VISIBLE);
            articleTextView.setVisibility(View.INVISIBLE);
            descriptionTextView.setVisibility(View.INVISIBLE);
            boxVolumeTextView.setVisibility(View.INVISIBLE);
            tilesCounterTextView.setVisibility(View.INVISIBLE);
            articleAdd.setVisibility(View.INVISIBLE);
            descriptionAdd.setVisibility(View.INVISIBLE);
            boxVolumeAdd.setVisibility(View.INVISIBLE);
            tilesCounterAdd.setVisibility(View.INVISIBLE);
        } else{
            historyText.setVisibility(View.INVISIBLE);
            articleTextView.setVisibility(View.VISIBLE);
            descriptionTextView.setVisibility(View.VISIBLE);
            boxVolumeTextView.setVisibility(View.VISIBLE);
            tilesCounterTextView.setVisibility(View.VISIBLE);
            articleAdd.setVisibility(View.VISIBLE);
            descriptionAdd.setVisibility(View.VISIBLE);
            boxVolumeAdd.setVisibility(View.VISIBLE);
            tilesCounterAdd.setVisibility(View.VISIBLE);
        }
    }

    private void addArticle(){
        setHistoryVisible(false);
        String article = articleAdd.getText().toString();
        String name = descriptionAdd.getText().toString();
        String boxVolume = boxVolumeAdd.getText().toString();
        String tilesInBox = tilesCounterAdd.getText().toString();
        if(article.isEmpty() || name.isEmpty() || boxVolume.isEmpty() || tilesInBox.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.EmptyFieldsMessage), Toast.LENGTH_SHORT).show();
            return;
        }
        if(name.contains(SEPARATOR)){
            name = name.replace(SEPARATOR, "-");
        }
        if(Double.parseDouble(boxVolume) == 0){
            Toast.makeText(this, getResources().getString(R.string.ZeroValueInVolumeMessage),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(Integer.parseInt(tilesInBox) == 0){
            Toast.makeText(this, getResources().getString(R.string.ZeroValueInPiecesMessage),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Box box = new Box(Integer.parseInt(article), name,
                Double.parseDouble(boxVolume), Integer.parseInt(tilesInBox));
        adapter.open();
        save(box);
        adapter.close();
        Toast.makeText(this, getResources().getString(R.string.AddedMessage), Toast.LENGTH_SHORT).show();
        articleAdd.setText("");
        descriptionAdd.setText("");
        boxVolumeAdd.setText("");
        tilesCounterAdd.setText("");
        writeLog(getResources().getString(R.string.AddedMessage) + " - " +article + " - " +
                name + " - " + boxVolume + " - " + tilesInBox);
    }

    public void save(Box box){
        int article = box.getArticle();
        if (adapter.boxIsExists(article)) {
            adapter.update(box);
        } else {
            adapter.insert(box);
        }
    }

    public void delete(int article){
        adapter.open();
        adapter.delete(article);
        adapter.close();
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
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    for (String line; (line = r.readLine()) != null; ) {
                        total.append(line).append('\n');
                    }
                    readTxtData(total.toString());
                } catch (Exception e) {
                    writeLog(e.getMessage());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void readTxtData(String text){
        boolean isAdded = false;
        if(text.isEmpty()){
            return;
        }
        String[] strings = text.split("\n");
        adapter.open();
        for (String line : strings) {
            Box box = writeArticleToDB(line);
            if (box != null) {
                save(box);
                isAdded = true;
            }
        }
        adapter.close();
        if(isAdded){
            Toast.makeText(this, getResources().getString(R.string.AddedMessage), Toast.LENGTH_SHORT).show();
        }
    }

    private Box writeArticleToDB(String line){
        String[] fields = line.split(SEPARATOR);
        if(fields.length != 4) {
            return null;
        }
        String article = fields[0].trim();
        String name = fields[1].trim();
        String volume = fields[2].trim();
        String tilesInBox = fields[3].trim();
        if(article.contains("[^0-9]")){
            return null;
        }
        if(volume.contains("[^0-9,.]")){
            if(volume.split("[,.]{1}").length > 2){
                return null;
            }
            return null;
        }
        volume = volume.replace(",", ".");
        if(tilesInBox.contains("[^0-9]")){
            return null;
        }
        return new Box(Integer.parseInt(article), name,
                Double.parseDouble(volume), Integer.parseInt(tilesInBox));
    }

    private void saveDBToTxt(){
        String text;
        StringBuilder sb = new StringBuilder();
        adapter.open();
        ArrayList<Box> boxes = adapter.getBoxes();
        adapter.close();
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            sb.append(box.getArticle());
            sb.append(SEPARATOR);
            sb.append(box.getName());
            sb.append(SEPARATOR);
            sb.append(box.getVolume());
            sb.append(SEPARATOR);
            sb.append(box.getPiecesInPack());
            sb.append("\n");
        }
        text = sb.toString();
        try {
            File file = new File(getExternalFilesDir(null), FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            Toast.makeText(this, getResources().getString(R.string.UploadedDataMessage), Toast.LENGTH_SHORT).show();
        } catch(IOException ex) {
            writeLog(ex.getMessage());
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showHelp(){
        setHistoryVisible(true);
        historyText.setText(getResources().getString(R.string.ExampleOfFillingText));
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