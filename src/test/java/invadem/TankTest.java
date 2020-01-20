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

public class TankTest extends App{
    private PImage img;

    @Before
    public void setupTest(){
        img = new PImage(22, 16);
        PApplet.runSketch(new String[]{"--location=0,0", ""}, this);
        delay(1000);
    }

   @Test
   public void testTankConstruction() {
       Tank tank = new Tank(img, 0, 0, 0, 0, new int[] {0, 0});
       assertNotNull(tank);
   }

   @Test
   public void testTankGetters(){
       Tank tank = new Tank(img, 15, 20, 25, 30, new int[] {5, 5});
       assertEquals(Tank.getLeft(), 37);
       assertEquals(Tank.getRight(), 39);
       assertEquals(Tank.getSpace(), 32);
       assertEquals(Tank.getPause(), 80);
       assertEquals(tank.getX(), 15);
       assertEquals(tank.getY(), 20);
       assertEquals(tank.getWidth(), 25);
       assertEquals(tank.getHeight(), 30);
       assertEquals(tank.getHealth(), 3);
       assertEquals(tank.getImg(), img);
   }

   @Test
   public void testTankReduceHealth(){
       Tank tank = new Tank(img, 15, 20, 25, 30, new int[] {5, 5});
       tank.reduceHealth(1);
       assertEquals(tank.getHealth(), 2);
   }

   @Test
   public void testTankDestroy(){
       Tank tank = new Tank(img, 0, 0, 0, 0, new int[] {0, 0});

       this.setScore(1000000);
       tank.destroy(this);

       //Resets level & score & highscore
       assertTrue(this.getScore() == 0);
       assertTrue(this.getLevel() == 0);
       assertTrue(this.getHighScore() == 1000000);

       //Testing both branches if score > high score & score < high score
       this.setScore(50000);
       assertTrue(this.getHighScore() == 1000000);
   }

   @Test
   public void testTankTickMovement(){
       Tank tank = new Tank(img, 309, 464, 22, 16, new int[] {1, 1});
       Map<Integer, Boolean> testKeys = new HashMap<Integer, Boolean>();
       testKeys.put(37, true);
       testKeys.put(39, false);
       testKeys.put(32, false);

       //Movement to the left when right is not pressed
       tank.tick(testKeys, this);
       assertEquals(tank.getX(), 308);

       //Movement to the right when left is not pressed
       testKeys.replace(37, false);
       testKeys.replace(39, true);
       tank.tick(testKeys, this);
       assertEquals(tank.getX(), 309);

       //When left and right are both pressed
       testKeys.replace(37, true);
       tank.tick(testKeys, this);
       assertEquals(tank.getX(), 309);
   }

   @Test
   public void testTankMovementBorders(){
       Tank tankLeftBorder = new Tank(img, 180, 464, 22, 16, new int[] {1, 1});
       Tank tankRightBorder = new Tank(img, 460-22, 464, 22, 16, new int[] {1, 1});
       Map<Integer, Boolean> testKeys = new HashMap<Integer, Boolean>();
       testKeys.put(37, true);
       testKeys.put(39, false);
       testKeys.put(32, false);

       //Movement to the left when tank is at left boundary
       tankLeftBorder.tick(testKeys, this);
       assertEquals(tankLeftBorder.getX(), 180);

       //Movement to the left when tank is at right boundary
       tankRightBorder.tick(testKeys, this);
       assertEquals(tankRightBorder.getX(), 460-22-1); //Subtracting width and 1 pixel

       //Movement to the right when tank is at left boundary
       testKeys.replace(39, true);
       testKeys.replace(37, false);
       tankLeftBorder.tick(testKeys, this);
       assertEquals(tankLeftBorder.getX(), 181);

       //Movement to the right when tank is at right boundary
       tankRightBorder.tick(testKeys, this);
       tankRightBorder.tick(testKeys, this); //We tick twice as we moved to left 1px above
       assertEquals(tankRightBorder.getX(), 460-22);
   }

   @Test
   public void testTankProjectile() {
       Tank tank = new Tank(img, 200, 464, 16, 16, new int[] {1, 1});
       Map<Integer, Boolean> testKeys = new HashMap<Integer, Boolean>();
       testKeys.put(37, false);
       testKeys.put(39, false);
       testKeys.put(32, true);

       //Firing when tank is static
       this.getProjectiles().clear();
       tank.tick(testKeys, this);
       assertEquals(this.getProjectiles().size(), 1);
       assertEquals(this.getProjectiles().get(0).getX(), 208);
       assertEquals(this.getProjectiles().get(0).getY(), 464);
       assertEquals(this.getProjectiles().get(0).getWidth(), 1);
       assertEquals(this.getProjectiles().get(0).getHeight(), 3);

       //Firing when still pressed (i.e. fired = true)
       tank.tick(testKeys, this);
       assertEquals(this.getProjectiles().size(), 1);

       //Testing setFired
       tank.setFired(false);
       tank.tick(testKeys, this);
       assertEquals(this.getProjectiles().size(), 2);

       //Firing when moving left
       testKeys.replace(37, true);
       tank.setFired(false);
       tank.tick(testKeys, this);
       assertEquals(this.getProjectiles().get(2).getX(), 207);

       //Firing when moving right
       testKeys.replace(37, false);
       testKeys.replace(39, true);
       tank.setFired(false);
       tank.tick(testKeys, this);
       assertEquals(this.getProjectiles().get(3).getX(), 208);

       //Firing when both left and right pressed
       testKeys.replace(37, true);
       tank.setFired(false);
       tank.tick(testKeys, this);
       assertEquals(this.getProjectiles().get(4).getX(), 208);

   }

}
