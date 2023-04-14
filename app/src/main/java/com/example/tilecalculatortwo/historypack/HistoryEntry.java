package com.example.tilecalculatortwo.historypack;

import java.io.Serializable;

public class HistoryEntry implements Serializable{

    private final String firstMessage;
    private final String messageBy;
    private final String secondMessage;
    private final String messagePack;
    private final String messagePcs;
    SearchingType searchingType;
    String boxName;
    String searchingData;
    String foundData;
    long boxCount;
    int tilesCount;

    public HistoryEntry(String firstMessage, String messageBy,
                        String secondMessage, String messagePack, String messagePcs,
                        SearchingType searchingType, String boxName,
                        String searchingData, String foundData,
                        long boxCount, int tilesCount) {
        this.firstMessage = firstMessage;
        this.messageBy = messageBy;
        this.secondMessage = secondMessage;
        this.messagePack = messagePack;
        this.messagePcs = messagePcs;
        this.searchingType = searchingType;
        this.boxName = boxName;
        this.searchingData = searchingData;
        this.foundData = foundData;
        this.boxCount = boxCount;
        this.tilesCount = tilesCount;
    }

    public SearchingType getSearchingType() {
        return searchingType;
    }

    public void setSearchingType(SearchingType searchingType) {
        this.searchingType = searchingType;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public String getSearchingData() {
        return searchingData;
    }

    public void setSearchingData(String searchingData) {
        this.searchingData = searchingData;
    }

    public String getFoundData() {
        return foundData;
    }

    public void setFoundData(String foundData) {
        this.foundData = foundData;
    }

    public long getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(long boxCount) {
        this.boxCount = boxCount;
    }

    public int getTilesCount() {
        return tilesCount;
    }

    public void setTilesCount(int tilesCount) {
        this.tilesCount = tilesCount;
    }

    @Override
    public String toString() {
        if(searchingType == SearchingType.BY_METHER){
            String name = boxName.isEmpty()? "" : boxName + "\n";
            return name + firstMessage + " " +
                    searchingData + " " + messageBy + " " +
                    foundData + " " + secondMessage + " " +
                    boxCount + " " + messagePack + " " +
                    tilesCount + " " + messagePcs;
        }
        if(searchingType == SearchingType.BY_METHER_AND_BY_PACK){
            String name = boxName.isEmpty()? "" : boxName + "\n";
            return name + firstMessage + " " +
                    searchingData + " " + messageBy + " " +
                    foundData + " " + secondMessage + " " +
                    boxCount + " " + messagePack;
        }
        if(searchingType == SearchingType.BY_PIECES){
            String name = boxName.isEmpty()? "" : boxName + "\n";
            return name + firstMessage + " " +
                    searchingData + " " + messageBy + " " +
                    foundData + " " + secondMessage + " " +
                    boxCount + " " + messagePack + " " +
                    tilesCount + " " + messagePcs;
        }
        return "";
    }
}
