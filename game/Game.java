import airhockey.*;

public class Game{
    public static void main(String[] args){
        int width = 1200;
        int height = 600;

        GameArena arena = new GameArena(1200, 600, true);
        Ball mallet1 = new Ball(350, 300, 50, "RED", 3);
        Ball mallet2 = new Ball(850, 300, 50, "BLUE", 3);
        Ball puck = new Ball(width/2, height/2, 20, "GREEN", 3);

        int topBottomIntend = 25;
        int leftRightIntend = 50;
        int goalWidth = 160;
        int gapsWidth = 20;
        int centreSize = 80;

        Line topLeftCornerTop = new Line(leftRightIntend+50, topBottomIntend, width/2-gapsWidth/2, topBottomIntend, 10, "YELLOW", 3);
        Line topLeftCornerLeft = new Line(leftRightIntend+25, topBottomIntend+25, leftRightIntend, height/2-goalWidth/2, 10, "YELLOW", 3);

        Line topRightCornerTop = new Line(width-leftRightIntend-50, topBottomIntend, width/2+gapsWidth/2, topBottomIntend, 10, "GREEN", 3);
        Line topRightCornerRight = new Line(width-leftRightIntend-25, topBottomIntend+25, width - leftRightIntend, height/2-goalWidth/2, 10, "GREEN", 3);

        Line middleLeft = new Line( width/2-gapsWidth/4, 35, width/2-gapsWidth/4, height - 35, 1.5, "White", 1);
        Line middleRight = new Line( width/2+gapsWidth/4, 35, width/2+gapsWidth/4, height - 35, 1.5, "White", 1);
        Ball midBallOut = new Ball(width/2, height/2, centreSize, "WHITE", 2);
        Ball midBallIn = new Ball(width/2, height/2, centreSize-2, "BLACK", 3);


        Line bottomLeftCornerBottom = new Line(leftRightIntend+50, height-topBottomIntend, width/2-gapsWidth/2, height-topBottomIntend, 10, "RED", 3);
        Line bottomLeftCornerLeft = new Line(leftRightIntend+25, height-topBottomIntend-25, leftRightIntend, height-height/2+goalWidth/2, 10, "RED", 3);
        
        Line bottomRightCornerBottom = new Line(width-leftRightIntend-50, height-topBottomIntend, width/2+gapsWidth/2, height-topBottomIntend, 10, "BLUE", 3);
        Line bottomRightCornerRight = new Line(width-leftRightIntend-25, height-topBottomIntend, width-leftRightIntend, height-height/2+goalWidth/2, 10, "BLUE", 3);


        arena.addBall(mallet1);
        arena.addBall(mallet2);

        arena.addLine(topLeftCornerTop);
        arena.addLine(topLeftCornerLeft);
        arena.addLine(topRightCornerTop);
        arena.addLine(topRightCornerRight);
        arena.addLine(middleLeft);
        arena.addLine(middleRight);
        arena.addBall(midBallIn);
        arena.addBall(midBallOut);
        arena.addLine(bottomLeftCornerBottom);
        arena.addLine(bottomLeftCornerLeft);
        arena.addLine(bottomRightCornerBottom);
        arena.addLine(bottomRightCornerRight);

        arena.addBall(puck);

        arena.run();
    }
}