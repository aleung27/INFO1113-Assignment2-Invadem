package invadem.assets;

import processing.core.PImage;

/**
 * Represents the parts of the barrier
 */
public class BarrierParts extends GameObject{

//================================================================================
// Properties
//================================================================================

    /** Stores the array of sprites being used for animation */
    private PImage[] sprites;

//================================================================================
// Constructor
//================================================================================

    /**
     * Constructor for the barrier object, using the super constructor
     * from gameObject.
     * @param img The sprite associated with the object
     * @param x The x coordinate associated with the TOP-LEFT of the object
     * @param y The y coordinate associated with the TOP-LEFT of the object
     * @param width The width of the object
     * @param height The height of the object
     * @param velocity The velocity (x and y respectively) of the object
     */
    public BarrierParts(PImage[] sprites, int x, int y, int width, int height, int[] velocity){
        super(x, y, width, height, velocity);
        this.sprites = sprites;
        img = sprites[0];
        health = 3; //Each barrierpart has 3 health
    }

//================================================================================
// Overridden Methods
//================================================================================

    /**
     * Overridden reduceHealth method so we can update the sprite at the same time
     * @param damage The amount of damage to reduce health by
     */
    @Override
    public void reduceHealth(int damage){
        health -= damage;

        if(health > 0){
            //Update image sprite only when its health > 0 or else we destory object
            img = sprites[3-health];
        }
    }
}
