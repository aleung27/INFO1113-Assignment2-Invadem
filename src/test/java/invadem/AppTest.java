package invadem;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

import processing.core.PApplet;
import processing.core.PImage;

import invadem.assets.*;
import invadem.App;

import java.util.Map;
import java.util.HashMap;

public class AppTest extends App{
    //As we have already tested most of apps functions through
    //each objects test cases which rely upon the functions
    //we only check certain things

    private PImage[] img;

    @Before
    public void setupTest(){
        img = new PImage[2];
        img[0] = new PImage(22, 16);
        img[1] = new PImage(29, 2);
        PApplet.runSketch(new String[]{"--location=0,0", ""}, this);
        delay(1000);
    }

    @Test
    public void testKeyPressedInvalid(){
        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause

        //When an invalid key is pressed
        this.keyCode = 34;
        this.keyPressed();

        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause
    }

    @Test
    public void testKeyPressedValid(){
        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause

        //When an valid key is pressed
        this.keyCode = 32;
        this.keyPressed();

        assertEquals(this.getKeys().get(32), true); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause
    }

    @Test
    public void testKeyPressedPause(){
        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause

        //When the pause key is first pressed
        this.keyCode = 80;
        this.keyPressed();

        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), true); //pause

        //When the pause key is held, should remain as true
        this.keyPressed();

        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), true); //pause

        //Releaseing pause and repressing it should swap it back to false
        //Emulates a pause screen
        this.keyReleased();
        this.keyPressed();
        assertEquals(this.getKeys().get(80), false); //pause
    }

    @Test
    public void testKeyReleasedInvalid(){
        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause

        //When an invalid key is released
        this.keyCode = 34;
        this.keyReleased();

        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause
    }

    @Test
    public void testKeyReleasedvalid(){
        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause

        //When a valid key is released
        this.keyCode = 39;
        this.keyPressed(); //we first need to swap to true to make sure something changes!
        assertEquals(this.getKeys().get(39), true);
        this.keyReleased();

        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause
    }

    @Test
    public void testKeyReleasedSpace(){
        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause

        //When space key is released
        this.keyCode = 32;
        this.keyPressed(); //we first need to swap to true to make sure something changes!
        assertEquals(this.getKeys().get(32), true);
        this.keyReleased();

        assertEquals(this.getKeys().get(32), false); //space
        assertEquals(this.getKeys().get(37), false); //left
        assertEquals(this.getKeys().get(39), false); //right
        assertEquals(this.getKeys().get(80), false); //pause
        assertFalse(this.getPlayer().getFired());
    }

    @Test
    public void testKeyReleasedPause(){
        assertEquals(this.getKeys().get(80), false); //pause

        //When pause key is released
        this.keyCode = 80;
        this.keyPressed(); //we first need to swap justPressed to true to make sure something changes!
        assertEquals(this.getKeys().get(80), true);
        this.keyReleased();
        this.keyPressed(); //This now sets the value of key(80) to false
        assertEquals(this.getKeys().get(80), false); //pause
    }

    @Test
    public void testIsPaused(){
        assertEquals(this.getKeys().get(80), false); //pause

        //Setting the game to paused
        this.keyCode = 80;
        this.addObjectToRemove(new Invader(img, 4, 4, 14, 33, new int[] {1, 2}));
        this.keyPressed();
        assertEquals(this.getKeys().get(80), true);

        this.tick(); //Game paused so removal of items doesnt happen yet
        assertTrue(this.getToRemove().size() > 0);
    }
}
