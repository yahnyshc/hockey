
public class Game
{
    private int width = 1200;
    private int height = 800;
    private GameArena arena;
    private Ball redMallet;
    private Ball blueMallet;
    private Ball puck;
    private int[] redMalletStartingPos = {this.width/6, this.height/2};
    private int[] blueMalletStartingPos = {(this.width/6)*5, this.height/2};
    private int[] puckStartingPos = {this.width/2, this.height/2};
    private int redMalletSize = 50;
    private int blueMalletSize = 50;
    private int puckSize = 20;
    private int topBottomIntend = 125;
    private int leftRightIntend = 100;
    private int goalWidth = 160;
    private int gapsWidth = 20;
    private int centreSize = puckSize+60;
    private Line[] borders;
    private double[] arenaGoalLimits;
    private int bordersThickness = 10;
    private Ball centreIn;
    private Ball centreOut;
    private Line[] goalNet;
    private String[] borderColours = {"YELLOW", "GREEN", "RED", "BLUE"};
    private Text redMalletScore;
    private Text blueMalletScore;

    public Game(){
        this.arena = new GameArena(width, height);

        restart();

        Text Cheats = new Text("Cheats: ", 20, width/4, height-60, "White", 3 );
        arena.addText(Cheats);
        Text Restart = new Text("1) Press R or N to restart ", 15, width/4+100, height-90, "CYAN", 3 );
        arena.addText(Restart);
        Text PuckSize = new Text("2) Press 'H' to make puck smaller or 'J' to make it bigger", 15, width/4+100, height-70, "CYAN", 3 );
        arena.addText(PuckSize);
        Text PuckSpeed = new Text("3) Press 'K' to make puck slower  or 'L' to make it faster", 15, width/4+100, height-50, "CYAN", 3 );
        arena.addText(PuckSpeed);
        Text GoalSize = new Text("4) Press 'G' to make goal smaller or 'B' to make it bigger", 15, width/4+100, height-30, "CYAN", 3 );
        arena.addText(GoalSize);
    }

    public void restart(){
        arena.clearGameArena();
        width = 1200;
        height = 800;
        redMalletStartingPos = new int[]{this.width/6, this.height/2};
        blueMalletStartingPos = new int[]{(this.width/6)*5, this.height/2};
        puckStartingPos = new int[]{this.width/2, this.height/2};
        redMalletSize = 50;
        blueMalletSize = 50;
        puckSize = 20;
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

        Line middleLeft = new Line( width/2-gapsWidth/4, topBottomIntend+10, width/2-gapsWidth/4, height - topBottomIntend-10, 1.5, "White", 1);
        Line middleRight = new Line( width/2+gapsWidth/4, topBottomIntend+10, width/2+gapsWidth/4, height - topBottomIntend-10, 1.5, "White", 1);
        arena.addLine(middleLeft);
        arena.addLine(middleRight);

        this.puck = new Ball(puckStartingPos[0], puckStartingPos[1], puckSize, "GREEN", 3);

        arena.addBall(puck);

        resetBorders();

        resetGoalNet();

        setScore(0,0);

        Line goalLeftLine  = new Line(leftRightIntend+5, height/2-goalWidth/2+5, leftRightIntend+5, height/2+goalWidth/2-5, 0.5, "WHITE", 1);
        Line goalRightLine = new Line(width - leftRightIntend-5, height/2-goalWidth/2+5, width - leftRightIntend-5, height/2+goalWidth/2-5, 0.5, "WHITE", 1);

        this.arenaGoalLimits = new double[]{goalLeftLine.getXStart(), goalRightLine.getXStart()};

        arena.addLine(goalLeftLine);
        arena.addLine(goalRightLine);
    }
    public void resetBorders(){
        Line topLeftCornerTop = new Line(leftRightIntend+25, topBottomIntend, width/2-gapsWidth/2, topBottomIntend, bordersThickness, borderColours[0], 3);
        Line topLeftCornerLeft = new Line(leftRightIntend+25, topBottomIntend, leftRightIntend, height/2-goalWidth/2, bordersThickness, borderColours[0], 3);

        Line topRightCornerTop = new Line(width-leftRightIntend-25, topBottomIntend, width/2+gapsWidth/2, topBottomIntend, bordersThickness, borderColours[1], 3);
        Line topRightCornerRight = new Line(width-leftRightIntend-25, topBottomIntend, width - leftRightIntend, height/2-goalWidth/2, bordersThickness, borderColours[1], 3);

        this.centreOut = new Ball( width/2, height/2, centreSize, "WHITE", 1);
        this.centreIn = new Ball( width/2, height/2, centreSize-2, "BLACK", 2);
        
        arena.addBall(centreIn);
        arena.addBall(centreOut); 

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

    public void resetGoalNet(){
        Line goalRightNet = new Line(width - 10, height/2-goalWidth/2+5, width - 10, height/2+goalWidth/2-5, 3, "WHITE", 3);
        Line goalLeftNet  = new Line(10, height/2-goalWidth/2+5, 10, height/2+goalWidth/2-5, 3, "WHITE", 3);

        Line goalRightNetTop = new Line(width - 10, height/2-goalWidth/2+5, width - leftRightIntend-5, height/2-goalWidth/2+5, 3, "WHITE", 3);
        Line goalLeftNetTop  = new Line(10, height/2-goalWidth/2+5, leftRightIntend+5, height/2-goalWidth/2+5, 3, "WHITE", 3);

        Line goalRightNetBottom = new Line(width - 10, height/2+goalWidth/2-5, width - leftRightIntend-5, height/2+goalWidth/2-5, 3, "WHITE", 3);
        Line goalLeftNetBottom  = new Line(10, height/2+goalWidth/2-5, leftRightIntend+5, height/2+goalWidth/2-5, 3, "WHITE", 3);
        this.goalNet = new Line[]{goalRightNet, goalLeftNet, 
            goalRightNetTop, goalLeftNetTop, 
            goalRightNetBottom, goalLeftNetBottom
        };

        for (int i = 0; i < this.goalNet.length; i++ ){
            arena.addLine(this.goalNet[i]);
        }
    }

    public void setScore(int s1, int s2){
        
        //String s1 = Integer.toString(redGoal ? Integer.parseInt(this.redMalletScore.getText()) + 1 : Integer.parseInt(this.redMalletScore.getText()));
        //String s2 = Integer.toString(blueGoal ? Integer.parseInt(this.blueMalletScore.getText()) + 1 : Integer.parseInt(this.redMalletScore.getText()));
        String score1 = Integer.toString(s1);
        String score2 = Integer.toString(s2);
        redMalletScore = new Text(score1, 50, 15, 50, "RED", 3 );
        blueMalletScore = new Text(score2, 50, width - 50, 50, "BLUE", 3 );
        arena.addText(redMalletScore);
        arena.addText(blueMalletScore);
    }   

    public Line[] getGoalNet() {
        return goalNet;
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

    public int getMalletSize() {
        return malletSize;
    }

    public void setMalletSize(int malletSize) {
        this.malletSize = malletSize;
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

    public int getCentreSize() {
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
}