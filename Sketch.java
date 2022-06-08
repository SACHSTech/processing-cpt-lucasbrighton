import processing.core.PApplet;
import processing.core.PImage; 
/**
 * Description: 
 * @author: B. Zhang & L. Pei
 */

public class Sketch extends PApplet {
	// global variables
  PImage imgForest;
  PImage imgSheep;
  PImage imgTarget;
  PImage imgCrossHair;
  PImage imgBackground;
  PImage imgSpace;
  float [] fltSheepX = new float[4];
  float [] fltSheepY = new float[4];
  float fltSheepSpeed = 2;
  boolean blnMouseClick = false;
  boolean [] blnSheepAlive = new boolean[4];
  
  int randomSheepX = (int) Math.round((Math.random() * 2));
  int randomSheepY = (int) Math.round((Math.random() * 2));

  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	// put your size call here
    size(1280, 720);
  }

  /** 
   * Called once at the beginning of execution.  Add initial set up
   * values here i.e background, stroke, fill etc.
   */
  public void setup() {
    frameRate(144);
    imgBackground = loadImage("MenuBackground.jpg");

    imgSpace = loadImage("Space.jpg");
    
    imgForest = loadImage("ForestBackground.jpg");
    imgForest.resize(1280,720);

    imgSheep = loadImage("Sheep.png");
    imgSheep.resize(200, 200);

    imgCrossHair = loadImage("Crosshair.png");
    imgCrossHair.resize(40,36);

    for (int i = 0; i < fltSheepY.length; i++) {
      fltSheepY[i] = random(height);
    }
    for (int i = 0; i < fltSheepX.length; i++) {
      fltSheepX[i] = 0;
    }
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    image(imgBackground, 0, 0);
    rect(480, 280, 320, 60);
    if(blnMouseClick){
      levelOne();
    }
  }

  public void levelOne() {
    image(imgForest, 0, 0);
    for(int i = 0; i < blnSheepAlive.length; i++){
      if(!blnSheepAlive[i]){
        image(imgSheep, fltSheepX[i], fltSheepY[i]);
        fltSheepX[i] += fltSheepSpeed;
      }
    }
    image(imgCrossHair, mouseX - 25, mouseY - 45/2);

   }

  public void levelThree() {
    image(imgSpace, 0, 0);
  }
  public void mousePressed() {
    if (mouseX > 480 && mouseX < 800 && mouseY > 280 && mouseY < 340) {
      blnMouseClick = true;
    }
  }
}