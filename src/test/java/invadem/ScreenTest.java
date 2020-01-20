package invadem;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

import processing.core.PApplet;
import processing.core.PImage;

import invadem.assets.*;
import invadem.App;

public class ScreenTest extends App{
    private PImage img;

    @Before
    public void setupTest(){
        img = new PImage(10,20);
        PApplet.runSketch(new String[]{"--location=0,0", ""}, this);
        delay(1000);
    }

    @Test
    public void testScreenConstructor(){
        Screen s = new Screen(img, 200, 300, 100, 20);
        assertNotNull(s);
        assertEquals(s.getLoadFrames(), 0);
    }

    @Test
    public void testTickNormal(){
        //When the tick only increments the loadFrames
        Screen s = new Screen(img, 200, 300, 100, 20);
        s.tick(this);
        assertEquals(s.getLoadFrames(), 1);
    }

    @Test
    public void testTickRestart(){
        //When screen has displayed for 2 seconds
        Screen s = new Screen(img, 200, 300, 100, 20);

        for(int i = 0; i < 120; i++){
            s.tick(this);
        }

        assertEquals(s.getLoadFrames(), 0);

        //Resetting instances
        assertEquals(this.getPlayer().getX(), 309);
        assertEquals(this.getPlayer().getY(), 464);
        assertEquals(this.getInvaders().size(), 40);
        assertEquals(this.getBarrierParts().size(), 21);

    }

}
