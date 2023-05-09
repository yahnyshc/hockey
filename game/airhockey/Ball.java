
/**
 * Models a simple solid sphere. 
 * This class represents a Ball object. When combined with the GameArena class,
 * instances of the Ball class can be displayed on the screen.
 */
public class Ball 
{
	// The following instance variables define the
	// information needed to represent a Ball
	// Feel free to declare more instance variables if you think it will support your work. 
	
	private double xPosition;			// The X coordinate of this Ball
	private double yPosition;			// The Y coordinate of this Ball
	private double size;				// The diameter of this Ball
	private int layer;					// The layer of this ball is on.
	private String colour;				// The colour of this Ball
	private double xSpeed = 0;
	private double ySpeed = 0;

										// Permissible colours are:
										// BLACK, BLUE, CYAN, DARKGREY, GREY,
										// GREEN, LIGHTGREY, MAGENTA, ORANGE,
										// PINK, RED, WHITE, YELLOW, BROWN

	/**
	 * Constructor for the Ball class. Sets a default layer of 0.
	 * @param x The x co-ordinate of centre of the Ball (in pixels)
	 * @param y The y co-ordinate of centre of the Ball (in pixels)
	 * @param diameter The diameter of the Ball (in pixels)
	 * @param col The colour of the Ball (Permissible colours are: BLACK, BLUE, CYAN, DARKGREY, GREY, GREEN, LIGHTGREY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW or BROWN)
	 */
	public Ball(double x, double y, double diameter, String col)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.size = diameter;
		this.colour = col;
		this.layer = 0;
	}	

	/**
	 * Constructor for the Ball class, with a layer specified.
	 * @param x The x co-ordinate of centre of the Ball (in pixels)
	 * @param y The y co-ordinate of centre of the Ball (in pixels)
	 * @param diameter The diameter of the Ball (in pixels)
	 * @param col The colour of the Ball (Permissible colours are: BLACK, BLUE, CYAN, DARKGREY, GREY, GREEN, LIGHTGREY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW or BROWN)
	 * @param layer The layer this ball is to be drawn on. Objects with a higher layer number are always drawn on top of those with lower layer numbers.
	 */
	public Ball(double x, double y, double diameter, String col, int layer)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.size = diameter;
		this.colour = col;
		this.layer = layer;
	}	


	public double getXSpeed() {
		return xSpeed;
	}

	public double getYSpeed() {
		return ySpeed;
	}

	public void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}

	public void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}

	/**
	 * Obtains the current position of this Ball.
	 * @return the X coordinate of this Ball within the GameArena.
	 */
	public double getXPosition()
	{
		return xPosition;
	}

	/**
	 * Obtains the current position of this Ball.
	 * @return the Y coordinate of this Ball within the GameArena.
	 */
	public double getYPosition()
	{
		return yPosition;
	}

	/**
	 * Moves the current position of this Ball to the given co-ordinates
	 * @param x the new x co-ordinate of this Ball
	 */
	public void setXPosition(double x)
	{
		this.xPosition = x;
	}

	/**
	 * Moves the current position of this Ball to the given co-ordinates
	 * @param y the new y co-ordinate of this Ball
	 */
	public void setYPosition(double y)
	{
		this.yPosition = y;
	}

	/**
	 * Obtains the size of this Ball.
	 * @return the diameter of this Ball,in pixels.
	 */
	public double getSize()
	{
		return size;
	}
	
	/**
	 * Sets the diameter of this Ball to the given size.
	 * @param s the new diameter of this Ball, in pixels.
	 */
	public void setSize(double s)
	{
		size = s;
	}

	/**
	 * Obtains the layer of this Ball.
	 * @return the layer of this Ball.
	 */
	public int getLayer()
	{
		return layer;
	}

	/**
	 * Sets the layer of this Ball.
	 * @param l the new layer of this Ball. Higher layer numbers are drawn on top of low layer numbers.
	 */
	public void setLayer(int l)
	{
		layer = l;
	}

	/**
	 * Obtains the colour of this Ball.
	 * @return a textual description of the colour of this Ball.
	 */
	public String getColour()
	{
		return colour;
	}

	/**
	 * Sets the colour of this Ball.
	 * @param c the new colour of this Ball, as a String value. Permissable colours are: BLACK, BLUE, CYAN, DARKGREY, GREY, GREEN, LIGHTGREY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW or #RRGGBB.
	 */
	public void setColour(String c)
	{
		colour = c;
	}

	/**
	 * Moves this Ball by the given amount.
	 * 
	 * @param dx the distance to move on the x axis (in pixels)
	 * @param dy the distance to move on the y axis (in pixels)
	 */
	public void move(double dx, double dy)
	{
		xPosition += dx;
		yPosition += dy;
	}

	/**
	 * Determines if this Ball is overlapping a given ball.
	 * If the two balls overlap, they have collided.
	 * 
	 * @param b the ball to test for collision
	 * @return true of this ball is overlapping the ball b, false otherwise.
	 */
	public boolean collides(Ball b)
	{
		double dx = b.xPosition - xPosition;
		double dy = b.yPosition - yPosition;
		double distance = Math.sqrt(dx*dx+dy*dy);

		return distance < size/2 + b.size/2;
	}

	/**
	 * Determines if this Ball hits the borders.
	 * 
	 * @param hockey hockey game object
	 * @return true if this ball hits the border
	 */
	public double[] collidesBorders(Game hockey, boolean checkGoal)
	{	
		double shortestDist = 10000;
		Line border = hockey.getBorders()[0];

		for(int i = 0; i < hockey.getBorders().length; i++){
			double x1 = hockey.getBorders()[i].getXStart();
			double y1 = hockey.getBorders()[i].getYStart();
			double x2 = hockey.getBorders()[i].getXEnd();
			double y2 = hockey.getBorders()[i].getYEnd();

			double a = y1-y2;
			double b = x2-x1;
			double c = (x1-x2)*y1 + (y2-y1)*x1;

			double distance = Math.abs((a*xPosition + b*yPosition + c))/Math.sqrt(a*a + b*b);
			if ( distance < shortestDist){
				shortestDist = distance;
				border = hockey.getBorders()[i];
			}
		}
		if ( shortestDist - hockey.getBordersThickness()/2 < this.size/2 
			&& (this.getXPosition() - this.getSize()/2 + 0.5 > hockey.getArenaGoalLimits()[0]) 
			&& (this.getXPosition() + this.getSize()/2 - 0.5 < hockey.getArenaGoalLimits()[1])){
			return getSpPoint(border, this);
		}

		shortestDist = 10000;
		border = hockey.getGoalNet()[0];
		for(int i = 0; i < hockey.getGoalNet().length; i++){
			double x1 = hockey.getGoalNet()[i].getXStart();
			double y1 = hockey.getGoalNet()[i].getYStart();
			double x2 = hockey.getGoalNet()[i].getXEnd();
			double y2 = hockey.getGoalNet()[i].getYEnd();

			double a = y1-y2;
			double b = x2-x1;
			double c = (x1-x2)*y1 + (y2-y1)*x1;

			double distance = Math.abs((a*xPosition + b*yPosition + c))/Math.sqrt(a*a + b*b);
			if ( distance < shortestDist){
				shortestDist = distance;
				border = hockey.getGoalNet()[i];
			}
		}

		if ( shortestDist - (hockey.getBordersThickness()-2)/2 < this.size/2 
			&& (this.getXPosition() - this.getSize()/2 < hockey.getArenaGoalLimits()[0] 
			|| this.getXPosition() + this.getSize()/2 > hockey.getArenaGoalLimits()[1])){
				return getSpPoint(border, this);
		}
		return new double[]{0,0};
	}

	public double[] getSpPoint(Line line, Ball ball){
		double x1=line.getXStart(), y1=line.getYStart(), x2=line.getXEnd(), y2=line.getYEnd(), x3=ball.getXPosition(), y3=ball.getYPosition();
		double px = x2-x1, py = y2-y1, dAB = px*px + py*py;
		double u = ((x3 - x1) * px + (y3 - y1) * py) / dAB;
		double x = x1 + u * px, y = y1 + u * py;
		return new double[]{x, y}; //this is D
	}
}