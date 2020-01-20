package invadem.assets;

import processing.core.PImage;
import java.util.Random;

import invadem.App;

/**
 * Represents the "Invaders" (enemies) for the game.
 * Also acts as a parent class for the "super" invader types
 */
public class Invader extends GameObject{

//================================================================================
// Properties
//================================================================================

    /**
     * Boolean which keeps track of whether an invader moved in the last frame
     * as it only moves once every two frames
     */
    protected boolean justMoved;

    /** Keeps track of the amount of pixelsMoved to coordinate movement changes */
    protected int pixelsMoved;

    /** Stores the sprites associated with each Invader object */
    protected PImage[] sprites;

    /** Generator for our random numbers */
    protected static Random generator = new Random();

    /** Point value of each invader */
    protected int points;


    /** Counter variable for the amount of frames that has passed since last firing  */
    private static int frameCount;

    /** Amount of frames needed before we fire a projectile */
    private static int shootFrames;

//================================================================================
// Constructor
//================================================================================

    /**
     * Constructor for the invader object, using the super constructor
     * from gameObject.
     * @param img The sprite associated with the object
     * @param x The x coordinate associated with the TOP-LEFT of the object
     * @param y The y coordinate associated with the TOP-LEFT of the object
     * @param width The width of the object
     * @param height The height of the object
     * @param velocity The velocity (x and y respectively) of the object
     */
    public Invader(PImage[] sprites, int x, int y, int width, int height, int[] velocity){
        super(x, y, width, height, velocity);
        this.sprites = sprites;
        justMoved = false;
        pixelsMoved = 0;
        img = sprites[0];
        health = 1; //Default value for regular invader
        points = 100; //Default value for points granted by regular invader
    }

//================================================================================
// Methods
//================================================================================

    /**
     * Logic behind the Invader, called once every draw() under normal execution
     */
    public void tick(App p){

        if(justMoved){
            //We don't move if we moved last frame
            justMoved = false;
        } else if(!justMoved){
            //Movement occurs if we didn't move last frame
            if(pixelsMoved >= 0 && pixelsMoved < 30){
                moveX(true); // Moves Right
                img = sprites[0];
            } else if(pixelsMoved >= 30 && pixelsMoved < 38){
                moveY(true); // Moves Down
                img = sprites[1];
            } else if(pixelsMoved >= 38 && pixelsMoved < 68){
                moveX(false); // Moves Left
                img = sprites[0];
            } else if(pixelsMoved >= 68 && pixelsMoved < 76){
                moveY(true); // Moves Down
                img = sprites[1];
            }

            pixelsMoved++;
            pixelsMoved = pixelsMoved % 76; //We reset the value after one "cycle" of movement
            justMoved = true;
        }
    }

//================================================================================
// Static Methods
//================================================================================

    /**
     * Called once every draw(), keeps track of the firing of invaders. As it is independent of each
     * instance we make it a static method instead
     * @param p The main App
     */
    public static void projectileTick(App p){
        frameCount++;

        if(frameCount == shootFrames){
            //Fire a projectile
            int randomInvader = generator.nextInt(p.getInvaders().size());
            if(p.getInvaders().get(randomInvader).getClass() == PowerInvader.class){
                p.addProjectile(p.getInvaders().get(randomInvader).getX(), p.getInvaders().get(randomInvader).getY(), false, true);
            } else {
                p.addProjectile(p.getInvaders().get(randomInvader).getX(), p.getInvaders().get(randomInvader).getY(), false, false);
            }
            frameCount = 0;
        }
    }

    /**
     * Called once every draw(), used to ensure that the game is still "playable"
     * and whether we need to end the game or not
     * @param p The main App
     */
    public static void checkGameState(App p){
        if(p.getInvaders().size() == 0){
            p.setNextLevel(true);
            if(p.getLevel() < 4){
                //We only increase the level of the game until shootFrames is a min of 60
                p.setLevel(p.getLevel()+1);
            }
        } else if(p.getInvaders().get(p.getInvaders().size()-1).getY() + 16 >= 390){ //Adding width (16)
            //If the invaders are 10px from the original barriers
            p.processGameOver();
        }
    }

//================================================================================
// Overridden Methods
//================================================================================

    /**
     * Overridden destroy method which also adds points to the score based upon
     * each Invaders points value
     * @param p The main App
     */
    @Override
    public void destroy(App p){
        if(!p.getToRemove().contains(this)){
            p.addObjectToRemove(this);
            p.setScore(p.getScore()+points);
        }
    }

//================================================================================
// Setter Methods
//================================================================================

    /**
     * Setter method for shootFrames
     * @param shootFrames the shootFrames to set
     */
    public static void setShootFrames(int shootFrames) {
    	Invader.shootFrames = shootFrames;
    }

    /**
     * Setter method for the amount of frames that has elapsed
     * @param frameCount the frameCount to set
     */
    public static void setFrameCount(int frameCount) {
    	Invader.frameCount = frameCount;
    }

//================================================================================
// Getter Methods - Only used for testing purposes
//================================================================================

    /**
     * Getter method for the points - Only used for testing
     * @return The points value for the invader
     */
    public int getPoints() {
    	return points;
    }

    /**
     * Getter method for the current framecount - Only used for testing
     * @return the current frame count
     */
    public static int getFrameCount() {
    	return frameCount;
    }

    /**
     * Getter method for the current amount of pixels moved
     * @return the amount of pixels ,oved
     */
    public int getPixelsMoved() {
    	return pixelsMoved;
    }
}
