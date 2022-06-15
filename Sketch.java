/*Notes:
 * Add an arraylist to keep track of high scores
 * Add escape key for pausing in game and returning to menu
 * Continue finishing other two stages
 * Finish menu
 */

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage; 

/**
 * Description: 
 * @author: B. Zhang & L. Pei
 */

public class Sketch extends PApplet {
	// global variables
  PImage imgForest, imgSheep, imgTarget, imgCrossHair, imgBackground, imgInverseSheep, imgScoreboard, imgOcean, imgUfoWithTarget, imgSpace2, imgExplode, imgLogo;
  float [] fltSheepX = new float[3], fltSheepY = new float[3], fltUfoSpeedX = new float[3], fltUfoSpeedY = new float[3], fltUfoX = new float[3], fltUfoY = new float[3];
  float fltSheepSpeed = 5, fltPoints = 0, fltTotalShots = 0, fltAccuracy;
  boolean [] blnInverse = new boolean[3], blnUfoShot = new boolean[3];
  boolean blnTime = false, blnStart = false, blnEnd = false, blnStageOneClicked = false, blnStageTwoClicked = false, blnHighScore = false, blnMouseClicked, blnBackSpace = false;
  int intRandomSheepX, intHolder, intAccuracyHolder, intRandomUfo, x = 0;
  int [] intUpOrDown = new int [3], intLeftOrRight = new int[3];
  long lngStartTime, lngElapsedTime;
  ArrayList<Integer> intHighScores = new ArrayList<Integer>(), intAccuracy = new ArrayList<Integer>();


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
    // Framerate is set to 144 fps
    frameRate(144);
    // Menu and Universal Assets
    imgBackground = loadImage("MenuBackground.jpg");
    imgCrossHair = loadImage("Crosshair.png");
    imgCrossHair.resize(40,36);
    imgTarget = loadImage("Target.png");
    imgTarget.resize(110,62);
    imgScoreboard = loadImage("Scoreboard.png");
    imgScoreboard.resize(400, 250);
    imgLogo = loadImage("LBLogo.png");
    imgLogo.resize(200, 200);

    // Level One Assets
    imgForest = loadImage("ForestBackground.jpg");
    imgForest.resize(1280,720);
    imgSheep = loadImage("Sheep.png");
    imgSheep.resize(150, 150);
    imgInverseSheep = loadImage("InverseSheep.png");
    imgInverseSheep.resize(150, 150);

    // Level Two Assets
    imgOcean = loadImage("OceanBackground.png");
    imgOcean.resize(1280, 720);

    //Level Three Assets
    imgSpace2 = loadImage("Space2.jpg");
    imgUfoWithTarget = loadImage("UfoWithTarget.png");
    imgUfoWithTarget.resize(150, 150);
    imgExplode = loadImage("Explosion.png");
    imgExplode.resize(150, 144);

    // For loop to generate starting locations for sheep
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

    // For loop to generate random starting locations and speeds for ufo
    for (int i = 0; i < fltUfoX.length; i++){
      fltUfoX[i] = random(160, width - 160);
      fltUfoY[i] = random(160, height - 160);
      blnUfoShot[i] = false;
    }
    for (int i = 0; i < fltUfoX.length; i++){
      fltUfoX[i] = random(0, width - 160);
      fltUfoY[i] = random(160, height - 160);
    }
    for (int i = 0; i < fltUfoSpeedX.length; i++){
      fltUfoSpeedX[i] = random(3, 6);
      fltUfoSpeedY[i] = random(3, 6);
      intUpOrDown[i] = (int)Math.round((Math.random() + 1));
      intLeftOrRight[i] = (int)Math.round((Math.random() + 1));

      if (intUpOrDown[i] == 2) {
        fltUfoSpeedY[i] *= -1;
      }
      if (intLeftOrRight[i] == 2) {
        fltUfoSpeedX[i] *= -1;
      }
    }
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    // Menu display
    image(imgBackground, 0, 0);
    image(imgLogo, 540, 45);
    fill(255);
    rect(480, 280, 320, 60);
    rect(480, 360, 320, 60);
    rect(480, 440, 320, 60);
    textSize(30);
    text("FPS Game", 565, 255);
    fill(0);
    text("Level 1: Forest", 540, 320);
    text("Level 2: Space", 540, 400);
    text("Leaderboard", 553, 480);

    // Conditional statements to run the following methods
    if(blnStageOneClicked){
      stageOne();
    }
    if(blnStageTwoClicked){
      stageTwo();
    }
    if(blnHighScore){
      highScore();
    }
  }

  /**
   * Level 1 method, runs the entirety of level 1 when called
   */
  public void stageOne() {
    // grabs the initial time the level starts at and stores it to the lngStartTime variable
    if(!blnTime){
        lngStartTime = System.currentTimeMillis();
        blnTime = true;
    }

    // uses blnStart to tell the program to grab the starting time again after restarting the level
    else if(blnStart){
      lngStartTime = System.currentTimeMillis();
      blnStart = false;
    }

    // if statement to track amount of shots/clicks taken
    if(blnMouseClicked){
      fltTotalShots++;
      blnMouseClicked = false;
    }

    // blnBackspace used to exit level when true
    if(blnBackSpace){
      blnStageOneClicked = false;
      fltPoints = 0;
      fltTotalShots = 0;
      blnStart = true;
    }

    image(imgForest, 0, 0);
    // code below runs if the level has not ended, when blnEnd is false
    if(!blnEnd){
      for(int i = 0; i < fltSheepX.length; i++){
        // conditional statements to draw inverse and regular sheep
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

      // crosshair image follows mouse as a reticle
      image(imgCrossHair, mouseX - 20, mouseY - 45/2 + 5);
      lngElapsedTime = (System.currentTimeMillis() - lngStartTime) / 1000;
      imgScoreboard.resize(400, 250);
      image(imgScoreboard, 440, 0);
      fill(255);
      textSize(50);
      text("Time: " + (int) lngElapsedTime, 530, 100);
      text("Points: " + (int) fltPoints, 530, 170);
    }
    
    // conditional statement to check if time has passed 60 seconds, responsible for ending the current level
    if(lngElapsedTime >= 60){
      blnStart = true;
      blnEnd = true;
      imgScoreboard.resize(640, 500);
      image(imgScoreboard, 320, 110);
      fill(255);
      textSize(50);
      text("Stage Complete", 470, 210);
      text("Score: " + (int) fltPoints, 390, 280);
      text("Accuracy: " + (int) fltAccuracy(fltPoints, fltTotalShots) + "%", 390, 350);
      fill(255);
      rect(490, 470, 300, 60);
      fill(0);
      text("Back", 575, 520);
    }
  }
  /**
   * Level 2 method, runs level 2 when called
   */
  public void stageTwo() {
    image(imgSpace2, 0, 0);

    // grabs the initial time the level starts at and stores it to the lngStartTime variable
    if(!blnTime){
        lngStartTime = System.currentTimeMillis();
        blnTime = true;
    }

    // uses blnStart to tell the program to grab the starting time again after restarting the level
    else if(blnStart){
      lngStartTime = System.currentTimeMillis();
      blnStart = false;
    }

    // if statement to track amount of shots/clicks taken
    if(blnMouseClicked){
      fltTotalShots++;
      blnMouseClicked = false;
    }

    // blnBackspace used to exit level when true
    if(blnBackSpace){
      blnStageTwoClicked = false;
      fltPoints = 0;
      fltTotalShots = 0;
      blnStart = true;
    }

    if(!blnEnd){
      image(imgCrossHair, mouseX - 20, mouseY - 45/2 + 5);
      lngElapsedTime = (System.currentTimeMillis() - lngStartTime) / 1000;
      imgScoreboard.resize(400, 250);
      image(imgScoreboard, 440, 0);
      fill(255);
      textSize(50);
      text("Time: " + (int) lngElapsedTime, 530, 100);
      text("Points: " + (int) fltPoints, 530, 170);

      // draws the ufos at random locations throughout when they are not shot 
      for (int i = 0; i < fltUfoX.length; i++){
        if (!blnUfoShot[i]){
          image(imgUfoWithTarget, fltUfoX[i], fltUfoY[i]);
          
          fltUfoX[i] = fltUfoX[i] + fltUfoSpeedX[i];
          fltUfoY[i] = fltUfoY[i] + fltUfoSpeedY[i];
          fill(255);
          
          // edge collision so the ufos don't leave the screen
          if (fltUfoX[i] < -20 || fltUfoX[i] > width - 150) {
            fltUfoSpeedX[i] *= -1;
          }
          if (fltUfoY[i] < -20 || fltUfoY[i] > height - 150) {
            fltUfoSpeedY[i] *= -1;
          }
        }
        // when a ufo is shot, it is hidden and reassigned a new location and speed
        else if (blnUfoShot[i]){
          image(imgExplode, fltUfoX[i], fltUfoY[i]);
          // random x and y locations
          fltUfoX[i] = random(width - 151);
          fltUfoY[i] = random(height - 151);
          // random x and y speeds
          fltUfoSpeedX[i] = random(3, 6);
          fltUfoSpeedY[i] = random(3, 6);
          // sets the array back to false so it can be drawn again
          blnUfoShot[i] = false;
        }
      }
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
      text("Accuracy: " + (int) fltAccuracy(fltPoints, fltTotalShots) + "%", 390, 350);
      fill(255);
      rect(490, 470, 300, 60);
      fill(0);
      text("Back", 575, 520);
    }
    image(imgCrossHair, mouseX - 20, mouseY - 45/2 + 5);   
  }

  // Leaderboard method, this method outputs a leaderboard to the screen of the top 5 scores obtained in the game 
  public void highScore() {
    image(imgBackground, 0, 0);
    imgScoreboard.resize(960, 640);
    image(imgScoreboard, 160, 20);
    fill(255);
    textSize(40);
    text("High Scores", 540, 120);
    text("Score:", 400, 175);
    text("Accuracy:", 750, 175);
    
    for(int i = 0; i < 5; i++){
      text("#" + (i + 1), 280, 230 + (i * 75));
    }

    // bubble sorting algorithm used to sort scores form largest to smallest
    for (byte byteSize = 0; byteSize < intHighScores.size() ; byteSize++) {
      for (byte byteCompare = 0; byteCompare < intHighScores.size() - byteSize - 1; byteCompare++) {
          if (intHighScores.get(byteCompare) < intHighScores.get(byteCompare + 1)) {
              intHolder = intHighScores.get(byteCompare);
              intHighScores.set(byteCompare, intHighScores.get(byteCompare + 1));
              intHighScores.set(byteCompare + 1, intHolder);

              intAccuracyHolder = intAccuracy.get(byteCompare);
              intAccuracy.set(byteCompare, intAccuracy.get(byteCompare + 1));
              intAccuracy.set(byteCompare + 1, intAccuracyHolder);
          }
        }
    }

    // nested for loop to output all scores before crashing code due to arraylist boundaries
    for(int i = 0; i < 5; i++){
      if(intHighScores.size() > i){
        text(intHighScores.get(i), 410, 230 + (i * 75));
        text(intAccuracy.get(i) + "%", 760, 230 + (i * 75));
      }
      else{
        break;
      }
    }

    // back button
    fill(255);
    rect(490, 640, 300, 60);
    fill(0);
    textSize(50);
    text("Back", 585, 690);
  }

  /** 
   * user input function for mouse point detection throughout the game
   */
  public void mousePressed() {
    // detects if the mouse is clicked within the menu buttons. If it is, then a boolean is set as true for it to be drawn within the draw() method
    if (mouseX > 480 && mouseX < 800 && mouseY > 280 && mouseY < 340 && (!blnStageTwoClicked && !blnHighScore)) {
      blnStageOneClicked = true;
    }
    else if (mouseX > 480 && mouseX < 800 && mouseY > 360 && mouseY < 420 && (!blnHighScore && !blnStageOneClicked)) {
      blnStageTwoClicked = true;
    }
    else if (mouseX > 480 && mouseX < 800 && mouseY > 440 && mouseY < 500 && (!blnStageTwoClicked && !blnStageOneClicked)) {
      blnHighScore = true;
    }

    // detects if the mouse is clicked within the targets in level 1
    if(blnStageOneClicked){
      for(int i = 0; i < fltSheepX.length; i++){
        // inverse movement
        // when the sheep is shot, it is moved to the right of the screen and assigned a random Y value
        if (dist(mouseX, mouseY, fltSheepX[i] + 97, fltSheepY[i] + 72) < 25 && blnInverse[i]) {
          fltSheepX[i] = 1430;
          fltSheepY[i] = (int) Math.round((Math.random() * 200 + 400));
          if(blnStageOneClicked){
            // add 100 points for every successful shot in level 1
            fltPoints += 100; 
          }
        }
        // regular
        // when the sheep is shot, it is moved to the left of the screen and assigned a random Y value
        if (dist(mouseX, mouseY, fltSheepX[i] + 55, fltSheepY[i] + 70) < 25 && !blnInverse[i]) {
          fltSheepX[i] = -300;
          fltSheepY[i] = (int) Math.round((Math.random() * 200 + 400));
          if(blnStageOneClicked){
            // add 100 points for every successful shot in level 1
            fltPoints += 100; 
          }
        }
      }
    }

    // detects if the mouse is clicked within the targets in level 2
    if(blnStageTwoClicked){
      for (int i = 0; i < fltUfoSpeedX.length; i++){
        if (dist(mouseX, mouseY, fltUfoX[i] + 83.9f, fltUfoY[i] + 78.8f) < 40){
          // add 200 points for every successful shot in level 2
          fltPoints += 200;
          // the ufo is marked as shot, and this is used in the stageTwo() method
          blnUfoShot[i] = true;
        }
      }
    }

    if((blnStageOneClicked || blnStageTwoClicked) && !blnEnd){
      blnMouseClicked = true;
    }
 
    // back button
    if(blnEnd){
      if (mouseX > 490 && mouseX < 790 && mouseY > 470 && mouseY < 530) {
        // .add() function to store accuracy and points for the highScore() method
        intHighScores.add((int) fltPoints);
        intAccuracy.add((int) fltAccuracy);
        // conditional statements to set booleans false for the current level to stop running
        if(blnStageOneClicked){
          blnStageOneClicked = false;
        }
        else if(blnStageTwoClicked){
          blnStageTwoClicked = false;
        }
        blnEnd = false;
        fltPoints = 0;
        fltTotalShots = 0;
      }
    }

    if(blnHighScore){
      // edge detection for highscore button
      if (mouseX > 490 && mouseX < 790 && mouseY > 640 && mouseY < 700) {
        blnHighScore = false;
      }
    }
  }

  /**
    * sets a boolean as true when backspace is pressed. this is used to escape a level at any given moment
    */
  public void keyPressed(){
    if(keyCode == BACKSPACE){
      blnBackSpace = true;
    }
  }

  /**
   * Resets the variable when backspace is released in order for iut to be used again
   */
  public void keyReleased(){
    if(keyCode == BACKSPACE){
      blnBackSpace = false;
    }
  }

  /**
   * Calculates how accurate the player was during each playthrough of a level
   * 
   * @param intPoints The amount of points the user scored from playing the level
   * @param intTotalShots The total amount of shots/clicks the user took during the level
   * @return Returns the accuracy as a rounded percent value equal or less then 100
   */
  public float fltAccuracy(float intPoints, float intTotalShots) {
    // in level 1, each shot is 100 points, so points are converted into hits and then divided by total shots to determine accuracy
    if(blnStageOneClicked){
      fltAccuracy = (fltPoints / 100) / (fltTotalShots - 1) * 100;
    }
    // in level 2, each shot is 200 points, so points are converted into hits and then divided by total shots to determine accuracy
    else if(blnStageTwoClicked){
      fltAccuracy = (fltPoints / 200) / (fltTotalShots - 1) * 100;
    }
    return (fltCollatCheck(fltAccuracy));
  }
  
  /**
   * If a player hits a collateral (kills two items in 1 shot), possibly creating accuracy over 100%, this method caps the percentage at 100%
   * 
   * @param fltOverHundred The percentage from the fltAccuracy method
   * @return Returns the final percentage
   */
  public float fltCollatCheck(float fltOverHundred){
    // if the accuracy is over 100%, it will be brought down to 100%
    if (fltOverHundred > 100){
      fltOverHundred = 100;
    }
    return fltOverHundred;
  }
}
