package com.example.zpwu.svhn;

import java.util.ArrayList;
import java.util.List;

public class DrawModel {

    public static class LineElem {
        public float x;
        public float y;

        public LineElem(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Line {
        private List<LineElem> elems = new ArrayList<>();

        public Line() {
        }

        public void addElem(LineElem elem) {
            elems.add(elem);
        }

        public int getElemSize() {
            return elems.size();
        }

        public LineElem getElem(int index) {
            return elems.get(index);
        }
    }

    private Line currentLine;
    private int width;
    private int height;
    private List<Line> lines = new ArrayList<>();

    public DrawModel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void startLine(float x, float y){
        currentLine = new Line();
        currentLine.addElem(new LineElem(x, y));
        lines.add(currentLine);
    }

    public void endLine() {
        currentLine = null;
    }

    public void addLineElem(float x, float y) {
        if (currentLine != null) {
            currentLine.addElem(new LineElem(x, y));
        }
    }

    public int getLineSize() {
        return lines.size();
    }

    public Line getLine(int index) {
        return lines.get(index);
    }

    public void clear() {
        lines.clear();
    }
}
