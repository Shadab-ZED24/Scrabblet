/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabblet;

/**
 *
 * @author Shadab Khan Zed
 */
import java.awt.*;
//import java.awt.Color.*;

class Letter {
    static int w,h;
    private static Font font,smfont;
    private static int y0,ys0;
    private static int lasth = -1;
    static final int NORMAL = 0;
    static final int DIM = 1;
    static final int BRIGHT = 2;
    
    private static final Color colors[][] = {
    mix (250,220,100), //normal
    mix (200,150,80), //DIM
    mix (255,230,150) //BRIGHT
    };
    
    private static Color mix(int r, int g, int b)[] {
        Color arr [] = new Color[3];
        arr[NORMAL] = new Color(r, g, b);
        arr[DIM] =  gain(arr[0], .71);
        arr[BRIGHT] = gain(arr[1],1.31);
        return arr;
    }
    
    private static int clamp(double d){
        return (d < 0) ? 0 : ((d > 255) ? 255 : (int) d);
    }
    private static Color gain(Color c, double f) {
        return new Color(
        clamp (c.getRed() * f),
        clamp (c.getGreen() * f),
        clamp (c.getBlue() * f));
    }
    
    private boolean  valid  = false;
    //quantized tile position of letter (just stored here).
    private Point tile = null;
    int x, y;           //postion of letter.
    private int x0;     //offset of symbol on tile.
    private int w0;     //width in pixel of symbol.
    private int xs0;    //offset of point on tile.
    private int ws0;    //width in pixel od points.
    private final int gap = 1;//pixel between symbol and points.
    
    private final String symbol;
    private final int points;

    Letter(char s, int p) {
        symbol = "" + s;
        points = p;
    }
    String getSymbol() {
        return symbol;
    }
    int getPoints() {
        return points;
    }
    void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
    void remember(Point t) {
        if(t == null) {
            tile = t;
        } else {
            tile = new Point(t.x, t.y);
        }
    }
    Point recall() {
        return tile;
    }
    static void resize(int w0, int h0) {
        w = w0;
        h = h0;
    }
    boolean hit(int xp, int yp) {
        return (xp >= x && xp < x + w && yp >= y && y < y + h);
    }
    
    private int font_ascent;
    void validate(Graphics g) {
        FontMetrics fm;
        if(h != lasth){
            font = new Font("SansSerif", Font.BOLD, (int)(h * .6));
            g.setFont(font);
            fm = g.getFontMetrics();
            font_ascent =  fm.getAscent();
            
            y0 = (h - font_ascent) * 4 / 10 + font_ascent;
            smfont = new Font("SansSerif", Font.BOLD, (int)(h * .3));
            g.setFont(font);
            fm = g.getFontMetrics();
            ys0  = y0 + fm.getAscent() / 2;
            lasth = h;
        }
        if(!valid) {
            valid = true;
            g.setFont(font);
            fm = g.getFontMetrics();
            w0 = fm.stringWidth(symbol);
            g.setFont(smfont);
            fm = g.getFontMetrics();
            ws0 = fm.stringWidth(""+ points);
            int slop = w - (w0 + gap + ws0);
            x = slop / 2;
            if(x0 < 1)
                x0 = 1;
            xs0 = x0 + w0 + gap;
            if(points < 9)
                xs0--;
        }
    }
    void paint(Graphics g, int i) {
        Color c[] = colors [i];
        validate(g);
        g.setColor(c[NORMAL]);
        g.fillRect(x, y, w, h);
        g.setColor(c[BRIGHT]);
        g.fillRect(x, y, w - 1, 1);
        g.fillRect(x, y + 1, 1, h - 2);
        g.setColor(Color.black);
        g.fillRect(x, y + h - 1, w, 1);
        g.fillRect(x + w - 1, y, 1, h - 1);
        g.setColor(c[DIM]);
        g.fillRect(x + 1, y + h + 1, w - 2, 1);
        g.fillRect(x + w - 2, y + 1, 1, h - 3);
        g.setColor(Color.black);
        if(points > 0) {
            g.setFont(font);
            g.drawString(symbol, x + x0, y + y0);
            g.setFont(smfont);
            g.drawString("" + points, x + xs0, y + ys0);
        }
    }
}
