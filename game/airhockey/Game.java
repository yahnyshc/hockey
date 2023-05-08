
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
    private int malletSize = 20;
    private int topBottomIntend = 100;
    private int leftRightIntend = 100;
    private int goalWidth = 160;
    private int gapsWidth = 20;
    private int centreSize = 80;
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
        this.redMallet = new Ball(redMalletStartingPos[0], redMalletStartingPos[1], redMalletSize, "RED", 3);
        this.blueMallet = new Ball(blueMalletStartingPos[0], blueMalletStartingPos[1], blueMalletSize, "BLUE", 3);
        this.puck = new Ball(puckStartingPos[0], puckStartingPos[1], malletSize, "GREEN", 3);

        Line topLeftCornerTop = new Line(leftRightIntend+25, topBottomIntend, width/2-gapsWidth/2, topBottomIntend, bordersThickness, borderColours[0], 3);
        Line topLeftCornerLeft = new Line(leftRightIntend+25, topBottomIntend, leftRightIntend, height/2-goalWidth/2, bordersThickness, borderColours[0], 3);

        Line topRightCornerTop = new Line(width-leftRightIntend-25, topBottomIntend, width/2+gapsWidth/2, topBottomIntend, bordersThickness, borderColours[1], 3);
        Line topRightCornerRight = new Line(width-leftRightIntend-25, topBottomIntend, width - leftRightIntend, height/2-goalWidth/2, bordersThickness, borderColours[1], 3);

        Line middleLeft = new Line( width/2-gapsWidth/4, topBottomIntend+10, width/2-gapsWidth/4, height - topBottomIntend-10, 1.5, "White", 1);
        Line middleRight = new Line( width/2+gapsWidth/4, topBottomIntend+10, width/2+gapsWidth/4, height - topBottomIntend-10, 1.5, "White", 1);
        this.centreOut = new Ball(width/2, height/2, centreSize, "WHITE", 1);
        this.centreIn = new Ball(width/2, height/2, centreSize-2, "BLACK", 2);

        Line bottomRightCornerBottom = new Line(width-leftRightIntend-25, height-topBottomIntend, width/2+gapsWidth/2, height-topBottomIntend, bordersThickness, borderColours[2], 3);
        Line bottomRightCornerRight = new Line(width-leftRightIntend-25, height-topBottomIntend, width-leftRightIntend, height-height/2+goalWidth/2, bordersThickness, borderColours[2], 3);

        Line bottomLeftCornerBottom = new Line(leftRightIntend+25, height-topBottomIntend, width/2-gapsWidth/2, height-topBottomIntend, bordersThickness, borderColours[3], 3);
        Line bottomLeftCornerLeft = new Line(leftRightIntend+25, height-topBottomIntend, leftRightIntend, height-height/2+goalWidth/2, bordersThickness, borderColours[3], 3);

        this.borders = new Line[]{topLeftCornerLeft, topLeftCornerTop, 
            topRightCornerTop, topRightCornerRight, 
            bottomRightCornerRight, bottomRightCornerBottom, 
            bottomLeftCornerBottom, bottomLeftCornerLeft
        };

        this.arenaGoalLimits = new double[]{topLeftCornerLeft.getXEnd()-bordersThickness/2, topRightCornerRight.getXEnd()+bordersThickness/2};

        arena.addBall(redMallet);
        arena.addBall(blueMallet);

        for (int i = 0; i < borders.length; i++ ){
            arena.addLine(borders[i]);
        }
        
        arena.addLine(middleLeft);
        arena.addLine(middleRight);
        arena.addBall(centreIn);
        arena.addBall(centreOut); 

        arena.addBall(puck);

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

        Line goalLeftLine  = new Line(leftRightIntend+3, height/2-goalWidth/2+5, leftRightIntend+3, height/2+goalWidth/2-5, 0.5, "WHITE", 1);
        Line goalRightLine = new Line(width - leftRightIntend-3, height/2-goalWidth/2+5, width - leftRightIntend-3, height/2+goalWidth/2-5, 0.5, "WHITE", 1);

        arena.addLine(goalLeftLine);
        arena.addLine(goalRightLine);

        for (int i = 0; i < this.goalNet.length; i++ ){
            arena.addLine(this.goalNet[i]);
        }

        redMalletScore = new Text("0", 50, 15, 50, "RED", 3 );
        blueMalletScore = new Text("0", 50, width - 50, 50, "BLUE", 3 );
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