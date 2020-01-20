package invadem;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

import processing.core.PApplet;
import processing.core.PImage;

import invadem.assets.*;
import invadem.App;

public class InvaderTest extends App{
    private PImage[] sprites;

    @Before
    public void testSetup(){
        sprites = new PImage[2];
        sprites[0] = new PImage(10, 20);
        sprites[1] = new PImage(20, 10);
        PApplet.runSketch(new String[]{"--location=0,0", ""}, this);
        delay(1000);
    }

   @Test
   public void testInvaderConstruction() {
       Invader inv = new Invader(sprites, 100, 200, 10, 10, new int[] {1, 1});
       Invader invPower = new PowerInvader(sprites, 200, 100, 50, 50, new int[] {1, 1});
       Invader invArmoured = new ArmouredInvader(sprites, 150, 20, 20, 20, new int[] {1, 1});
       assertNotNull(inv);
       assertNotNull(invPower);
       assertNotNull(invArmoured);

       //Testing getters and values for constructor
       assertEquals(inv.getX(), 100);
       assertEquals(inv.getY(), 200);
       assertEquals(inv.getWidth(), 10);
       assertEquals(inv.getHeight(), 10);
       assertEquals(inv.getHealth(), 1);
       assertEquals(inv.getImg(), sprites[0]);
       assertEquals(inv.getPoints(), 100);

       //Testing getters and values for Power Invader
       assertEquals(invPower.getX(), 200);
       assertEquals(invPower.getY(), 100);
       assertEquals(invPower.getWidth(), 50);
       assertEquals(invPower.getHeight(), 50);
       assertEquals(invPower.getHealth(), 1);
       assertEquals(invPower.getImg(), sprites[0]);
       assertEquals(invPower.getPoints(), 250);

       //Testing getters and values for Armoured invader
       assertEquals(invArmoured.getX(), 150);
       assertEquals(invArmoured.getY(), 20);
       assertEquals(invArmoured.getWidth(), 20);
       assertEquals(invArmoured.getHeight(), 20);
       assertEquals(invArmoured.getHealth(), 3);
       assertEquals(invArmoured.getImg(), sprites[0]);
       assertEquals(invArmoured.getPoints(), 250);
   }

   @Test
   public void testInvaderDestroy(){
       //Armoured Invader being destroyed
       Invader toDestroyArmoured = this.getInvaders().get(0);

       assertTrue(this.getInvaders().contains(toDestroyArmoured));
       assertEquals(this.getScore(), 0);
       toDestroyArmoured.destroy(this);
       assertTrue(this.getToRemove().contains(toDestroyArmoured));
       assertEquals(this.getScore(), 250);

       //Power invader being destroyed
       Invader toDestroyPower = this.getInvaders().get(10);

       assertTrue(this.getInvaders().contains(toDestroyPower));
       assertEquals(this.getScore(), 250);
       toDestroyPower.destroy(this);
       assertTrue(this.getToRemove().contains(toDestroyPower));
       assertEquals(this.getScore(), 500);

       //Regular invader being destroyed
       Invader toDestroyRegular = this.getInvaders().get(20);

       assertTrue(this.getInvaders().contains(toDestroyRegular));
       assertEquals(this.getScore(), 500);
       toDestroyRegular.destroy(this);
       assertTrue(this.getToRemove().contains(toDestroyRegular));
       assertEquals(this.getScore(), 600);
   }

   @Test
   public void testInvaderDestroyExistsAlready(){
       //When the invader has already been added to the list of items to remove
       //E.g. when two projectiles hit it at same time
       Invader toDestroy = this.getInvaders().get(32);

       assertEquals(this.getToRemove().size(), 0);
       toDestroy.destroy(this);
       assertEquals(this.getToRemove().size(), 1);
       toDestroy.destroy(this);
       assertEquals(this.getToRemove().size(), 1);
   }

   @Test
   public void testCheckGameStateNextLevel(){
       //Testing the checkgamestate function for the first branch - next level
       this.getInvaders().clear();
       assertEquals(this.getInvaders().size(), 0);
       assertEquals(this.getLevel(), 0);
       Invader.checkGameState(this);
       assertEquals(this.getLevel(), 1);

       //Testing when level is already at max
       Invader.checkGameState(this);
       assertEquals(this.getLevel(), 2);
       Invader.checkGameState(this);
       assertEquals(this.getLevel(), 3);
       Invader.checkGameState(this);
       assertEquals(this.getLevel(), 4);
       Invader.checkGameState(this);
       assertEquals(this.getLevel(), 4);
   }

   @Test
   public void testCheckGameStateGameOver(){
       //Testing when invaders get within 10px of barrier

       //When invader is below the 10px boundary
       this.getInvaders().clear();
       this.setScore(50000);
       this.setLevel(1);
       this.getInvaders().add(new Invader(sprites, 100, 400, 16, 16, new int[] {1,1}));
       Invader.checkGameState(this);

       assertEquals(this.getHighScore(), 50000);
       assertEquals(this.getScore(), 0);
       assertEquals(this.getLevel(), 0);

       //When invader is above the 10px boundary
       this.getInvaders().clear();
       this.setScore(70000);
       this.setLevel(1);
       this.getInvaders().add(new Invader(sprites, 100, 300, 16, 16, new int[] {1,1}));
       Invader.checkGameState(this);

       assertEquals(this.getHighScore(), 50000);
       assertEquals(this.getScore(), 70000);
       assertEquals(this.getLevel(), 1);

       //When invader is exactly within 10px boundary
       this.getInvaders().clear();
       this.setScore(70000);
       this.setLevel(1);
       this.getInvaders().add(new Invader(sprites, 100, 374, 16, 16, new int[] {1,1}));
       Invader.checkGameState(this);

       assertEquals(this.getHighScore(), 70000);
       assertEquals(this.getScore(), 0);
       assertEquals(this.getLevel(), 0);

   }

   @Test
   public void testProjectileTick(){
       //Normal call when no invader should fire yet
       Invader.setFrameCount(0);
       Invader.projectileTick(this);
       assertEquals(Invader.getFrameCount(), 1);

       //When an invader fires
       this.getProjectiles().clear();
       Invader.setFrameCount(119);
       Invader.setShootFrames(120);
       Invader.projectileTick(this);
       assertEquals(this.getProjectiles().size(), 1);
       assertEquals(Invader.getFrameCount(), 0);

       //When a Power invader fires
       this.getInvaders().clear();
       this.getProjectiles().clear();
       this.getInvaders().add(new PowerInvader(sprites, 100, 200, 16, 16, new int[] {1, 1}));
       Invader.setFrameCount(119);
       Invader.setShootFrames(120);
       Invader.projectileTick(this);
       assertEquals(this.getProjectiles().get(0).getDamage(), 3);
       assertEquals(Invader.getFrameCount(), 0);


      //When a Normal/Armoured invader fires
      this.getInvaders().clear();
      this.getProjectiles().clear();
      this.getInvaders().add(new Invader(sprites, 100, 200, 16, 16, new int[] {1, 1}));
      Invader.setFrameCount(119);
      Invader.setShootFrames(120);
      Invader.projectileTick(this);
      assertEquals(this.getProjectiles().get(0).getDamage(), 1);
      assertEquals(Invader.getFrameCount(), 0);
   }

   @Test
   public void testTickMovementRight(){
       //Movement to the right
       Invader inv = new Invader(sprites, 114, 222, 8, 10, new int[] {1, 1});
       inv.tick(this);
       assertEquals(inv.getX(), 115);
       assertEquals(inv.getPixelsMoved(), 1);
       assertEquals(inv.getImg(), sprites[0]);

       //Testing for movement downwards
       for(int i = 0; i < 59; i++){
           inv.tick(this);
       }
       assertEquals(inv.getX(), 144);
       inv.tick(this);
       inv.tick(this);
       assertEquals(inv.getY(), 223);

       for(int i = 0; i < 14; i++){
           inv.tick(this);
       }
       assertEquals(inv.getX(), 144);
       assertEquals(inv.getY(), 230);

       //Testing for movement leftwards
       for(int i = 0; i < 60; i++){
           inv.tick(this);
       }
       assertEquals(inv.getX(), 114);
       assertEquals(inv.getY(), 230);

       //Testing for movement downwards
       for(int i = 0; i < 16; i++){
           inv.tick(this);
       }
       assertEquals(inv.getX(), 114);
       assertEquals(inv.getY(), 238);

       //Testing for reset
       inv.tick(this);
       inv.tick(this);
       assertEquals(inv.getX(), 115);
       assertEquals(inv.getY(), 238);

   }

   @Test
   public void testTickNoMovement(){
       Invader inv = new Invader(sprites, 114, 222, 8, 10, new int[] {1, 1});
       inv.tick(this);
       assertEquals(inv.getPixelsMoved(), 1);
       inv.tick(this);
       assertEquals(inv.getPixelsMoved(), 1);
   }

}
