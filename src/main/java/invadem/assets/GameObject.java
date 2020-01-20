package invadem.assets;

import processing.core.PApplet;
import processing.core.PImage;

import invadem.App;

/**
 * Represents all objects in the game.
 * Most objects inherit from this class
 */
public class GameObject{

//================================================================================
// Properties
//================================================================================

    /** Sprite for each object */
    protected PImage img;

    /** Stores the x and y coordinate respectively */
    protected int x, y;

    /** Stores the width and height respectively */
    protected int width, height;

    /** Stores the postive values for x and y velocity (size 2) */
    protected int[] velocity;

    /** Stores the health of the GameObject*/
    protected int health;

//================================================================================
// Constructors
//================================================================================

    /**
     * Constructor for the object
     * @param img The sprite associated with the object
     * @param x The x coordinate associated with the TOP-LEFT of the object
     * @param y The y coordinate associated with the TOP-LEFT of the object
     * @param width The width of the object
     * @param height The height of the object
     * @param velocity The velocity (x and y respectively) of the object
     */
    public GameObject(PImage img, int x, int y, int width, int height, int[] velocity){
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocity= velocity;
        health = 1; //Default value for health
    }

    /**
     * Alternative constructor for the object when a list of images/no image is needed
     * @param x The x coordinate associated with the TOP-LEFT of the object
     * @param y The y coordinate associated with the TOP-LEFT of the object
     * @param width The width of the object
     * @param height The height of the object
     * @param velocity The velocity (x and y respectively) of the object
     */
    public GameObject(int x, int y, int width, int height, int[] velocity){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocity= velocity;
    }

//================================================================================
// Methods
//================================================================================

    /**
     * Renders the sprite of the gameobject onto the screen
     * @param p The PApplet which the image will be drawn to
     */
    public void draw(PApplet p){
            p.image(img, x, y, width, height);
    }

    /**
     * Movement of the object to the right and left based on velocity
     * @param isRight Moves to right if true or else object moves left
     */
    public void moveX(boolean isRight){
        if(isRight){
            this.x += velocity[0];
        } else{
            this.x -= velocity[0];
        }
    }

    /**
     * Movement of the object upwards and downwards based on velocity
     * @param isDown Moves downwards if true or else object moves upward
     */
     public void moveY(boolean isDown){
        if(isDown){
            this.y += velocity[1];
        } else{
            this.y -= velocity[1];
        }
    }

    /**
     * Removes object from the game
     * @param p The application which stores a list of the object
     */
    public void destroy(App p){
        if(!p.getToRemove().contains(this)){
            p.addObjectToRemove(this);
        }
    }

    /**
     * Reduces the health of the current GameObject by the damage amount
     * @param damage Amount to reduce health by
     */
    public void reduceHealth(int damage){
        health -= damage;
    }

//================================================================================
// Getter Methods
//================================================================================

    /**
     * Getter method for the image - Only used for testing
     * @return The current image associated with the object
     */
    public PImage getImg() {
    	return img;
    }

    /**
     * Getter method for the x coordinate
     * @return The x coordinate of the object
     */
    public int getX(){
        return x;
    }

    /**
     * Getter method for the y coordinate
     * @return The y coordinate of the object
     */
    public int getY(){
        return y;
    }

    /**
     * Getter method for the width of the object
     * @return The width of the object
     */
    public int getWidth(){
        return width;
    }

    /**
     * Getter method for the height of the object
     * @return The width of the object
     */
    public int getHeight(){
        return height;
    }

    /**
     * Getter method for the health of the GameObject
     * @return The health of the GameObject
     */
    public int getHealth(){
        return health;
    }
}
