package invadem;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

import processing.core.PApplet;
import processing.core.PImage;

import invadem.assets.*;
import invadem.App;

public class BarrierTest {
    private PImage[] sprites;

    @Before
    public void setupTest(){
        sprites = new PImage[3];
        sprites[0] = new PImage(32, 4);
        sprites[1] = new PImage(4, 32);
        sprites[2] = new PImage(16, 16);
    }

    @Test
    public void testBarrierGetters(){
        //Getter functions

        BarrierParts b = new BarrierParts(sprites, 300, 400, 20, 16, new int[] {0, 0});
        assertEquals(b.getX(), 300);
        assertEquals(b.getY(), 400);
        assertEquals(b.getWidth(), 20);
        assertEquals(b.getHeight(), 16);
        assertEquals(b.getHealth(), 3);
        assertEquals(b.getImg(), sprites[0]);
    }

    @Test
    public void testBarrierConstruction() {
       BarrierParts b = new BarrierParts(sprites, 400, 400, 16, 16, new int[] {1, 4});
       assertNotNull(b);
       assertEquals(b.getHealth(), 3);
       assertEquals(b.getImg(), sprites[0]);
    }

    @Test
    public void testBarrierReduceHealth(){
       BarrierParts b = new BarrierParts(sprites, 400, 400, 16, 16, new int[] {1, 4});

       //Taking one damage
       b.reduceHealth(1);
       assertEquals(b.getHealth(), 2);
       assertEquals(b.getImg(), sprites[1]);

       //Taking two damage total
       b.reduceHealth(1);
       assertEquals(b.getHealth(), 1);
       assertEquals(b.getImg(), sprites[2]);

       //Taking 3 damage total
       b.reduceHealth(1);
       assertEquals(b.getHealth(), 0);
       assertEquals(b.getImg(), sprites[2]); //Sprite doesn't change as the barrier is destroyed
    }
}
