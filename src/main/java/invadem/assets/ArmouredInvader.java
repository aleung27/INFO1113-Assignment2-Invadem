package invadem.assets;

import processing.core.PImage;

public class ArmouredInvader extends Invader{

    public ArmouredInvader(PImage[] sprites, int x, int y, int width, int height, int[] velocity){
        super(sprites, x, y, width, height, velocity);
        points = 250;
        health = 3;
    }

}
