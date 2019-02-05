package com.Maxonchik;

import static java.lang.Math.abs;

public class Line {

    double a, b, c;

    public Line(Point p1, Point p2){
        this.a = p1.y - p2.y;
        this.b = p2.x - p1.x;
        this.c = -(this.a * p1.x + this.b * p1.y);
    }

    public Line(double a, double b, double c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public void reverse(){
        this.a *= -1;
        this.b *= -1;
        this.c *= -1;
    }

    static double eps = 1e-5;

    public boolean doesnotIntersect(Line l){
        return abs(this.a * l.b - this.b * l.a) < eps;
    }


    public Point intersect(Line l){
        Point p = new Point();
        p.x = (l.c * this.b - this.c * l.b) / (this.a * l.b - this.b * l.a);
        p.y = (this.a * l.c - this.c * l.a) / (this.b * l.a - this.a * l.b);
        return p;
    }

    public boolean getDist(Point p){
        return this.a * p.x + this.b * p.y + this.c >= 0;
    }

    public Point intersection(Line l1, Line l2){
        return new Point(0, 0);
    }
}
