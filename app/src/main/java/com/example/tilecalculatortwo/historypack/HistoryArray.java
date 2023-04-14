package com.example.tilecalculatortwo.historypack;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryArray implements Serializable {
    private ArrayList<HistoryEntry> history = new ArrayList<>();

    public HistoryArray(){    }

    public ArrayList<HistoryEntry> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<HistoryEntry> history) {
        this.history = history;
    }

    public void addEntry(HistoryEntry entry){
        history.add(entry);
    }
}
