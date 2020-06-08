package cn.medicine.graph.entity;

import java.util.ArrayList;

public class GraphEntity {

    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<Point> dirtyPoints = new ArrayList<>();


    public void addPoint(Point point){
        this.points.add(point);
    }

    public void addLine(Line line){
        this.lines.add(line);
    }

    public void addDirtyPoint(Point point){
        this.dirtyPoints.add(point);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public ArrayList<Point> getDirtyPoints() {
        return dirtyPoints;
    }

    public void setDirtyPoints(ArrayList<Point> dirtyPoints) {
        this.dirtyPoints = dirtyPoints;
    }
}
