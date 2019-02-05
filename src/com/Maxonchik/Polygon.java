package com.Maxonchik;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.max;
import static java.lang.Math.abs;
import static java.lang.Math.min;

public class Polygon {

    ArrayList<Point> polygon = new ArrayList<>();

    Polygon(Point a, Point b, Point c){
        polygon.add(a);
        polygon.add(b);
        polygon.add(c);
    }

    Polygon(Point a, Point b, Point c, Point d){
        polygon.add(a);
        polygon.add(b);
        polygon.add(c);
        polygon.add(d);
    }

    Polygon(ArrayList<Point> s){
        for (int i = 0; i < s.size(); i++)
            polygon.add(s.get(i));
    }

    Polygon(int n){
        for (int i = 0; i < n; i++)
            polygon.add(new Point());
    }

    int size(){
        return polygon.size();
    }

    public Point get(int index){
        int n = polygon.size();
        if (index < n)
            return polygon.get(index);
        return polygon.get(index - n);
    }

    public void set(int index, Point p){
        polygon.set(index, p);
    }

    double getArea(){
        double answer = 0;
        int n = polygon.size();
        for (int i = 0; i < n; i++)
            answer += polygon.get(i).crossProduct(polygon.get((i + 1) % n));
        return abs(answer) / 2;
    }

    static double eps = 1e-5;

     Polygon convexHull(){
         int n = polygon.size();
         if (n == 0)
             return this;
         int x = 0;
         for (int i = 0; i < n; i++){
             boolean ok = false;
             if (abs(polygon.get(x).y - polygon.get(i).y) < eps && polygon.get(x).x - polygon.get(i).x > eps)
                 ok = true;
             if (polygon.get(x).y - polygon.get(i).y > eps || ok)
                 x = i;
         }
         Point p = new Point(polygon.get(x));
         ArrayList<Point> s = new ArrayList<Point>();
         for (int i = 0; i < n; i++)
             s.add(polygon.get(i).subtract(p));
         Collections.sort(s);
         ArrayList<Point> lst = new ArrayList<>();
         for (int i = 0; i < n; i++){
             while (lst.size() >= 2){
                 Point v = s.get(i).subtract(lst.get(lst.size() - 2));
                 Point u = lst.get(lst.size() - 1).subtract((lst.get(lst.size() - 2)));
                 if (v.crossProduct(u) > -eps)
                     lst.remove(lst.size() - 1);
                 else
                     break;
             }
             lst.add(s.get(i));
         }
         for (int i = 0; i < lst.size(); i++)
             lst.set(i, lst.get(i).sum(p));
         return new Polygon(lst);
    }

    boolean pointInsideRectangle(Point p){
         if (polygon.size() != 4)
             System.out.println("Polygon is not Rectangle");

         Point mini = new Point(polygon.get(0).x, polygon.get(0).y);
         Point maxi = new Point(polygon.get(0).x, polygon.get(0).y);
         for (int i = 0; i < polygon.size(); i++) {
             mini.x = min(mini.x, polygon.get(i).x);
             mini.y = min(mini.y, polygon.get(i).y);
             maxi.x = max(maxi.x, polygon.get(i).x);
             maxi.y = max(maxi.y, polygon.get(i).y);
         }
         return p.x > mini.x - eps && p.x < maxi.x + eps && p.y >= mini.y - eps && p.y <= maxi.y + eps;
    }

    boolean pointInsideTriangle(Point p){
         if (polygon.size() != 3)
             System.out.println("Polygon is not Triangle");
         double area = this.getArea();
         for (int i = 0; i < 3; i++)
            area -= p.getArea(polygon.get(i), polygon.get((i + 1) % 3));
         return abs(area) < eps;
    }

    public Polygon intersectRectangleWithTriangle(Polygon a){
         ArrayList<Point> s = new ArrayList<>();
         for (int i = 0; i < a.size(); i++) {
             if (this.pointInsideRectangle(a.get(i))) {
                 s.add(a.get(i));
             }
             break;
         }
         for (int i = 0; i < this.size(); i++){
             if (a.pointInsideTriangle(polygon.get(i)))
                 s.add(polygon.get(i));
         }
         for (int i = 0; i < a.size(); i++){
             for (int j = 0; j < this.size(); j++){
                 Line l1 = new Line(a.get(i), a.get(i + 1));
                 Line l2 = new Line(this.get(j), this.get(j + 1));
                 if (l1.doesnotIntersect(l2))
                     continue;;
                 Point p = l1.intersect(l2);
                 if (this.pointInsideRectangle(p) && a.pointInsideTriangle(p))
                    s.add(p);
             }
         }
         return new Polygon(s).convexHull();
    }
}
