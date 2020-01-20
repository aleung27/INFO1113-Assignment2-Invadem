package invadem.assets;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.Map;
import invadem.App;

/**
 * Represents the tank object which the player controls in the game.
 */
public class Tank extends GameObject{

//================================================================================
// Properties
//================================================================================

    /**
     * Boolean which keeps track of whether a projectile has been fired
     * yet. Ensures we can't fire multiple times by holding down Spacebar
     */
    private boolean fired;

    /** Value of the left and right boundary for movement*/
    private static final int leftBoundary = 180, rightBoundary = 460;

    /** Keycode value of the left, right and space key */
    private static final int left = 37, right = 39, space = 32, pause = 80;

//================================================================================
// Constructor
//================================================================================

    /**
     * Constructor for the tank object, using the super constructor
     * from gameObject.
     * @param img The sprite associated with the object
     * @param x The x coordinate associated with the TOP-LEFT of the object
     * @param y The y coordinate associated with the TOP-LEFT of the object
     * @param width The width of the object
     * @param height The height of the object
     * @param velocity The velocity (x and y respectively) of the object
     */
    public Tank(PImage img, int x, int y, int width, int height, int[] velocity){
        super(img, x, y, width, height, velocity);
        health = 3;
        fired = false;
    }

//================================================================================
// Methods
//================================================================================

    /**
     * Logic behind the tank, called once every draw() under normal execution.
     * @param keys The keys which are mapped to valid commands for the tank
     * @param p The main App
     */
    public void tick(Map<Integer, Boolean> keys, App p){
        if(keys.get(left) && !keys.get(right) && x > leftBoundary){
            //We only move when one movement key is pressed and the tank is within the boundary
            this.moveX(false); //Move left
        } else if(keys.get(right) && !keys.get(left) && x+width < rightBoundary){
            this.moveX(true); //Move right
        }

        if(keys.get(space) && !fired){
            p.addProjectile(x+width/2, y, true, false);
            fired = true;
        }
    }

//================================================================================
// Overridden Methods
//================================================================================

    /**
     * Overridden method for destory which takes into accout restarting the game
     * as when the tank is destroyed the game is over.
     * @param p The main App
     */
    @Override
    public void destroy(App p){
        p.processGameOver();
    }

//================================================================================
// Setter Methods
//================================================================================

    /**
     * Simple setter method for whether the tank has fired or not
     * @param fired Whether the tank has fired or not yet.
     */
    public void setFired(boolean fired){
        this.fired = fired;
    }

//================================================================================
// Getter Methods
//================================================================================

    /**
     * Getter method for whether fired is true or not - Only used for testing
     * @return Whether the tank has fired or not
     */
    public boolean getFired(){
        return fired;
    }

    /**
     * Getter method for the left arrow keyCode
     * @return the keyCode for left arrow key
     */
    public static int getLeft() {
    	return left;
    }

    /**
     * Getter method for the right arrow keyCode
     * @return the keyCode for the right arrow key
     */
    public static int getRight() {
    	return right;
    }

    /**
     * Getter method for the spacebar keycode
     * @return the keyCode for the space key
     */
    public static int getSpace() {
    	return space;
    }

    /**
     * Getter method for the pause keycode
     * @return the keyCode for the pause (p) button
     */
    public static int getPause() {
    	return pause;
    }
}
