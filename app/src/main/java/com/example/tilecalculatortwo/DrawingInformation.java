package com.example.tilecalculatortwo;

import java.io.Serializable;

public class DrawingInformation implements Serializable {
    double length;
    double width;
    double tileLength;
    double tileWidth;
    double offset;
    boolean usingRemnant;
    boolean centering;
    boolean alongLongSide;
    boolean isLaminate;

    public DrawingInformation(double length, double width, double tileLength,
                              double tileWidth, double offset, boolean usingRemnant,
                              boolean centering, boolean alongLongSide, boolean isLaminate) {
        this.length = length;
        this.width = width;
        this.tileLength = tileLength;
        this.tileWidth = tileWidth;
        this.offset = offset;
        this.usingRemnant = usingRemnant;
        this.centering = centering;
        this.alongLongSide = alongLongSide;
        this.isLaminate = isLaminate;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getTileLength() {
        return tileLength;
    }

    public void setTileLength(double tileLength) {
        this.tileLength = tileLength;
    }

    public double getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(double tileWidth) {
        this.tileWidth = tileWidth;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public boolean isUsingRemnant() {
        return usingRemnant;
    }

    public void setUsingRemnant(boolean usingRemnant) {
        this.usingRemnant = usingRemnant;
    }

    public boolean isCentering() {
        return centering;
    }

    public void setCentering(boolean centering) {
        this.centering = centering;
    }

    public boolean isAlongLongSide() {
        return alongLongSide;
    }

    public void setAlongLongSide(boolean alongLongSide) {
        this.alongLongSide = alongLongSide;
    }

    public boolean isLaminate() {
        return isLaminate;
    }

    public void setLaminate(boolean laminate) {
        isLaminate = laminate;
    }
}
