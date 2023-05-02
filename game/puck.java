

public class puck{
    private double xPosition = 300;			// The X coordinate of this Ball
	private double yPosition = 150;			// The Y coordinate of this Ball
	private double size = 25;				// The diameter of this Ball
	private int layer = 3;					// The layer of this ball is on.
	private String colour = "BLACK";				// The colour of this Ball
    private Ball ball;

    public puch(){
        ball = new Ball(xPosition, yPosition, size, colour, layer);
    }


}