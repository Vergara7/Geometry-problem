package com.Maxonchik;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


import static java.lang.Integer.max;
import static java.lang.Integer.reverse;
import static java.lang.Math.min;

public class MainFrame extends JFrame implements MouseListener, KeyListener {

    static int width = 1200, height = 800;

    private MainFrame(){ //создаём окошко
        setPreferredSize(new Dimension(width, height)); //задаём размеры окошка
        pack();
        addMouseListener(this); //подключаем мышку
        addKeyListener(this); //подключаем клавиатуру
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //по закрытию окошка, программа завершает работу
        setVisible(true); //делаем так, чтобы окошко было видно
    }

    private static MainFrame frame;
    private static int status = 0;

    public static void main(String[] args) throws InterruptedException { //точка входа программы
        frame = new MainFrame();
    }

    private static Polygon rectangle = new Polygon(4);
    private static ArrayList<Point> points = new ArrayList<>(); //массив для хранения точек


    private void readFromConsole() throws InterruptedException { //считываем числа с помощью консоли
        System.out.println("Координаты противоположныx углов, пожалуйста"); //говорим, что готовы считывать
        Scanner sc = new Scanner(System.in);
        int x1 = sc.nextInt(), y1 = sc.nextInt();
        int x2 = sc.nextInt(), y2 = sc.nextInt();
        y1 += shift;
        y2 += shift;
        int minX = min(x1, x2), maxX = max(x1, x2);
        int minY = min(y1, y2), maxY = max(y1, y2);
        rectangle = new Polygon(new Point(minX, minY), new Point(minX, maxY), new Point(maxX, maxY), new Point(maxX, minY));
        System.out.println(rectangle.size());
        System.out.println("Отлично, теперь введите количество точек"); //говорим, что готовы считывать
        int n = sc.nextInt();
        System.out.println("И финальный штрих - сами точки"); //говорим, что готовы считывать
        for (int i = 0; i < n; i++){
            int x = sc.nextInt(), y = sc.nextInt();
            y += shift;
            points.add(new Point(x, y));
        }
        status = 5;
        start(); //мы всё считали, начинаем
    }

    static int x1, y1, x2, y2;

    private void readFromFile() throws IOException, InterruptedException { //считываем числа с помощью файла
        Scanner sc = new Scanner(new File("test.in")); //открываем файл
        x1 = sc.nextInt();
        y1 = sc.nextInt();
        x2 = sc.nextInt();
        y2 = sc.nextInt();
        y1 += shift;
        y2 += shift;
        int minX = min(x1, x2), maxX = max(x1, x2);
        int minY = min(y1, y2), maxY = max(y1, y2);
        rectangle = new Polygon(new Point(minX, minY), new Point(minX, maxY), new Point(maxX, minY), new Point(maxX, maxY));
        int n = sc.nextInt();
        for (int i = 0; i < n; i++){
            int x = sc.nextInt(), y = sc.nextInt();
            y += shift;
            points.add(new Point(x, y));
        }
        status = 5;
        start(); //мы всё считали, начинаем
    }

    static int r = 10;
    private static boolean clear = false; // переменная, в которой мы храним, очищали ли мы окошко перед считыванием с помощью мышки
    private static int shift = 120;

    private static double eps = 1e-5;
    private static double answer = 0;
    Polygon triangle = new Polygon(3);

    @Override
    public void paint(Graphics g){
        BufferStrategy bufferStrategy = getBufferStrategy(); //тут реализуется двойная буферизация
        if (bufferStrategy == null){ //создаём, если ещё нет
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }
        g = bufferStrategy.getDrawGraphics(); //берём буфер в который будем рисовать
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(new Font("TimesRoman", Font.PLAIN, 35));
        g2d.setStroke(new BasicStroke(3));
        g.setColor(((Graphics2D) g).getBackground());
        g.fillRect(0, 0, width, height);


        g.setColor(new Color(150, 0, 0));
        g.drawLine(0, shift, width, shift);
        if (status == 0){
            ((Graphics2D) g).drawString("Выберите вид считывания данных", 350, 50);
            ((Graphics2D) g).drawString("ввод из консоли", 250, 100);
            ((Graphics2D) g).drawString("ввод из файла", 550, 100);
            ((Graphics2D) g).drawString("ввод ручками", 830, 100);
        }
        if (status == 1) {
            ((Graphics2D) g).drawString("Введите вначале в консоль координаты противоположных углов", 100, 60);
            ((Graphics2D) g).drawString(" прямоугольника, потом количество точек и их координаты", 130, 100);
        }
        if (status == 3){
            ((Graphics2D) g).drawString("Укажите координаты противоположных углов прямоугольника", 110, 60);
        }
        if (status == 4){
            ((Graphics2D) g).drawString("Супер, еще один остался", 360, 60);
            g.setColor(new Color(0, 0, 255));
            g.fillOval(x1, y1, r, r);
        }
        if (status >= 5){
            if (status != 6) {
                if (points.size() < 7)
                    ((Graphics2D) g).drawString("Теперь можете указывать точки", 360, 60);
                if (points.size() > 0)
                    ((Graphics2D) g).drawString("Хочу убрать точку!", 800, 100);
            }
            g.setColor(new Color(0, 0, 255));
            for (int i = 0; i < 4; i++)
                g.fillOval((int)rectangle.get(i).x, (int)rectangle.get(i).y, r, r);
            for (int i = 0; i < 4; i++){
                Point p1 = rectangle.get(i);
                Point p2 = rectangle.get(i + 1);
                g.drawLine((int)p1.x + 4, (int)p1.y + 4, (int)p2.x + 4, (int)p2.y + 4);
            }
            g.setColor(new Color(0, 255, 0));
            for (int i = 0; i < points.size(); i++)
                g.fillOval((int)points.get(i).x, (int)points.get(i).y, r, r);
            if (answer > eps && status != 6){
                g.setColor(new Color(150, 0, 0));
                String ans = "Ответ:   " + String.valueOf(answer);
                ((Graphics2D) g).drawString(ans, 60, 100);
                g.setColor(new Color(0, 200, 0));
                for (int i = 0; i < 3; i++)
                    g.drawLine((int)triangle.get(i).x + 4, (int)triangle.get(i).y + 4, (int)triangle.get(i + 1).x + 4, (int)triangle.get(i + 1).y + 4);
                Polygon s = rectangle.intersectRectangleWithTriangle(triangle);
                int n = s.size();
                int x[] = new int[n];
                int y[] = new int[n];
                for (int i = 0; i < n; i++) {
                    x[i] = (int) s.get(i).x + 4;
                    y[i] = (int) s.get(i).y + 4;
                }
                g.setColor(new Color(150, 0, 255));
                g.fillPolygon(x, y, n);
                g.setColor(new Color(255, 0, 255));
                for (int i = 0; i < n; i++)
                    g2d.drawLine((int) s.get(i).x + 4, (int) s.get(i).y + 4, (int) s.get(i + 1).x + 4, (int) s.get(i + 1).y + 4);
            }
        }
        if (status == 6){
            g.setColor(new Color(150, 0, 0));
            ((Graphics2D) g).drawString("Чтобы убрать точку, нажмите на неё, можно убрать только зеленую точку", 10, 50);
        }
        g.dispose();
        bufferStrategy.show();


        if (status == 1){
            try {
                readFromConsole();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (status == 2){
            try {
                readFromFile();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    void start() throws InterruptedException { //собственно само решение
        answer = 0;
        for (int i = 0; i < points.size(); i++){
            for (int j = 0; j < i; j++){
                for (int k = 0; k < j; k++){
                    Polygon triangle1 = new Polygon(points.get(i), points.get(j), points.get(k));
                    Polygon intersection = rectangle.intersectRectangleWithTriangle(triangle1);
                    double area = intersection.getArea();
                    if (area > answer){
                        answer = area;
                        for (int l = 0; l < 3; l++)
                            triangle.set(l, triangle1.get(l));
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() - 4, y = e.getY() - 4;
        if (status == 0){
            if (y >= 110 || y < 80 || (x >= 500 && x <= 545) || (x >= 765 && x <= 830) || x > 1035)
                return;
            if (x < 500){
                status = 1;
                repaint();
                return;
            }
            if (x < 765){
                status = 2;
                repaint();
                return;
            }
            status = 3;
            repaint();
            return;
        }
        if (y < shift && status != 6) {
            if (y < 75 || y > 110 || x < 800 || x > 1110)
                return;
            status = 6;
            repaint();
            return;
        }
        if (status == 3){
            x1 = x;
            y1 = y;
            status = 4;
            repaint();
            return;
        }

        if (status == 4){
            x2 = x;
            y2 = y;
            int minX = min(x1, x2), maxX = max(x1, x2);
            int minY = min(y1, y2), maxY = max(y1, y2);
            rectangle = new Polygon(new Point(minX, minY), new Point(minX, maxY), new Point(maxX, maxY), new Point(maxX, minY));
            status = 5;
            repaint();
            return;
        }
        if (status == 5){
            points.add(new Point(x, y));
            repaint();
            try {
                start();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return;
        }
        if (status == 6){
            if (y < shift){
                if (x < 995 || x > 1105 || y < 75 || y > 1105)
                    return;
                status = 5;
                repaint();
                return;
            }
            int closest = -1, dist = 0;
            for (int i = 0; i < points.size(); i++) {
                int d = ((int)points.get(i).x - x) * ((int)points.get(i).x - x) + ((int)points.get(i).y - y) * ((int)points.get(i).y - y);
                if (closest == -1 || dist > d) {
                    closest = i;
                    dist = d;
                }
            }
            if (dist < 70) {
                points.remove(closest);
                status = 5;
                try {
                    start();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                repaint();
            }
            return;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            status = 0;
            answer = 0;
            points.clear();
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
