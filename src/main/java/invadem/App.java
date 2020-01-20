package invadem;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import invadem.assets.*;

public class App extends PApplet{

//================================================================================
// Properties
//================================================================================

    /** List of Invader instances which are in the game */
    private List<Invader> invaders;

    /** List of BarrierParts instances which are in the game */
    private List<BarrierParts> barriers;

    /** List of Projectile instances which are in the game */
    private List<Projectile> projectiles;

    /** List of GameObjects which we need to destroy due to a collision with a projectile */
    private List<GameObject> toRemove;

    /** A Map which stores valid key-value pairs for player controls */
    private Map<Integer, Boolean> keys;

    /** GameOver Screen instance */
    private Screen gameOverScreen;

    /** NextLevel Screen instance */
    private Screen nextLevelScreen;

    /** Array of the heart icons used for the HUD (Extension)*/
    private GameObject[] heart;

    /** Sprite for the heart */
    private PImage heartSprite;

    /**
     * Array of sprites for animation for the invaders. Armoured invader, power invader and
     * regular invader stored at index 0, 1, 2 respectively.
     */
    private PImage[][] invaderSprites;

    /**
     * Array of sprites for the barrierParts. Left, top, right and solid are stored
     * at index 0, 1, 2, 3 respectively.
     */
    private PImage[][] barrierSprites;

    /** Sprite for the regular projectile */
    private PImage projectileRegularSprite;

    /** Sprite for the power projectile */
    private PImage projectilePowerSprite;

    /** The tank instance which the player controls */
    private Tank player;

    /** The font used to write text in the game */
    private PFont font;

    /** Boolean which stores whether we should load the gameOver screen and restart*/
    private boolean loadGameOver;

    /** Boolean which stores whether we should load the next level screen and restart */
    private boolean loadNextLevel;

    /** The current level the player is on (can be 0-4) to cap fire speed of invaders */
    private int level;

    /** The current score of the player */
    private int score;

    /** The current high score */
    private int highScore;

    /** Keeps track of whether we had just pressed and released a key.
     * Used for the pause screen
     */
    private boolean justPressed;


//================================================================================
// Constructor
//================================================================================

    /**
     * Constructor for app, setup variables here
     */
    public App() {
        invaders = new ArrayList<Invader>();
        barriers = new ArrayList<BarrierParts>();
        projectiles = new ArrayList<Projectile>();
        toRemove = new ArrayList<GameObject>();
        heart = new GameObject[3];

        keys = new HashMap<Integer, Boolean>();

        invaderSprites = new PImage[3][2];
        barrierSprites = new PImage[4][3];

        loadGameOver = false;
        loadNextLevel = false;
        justPressed = false;
        level = 0;
        score = 0;
        highScore = 10000;

        //As only left, right & space are responsible for actions we add these into the map
        keys.put(Tank.getLeft(), false); //Left arrow key
        keys.put(Tank.getRight(), false); //Right arrow key
        keys.put(Tank.getSpace(), false); //Spacebar
        keys.put(Tank.getPause(), false); //Pause key (p)
    }

//================================================================================
// Methods
//================================================================================

    /**
     * Resets the instances of the game for either the next level or game over
     */
    public void restart(){
        //Clearing all instances
        invaders.clear();
        barriers.clear();
        projectiles.clear();

        //Reinstantiating by calling setup()
        setup();
    }

    /**
     * Method for loading all the sprites into their appropriate variables
     */
    public void loadSprites(){
        //Loading invader sprites
        for(int i = 0; i < 2; i++){
            invaderSprites[0][i] = loadImage("invader" + Integer.toString(i+1) + "_armoured.png");
            invaderSprites[1][i] = loadImage("invader" + Integer.toString(i+1) + "_power.png");
            invaderSprites[2][i] = loadImage("invader" + Integer.toString(i+1) + ".png");
        }

        //Loading BarrierParts Sprites
        for(int i = 0; i < 3; i++){
            barrierSprites[0][i] = loadImage("barrier_left" + Integer.toString(i+1) + ".png");
            barrierSprites[1][i] = loadImage("barrier_top" + Integer.toString(i+1) + ".png");
            barrierSprites[2][i] = loadImage("barrier_right" + Integer.toString(i+1) + ".png");
            barrierSprites[3][i] = loadImage("barrier_solid" + Integer.toString(i+1) + ".png");
        }

        //Loading Projectile Sprites and heart Sprite
        projectileRegularSprite = loadImage("projectile.png");
        projectilePowerSprite = loadImage("projectile_lg.png");
        heartSprite = loadImage("heart.png");
    }

    /**
     * Method for setting up instances and sprites
     */
    public void setup() {
        loadSprites();
        frameRate(60);
        Invader.setShootFrames(60*(5-level));
        Invader.setFrameCount(0);

        font = createFont("PressStart2P-Regular.ttf", 12);
        textFont(font);

        //New tank with width and height (22, 16) which is centered (309, 464)
        player = new Tank(loadImage("tank1.png"), 309, 464, 22, 16, new int[] {1, 0});

        //New heart GameObjects for use in the HUD
        for(int i = 0; i < 3; i++){
            //We render at y = -2 to centre due to image size
            heart[i] = new GameObject(heartSprite, 340 + 20*i, -2, 16, 16, new int[] {0, 0});
        }

        //New Screens with respective widths and height which is centred (264, 232)
        gameOverScreen = new Screen(loadImage("gameover.png"), 264, 232, 112, 16);
        nextLevelScreen = new Screen(loadImage("nextlevel.png"), 264, 232, 122, 16);

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 10; j++){
                //Invaders generated with a gap of (23px, 50px) to form a grid

                if(i == 0){
                    //First row has Armoured Invaders
                    invaders.add(new ArmouredInvader(invaderSprites[i],
                    193 + 23*j, 100 + 50*i, 16, 16, new int[] {1, 1}));
                }else if(i == 1){
                    //Second row has Power Invaders
                    invaders.add(new PowerInvader(invaderSprites[i],
                    193 + 23*j, 100 + 50*i, 16, 16, new int[] {1, 1}));
                } else{
                    //Third and fourth rows have regular invaders
                    invaders.add(new Invader(invaderSprites[2],
                    193 + 23*j, 100 + 50*i, 16, 16, new int[] {1, 1}));
                }
            }
        }

        for(int i = 0; i < 3; i++){
            //Adding all the parts which make up a barrier
            barriers.add(new BarrierParts(barrierSprites[0], 208+100*i, 400, 8, 8, new int[] {0, 0}));
            barriers.add(new BarrierParts(barrierSprites[1], 216+100*i, 400, 8, 8, new int[] {0, 0}));
            barriers.add(new BarrierParts(barrierSprites[2], 224+100*i, 400, 8, 8, new int[] {0, 0}));
            barriers.add(new BarrierParts(barrierSprites[3], 208+100*i, 400 + 8, 8, 8, new int[] {0, 0}));
            barriers.add(new BarrierParts(barrierSprites[3], 224+100*i, 400 + 8, 8, 8, new int[] {0, 0}));
            barriers.add(new BarrierParts(barrierSprites[3], 208+100*i, 400 + 2*8, 8, 8, new int[] {0, 0}));
            barriers.add(new BarrierParts(barrierSprites[3], 224+100*i, 400 + 2*8, 8, 8, new int[] {0, 0}));
        }
    }

    /**
     * Settings for the PApplet
     */
    public void settings() {
        size(640, 480);
    }

    /**
     * Called once every frame of the game, main logic of game here
     */
    public void draw() {
        //We render the objects twice per draw() to account for changes
        render();
        tick();
        render();
    }

    /**
     * Update instances, perform logic etc. here
     */
    public void tick(){
        if(loadNextLevel){
            //We tick the nextLevelScreen instead
            nextLevelScreen.tick(this);
        } else if(loadGameOver){
            //We tick the gameOverScreen instead
            gameOverScreen.tick(this);
        } else if(!keys.get(Tank.getPause())){
            //Logic for each object called first
            Invader.projectileTick(this);
            player.tick(keys, this);
            invaders.stream().forEach((Invader i) -> i.tick(this));
            projectiles.stream().forEach((Projectile p) -> p.tick(this));

            //Destroy any objects which have had a collision
            destroy();

            //Checks whether game is over or not
            Invader.checkGameState(this);
        }
    }

    /**
     * Called twice every draw(), in charge of actually displaying objects
     */
    public void render(){
        background(0);

        if(loadNextLevel){
            //We draw the nextLevelScreen instead
            nextLevelScreen.draw(this);
        } else if(loadGameOver){
            //We draw the gameOverScreen instead
            gameOverScreen.draw(this);
        } else if(keys.get(Tank.getPause())){
            textAlign(CENTER, CENTER);
            text("PAUSED", 320, 240);
        } else{
            //Drawing of the score and highscore
            textAlign(LEFT, TOP);
            text("Current Score:" + score, 0, 0);
            textAlign(LEFT, TOP);
            text("Health:" , 255, 0);
            textAlign(RIGHT, TOP);
            text("High Score:" + highScore, 640, 0);

            //Drawing of each instance
            player.draw(this);
            barriers.stream().forEach((BarrierParts b) -> b.draw(this));
            invaders.stream().forEach((Invader i) -> i.draw(this));
            projectiles.stream().forEach((Projectile p) -> p.draw(this));
            for(int i = 0; i < player.getHealth(); i++){
                heart[i].draw(this);
            }
        }

    }

    /**
     * Adds a projectile to the list of instances we have
     * @param x             x coordinate of projectile
     * @param y             y coordinate of projectile
     * @param firedFromTank True if fired from tank otherwise false for invaders
     * @param isPower       True if its a power projectile which does more damage
     */
    public void addProjectile(int x, int y, boolean firedFromTank, boolean isPower){
        if(isPower){
            projectiles.add(new Projectile(projectilePowerSprite, x, y, 2, 5, new int[] {0, 1}, firedFromTank, isPower));
        } else{
            projectiles.add(new Projectile(projectileRegularSprite, x, y, 1, 3, new int[] {0, 1}, firedFromTank, isPower));
        }
    }

    /**
     * Adds an object to the list of GameObjects which need to be removed
     * @param o The GameObject to remove
     */
    public void addObjectToRemove(GameObject o){
        toRemove.add(o);
    }

    /**
     * Removes the instances corresponding to those in the
     * toRemove list
     */
    public void destroy(){
        barriers.removeAll(toRemove);
        invaders.removeAll(toRemove);
        projectiles.removeAll(toRemove);
        toRemove.clear();
    }


    public void processGameOver(){
        if(highScore < getScore()){
            highScore = score;
        }

        setGameOver(true);
        setLevel(0);
        setScore(0);
    }

//================================================================================
// Overridden Methods
//================================================================================

    /**
     * Overridden method so that when a key is released, we swap the value in the
     * keys map to false, indicating it is not being pressed
     */
    @Override
    public void keyReleased(){
        if(keys.containsKey(keyCode)){
            if(keyCode == Tank.getPause()){
                justPressed = false;
            } else {
                keys.replace(keyCode, false);

                if(keyCode == Tank.getSpace()){
                    //When space is released, we allow the tank to fire again.
                    player.setFired(false);
                }
            }
        }
    }

    /**
     * Overridden method so the when a key is pressed, we set its mapped value
     * to true, indicating it is being pressed
     */
    @Override
    public void keyPressed(){
        if(keys.containsKey(keyCode)){
            if(keyCode == Tank.getPause() && !justPressed){
                //With the pause key, we only want to switch state once per time
                //it is pressed, regardless of holding it down
                keys.replace(keyCode, !keys.get(keyCode));
                justPressed = true;
            } else keys.replace(keyCode, true);
        }
    }


//================================================================================
// Setter Methods
//================================================================================

    /**
     * Setter method for the value of loadGameOver
     * @param loadGameOver Whether we load the game over or not
     */
    public void setGameOver(boolean loadGameOver){
        this.loadGameOver = loadGameOver;
    }

    /**
     * Setter method for the value of loadNextLevel
     * @param loadNextLevel Whether we load the next level or not
     */
    public void setNextLevel(boolean loadNextLevel){
        this.loadNextLevel = loadNextLevel;
    }

    /**
     * Setter method for the value of level
     * @param level The level to set to (0-4)
     */
    public void setLevel(int level){
        this.level = level;
    }

    /**
     * Setter method for the value of score
     * @param score The score to set to
     */
    public void setScore(int score){
        this.score = score;
    }

//================================================================================
// Getter Methods
//================================================================================

    /**
     * Getter for the keys - Only used for testing
     * @return the map of keys
     */
    public Map<Integer, Boolean> getKeys() {
    	return keys;
    }

    /**
     * Getter for high Score - Only used for testing
     * @return the highScore
     */
    public int getHighScore() {
    	return highScore;
    }

    /**
     * Getter for projectiles - Only used for testing
     * @return the list of projectiles
     */
    public List<Projectile> getProjectiles() {
    	return projectiles;
    }

    /**
     * Getter method for the current score
     * @return The current score
     */
    public int getScore(){
        return score;
    }

    /**
     * Getter method for the current level
     * @return The current level
     */
    public int getLevel(){
        return level;
    }

    /**
     * Getter method for the current invaders in the game
     * @return The list of invader instances
     */
    public List<Invader> getInvaders(){
        return invaders;
    }

    /**
     * Getter method for the current BarrierParts in the game
     * @return The list of barrierPart instances
     */
    public List<BarrierParts> getBarrierParts(){
        return barriers;
    }

    /**
     * Getter method for the player tank
     * @return The player
     */
    public Tank getPlayer(){
        return player;
    }

    /**
     * Getter method for the list of GameObjects to remove
     * @return The list of GameObjects to remove
     */
    public List<GameObject> getToRemove(){
        return toRemove;
    }

//================================================================================
// Main Method
//================================================================================
    /**
     * The main function of our program
     * @param args Arguments
     */
    public static void main(String[] args) {
        PApplet.main("invadem.App");
    }

}
