/*Notes:
 * Create a start level boolean to check state of the user.
 */

import processing.core.PApplet;
import processing.core.PImage; 

/**
 * Description: 
 * @author: B. Zhang & L. Pei
 */

public class Sketch extends PApplet {
	// global variables
  PImage imgForest, imgSheep, imgTarget, imgCrossHair, imgBackground, imgSpace, imgInverseSheep, imgScoreboard;
  float [] fltSheepX = new float[3], fltSheepY = new float[3];
  float fltSheepSpeed = 3;
  boolean [] blnInverse = new boolean[3];
  boolean time = false, start = false, blnMouseClick = false;
  int randomSheepX, points = 0;
  long lngStartTime, lngElapsedTime;

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
    imgSheep.resize(150, 150);
    imgInverseSheep = loadImage("InverseSheep.png");
    imgInverseSheep.resize(150, 150);
    imgCrossHair = loadImage("Crosshair.png");
    imgCrossHair.resize(40,36);
    imgTarget = loadImage("Target.png");
    imgTarget.resize(110,62);
    imgScoreboard = loadImage("Scoreboard.png");
    imgScoreboard.resize(400, 250);

    for (int i = 0; i < fltSheepX.length; i++) {
      randomSheepX = (int) Math.round((Math.random() + 1));
      if(randomSheepX == 1){
        fltSheepX[i] = -200;
        fltSheepY[i] = 500;
        if(i >= 1){
          fltSheepY[i] = 600;
        }
      }
      else if(randomSheepX == 2){
        fltSheepX[i] = 1280;
        fltSheepY[i] = 400;
        blnInverse[i] = true;
        if(i >= 1){
          fltSheepY[i] = 600;
        }
      }
    }
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    image(imgBackground, 0, 0);
    fill(255);
    rect(480, 280, 320, 60);
    if(blnMouseClick){
      levelOne();
    }
  }

  public void levelOne() {
    if(!time){
        lngStartTime = System.currentTimeMillis();
        time = true;
    }

    else if(start){
      lngStartTime = System.currentTimeMillis();
      start = false;
    }

    image(imgForest, 0, 0);
    for(int i = 0; i < fltSheepX.length; i++){
      if(!blnInverse[i]){
        image(imgSheep, fltSheepX[i], fltSheepY[i]);
        image(imgTarget, fltSheepX[i], fltSheepY[i] + 42);
        fltSheepX[i] += fltSheepSpeed;
          if(fltSheepX[i] > 1150){
            blnInverse[i] = true;
          }
      }
      else if(blnInverse[i]){
        image(imgInverseSheep, fltSheepX[i], fltSheepY[i]);
        image(imgTarget, fltSheepX[i] + 42, fltSheepY[i] + 42);
        fltSheepX[i] -= fltSheepSpeed;
        if(fltSheepX[i] < 0){
          blnInverse[i] = false;
        }
      }
    }
    image(imgCrossHair, mouseX - 20, mouseY - 45/2 + 5);
    lngElapsedTime = (System.currentTimeMillis() - lngStartTime) / 1000;
    image(imgScoreboard, 440, 0);
    fill(0);
    textSize(50);
    text("Time: " + (int)lngElapsedTime, 530, 100);
    text("Points: " + points, 530, 170);
    if(lngElapsedTime >= 60){
      blnMouseClick = false;
      points = 0;
      lngElapsedTime = 0;
      start = true;
      return;
    }
  }

  public void levelThree() {
    image(imgSpace, 0, 0);
  }

  public void mousePressed() {
    if (mouseX > 480 && mouseX < 800 && mouseY > 280 && mouseY < 340) {
      blnMouseClick = true;
    }
    for(int i = 0; i < fltSheepX.length; i++){
      // inverse
      if (dist(mouseX, mouseY, fltSheepX[i] + 97, fltSheepY[i] + 72) < 25 && blnInverse[i]) {
        fltSheepX[i] = 1430;
        fltSheepY[i] = (int) Math.round((Math.random() * 200 + 400));
        points++;
      }
      // regular
      if (dist(mouseX, mouseY, fltSheepX[i] + 55, fltSheepY[i] + 70) < 25 && !blnInverse[i]) {
        fltSheepX[i] = -300;
        fltSheepY[i] = (int) Math.round((Math.random() * 200 + 400));
        points++;
      }
    } 
  }
}