/*Notes:
 * Add an arraylist to keep track of high scores
 * Add escape key for pausing in game and returning to menu
 * Continue finishing other two stages
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
  float fltSheepSpeed = 3, fltPoints = 0, fltTotalShots = 0;
  boolean [] blnInverse = new boolean[3];
  boolean blnTime = false, blnStart = false, blnEnd = false, blnStageOneClicked = false, blnMouseClicked, blnLevelOneBack = false;
  int intRandomSheepX;
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
      intRandomSheepX = (int) Math.round((Math.random() + 1));
      if(intRandomSheepX == 1){
        fltSheepX[i] = -200;
        fltSheepY[i] = 500;
        if(i >= 1){
          fltSheepY[i] = 600;
        }
      }
      else if(intRandomSheepX == 2){
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
    if(blnStageOneClicked){
      levelOne();
    }
  }

  public void levelOne() {
    if(!blnTime){
        lngStartTime = System.currentTimeMillis();
        blnTime = true;
    }

    else if(blnStart){
      lngStartTime = System.currentTimeMillis();
      blnStart = false;
    }

    if(blnMouseClicked){
      fltTotalShots++;
      blnMouseClicked = false;
    }

    image(imgForest, 0, 0);
    if(!blnEnd){
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
      imgScoreboard.resize(400, 250);
      image(imgScoreboard, 440, 0);
      fill(255);
      textSize(50);
      text("Time: " + (int) lngElapsedTime, 530, 100);
      text("Points: " + (int) fltPoints, 530, 170);
    }
    
    if(lngElapsedTime >= 60){
      blnStart = true;
      blnEnd = true;
      imgScoreboard.resize(640, 500);
      image(imgScoreboard, 320, 110);
      fill(255);
      textSize(50);
      text("Stage Complete", 470, 210);
      text("Score: " + (int) fltPoints, 390, 280);
      text("Accuracy: " + (int)(fltPoints / (fltTotalShots - 1) * 100) + "%", 390, 350);
      fill(255);
      rect(490, 470, 300, 60);
      fill(0);
      text("Back", 575, 520);
    }
  }

  public void levelThree() {
    image(imgSpace, 0, 0);
  }

  public void mousePressed() {
    if (mouseX > 480 && mouseX < 800 && mouseY > 280 && mouseY < 340) {
      blnStageOneClicked = true;
    }
    for(int i = 0; i < fltSheepX.length; i++){
      // inverse
      if (dist(mouseX, mouseY, fltSheepX[i] + 97, fltSheepY[i] + 72) < 25 && blnInverse[i]) {
        fltSheepX[i] = 1430;
        fltSheepY[i] = (int) Math.round((Math.random() * 200 + 400));
        fltPoints++;
      }
      // regular
      if (dist(mouseX, mouseY, fltSheepX[i] + 55, fltSheepY[i] + 70) < 25 && !blnInverse[i]) {
        fltSheepX[i] = -300;
        fltSheepY[i] = (int) Math.round((Math.random() * 200 + 400));
        fltPoints++;
      }
    }
    if(blnStageOneClicked && !blnEnd){
      blnMouseClicked = true;
    }

    //back button
    if(blnEnd){
      if (mouseX > 490 && mouseX < 790 && mouseY > 470 && mouseY < 530) {
        blnLevelOneBack = true;
        blnStageOneClicked = false;
        blnEnd = false;
        fltPoints = 0;
      }
    }
  }
}