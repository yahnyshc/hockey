import java.util.Random;
import javax.sound.sampled.Clip;
import java.io.File;
import javax.sound.sampled.AudioSystem;

public class Game
{
    private int width;  // The width of the game arena
    private int height; // The height of the game arena
    private GameArena arena; // The game arena
    private Ball redMallet; // The red mallet
    private Ball blueMallet; // The blue mallet
    private Ball puck; // The puck
    private double puckSpeedMultiplier; // The puck speed multiplier
    private int[] redMalletStartingPos; // The red mallet starting positions
    private int[] blueMalletStartingPos; // The blue mallet starting positions
    private int[] puckStartingPos; // The puck starting positions
    private int redMalletSize; // The red mallet size
    private int blueMalletSize; // The blue mallet size
    private double puckSize; // The puck size 
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
    private boolean soundMuted = false;
    private Thread soundThread = null;
    private File bounceSound = new File("bounce.wav"); // The bounce sound file
    private File goalSound = new File("applause.wav"); // The win sound file
    private boolean StopGoalCelebrations = false; // The stop goal celebrations flag
    private int goalsToWin = 5; // The number of goals to win the game


    // Constructor of the hockey game
    public Game(){
        width = 1200;
        height = 800;
        this.arena = new GameArena(width, height);

        // Cheat messages
        Text Cheats = new Text("Cheats: ", 20, width/4, height-60, "White", 3 );
        Text Restart = new Text("1) Press R to reset positions or N to restart ", 15, width/4+100, height-90, "CYAN", 3 );
        Text PuckSize = new Text("2) Press 'H' to make puck smaller or 'J' to make it bigger", 15, width/4+100, height-70, "CYAN", 3 );
        Text PuckSpeed = new Text("3) Press 'K' to make puck slower  or 'L' to make it faster", 15, width/4+100, height-50, "CYAN", 3 );
        Text GoalSize = new Text("4) Press 'G' to make goal smaller or 'B' to make it bigger", 15, width/4+100, height-30, "CYAN", 3 );
        Text Mute = new Text("5) Press 'M' to mute the sound", 15, width/4+100, height-10, "CYAN", 3 );

        cheatCodes = new Text[]{Cheats, Restart, PuckSize, PuckSpeed, GoalSize, Mute};
        
        // Game parameters by default
        Text puckSpd = new Text("Puck speed = 0.6", 20, 250, 30, "White", 3 );
        Text puckSz = new Text("Puck size = 20", 20, 525, 30, "White", 3 );
        Text goalSz = new Text("Goal size = 160", 20, 775, 30, "White", 3 );
        
        gameParams = new Text[]{puckSpd, puckSz, goalSz };

        // Initialize variables
        start();
    }

    // Method to set variables
    public void start(){
        // remove if somthing present on the game arena
        arena.clearGameArena();
        redMalletStartingPos = new int[]{this.width/6, this.height/2};
        blueMalletStartingPos = new int[]{(this.width/6)*5, this.height/2};
        puckStartingPos = new int[]{this.width/2, this.height/2};
        redMalletSize = 50;
        blueMalletSize = 50;
        puckSize = 20;
        puckSpeedMultiplier = 0.6;
        topBottomIntend = 125;
        leftRightIntend = 100;
        goalWidth = 160;
        gapsWidth = 20;
        centreSize = puckSize+60;
        bordersThickness = 10;
        borderColours = new String[]{"YELLOW", "GREEN", "RED", "BLUE"};

        this.redMallet = new Ball(redMalletStartingPos[0], redMalletStartingPos[1], redMalletSize, "RED", 3);
        this.blueMallet = new Ball(blueMalletStartingPos[0], blueMalletStartingPos[1], blueMalletSize, "BLUE", 3);

        arena.addBall(redMallet);
        arena.addBall(blueMallet);

        this.puck = new Ball(puckStartingPos[0], puckStartingPos[1], puckSize, "GREEN", 3);

        arena.addBall(puck);

        setBorders();

        setGoalNet();

        additionalLines();

        setScore(0,0);

        addParams();
    }

    // function to constantly move the puck
    public void movePuck(){
        // array to have postion of ball collision with the wall to reflect the ball
        double[] borderCollision = {0,0};
        // friction to slow down th puck
        double friction = 0.99973;
        double prevXpos = 0;
        double prevYpos = 0;
        double prevXspd = 0;
        double prevYspd = 0;

        while(true){
            // safety checks
            if (Double.isNaN(puck.getXSpeed())
            || Double.isNaN(puck.getYSpeed())){
                puck.setXPosition(prevXpos);
                puck.setYPosition(prevYpos);
                puck.setXSpeed(-(prevXspd+0.1));
                puck.setYSpeed(-(prevYspd+0.1));
            }
            prevXpos = puck.getXPosition();
            prevYpos = puck.getYPosition();
            prevXspd = puck.getXSpeed();
            prevYspd = puck.getYSpeed();
            
            // Celebration in case of goal or win
            checkGoal();

            borderCollision[0] = 0;
            borderCollision[1] = 0;

            // move puck
            puck.move(puck.getXSpeed(), puck.getYSpeed());

            // find collision point (if no colision return 0,0)
            borderCollision = puck.collidesBorders(this, true);
            if (! (borderCollision[0] == 0 && borderCollision[1] == 0) ){
                puck.deflect(this, puck, borderCollision, true);
                // don't let balls go into each other
                if ( puck.collides( redMallet) ){
                    redMallet.move( puck.getXSpeed()*5, puck.getYSpeed()*5 );
                }
                if( puck.collides( blueMallet) ){
                    blueMallet.move( puck.getXSpeed()*5, puck.getYSpeed()*5 );
                }
            }
            // don't let balls go into each other
            else if ( puck.collides( blueMallet) && puck.collides( redMallet)){
                redMallet.move( blueMallet.getXSpeed()*5, blueMallet.getYSpeed()*5 );
                blueMallet.move( redMallet.getXSpeed()*5, redMallet.getYSpeed()*5 );
            }
            else if ( puck.collides( blueMallet ) ){
                puck.deflect(this, blueMallet, borderCollision, false);
            }
            else if( puck.collides( redMallet ) ){
                puck.deflect(this, redMallet, borderCollision, false);
            }
            // apply friction
            puck.setXSpeed(puck.getXSpeed() * friction);
            puck.setYSpeed(puck.getYSpeed() * friction);
            try { Thread.sleep(1); }
		    catch (Exception e) {};
        }
    }

    // move red mallet
    public void moveRedMallet(){
        boolean w = arena.letterPressed('W');
        boolean a = arena.letterPressed('A');
        boolean s = arena.letterPressed('S');
        boolean d = arena.letterPressed('D');
        
        // determine x direction
        int xMove = 0;
        xMove += d ? 1 : 0;
        xMove += a ? -1 : 0;

        // determine y direction
        int yMove = 0;
        yMove += s ? 1 : 0;
        yMove += w ? -1 : 0;

        redMallet.setXSpeed(xMove);
        redMallet.setYSpeed(yMove);

        redMallet.move( xMove, yMove );
        
        double[] borderCollision = redMallet.collidesBorders(this, true);
        if( ! (borderCollision[0] == 0 && borderCollision[1] == 0) ){
            redMallet.move( -xMove, -yMove );
        }
        else if( redMallet.getXPosition() + blueMallet.getSize()/2 >= getWidth()/2 ){
            redMallet.move( -xMove, -yMove );
        }
        else if( redMallet.collides(puck) ){
            double[] borderC = puck.collidesBorders(this, false);
            if ( borderC[0]!= 0 && borderC[1]!=0 ){
                redMallet.move( -xMove, -yMove );
            }
        }

    }

    // move blue mallet
    public void moveBlueMallet(){
        boolean up = arena.upPressed();
        boolean left = arena.leftPressed();
        boolean right = arena.rightPressed();
        boolean down = arena.downPressed();

        int xMove = 0;
        xMove += right ? 1 : 0;
        xMove += left ? -1 : 0;

        int yMove = 0;
        yMove += down ? 1 : 0;
        yMove += up ? -1 : 0;

        blueMallet.setXSpeed(xMove);
        blueMallet.setYSpeed(yMove);

        blueMallet.move( xMove, yMove );
        double[] borderCollision = blueMallet.collidesBorders(this, true);
        if( ! (borderCollision[0] == 0 && borderCollision[1] == 0) ){
            blueMallet.move( -xMove, -yMove );
        }
        else if( blueMallet.getXPosition() - blueMallet.getSize()/2 <= getWidth()/2 ){
            blueMallet.move( -xMove, -yMove );
        }
        else if( blueMallet.collides(puck) ){
            double[] borderC = puck.collidesBorders(this, false);
            if ( borderC[0]!= 0 && borderC[1]!=0 ){
                blueMallet.move( -xMove, -yMove );
            }
        }
    }

    // reset balls positions
    public void resetPositions(){
        redMallet.setXPosition(redMalletStartingPos[0]);
        redMallet.setYPosition(redMalletStartingPos[1]);
        blueMallet.setXPosition(blueMalletStartingPos[0]);
        blueMallet.setYPosition(blueMalletStartingPos[1]);
        puck.setXPosition(puckStartingPos[0]);
        puck.setYPosition(puckStartingPos[1]);

        puck.setXSpeed(0);
        puck.setYSpeed(0);
    }

    // create arena borders
    public void setBorders(){
        Line topLeftCornerTop = new Line(leftRightIntend+25, topBottomIntend, width/2-gapsWidth/2, topBottomIntend, bordersThickness, borderColours[0], 3);
        Line topLeftCornerLeft = new Line(leftRightIntend+25, topBottomIntend, leftRightIntend, height/2-goalWidth/2+2, bordersThickness, borderColours[0], 3);

        Line topRightCornerTop = new Line(width-leftRightIntend-25, topBottomIntend, width/2+gapsWidth/2, topBottomIntend, bordersThickness, borderColours[1], 3);
        Line topRightCornerRight = new Line(width-leftRightIntend-25, topBottomIntend, width - leftRightIntend, height/2-goalWidth/2+2, bordersThickness, borderColours[1], 3);

        Line bottomRightCornerBottom = new Line(width-leftRightIntend-25, height-topBottomIntend, width/2+gapsWidth/2, height-topBottomIntend, bordersThickness, borderColours[2], 3);
        Line bottomRightCornerRight = new Line(width-leftRightIntend-25, height-topBottomIntend, width-leftRightIntend, height-height/2+goalWidth/2-2, bordersThickness, borderColours[2], 3);

        Line bottomLeftCornerBottom = new Line(leftRightIntend+25, height-topBottomIntend, width/2-gapsWidth/2, height-topBottomIntend, bordersThickness, borderColours[3], 3);
        Line bottomLeftCornerLeft = new Line(leftRightIntend+25, height-topBottomIntend, leftRightIntend, height-height/2+goalWidth/2-2, bordersThickness, borderColours[3], 3);

        this.borders = new Line[]{topLeftCornerLeft, topLeftCornerTop, 
            topRightCornerTop, topRightCornerRight, 
            bottomRightCornerRight, bottomRightCornerBottom, 
            bottomLeftCornerBottom, bottomLeftCornerLeft
        };

        for (int i = 0; i < borders.length; i++ ){
            arena.addLine(borders[i]);
        }
    }

    // remove borders
    public void resetBorders(){
        for (int i = 0; i < borders.length; i++ ){
            arena.removeLine(borders[i]);
        }
        setBorders();
    }

    // create goal net
    public void setGoalNet(){
        Line goalRightNet = new Line(width - 10, height/2-goalWidth/2+5, width - 10, height/2+goalWidth/2-5, 3, "WHITE", 2);
        Line goalLeftNet  = new Line(10, height/2-goalWidth/2+5, 10, height/2+goalWidth/2-5, 3, "WHITE", 3);

        Line goalRightNetTop = new Line(width - 10, height/2-goalWidth/2+5, width - leftRightIntend-2.5, height/2-goalWidth/2+5, 3, "WHITE", 2);
        Line goalLeftNetTop  = new Line(10, height/2-goalWidth/2+5, leftRightIntend+2.5, height/2-goalWidth/2+5, 3, "WHITE", 2);

        Line goalRightNetBottom = new Line(width - 10, height/2+goalWidth/2-5, width - leftRightIntend-2.5, height/2+goalWidth/2-5, 3, "WHITE", 2);
        Line goalLeftNetBottom  = new Line(10, height/2+goalWidth/2-5, leftRightIntend+2.5, height/2+goalWidth/2-5, 3, "WHITE", 2);
        this.goalNet = new Line[]{goalRightNet, goalLeftNet, 
            goalRightNetTop, goalLeftNetTop, 
            goalRightNetBottom, goalLeftNetBottom
        };

        for (int i = 0; i < this.goalNet.length; i++ ){
            arena.addLine(this.goalNet[i]);
        }

        this.goalLeftLine  = new Line(leftRightIntend+5, height/2-goalWidth/2+5, leftRightIntend+2.5, height/2+goalWidth/2-5, 0.5, "WHITE", 1);
        this.goalRightLine = new Line(width - leftRightIntend-5, height/2-goalWidth/2+5, width - leftRightIntend-2.5, height/2+goalWidth/2-5, 0.5, "WHITE", 1);

        this.arenaGoalLimits = new double[]{goalLeftLine.getXStart(), goalRightLine.getXStart()};

        arena.addLine(goalLeftLine);
        arena.addLine(goalRightLine);
    }

    // remove goal net
    public void resetGoalNet(){
        for (int i = 0; i < this.goalNet.length; i++ ){
            arena.removeLine(this.goalNet[i]);
        }

        arena.removeLine(goalLeftLine);
        arena.removeLine(goalRightLine);

        setGoalNet();
    }

    // output game parameters
    public void addParams(){
        for(int i = 0; i < this.gameParams.length; i++){
            arena.addText(this.gameParams[i]);
        }
    }

    // add additional design lines
    public void additionalLines(){
        Line middleLeft = new Line( width/2-gapsWidth/4, topBottomIntend+10, width/2-gapsWidth/4, height - topBottomIntend-10, 1.5, "White", 1);
        Line middleRight = new Line( width/2+gapsWidth/4, topBottomIntend+10, width/2+gapsWidth/4, height - topBottomIntend-10, 1.5, "White", 1);

        arena.addLine(middleLeft);
        arena.addLine(middleRight);

        resetCentreLine();
    }

    public void resetCentreLine(){
        this.centreOut = new Ball( width/2, height/2, centreSize, "WHITE", 1);
        this.centreIn = new Ball( width/2, height/2, centreSize-2, "BLACK", 2);
        
        arena.addBall(centreIn);
        arena.addBall(centreOut); 
    }

    // set score to s1 and s2
    public void setScore(int s1, int s2){
        String score1 = Integer.toString(s1);
        String score2 = Integer.toString(s2);
        arena.removeText(redMalletScore);
        arena.removeText(blueMalletScore);
        redMalletScore = new Text(score1, 50, 15, 50, "RED", 3 );
        blueMalletScore = new Text(score2, 50, width - 50, 50, "BLUE", 3 );
        arena.addText(redMalletScore);
        arena.addText(blueMalletScore);
    }   

    // check if it is a goal
    public void checkGoal(){
        boolean redScored = this.getPuck().getXPosition() - this.getPuck().getSize()/2 - 2 > this.getArenaGoalLimits()[1];
        boolean blueScored = this.getPuck().getXPosition() + this.getPuck().getSize()/2 + 2 < this.getArenaGoalLimits()[0];
        Text goalMessage = null;
        String message = "GOAL!!!";
        String colour = "ORANGE";
        boolean isWin = false;
        if (redScored || blueScored){
            if ( blueScored ){
                this.setScore(Integer.parseInt(this.getRedMalletScore().getText()), Integer.parseInt(this.getBlueMalletScore().getText()) + 1);
            }
            else{
                this.setScore(Integer.parseInt(this.getRedMalletScore().getText()) + 1, Integer.parseInt(this.getBlueMalletScore().getText()));
            }
            
            boolean blueWon = Integer.parseInt(this.getBlueMalletScore().getText()) == goalsToWin;
            boolean redWon = Integer.parseInt(this.getRedMalletScore().getText()) == goalsToWin;
            if(blueWon || redWon){
                colour = blueWon ? "BLUE":"RED";
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
                    return;
                }
                String last = colours[3];
                for(int j = 3; j > 0; j--){
                    colours[j] = colours[j-1];
                }
                colours[0] = last;
                this.setBorderColours(colours);
                goalMessage = appearingMessage(message, colour);
                try { Thread.sleep(400);}
                catch (Exception e) {};
                arena.removeText(goalMessage);
            }
            
            // who scored - set puck position
            int xPosition = this.getWidth()/2;
            if (this.getPuck().getXPosition() + this.getPuck().getSize()/2 < this.getArenaGoalLimits()[0]){
                xPosition -= 150; 
            }
            else{
                xPosition += 150; 
            }
            this.getPuck().setXPosition(xPosition);
            this.getPuck().setYPosition(this.getHeight()/2);
            this.getPuck().setXSpeed(0);
            this.getPuck().setYSpeed(0);

            this.getRedMallet().setXPosition(this.getRedMalletStartingPos()[0]);
            this.getRedMallet().setYPosition(this.getBlueMalletStartingPos()[1]);
            this.getBlueMallet().setXPosition(this.getBlueMalletStartingPos()[0]);
            this.getBlueMallet().setYPosition(this.getBlueMalletStartingPos()[1]);
        }
    }

    // check if cheats are used
    public void checkCheat(){
        if ( arena.letterPressed('C') ){
            displayCheats();
            while ( arena.letterPressed('C') ){
                arena.pause();
            };
            hideCheats();
        }
        if ( arena.letterPressed('R') ){
            resetPositions();
            StopGoalCelebrations = true;
        }
        else if ( arena.letterPressed('N') ){
            arena.pause();
            start();
            arena.pause();
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
            gameParams[2].setText("Goal size = "+Integer.toString(this.goalWidth));
            resetBorders();
            resetGoalNet();
        }
        else if ( arena.letterPressed('B') ){
            if ( this.goalWidth + 5 < this.height/2){
                this.goalWidth += 5;
                gameParams[2].setText("Goal size = "+Integer.toString(this.goalWidth));
                resetBorders();
                resetGoalNet();
            }
            pause15ms();
        }
        else if ( arena.letterPressed('K') ){
            if (puckSpeedMultiplier > 0.5){
                puckSpeedMultiplier *= 0.95;
                gameParams[0].setText("Puck speed = "+Double.toString(puckSpeedMultiplier).substring(0,4));
            }
            pause15ms();
        }
        else if ( arena.letterPressed('L') ){
            if (puckSpeedMultiplier < 0.9){
                puckSpeedMultiplier *= 1.05;
                gameParams[0].setText("Puck speed = "+Double.toString(puckSpeedMultiplier).substring(0,4));
            }
            pause15ms();
        }
        else if ( arena.letterPressed('H') ){
            if (puckSize > 15){
                puckSize *= 0.95;
                centreSize = puckSize+60;
                this.puck.setSize(puckSize);
                arena.removeBall(this.centreOut);
                arena.removeBall(this.centreIn);
                resetCentreLine();
                gameParams[1].setText("Puck size = "+Double.toString(puckSize).substring(0,4));
            }
            pause15ms();
        }
        else if ( arena.letterPressed('J') ){
            if (puckSize < 50){
                puckSize *= 1.05;
                centreSize = puckSize+60;
                this.puck.setSize(puckSize);
                arena.removeBall(this.centreOut);
                arena.removeBall(this.centreIn);
                resetCentreLine();
                gameParams[1].setText("Puck size = "+Double.toString(puckSize).substring(0,4));
            }
            pause15ms();
        }
        else if ( arena.letterPressed('M') ){
            soundMuted = !soundMuted;
            pause15ms();
            pause15ms();
        }
    }

    // display appearing messages
    public Text appearingMessage(String msg, String colour){
        Random rand = new Random(); 
        Text message = null;

        double x = leftRightIntend*1.5 + rand.nextDouble(width-leftRightIntend*3);
        double y = topBottomIntend*1.5 + rand.nextDouble(height-topBottomIntend*3);

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

    

    public void playSound(File filename)
    {   
        Thread thread = new Thread(){
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
        }
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

    public File getGoalSound() {
        return goalSound;
    }

    public void setGoalSound(File goalSound) {
        this.goalSound = goalSound;
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

    public void setPuckSize(double puckSize) {
        this.puckSize = puckSize;
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

    public File getBounceSound() {
        return bounceSound;
    }

    public void setBounceSound(File bounceSound) {
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
        this.redMallet.setXPosition(redMalletStartingPos[0]);
        this.redMallet.setYPosition(redMalletStartingPos[1]);
    }

    public void setBlueMalletToStart(){
        this.blueMallet.setXPosition(blueMalletStartingPos[0]);
        this.blueMallet.setYPosition(blueMalletStartingPos[1]);
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