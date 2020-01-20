package invadem.assets;

import processing.core.PImage;

import invadem.App;

/**
 * Represents projectiles, from both enemies and tanks, in the game
 */
public class Projectile extends GameObject{

//================================================================================
// Properties
//================================================================================

    /**
     * Whether the projectile is from the tank or the invaders
     */
    private boolean firedFromTank;

    /** Damage that the projectile does */
    private int damage;

//================================================================================
// Constructor
//================================================================================

    /**
     * Constructor for the projectile object, using the super constructor
     * from GameObject.
     * @param img The sprite associated with the object
     * @param x The x coordinate associated with the TOP-LEFT of the object
     * @param y The y coordinate associated with the TOP-LEFT of the object
     * @param width The width of the object
     * @param height The height of the object
     * @param velocity The velocity (x and y respectively) of the object
     * @param firedFromTank Whether the projectle is from the tank or not
     */
    public Projectile(PImage img, int x, int y, int width, int height, int[] velocity, boolean firedFromTank, boolean isPower){
       super(img, x, y, width, height, velocity);
       this.firedFromTank = firedFromTank;

       if(isPower){
           //We instead use a boolean value rather than a new projectile class due to the vast similarites
           damage = 3;
       } else {
           damage = 1;
       }
    }

//================================================================================
// Methods
//================================================================================

    /**
     * Logic behind the projectile object
     * @param p The main App
     */
    public void tick(App p){
        if(firedFromTank){
            this.moveY(false); //Move up
        } else {
            this.moveY(true); //Move down
        }

        if(y < 0 || y > 480){
            //Destroying projectile when it is off the screen
            destroy(p);
        }

        if(firedFromTank){
            //Checking collision with each barrierpart & invader
            p.getBarrierParts().stream().forEach((BarrierParts b) -> checkCollision(b, p));
            p.getInvaders().stream().forEach((Invader i) -> checkCollision(i, p));
        } else {
            //Checking collision with each barrierpart & tank
            p.getBarrierParts().stream().forEach((BarrierParts b) -> checkCollision(b, p));
            checkCollision(p.getPlayer(), p);
        }
    }

    /**
     * Logic behind whether GameObject O has a collision with the current projectile instance
     * @param o The other GameObject we are checking for collisions with
     * @param p The main app
     */
    public void checkCollision(GameObject o, App p){
        if((x < o.getX() + o.getWidth()) &&
            (x + width > o.getX()) &&
            (y < o.getY() + o.getHeight()) &&
            (height + y > o.getY())){
                //Checking for collision based upon image sizes
                destroy(p);
                o.reduceHealth(damage);

                if(o.getHealth() <= 0){
                    o.destroy(p);
                }
            }
    }

//================================================================================
// Getter Methods - All for testing
//================================================================================

    /**
     * Getter method for whether the projectile was fired from a tank
     * @return Whether it was fired from a tank or not
     */
    public boolean getFiredFromTank(){
        return firedFromTank;
    }

    /**
     * Getter method for the damage of the projectile
     * @return the damage of the projectile
     */
    public int getDamage() {
    	return damage;
    }

}
