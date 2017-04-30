package bcscrew.colourfy;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Kyle on 2017-03-11.
 */

public class ColorName {

    private static HashMap<String, Integer> colourMap;


    public static String getColourName(int pixelColor) {

        // if this is the first time accessing the map, initialize it (singleton)
        if (colourMap == null) {
            colourMap = initColourMap();
        }

        // largest difference is 255 for every colour component
        int currentDifference = 3 * 255;

        // name of the best matching colour, orignially null
        String closestColorName = null;

        // get int values for all three colour components of the pixel
        int pixelColorR = Color.red(pixelColor);
        int pixelColorG = Color.green(pixelColor);
        int pixelColorB = Color.blue(pixelColor);

        Iterator<String> colorNameIterator = colourMap.keySet().iterator();

        // continue iterating if the map contains a next colour and the difference is greater than zero.
        // a difference of zero means we've found an exact match, so there's no point in iterating further.
        while (colorNameIterator.hasNext() && currentDifference > 0) {


            // this colour's name
            String currentColorName = colorNameIterator.next();

            // this colour's int value
            int color = initColourMap().get(currentColorName);

            // get int values for all three colour components of this colour
            int colorR = Color.red(color);
            int colorG = Color.green(color);
            int colorB = Color.blue(color);

            // calculate sum of absolute differences that indicates how good this match is
            int difference = Math.abs(pixelColorR - colorR) + Math.abs(pixelColorG - colorG) + Math.abs(pixelColorB - colorB);

            // a smaller difference means a better match, so keep track of it
            if (currentDifference > difference) {
                currentDifference = difference;
                closestColorName = currentColorName;
            }
        }
        return closestColorName;
    }
    // table from http://www.rapidtables.com/web/color/RGB_Color.html
    // parsed with python script and beautifulSoup
    public static HashMap<String, Integer> initColourMap() {
        HashMap<String, Integer> mColors = new HashMap<>();
        mColors.put("dark red", Color.rgb(128,0,0));
        mColors.put("dark red", Color.rgb(139,0,0));
        mColors.put("dark red", Color.rgb(165,42,42));
        mColors.put("dark red", Color.rgb(178,34,34));
        mColors.put("dark red", Color.rgb(220,20,60));
        mColors.put("red", Color.rgb(255,0,0));
        mColors.put("red orange", Color.rgb(255,99,71));
        mColors.put("red orange", Color.rgb(255,127,80));
        mColors.put("dark pink", Color.rgb(205,92,92));
        mColors.put("pink", Color.rgb(240,128,128));
        mColors.put("dark salmon", Color.rgb(233,150,122));
        mColors.put("salmon", Color.rgb(250,128,114));
        mColors.put("light salmon", Color.rgb(255,160,122));
        mColors.put("red", Color.rgb(255,69,0));
        mColors.put("orange", Color.rgb(255,140,0));
        mColors.put("orange", Color.rgb(255,165,0));
        mColors.put("light gold", Color.rgb(255,215,0));
        mColors.put("dark golden", Color.rgb(184,134,11));
        mColors.put("gold", Color.rgb(218,165,32));
        mColors.put("khaki", Color.rgb(238,232,170));
        mColors.put("dark khaki", Color.rgb(189,183,107));
        mColors.put("khaki", Color.rgb(240,230,140));
        mColors.put("olive", Color.rgb(128,128,0));
        mColors.put("yellow", Color.rgb(255,255,0));
        mColors.put("green", Color.rgb(154,205,50));
        mColors.put("dark green", Color.rgb(85,107,47));
        mColors.put("dark green", Color.rgb(107,142,35));
        mColors.put("light green", Color.rgb(124,252,0));
        mColors.put("light reuse", Color.rgb(127,255,0));
        mColors.put("light green", Color.rgb(173,255,47));
        mColors.put("dark green", Color.rgb(0,100,0));
        mColors.put("green", Color.rgb(0,128,0));
        mColors.put("green", Color.rgb(34,139,34));
        mColors.put("bright green", Color.rgb(0,255,0));
        mColors.put("light green", Color.rgb(50,205,50));
        mColors.put("light green", Color.rgb(144,238,144));
        mColors.put("pale green", Color.rgb(152,251,152));
        mColors.put("greyish green", Color.rgb(143,188,143));
        mColors.put("bright green", Color.rgb(0,250,154));
        mColors.put("bright green", Color.rgb(0,255,127));
        mColors.put("dark green", Color.rgb(46,139,87));
        mColors.put("pale green", Color.rgb(102,205,170));
        mColors.put("green", Color.rgb(60,179,113));
        mColors.put("teal", Color.rgb(32,178,170));
        mColors.put("dark teal", Color.rgb(47,79,79));
        mColors.put("teal", Color.rgb(0,128,128));
        mColors.put("teal", Color.rgb(0,139,139));
        mColors.put("aqua", Color.rgb(0,255,255));
        mColors.put("aqua", Color.rgb(0,255,255));
        mColors.put("pale blue", Color.rgb(224,255,255));
        mColors.put("turquoise", Color.rgb(0,206,209));
        mColors.put("turquoise", Color.rgb(64,224,208));
        mColors.put("turquoise", Color.rgb(72,209,204));
        mColors.put("pale blue", Color.rgb(175,238,238));
        mColors.put("aqua marine", Color.rgb(127,255,212));
        mColors.put("pale blue", Color.rgb(176,224,230));
        mColors.put("dark teal", Color.rgb(95,158,160));
        mColors.put("steel blue", Color.rgb(70,130,180));
        mColors.put("greyish blue", Color.rgb(100,149,237));
        mColors.put("bright blue", Color.rgb(0,191,255));
        mColors.put("light blue", Color.rgb(30,144,255));
        mColors.put("pale blue", Color.rgb(173,216,230));
        mColors.put("light blue", Color.rgb(135,206,235));
        mColors.put("light sky blue", Color.rgb(135,206,250));
        mColors.put("dark blue", Color.rgb(25,25,112));
        mColors.put("navy", Color.rgb(0,0,128));
        mColors.put("dark blue", Color.rgb(0,0,139));
        mColors.put("blue", Color.rgb(0,0,205));
        mColors.put("blue", Color.rgb(0,0,255));
        mColors.put("royal blue", Color.rgb(65,105,225));
        mColors.put("purple", Color.rgb(138,43,226));
        mColors.put("dark purple", Color.rgb(75,0,130));
        mColors.put("dark purple", Color.rgb(72,61,139));
        mColors.put("pale purple", Color.rgb(106,90,205));
        mColors.put("pale purple", Color.rgb(123,104,238));
        mColors.put("medium purple", Color.rgb(147,112,219));
        mColors.put("dark magenta", Color.rgb(139,0,139));
        mColors.put("purple", Color.rgb(148,0,211));
        mColors.put("purple", Color.rgb(153,50,204));
        mColors.put("light purple", Color.rgb(186,85,211));
        mColors.put("pale purple", Color.rgb(128,0,128));
        mColors.put("pale pink", Color.rgb(216,191,216));
        mColors.put("light pink", Color.rgb(221,160,221));
        mColors.put("pink", Color.rgb(238,130,238));
        mColors.put("bright pink", Color.rgb(255,0,255));
        mColors.put("pink", Color.rgb(218,112,214));
        mColors.put("red pink", Color.rgb(199,21,133));
        mColors.put("pale pink", Color.rgb(219,112,147));
        mColors.put("bright pink", Color.rgb(255,20,147));
        mColors.put("hot pink", Color.rgb(255,105,180));
        mColors.put("light pink", Color.rgb(255,182,193));
        mColors.put("light pink", Color.rgb(255,192,203));
        mColors.put("cream", Color.rgb(250,235,215));
        mColors.put("cream", Color.rgb(245,245,220));
        mColors.put("beige", Color.rgb(255,228,196));
        mColors.put("beige", Color.rgb(255,235,205));
        mColors.put("beige", Color.rgb(245,222,179));
        mColors.put("cream", Color.rgb(255,248,220));
        mColors.put("pale yellow", Color.rgb(255,250,205));
        mColors.put("pale yellow", Color.rgb(250,250,210));
        mColors.put("pale yellow", Color.rgb(255,255,224));
        mColors.put("brown", Color.rgb(139,69,19));
        mColors.put("brown", Color.rgb(160,82,45));
        mColors.put("orange brown", Color.rgb(210,105,30));
        mColors.put("light brown", Color.rgb(205,133,63));
        mColors.put("orange brown", Color.rgb(244,164,96));
        mColors.put("light brown", Color.rgb(222,184,135));
        mColors.put("light brown", Color.rgb(210,180,140));
        mColors.put("red brown", Color.rgb(188,143,143));
        mColors.put("beige", Color.rgb(255,228,181));
        mColors.put("beige", Color.rgb(255,222,173));
        mColors.put("peach", Color.rgb(255,218,185));
        mColors.put("pale pink", Color.rgb(255,228,225));
        mColors.put("pale pink", Color.rgb(255,240,245));
        mColors.put("cream", Color.rgb(250,240,230));
        mColors.put("cream", Color.rgb(253,245,230));
        mColors.put("beige", Color.rgb(255,239,213));
        mColors.put("pale pink", Color.rgb(255,245,238));
        mColors.put("light blue", Color.rgb(245,255,250));
        mColors.put("slate gray", Color.rgb(112,128,144));
        mColors.put("slate gray", Color.rgb(119,136,153));
        mColors.put("light steel blue", Color.rgb(176,196,222));
        mColors.put("lavender", Color.rgb(230,230,250));
        mColors.put("cream", Color.rgb(255,250,240));
        mColors.put("pale blue", Color.rgb(240,248,255));
        mColors.put("pale purple", Color.rgb(248,248,255));
        mColors.put("pale green", Color.rgb(240,255,240));
        mColors.put("white", Color.rgb(255,255,240));
        mColors.put("pale blue", Color.rgb(240,255,255));
        mColors.put("white", Color.rgb(255,250,250));
        mColors.put("black", Color.rgb(0,0,0));
        mColors.put("grey", Color.rgb(105,105,105));
        mColors.put("grey", Color.rgb(128,128,128));
        mColors.put("grey", Color.rgb(169,169,169));
        mColors.put("silver", Color.rgb(192,192,192));
        mColors.put("light grey", Color.rgb(211,211,211));
        mColors.put("light grey", Color.rgb(220,220,220));
        mColors.put("light grey", Color.rgb(245,245,245));
        mColors.put("white", Color.rgb(255,255,255));
        return mColors;
    }
}