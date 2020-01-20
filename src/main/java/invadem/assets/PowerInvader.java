package invadem.assets;

import processing.core.PImage;

public class PowerInvader extends Invader{

    public PowerInvader(PImage[] sprites, int x, int y, int width, int height, int[] velocity){
        super(sprites, x, y, width, height, velocity);
        points = 250;
    }

}
