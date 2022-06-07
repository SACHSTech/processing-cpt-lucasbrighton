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
  int intSheepX = 100;
  int intSheepY = 500;
  float fltSheepSpeed = 2;
  boolean blnMouseClick = false;

  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	// put your size call here
    size(1600, 900);
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
    imgForest.resize(1600,900);

    imgSheep = loadImage("Sheep.png");
  
    
    imgCrossHair = loadImage("Crosshair.png");
    imgCrossHair.resize(50,45);
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    image(imgBackground, 0, 0);
	  image(imgCrossHair, mouseX - 25, mouseY - 45/2);
    rect(600, 350, 400, 75);
    if(blnMouseClick){
      levelOne();
    }
  }

  public void levelOne() {
    image(imgForest, 0, 0);
    image(imgSheep, intSheepX, intSheepY);
    intSheepX += fltSheepSpeed;
   }

  public void levelThree() {
    image(imgSpace, 0, 0);
  }
  public void mousePressed() {
    if (mouseX > 600 && mouseX < 1000 && mouseY > 350 && mouseY < 425) {
      blnMouseClick = true;
    }
  }
}