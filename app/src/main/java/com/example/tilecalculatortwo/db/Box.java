package com.example.tilecalculatortwo.db;

import androidx.annotation.NonNull;

public class Box {
    int article;
    String name;
    double volume;
    int piecesInPack;

    public Box(int article, String name, double volume, int piecesInPack) {
        this.article = article;
        this.name = name;
        this.volume = volume;
        this.piecesInPack = piecesInPack;
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getPiecesInPack() {
        return piecesInPack;
    }

    public void setPiecesInPack(int piecesInPack) {
        this.piecesInPack = piecesInPack;
    }

    @NonNull
    @Override
    public String toString() {
        return article +
                " - " + name +
                " - " + volume +
                " - " + piecesInPack;
    }
}
