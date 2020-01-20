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

public class ProjectileTest extends App {

    private PImage img;
    private PImage[] sprites;

    @Before
    public void setupTest(){
        img = new PImage(5, 10);
        sprites = new PImage[2];
        sprites[0] = new PImage(2, 4);
        sprites[1] = new PImage(5, 3);

        PApplet.runSketch(new String[]{"--location=0,0", ""}, this);
        delay(1000);
    }


   @Test
   public void testProjectileConstruction() {
       Projectile projPower = new Projectile(img, 111, 243, 2, 5, new int[] {1, 1}, true, true);
       Projectile projRegular = new Projectile(img, 222, 112, 4, 6, new int[] {1, 1}, false, false);

       //Teting for power projectile
       assertNotNull(projPower);
       assertEquals(projPower.getDamage(), 3);
       assertEquals(projPower.getFiredFromTank(), true);
       assertEquals(projPower.getX(), 111);
       assertEquals(projPower.getY(), 243);
       assertEquals(projPower.getWidth(), 2);
       assertEquals(projPower.getHeight(), 5);
       assertEquals(projPower.getImg(), img);

       //Testing for regular projectile
       assertNotNull(projRegular);
       assertEquals(projRegular.getDamage(), 1);
       assertEquals(projRegular.getFiredFromTank(), false);
       assertEquals(projRegular.getX(), 222);
       assertEquals(projRegular.getY(), 112);
       assertEquals(projRegular.getWidth(), 4);
       assertEquals(projRegular.getHeight(), 6);
       assertEquals(projRegular.getImg(), img);
   }


   @Test
   public void testProjectileIntersect() {
       //They intersect and only one damage happens
       Projectile proj = new Projectile(img, 135, 200, 20, 20, new int[] {1, 1}, true, false);
       Invader inv = new ArmouredInvader(sprites, 150, 215, 20, 20, new int[] {2, 2});


       assertEquals(inv.getHealth(), 3);
       this.getToRemove().clear();
       proj.checkCollision(inv, this);
       assertEquals(inv.getHealth(), 2);
       assertTrue(this.getToRemove().contains(proj));
   }

   @Test
   public void testCollisionNonintersecting() {
       //They dont overlap
       Projectile proj = new Projectile(img, 135, 200, 4, 6, new int[] {1, 1}, true, false);
       Invader inv = new ArmouredInvader(sprites, 150, 215, 15, 15, new int[] {2, 2});

       assertEquals(inv.getHealth(), 3);
       proj.checkCollision(inv, this);
       assertEquals(inv.getHealth(), 3);
   }

   @Test
   public void testCollisionNonintersectingEdge() {
       //Edge case where they just barely dont overlap
       Projectile proj = new Projectile(img, 135, 200, 1, 1, new int[] {1, 1}, true, false);
       Invader inv = new ArmouredInvader(sprites, 137, 200, 1, 1, new int[] {2, 2});

       assertEquals(inv.getHealth(), 3);
       proj.checkCollision(inv, this);
       assertEquals(inv.getHealth(), 3);
   }

   @Test
   public void testCollisionBothDestroyed() {
       //Intersect and object is destroyed
       Projectile proj = new Projectile(img, 135, 200, 20, 20, new int[] {1, 1}, true, true);
       Invader inv = new ArmouredInvader(sprites, 150, 215, 20, 20, new int[] {2, 2});

       assertEquals(inv.getHealth(), 3);
       proj.checkCollision(inv, this);
       assertEquals(inv.getHealth(), 0);
       assertTrue(this.getToRemove().contains(proj));
       assertTrue(this.getToRemove().contains(inv));

   }

   @Test
   public void testTickMovement(){
       //Movement up and down
       Projectile projTank = new Projectile(img, 111, 243, 2, 5, new int[] {1, 1}, true, true);
       Projectile projInv = new Projectile(img, 222, 112, 4, 6, new int[] {1, 1}, false, false);

       assertEquals(projTank.getY(), 243);
       assertEquals(projInv.getY(), 112);
       projTank.tick(this);
       projInv.tick(this);
       assertEquals(projTank.getY(), 242);
       assertEquals(projInv.getY(), 113);
   }

   @Test
   public void testTickOffScreen(){
       //When the projectile is off the screen
       Projectile projTank = new Projectile(img, 111, 600, 2, 5, new int[] {1, 1}, true, true);
       Projectile projInv = new Projectile(img, 222, -30, 4, 6, new int[] {1, 1}, false, false);

       this.getToRemove().clear();
       projTank.tick(this);
       projInv.tick(this);
       assertTrue(this.getToRemove().contains(projTank));
       assertTrue(this.getToRemove().contains(projInv));
   }

   @Test
   public void testTickCollisions(){
       //Checking things are being checked for collision
       Projectile projTank = new Projectile(img, 193, 100, 200, 200, new int[] {1, 1}, true, true);
       Projectile projInv = new Projectile(img, 208, 400, 200, 200, new int[] {1, 1}, false, false);
       //We set the two projectiles at large widths and height so they collide
       //And we check if destroy() has been called

       projTank.tick(this);
       projInv.tick(this);
       assertTrue(this.getToRemove().contains(projTank));
       assertTrue(this.getToRemove().contains(projInv));
   }
}
