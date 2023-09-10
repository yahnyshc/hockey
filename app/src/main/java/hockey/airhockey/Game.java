package airhockey;

import engine.GameArena;
import engine.Ball;
import engine.Line;
import engine.Text;
import engine.Rectangle;

import java.util.Random;
import javax.sound.sampled.Clip;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.*;

/**
 * A class of the hockey game and its methods and fields
 */
public class Game
{
    private int width;  // The width of the game arena
    private int height; // The height of the game arena
    private GameArena arena; // The game arena
    private Ball redMallet; // The red mallet
    private Ball blueMallet; // The blue mallet
    private double malletSpeed;
    private Ball puck; // The puck
    private double puckSpeedMultiplier; // The puck speed multiplier
    private int[] redMalletStartingPos; // The red mallet starting positions
    private int[] blueMalletStartingPos; // The blue mallet starting positions
    private int[] puckStartingPos; // The puck starting positions
    private int redMalletSize; // The red mallet size
    private int blueMalletSize; // The blue mallet size
    private int puckSize; // The puck size 
    private int topBottomIntend; // The top bottom intend
    private int leftRightIntend; // The left right intend
    private int goalWidth; // The goal width 
    private int gapsWidth; // The gaps width
    private double centreSize; // The centre size
    private Line[] borders; // The borders array
    private double[] arenaGoalLimits; // The arena goal limits
    private int bordersThickness; // The borders thickness
    private Ball centreIn; // The centre inner circle
    private Ball centreOut; // The centre outer circle
    private Line goalLeftLine; // The goal left line
    private Line goalRightLine; // The goal right line
    private Line[] goalNet; // goal net borders array
    private String[] borderColours; // The border colours array
    private Text redMalletScore; // The red mallet score
    private Text blueMalletScore; // The blue mallet score
    private Text[] cheatCodes; // The cheat codes array
    private Text[] gameParams; // The game params array
    private boolean soundMuted = true;
    private Thread soundThread = null;
    private String bounceSound = "/sounds/bounce.wav"; // The bounce sound file
    private String goalSound = "/sounds/applause.wav"; // The win sound file
    private boolean StopGoalCelebrations = false; // The stop goal celebrations flag
    private int goalsToWin = 5; // The number of goals to win the game
    private boolean goalCelebrationOngoing = false;

    /**
     * Constructor
     */
    public Game(){
        width = 1200;
        height = 800;
        this.arena = new GameArena(width, height, "/iceBackground.png", "/dashboard.png");
        // Initialize variables
        start();
    }

    /**
     * set variables and objects to default values
     */
    public void start(){
        // remove if somthing present on the game arena
        arena.clearGameArena();
        redMalletStartingPos = new int[]{this.width/6, this.height/2};
        blueMalletStartingPos = new int[]{(this.width/6)*5, this.height/2};
        puckStartingPos = new int[]{this.width/2, this.height/2};
        redMalletSize = (width+height)/35;
        blueMalletSize = (width+height)/35;
        puckSize = (width+height)/75;
        puckSpeedMultiplier = 0.6;
        topBottomIntend = height / 7;
        leftRightIntend = width / 12;
        goalWidth = (width/10)*2;
        gapsWidth = (width/40);
        centreSize = puckSize+60;
        bordersThickness = 16;
        malletSpeed = (double)(width+height)/(double)2000;
        borderColours = new String[]{"RED", "BLUE", "BLUE", "RED"};
        goalCelebrationOngoing = false;

        Text puckSpdText = new Text("Puck speed", 20, arena.getArenaWidth()/3-55, 25, "WHITE", 3 );
        Text puckSzText = new Text("Puck size", 20, arena.getArenaWidth()/2-55, 25, "WHITE", 3 );
        Text goalSzText = new Text("Goal size", 20, arena.getArenaWidth()-arena.getArenaWidth()/3-55, 25, "WHITE", 3 );

        Text puckSpd = new Text("       "+Double.toString(puckSpeedMultiplier), 20, arena.getArenaWidth()/3-55, 50, "WHITE", 3 );
        Text puckSz = new Text("      "+Integer.toString(puckSize), 20, arena.getArenaWidth()/2-55, 50, "WHITE", 3 );
        Text goalSz = new Text("     "+Integer.toString(goalWidth), 20, arena.getArenaWidth()-arena.getArenaWidth()/3-55, 50, "WHITE", 3 );
        
        gameParams = new Text[]{ puckSpd, puckSz, goalSz, puckSpdText, puckSzText, goalSzText };

        // Cheat messages
        Text Cheats = new Text("Cheats: ", 20, width/4, height-60, "BLACK", 3 );
        Text Restart = new Text("1) Press R to reset positions or N to restart ", 15, width/4+100, height-90, "BLACK", 3 );
        Text PuckSize = new Text("2) Press 'H' to make puck smaller or 'J' to make it bigger", 15, width/4+100, height-70, "BLACK", 3 );
        Text PuckSpeed = new Text("3) Press 'K' to make puck slower  or 'L' to make it faster", 15, width/4+100, height-50, "BLACK", 3 );
        Text GoalSize = new Text("4) Press 'G' to make goal smaller or 'B' to make it bigger", 15, width/4+100, height-30, "BLACK", 3 );
        Text Mute = new Text("5) Press 'M' to mute the sound", 15, width/4+100, height-10, "BLACK", 3 );

        cheatCodes = new Text[]{Cheats, Restart, PuckSize, PuckSpeed, GoalSize, Mute};

        Text cheatsHint = new Text("Press C or P to see cheat combinations", 13, 15, this.height-15, "BLACK", 3);
        arena.addText(cheatsHint);

        this.redMallet = new Ball(redMalletStartingPos[0], redMalletStartingPos[1], redMalletSize, "RED", 3, 1);
        redMallet.setImage("/redMallet.png");
        
        this.blueMallet = new Ball(blueMalletStartingPos[0], blueMalletStartingPos[1], blueMalletSize, "BLUE", 3, 2);
        blueMallet.setImage("/blueMallet.png");

        arena.addBall(redMallet);
        arena.addBall(blueMallet);

        this.puck = new Ball(puckStartingPos[0], puckStartingPos[1], puckSize, "BLACK", 3);
        puck.setImage("/puck.png");

        arena.addBall(puck);

        setBorders();
        setGoalNet();
        additionalLines();
        setScore(0,0);
        addParams();
    }

    /** 
     * Update puck position
     */
    public void movePuck(){
        // friction to slow down th puck
        double friction = 0.99973;

        while(true){
            // move puck
            puck.move(puck.getXSpeed(), puck.getYSpeed());

            double[] lineCollisionPoint = puck.getCollisionPoint(this);

            if ( lineCollisionPoint != null ){
                puck.deflect(this, puck, lineCollisionPoint, true);
            }
            if (puck.collides( blueMallet ) && puck.collides( redMallet )){
                redMallet.move( blueMallet.getXSpeed()*5, blueMallet.getYSpeed()*5 );
                blueMallet.move( redMallet.getXSpeed()*5, redMallet.getYSpeed()*5 );
            }
            if ( puck.collides( blueMallet ) ) puck.deflect(this, blueMallet, lineCollisionPoint, false);
            if ( puck.collides( redMallet ) )  puck.deflect(this, redMallet, lineCollisionPoint, false);
            
            // apply friction
            puck.setXSpeed(puck.getXSpeed() * friction);
            puck.setYSpeed(puck.getYSpeed() * friction);
            try { Thread.sleep(1); }
		    catch (Exception e) {};
        }
    }

    /** 
     * Update mallet position
     */
    public void moveMallet(Ball mallet){
        double xMove = 0, yMove = 0;
        xMove += mallet.moveRight(arena) ? malletSpeed : 0;
        xMove += mallet.moveLeft(arena) ? -malletSpeed : 0;
        yMove += mallet.moveDown(arena) ? malletSpeed : 0;
        yMove += mallet.moveUp(arena) ? -malletSpeed : 0;

        mallet.setXSpeed(xMove);
        mallet.setYSpeed(yMove);
        mallet.move( xMove, yMove );

        if( mallet.getCollisionPoint(this) != null || mallet.crossesMiddleLine(arena) ){
            mallet.move( -xMove, -yMove );
        }
    }
    
    /** 
     * Reset mallets and puck positions
     */
    public void resetPositions(){
        redMallet.setPosition(redMalletStartingPos[0], redMalletStartingPos[1]);
        blueMallet.setPosition(blueMalletStartingPos[0], blueMalletStartingPos[1]);
        puck.setPosition(puckStartingPos[0], puckStartingPos[1]);

        puck.setSpeed(0, 0);
    }
    
    /** 
     * Create arena borders
     */
    public void setBorders(){
        Line topLeftCornerTop = new Line(leftRightIntend+25, topBottomIntend, width/2-gapsWidth/2, topBottomIntend, bordersThickness, borderColours[0], 3);
        Line topLeftCornerLeft = new Line(leftRightIntend+25, topBottomIntend, leftRightIntend, height/2-goalWidth/2, bordersThickness, borderColours[0], 3);

        Line topRightCornerTop = new Line(width-leftRightIntend-25, topBottomIntend, width/2+gapsWidth/2, topBottomIntend, bordersThickness, borderColours[1], 3);
        Line topRightCornerRight = new Line(width-leftRightIntend-25, topBottomIntend, width - leftRightIntend, height/2-goalWidth/2, bordersThickness, borderColours[1], 3);

        Line bottomRightCornerBottom = new Line(width-leftRightIntend-25, height-topBottomIntend, width/2+gapsWidth/2, height-topBottomIntend, bordersThickness, borderColours[2], 3);
        Line bottomRightCornerRight = new Line(width-leftRightIntend-25, height-topBottomIntend, width-leftRightIntend, height-height/2+goalWidth/2, bordersThickness, borderColours[2], 3);

        Line bottomLeftCornerBottom = new Line(leftRightIntend+25, height-topBottomIntend, width/2-gapsWidth/2, height-topBottomIntend, bordersThickness, borderColours[3], 3);
        Line bottomLeftCornerLeft = new Line(leftRightIntend+25, height-topBottomIntend, leftRightIntend, height-height/2+goalWidth/2, bordersThickness, borderColours[3], 3);

        this.borders = new Line[]{topLeftCornerLeft, topLeftCornerTop, 
            topRightCornerTop, topRightCornerRight, 
            bottomRightCornerRight, bottomRightCornerBottom, 
            bottomLeftCornerBottom, bottomLeftCornerLeft
        };

        for (int i = 0; i < borders.length; i++ ){
            arena.addLine(borders[i]);
        }
    }

    /**
     * Remove arena borders
     */
    public void resetBorders(){
        for (int i = 0; i < borders.length; i++ ){
            arena.removeLine(borders[i]);
        }
        borderColours = new String[]{"RED", "BLUE", "BLUE", "RED"};
        setBorders();
    }

    /**
     * Create arena goal nets
     */
    public void setGoalNet(){
        Line goalRightNet = new Line(width - 10, height/2-goalWidth/2+5, width - 10, height/2+goalWidth/2-5, 5, "BLACK", 2);
        Line goalLeftNet  = new Line(10, height/2-goalWidth/2+5, 10, height/2+goalWidth/2-5, 5, "BLACK", 3);

        Line goalRightNetTop = new Line(width - 10, height/2-goalWidth/2+5, width - leftRightIntend-2.5, height/2-goalWidth/2+5, 5, "BLACK", 2);
        Line goalLeftNetTop  = new Line(10, height/2-goalWidth/2+5, leftRightIntend+2.5, height/2-goalWidth/2+5, 5, "BLACK", 2);

        Line goalRightNetBottom = new Line(width - 10, height/2+goalWidth/2-5, width - leftRightIntend-2.5, height/2+goalWidth/2-5, 5, "BLACK", 2);
        Line goalLeftNetBottom  = new Line(10, height/2+goalWidth/2-5, leftRightIntend+2.5, height/2+goalWidth/2-5, 5, "BLACK", 2);
        this.goalNet = new Line[]{goalRightNet, goalLeftNet, 
            goalRightNetTop, goalLeftNetTop, 
            goalRightNetBottom, goalLeftNetBottom
        };

        for (int i = 0; i < this.goalNet.length; i++ ){
            arena.addLine(this.goalNet[i]);
        }

        this.goalLeftLine  = new Line(leftRightIntend+5, height/2-goalWidth/2+5, leftRightIntend+2.5, height/2+goalWidth/2-5, 2, "BLACK", 1);
        this.goalRightLine = new Line(width - leftRightIntend-5, height/2-goalWidth/2+5, width - leftRightIntend-2.5, height/2+goalWidth/2-5, 2, "BLACK", 1);

        this.arenaGoalLimits = new double[]{goalLeftLine.getXStart(), goalRightLine.getXStart()};

        arena.addLine(goalLeftLine);
        arena.addLine(goalRightLine);
    }

    /**
     * remove goal net and set again with updated positions
     */
    public void resetGoalNet(){
        for (int i = 0; i < this.goalNet.length; i++ ){
            arena.removeLine(this.goalNet[i]);
        }

        arena.removeLine(goalLeftLine);
        arena.removeLine(goalRightLine);

        setGoalNet();
    }

    /**
     * Display game parameters such as puck size, speed and goal size
     */
    public void addParams(){
        for(int i = 0; i < this.gameParams.length; i++){
            arena.addText(this.gameParams[i]);
        }
    }

    /**
     * add additional design lines
     */
    public void additionalLines(){
        Line middleLeft = new Line( width/2-gapsWidth/4, topBottomIntend+10, width/2-gapsWidth/4, height - topBottomIntend-10, 2.5, "BLACK", 1);
        Line middleRight = new Line( width/2+gapsWidth/4, topBottomIntend+10, width/2+gapsWidth/4, height - topBottomIntend-10, 2.5, "BLACK", 1);

        arena.addLine(middleLeft);
        arena.addLine(middleRight);

        resetCentreLine();
    }

    /**
     * reset centre circle
     */
    public void resetCentreLine(){
        this.centreOut = new Ball( (double)width/(double)2, (double)height/(double)2, centreSize, "BLACK", 1);
        this.centreIn = new Ball( (double)width/(double)2, (double)height/(double)2, centreSize-5, "RED", 2);
        centreIn.setImage("/iceCentre.png");
        
        arena.addBall(centreOut); 
        arena.addBall(centreIn);
    }

    /**
     * sets game score to given params
     * @param s1 score of red mallet
     * @param s2 score of blue mallet
     */
    public void setScore(int s1, int s2){
        String score1 = Integer.toString(s1);
        String score2 = Integer.toString(s2);
        arena.removeText(redMalletScore);
        arena.removeText(blueMalletScore);
        redMalletScore = new Text(score1, 50, 15, 50, "WHITE", 3 );
        blueMalletScore = new Text(score2, 50, width - 50, 50, "BLACK", 3 );
        arena.addText(redMalletScore);
        arena.addText(blueMalletScore);
    }   

    /**
     * Checks if it is goal and display celebrations if it is
     */
    public void checkGoal(){
        boolean redScored = this.getPuck().getXPosition() - this.getPuck().getSize()/2 - 3 > this.getArenaGoalLimits()[1];
        boolean blueScored = this.getPuck().getXPosition() + this.getPuck().getSize()/2 + 3 < this.getArenaGoalLimits()[0];
        boolean isWin = false;
        if ( redScored || blueScored ){
            goalCelebrationOngoing = true;

            if ( blueScored ){
                this.setScore(Integer.parseInt(this.getRedMalletScore().getText()), Integer.parseInt(this.getBlueMalletScore().getText()) + 1);
            }
            else{
                this.setScore(Integer.parseInt(this.getRedMalletScore().getText()) + 1, Integer.parseInt(this.getBlueMalletScore().getText()));
            }

            Text goalMessage;
            String message = "GOAL!!!";
            String colour = redScored ? "RED" : "BLUE";
            
            boolean blueWon = Integer.parseInt(this.getBlueMalletScore().getText()) == goalsToWin;
            boolean redWon = Integer.parseInt(this.getRedMalletScore().getText()) == goalsToWin;
            if(blueWon || redWon){
                message = colour+" WON!";
                // if game over, extend celebrations
                isWin = true;
            }

            if (!soundMuted){
                playSound(goalSound);
            }

            String[] colours = this.getBorderColours();
            for(int i = 0; i < 8*(isWin ? 30 : 1); i++){
                if ( StopGoalCelebrations ){
                    StopGoalCelebrations = false;
                    goalCelebrationOngoing = false;
                    return;
                }
                String last = colours[3];
                for(int j = 3; j > 0; j--) colours[j] = colours[j-1];
                colours[0] = last;
                this.setBorderColours(colours);
                goalMessage = appearingMessage(message, colour);
                try { Thread.sleep(400);}
                catch (Exception e) {};
                arena.removeText(goalMessage);
            }
            
            // who scored - set puck position
            int xPosition = this.getWidth()/2 + (blueScored ? -150 : 150);
            this.getPuck().setPosition(xPosition, this.getHeight()/2);
            this.getPuck().setSpeed(0, 0);

            this.setRedMalletToStart();
            this.setBlueMalletToStart();
            this.getRedMallet().setPosition(this.getRedMalletStartingPos()[0], this.getRedMalletStartingPos()[1]);
            this.getBlueMallet().setPosition(this.getBlueMalletStartingPos()[0], this.getBlueMalletStartingPos()[1]);

            goalCelebrationOngoing = false;
        }
    }

    /**
     * Checks cheats usage
     */
    public void checkCheat(){
        if ( arena.letterPressed('C') || arena.letterPressed('P') ){
            displayCheats();
            while ( arena.letterPressed('C') || arena.letterPressed('P') ){
                arena.pause();
            };
            hideCheats();
        }
        if ( arena.letterPressed('R') ){
            resetPositions();
            resetBorders();
            StopGoalCelebrations = true;
            goalCelebrationOngoing = false;
        }
        else if ( arena.letterPressed('N') ){
            pause15ms();
            start();
            StopGoalCelebrations = true;
        }
        else if ( arena.letterPressed('G') ){
            if ( this.goalWidth - 5 > this.redMalletSize+this.puckSize){
                this.goalWidth -= 5;
            }
            if (
                (redMallet.getYPosition()-redMalletSize/2 < height/2-goalWidth/2+6.5
            &&  redMallet.getYPosition()+redMalletSize/2 < height/2+goalWidth/2+6.5
            &&  redMallet.getXPosition()-redMalletSize/2 < getArenaGoalLimits()[0])
            ){
                redMallet.move( 0, 5 );
            }
            if (
                (blueMallet.getYPosition()-blueMalletSize/2 < height/2-goalWidth/2+6.5
            &&  blueMallet.getYPosition()+blueMalletSize/2 < height/2+goalWidth/2+6.5
            &&  blueMallet.getXPosition()+blueMalletSize/2 > getArenaGoalLimits()[1])){
                blueMallet.move( 0, 5 );
            }
            if (
                (redMallet.getYPosition()+redMalletSize/2 > height/2+goalWidth/2-6.5
            &&  redMallet.getYPosition()-redMalletSize/2 > height/2-goalWidth/2+6.5
            &&  redMallet.getXPosition()-redMalletSize/2 < getArenaGoalLimits()[0])
            ){
                redMallet.move( 0, -5 );
            }
            if (
                (blueMallet.getYPosition()+blueMalletSize/2 > height/2+goalWidth/2-6.5
            &&  blueMallet.getYPosition()-blueMalletSize/2 > height/2-goalWidth/2+6.5
            &&  blueMallet.getXPosition()-blueMalletSize/2 > getArenaGoalLimits()[1])){
                blueMallet.move( 0, -5 );
            }
            pause15ms();
            gameParams[2].setText("    "+Integer.toString(this.goalWidth));
            resetBorders();
            resetGoalNet();
        }
        else if ( arena.letterPressed('B') ){
            if ( this.goalWidth + 5 < this.height/2){
                this.goalWidth += 5;
                gameParams[2].setText("    "+Integer.toString(this.goalWidth));
                resetBorders();
                resetGoalNet();
            }
            pause15ms();
        }
        else if ( arena.letterPressed('K') ){
            if (puckSpeedMultiplier > 0.5){
                puckSpeedMultiplier *= 0.95;
                gameParams[0].setText("      "+Double.toString(puckSpeedMultiplier).substring(0,4));
            }
            pause15ms();
        }
        else if ( arena.letterPressed('L') ){
            if (puckSpeedMultiplier < 0.9){
                puckSpeedMultiplier *= 1.05;
                gameParams[0].setText("      "+Double.toString(puckSpeedMultiplier).substring(0,4));
            }
            pause15ms();
        }
        else if ( arena.letterPressed('H') ){
            if (puckSize > 15){
                puckSize -= 1;
                this.puck.setSize(puckSize);
                gameParams[1].setText("     "+Integer.toString(puckSize));
            }
            pause15ms();
        }
        else if ( arena.letterPressed('J') ){
            if (puckSize < 50){
                puckSize += 1;
                this.puck.setSize(puckSize);
                gameParams[1].setText("     "+Integer.toString(puckSize));
            }
            pause15ms();
        }
        else if ( arena.letterPressed('M') ){
            soundMuted = !soundMuted;
            pause15ms();
            pause15ms();
        }
    }

    /**
     * Display apearing message
     */
    public Text appearingMessage(String msg, String colour){
        Random rand = new Random(); 
        Text message = null;

        double x = leftRightIntend * 1.5 + rand.nextDouble() * (width - leftRightIntend * 3);
        double y = topBottomIntend * 1.5 + rand.nextDouble() * (height-topBottomIntend * 3);

        message = new Text(msg, 40, x, y, colour, 7 );
        arena.addText(message);
        return message; 
    }

    public void displayCheats(){
        for (int i = 0; i < cheatCodes.length; i++){
            arena.addText(cheatCodes[i]);
        }
    }

    public void pause15ms(){
        for(int i = 0; i < 5; i++){
            arena.pause();
        }
    }

    /**
     * play sound
     * @param filename file to get sound from
     */
    public void playSound(String filename)
    {   

         try {
            // Create an AudioInputStream from the sound file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Game.class.getResourceAsStream(filename));

            // Get the Clip for playback
            Clip clip = AudioSystem.getClip();

            // Open the audio clip with the provided audio input stream
            clip.open(audioInputStream);

            // Start playing the sound
            clip.start();

            // Sleep to allow the sound to finish playing (you can remove this if needed)
            Thread.sleep(clip.getMicrosecondLength() / 1000);

            // Close the clip after playing
            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
/*         Thread thread = new Thread(){
            public void run(){
                try
                {
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(filename));
                    clip.start();
                }
                catch (Exception exc)
                {
                    exc.printStackTrace(System.out);
                }
            }
        };

        if(soundThread == null){
            soundThread = thread;
        }
        if (!soundThread.isAlive()){
            thread.start();
        } */
    }

    public void hideCheats(){
        for (int i = 0; i < cheatCodes.length; i++){
            arena.removeText(cheatCodes[i]);
        }
    }

    public Thread getSoundThread() {
        return soundThread;
    }

    public void setSoundThread(Thread soundThread) {
        this.soundThread = soundThread;
    }

    public String getGoalSound() {
        return goalSound;
    }

    public void setGoalSound(String goalSound) {
        this.goalSound = goalSound;
    }

    public boolean goalCelebrationOngoing(){
        return this.goalCelebrationOngoing;
    }

    public boolean isStopGoalCelebrations() {
        return StopGoalCelebrations;
    }

    public void setStopGoalCelebrations(boolean stopGoalCelebrations) {
        StopGoalCelebrations = stopGoalCelebrations;
    }

    public double getPuckSize() {
        return puckSize;
    }

    public void setPuckSize(int puckSize) {
        this.puckSize = puckSize;
    }

    public Text getRedMalletScore() {
        return redMalletScore;
    }

    public void setRedMalletScore(Text redMalletScore) {
        this.redMalletScore = redMalletScore;
    }

    public Text getBlueMalletScore() {
        return blueMalletScore;
    }

    public void setBlueMalletScore(Text blueMalletScore) {
        this.blueMalletScore = blueMalletScore;
    }

    public boolean isSoundMuted() {
        return soundMuted;
    }

    public void setSoundMuted(boolean soundMuted) {
        this.soundMuted = soundMuted;
    }

    public int getGoalsToWin() {
        return goalsToWin;
    }

    public void setGoalsToWin(int goalsToWin) {
        this.goalsToWin = goalsToWin;
    }

    public Line[] getGoalNet() {
        return goalNet;
    }

    public void setCentreSize(double centreSize) {
        this.centreSize = centreSize;
    }

    public Line getGoalLeftLine() {
        return goalLeftLine;
    }

    public void setGoalLeftLine(Line goalLeftLine) {
        this.goalLeftLine = goalLeftLine;
    }

    public Line getGoalRightLine() {
        return goalRightLine;
    }

    public void setGoalRightLine(Line goalRightLine) {
        this.goalRightLine = goalRightLine;
    }

    public Text[] getCheatCodes() {
        return cheatCodes;
    }

    public void setCheatCodes(Text[] cheatCodes) {
        this.cheatCodes = cheatCodes;
    }

    public Text[] getGameParams() {
        return gameParams;
    }

    public void setGameParams(Text[] gameParams) {
        this.gameParams = gameParams;
    }

    public String getBounceSound() {
        return bounceSound;
    }

    public void setBounceSound(String bounceSound) {
        this.bounceSound = bounceSound;
    }

    public void setGoalNet(Line[] goalNet) {
        this.goalNet = goalNet;
    }

    public String[] getBorderColours() {
        return borderColours;
    }

    public void setBorderColours(String[] borderColours) {
        for(int i = 0, j = 0; i < 4; i++, j+=2){
            borders[j].setColour(borderColours[i]);
            borders[j+1].setColour(borderColours[i]);
        }
    }

    public void setRedMalletToStart(){
        this.redMallet.setPosition(redMalletStartingPos[0], redMalletStartingPos[1]);
    }

    public void setBlueMalletToStart(){
        this.blueMallet.setPosition(blueMalletStartingPos[0], blueMalletStartingPos[1]);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        this.arena.setSize(this.width, this.height);
    }

    public int getHeight() {
        return height;
    }

    public int[] getPuckStartingPos() {
        return puckStartingPos;
    }

    public void setPuckStartingPos(int[] puckStartingPos) {
        this.puckStartingPos = puckStartingPos;
    }

    public int getBordersThickness() {
        return bordersThickness;
    }

    public void setBordersThickness(int bordersThickness) {
        this.bordersThickness = bordersThickness;
    }

    public void setHeight(int height) {
        this.height = height;
        this.arena.setSize(this.width, this.height);
    }

    public GameArena getArena() {
        return arena;
    }

    public void setArena(GameArena arena) {
        this.arena = arena;
    }

    public Ball getRedMallet() {
        return redMallet;
    }

    public void setRedMallet(Ball redMallet) {
        this.arena.removeBall(this.redMallet);
        this.redMallet = redMallet;
        this.arena.addBall(this.redMallet);
    }

    public Ball getBlueMallet() {
        return blueMallet;
    }

    public void setBlueMallet(Ball blueMallet) {
        this.arena.removeBall(this.blueMallet);
        this.blueMallet = blueMallet;
        this.arena.removeBall(this.blueMallet);
    }

    public Ball getPuck() {
        return puck;
    }

    public void setPuck(Ball puck) {
        this.puck = puck;
    }

    public int[] getRedMalletStartingPos() {
        return redMalletStartingPos;
    }

    public void setRedMalletStartingPos(int[] redMalletStartingPos) {
        this.redMalletStartingPos = redMalletStartingPos;
    }

    public int[] getBlueMalletStartingPos() {
        return blueMalletStartingPos;
    }

    public void setBlueMalletStartingPos(int[] blueMalletStartingPos) {
        this.blueMalletStartingPos = blueMalletStartingPos;
    }

    public int getRedMalletSize() {
        return redMalletSize;
    }

    public void setRedMalletSize(int redMalletSize) {
        this.redMalletSize = redMalletSize;
    }

    public int getBlueMalletSize() {
        return blueMalletSize;
    }

    public void setBlueMalletSize(int blueMalletSize) {
        this.blueMalletSize = blueMalletSize;
    }

    public int getTopBottomIntend() {
        return topBottomIntend;
    }

    public void setTopBottomIntend(int topBottomIntend) {
        this.topBottomIntend = topBottomIntend;
    }

    public int getLeftRightIntend() {
        return leftRightIntend;
    }

    public void setLeftRightIntend(int leftRightIntend) {
        this.leftRightIntend = leftRightIntend;
    }

    public int getGoalWidth() {
        return goalWidth;
    }

    public void setGoalWidth(int goalWidth) {
        this.goalWidth = goalWidth;
    }

    public int getGapsWidth() {
        return gapsWidth;
    }

    public void setGapsWidth(int gapsWidth) {
        this.gapsWidth = gapsWidth;
    }

    public double getCentreSize() {
        return centreSize;
    }

    public void setCentreSize(int centreSize) {
        this.centreSize = centreSize;
    }

    public Line[] getBorders() {
        return borders;
    }

    public void setBorders(Line[] borders) {
        this.borders = borders;
    }

    public Ball getCentreIn() {
        return centreIn;
    }

    public void setCentreIn(Ball centreIn) {
        this.centreIn = centreIn;
    }

    public Ball getCentreOut() {
        return centreOut;
    }

    public void setCentreOut(Ball centreOut) {
        this.centreOut = centreOut;
    }

    public double[] getArenaGoalLimits() {
        return arenaGoalLimits;
    }

    public void setArenaGoalLimits(double[] arenaGoalLimits) {
        this.arenaGoalLimits = arenaGoalLimits;
    }
    
    public double getPuckSpeedMultiplier() {
        return puckSpeedMultiplier;
    }

    public void setPuckSpeedMultiplier(double puckSpeedMultiplier) {
        this.puckSpeedMultiplier = puckSpeedMultiplier;
    }
}