/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabblet;

//import java.applet.Applet;
import java.awt.Canvas;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author Shadab Khan Zed
 */
class IntroCanvas extends Canvas {
    private final Color pink = new Color(255,200,200);
    private final Color blue = new Color(150,200,255);
    private final Color yellow = new Color(250,220,100);
    
    private int w, h;
    private final int edge = 16;
    private static final String title = "Scrabblet";
    private static final String name = "CopyRight - 2015 Shadab Khan Zed";
    private static final String book = "The Complete Reference";
    private Font namefont ,titlefont ,bookfont;

    public IntroCanvas() {
        setBackground(yellow);
        titlefont = new Font("SansSertif", Font.BOLD, 58);
        namefont = new Font("SansSerif", Font.BOLD, 18);
        bookfont = new Font("SansSerif", Font.PLAIN, 12);
        addMouseListener(new MyMouseAdapter());
    }
    
    private void d(Graphics g, String s, Color c, Font f, int y, int off) {
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(c);
        g.drawString(s, (w - fm.stringWidth(s)) / 2 + off , y + off);
    }
    
    @Override
    public void paint(Graphics g) {
        Dimension d = getSize();
        w = d.width;
        h = d.height;
        g.setColor(blue);
        g.fill3DRect(edge, edge, w - 2 * edge, h - 2 * edge, true);
        d(g, title, Color.black, titlefont , h / 2 , 1);
        d(g, title, Color.white, titlefont , h / 2 , -1);
        d(g, title, pink, titlefont , h / 2 , 0);
        d(g, name, Color.black, namefont , h * 3 / 4 , 0);
        d(g, name, Color.black, bookfont , h * 7 / 8 , 0);
    }
    class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent me) {
            ((Frame)getParent()).setVisible(false);
        }
    }
}
