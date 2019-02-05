package com.Maxonchik;

import static java.lang.Math.abs;

public class Point implements Comparable<Point> {

    double x, y;

    public Point(){
        this.x = 0;
        this.y = 0;
    }

    public Point(double x, double y){ //конструктор
        this.x = x;
        this.y = y;
    }

    public Point(Point p){
        this.x = p.x;
        this.y = p.y;
    }

    Point sum(Point p) { return new Point(x + p.x, y + p.y); } //сложение векторов
    Point subtract(Point p) { return new Point(x - p.x, y - p.y); } //разность векторов
    double crossProduct(Point p) { return x * p.y - y * p.x; } //векторное произведение

    static double eps = 1e-5;

    @Override
    public int compareTo(Point p) {
        if (abs(p.x - x) < eps && abs(p.y - y) < eps)
            return 0;
        if (this.crossProduct(p) > 0)
            return -1;
        return 1;
    }

    public double getArea(Point a, Point b){
        double area = a.crossProduct(b) + b.crossProduct(this) + this.crossProduct(a);
        return abs(area) / 2;
    }
}

