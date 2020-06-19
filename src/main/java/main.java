/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Owner
 */

import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class main {

    static Drawpanel panel;
    public static void main(String[] args) {
        JFrame mainF = new JFrame("My Frame");
        mainF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainF.setSize(800,800);
        panel = new Drawpanel();
        
        try{
            Socket timeS = new Socket("time-a-g.nist.gov",13);
            Scanner cin = new Scanner(timeS.getInputStream());
            cin.next();
            cin.next();
            String time = cin.next();
            //System.out.println(time);
            Integer hours = Integer.parseInt(time.substring(0, 2));
            Integer minutes = Integer.parseInt(time.substring(3,5));
            Integer seconds = Integer.parseInt(time.substring(6,8));
            panel.sdegree = seconds * -6;
            panel.mdegree = minutes * -6;
            panel.hdegree = hours * -30;
            panel.repaint();
        }catch(Exception e){
            System.out.println("error " + e.toString());
        }
        
        mainF.add(panel);
        mainF.setVisible(true);
        MyTimer t = new MyTimer();
        t.start();
        
    }
    
}
class Drawpanel extends JPanel{
    int sdegree;
    int mdegree;
    int hdegree;
    
    Drawpanel(){
        super();
        sdegree = 0;
        mdegree = 0;
        hdegree = 0;
    }
    
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        int d1 = 600;
        int r1 = d1/2;
        int d2 = d1/60;
        int r2 = d2/2;
        int x = this.getWidth()/2;
        int y = this.getHeight()/2;
        g.drawOval(x - r1, y - r1, d1, d1);
        g.fillOval(x - r2, y - r2, d2, d2);
        
        g.drawLine(x, y-r1, x, y-r1+30); //North tick
        g.drawLine(x+r1, y, x+r1 - 30, y); //East tick
        g.drawLine(x, y+r1, x, y+r1-30); //South tick
        g.drawLine(x-r1, y, x-r1+30, y); //West tick
        
        double sx = Math.sin(Math.toRadians(sdegree))*(r1 * 0.95);
        double sy = Math.cos(Math.toRadians(sdegree))*(r1 * 0.95);
        g.drawLine(x, y, x-(int)sx,y-(int)sy);
        
        double mx = Math.sin(Math.toRadians(mdegree))*(r1 * 0.8);
        double my = Math.cos(Math.toRadians(mdegree))*(r1 * 0.8);
        g.drawLine(x, y, x-(int)mx,y-(int)my);
        
        double hx = Math.sin(Math.toRadians(hdegree))*(r1 * 0.6);
        double hy = Math.cos(Math.toRadians(hdegree))*(r1 * 0.6);
        g.drawLine(x, y, x-(int)hx,y-(int)hy);
    }
    
    public void update(){
         if(sdegree > -360){
            sdegree-=6;
            revalidate();
            repaint();
        }else{
            sdegree = -6;
            mdegree -= 6;
            revalidate();
            repaint();
        }
        if(mdegree == -360){
            mdegree = -6;
            hdegree -= 6;
            revalidate();
            repaint();
        }
        if(hdegree == -360){
            hdegree = -6;
            revalidate();
            repaint();
        }
    }
}
class MyTimer extends Thread{
    int count = 0;
    public void run(){
        try{
            while(true){
                sleep(1000);
                main.panel.update();
                count++;
                if(count == 60){
                    Socket timeS = new Socket("time-a-g.nist.gov",13);
                    Scanner cin = new Scanner(timeS.getInputStream());
                    cin.next();
                    cin.next();
                    String time = cin.next();
                    //System.out.println(time);
                    Integer hours = Integer.parseInt(time.substring(0, 2));
                    Integer minutes = Integer.parseInt(time.substring(3,5));
                    Integer seconds = Integer.parseInt(time.substring(6,8));
                    main.panel.sdegree = seconds * -6;
                    main.panel.mdegree = minutes * -6;
                    main.panel.hdegree = hours * -30;
                    main.panel.repaint();
                    count = 0;
                }
            }
        }catch(Exception e){
            System.out.println("error " + e.toString());
        }
    }
}