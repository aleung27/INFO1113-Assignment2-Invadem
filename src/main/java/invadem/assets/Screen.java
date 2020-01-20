package invadem.assets;

import processing.core.PApplet;
import processing.core.PImage;

import invadem.App;

/**
 * Represents static screens in the game. We don't extend from GameObject
 * as it is technically just an image we display and lacks object properties.
 */
public class Screen {

//================================================================================
// Properties
//================================================================================

    /** The sprite associated with the screen */
    private PImage img;

    /** The x and y coordinate of the screen text */
    private int x, y;

    /** The width and height of the text */
    private int width, height;

    /** Counter variable for the amount of frames since we started rendering a screen */
    private int loadFrames;

//================================================================================
// Constructor
//================================================================================

    /**
     * Constructor method for the Screen
     * @param img    The Sprite associated with the Screen
     * @param x      The x coordinate of the image
     * @param y      The y coordinate of the image
     * @param width  The width of the image
     * @param height The height of the image
     */
    public Screen(PImage img, int x, int y, int width, int height){
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        loadFrames = 0;
    }

//================================================================================
// Methods
//================================================================================

    /**
     * Renders the screen onto the App p
     * @param p The main App
     */
    public void draw(PApplet p){
            p.image(img, x, y, width, height);
    }

    /**
     * Logic behind the screen. Mostly in charge of ensuring the screen is rendered for 2 seconds
     * @param p The main App
     */
    public void tick(App p){
        loadFrames++;

        if(loadFrames == 60*2){
            //When the screen has displayed for 2 seconds
            p.setNextLevel(false);
            p.setGameOver(false);
            loadFrames = 0;
            p.restart();
        }
    }

//================================================================================
// Getter Methods
//================================================================================

    /**
     * Getter method for loadframes - Only used for testing
     * @return The value of loadFrames
     */
    public int getLoadFrames() {
    	return loadFrames;
    }

}
