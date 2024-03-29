package engine;

import airhockey.Game;
import engine.GameArena;
import engine.Line;
import engine.Text;
import engine.Rectangle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.InputStream;

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
	private int player;
	private BufferedImage image = null;

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
	public Ball(double x, double y, double diameter, String col, int layer)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.size = diameter;
		this.colour = col;
		this.layer = layer;
	}	

	/**
	 * Constructor for the Ball class, with a layer specified.
	 * @param x The x co-ordinate of centre of the Ball (in pixels)
	 * @param y The y co-ordinate of centre of the Ball (in pixels)
	 * @param diameter The diameter of the Ball (in pixels)
	 * @param col The colour of the Ball (Permissible colours are: BLACK, BLUE, CYAN, DARKGREY, GREY, GREEN, LIGHTGREY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW or BROWN)
	 * @param layer The layer this ball is to be drawn on. Objects with a higher layer number are always drawn on top of those with lower layer numbers.
	 */
	public Ball(double x, double y, double diameter, String col, int layer, int player)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.size = diameter;
		this.colour = col;
		this.layer = layer;
		this.player = player;
	}	

	public boolean moveUp(GameArena arena){
		if (player == 1) {return arena.letterPressed('W');}
		if (player == 2) {return arena.upPressed();}
		return false;
	}

	public boolean moveLeft(GameArena arena){
		if (player == 1) {return arena.letterPressed('A');}
		if (player == 2) {return arena.leftPressed();}
		return false;
	}
	
	public boolean moveRight(GameArena arena){
		if (player == 1) {return arena.letterPressed('D');}
		if (player == 2) {return arena.rightPressed();}
		return false;
	}
	
	public boolean moveDown(GameArena arena){
		if (player == 1) {return arena.letterPressed('S');}
		if (player == 2) {return arena.downPressed();}
		return false;
	}

	public boolean crossesMiddleLine(GameArena arena){
		if (player == 1) { return (this.getXPosition() + this.getSize()/2 >= arena.getWidth()/2); }
		if (player == 2) { return (this.getXPosition() - this.getSize()/2 <= arena.getWidth()/2); }
		return false;
	}

	public void setImage(String filename){
		try {
            InputStream imageStream = Game.class.getResourceAsStream(filename);
        	this.image = ImageIO.read(imageStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public BufferedImage getImage(){
		return this.image;
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
	 * Set speed of a ball
	 * @param x and y speed
	 */
	public void setSpeed(double x, double y){
		this.xSpeed = x;
		this.ySpeed = y;
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

	public void setPosition(double x, double y){
		this.xPosition = x;
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
	 * Get the line ball colides.
	 * 
	 * @param hockey hockey game object
	 * @return line that the ball colides
	 * else returns null otherwise
	 */
	public Line getCollisionLine(Game hockey){
		Line collisionLine = colidesLine(hockey, hockey.getBorders());
		if ( ! inTheGoalArea(hockey) && collisionLine != null ) return collisionLine;
		collisionLine = colidesLine(hockey, hockey.getGoalNet());
		if ( inTheGoalArea(hockey) && collisionLine != null ) return collisionLine;
		return null;
	}

	/**
	 * Get the point where ball colides border.
	 * 
	 * @param hockey hockey game object
	 * @return point where the ball collides any line
	 * else returns null otherwise
	 */
	public double[] getCollisionPoint(Game hockey){
		Line l = getCollisionLine(hockey);
		if (l != null) return this.collisionPoint(l, this);
		else return null;
	}

	/**
	 * Determines if the ball is in the goal area or not
	 * 
	 * @param hockey hockey game object
	 * @return true if ball is in the goal area, false otherwise
	 */
	public boolean inTheGoalArea(Game hockey){
		return ((this.getXPosition() - this.getSize()/2 < hockey.getArenaGoalLimits()[0] || 
				 this.getXPosition() + this.getSize()/2 > hockey.getArenaGoalLimits()[1]) && 
				 this.getYPosition() - this.getSize()/2 > hockey.getHeight()/2-hockey.getGoalWidth()/2 && 
				 this.getYPosition() + this.getSize()/2 < hockey.getHeight()/2+hockey.getGoalWidth()/2 );
	}

	/**
	 * Determines if the ball colides any of the given lines
	 * 
	 * @param hockey hockey game object
	 * @param lines lines we check for collision
	 * @return Line we colide, or null if we do not collide anything
	 */
	public Line colidesLine(Game hockey, Line[] lines){
		double shortestDist = Double.POSITIVE_INFINITY;
		Line collisionLine = null;
		for(int i = 0; i < lines.length; i++){
			double distance = distanceToLine(lines[i]);
			if ( distance < shortestDist && (distance - hockey.getBordersThickness()/2 < this.size/2) ){
				shortestDist = distance;
				collisionLine = lines[i];
			}
		}
		return collisionLine;
	}

	/**
	 * Calculates distance of the ball to the given line
	 * 
	 * @param line line we check distance to
	 * @return distance to the given line
	 */
	public double distanceToLine(Line line){
		double x1 = line.getXStart();
		double y1 = line.getYStart();
		double x2 = line.getXEnd();
		double y2 = line.getYEnd();

		double a = y1-y2;
		double b = x2-x1;
		double c = (x1-x2)*y1 + (y2-y1)*x1;

		return Math.abs((a*xPosition + b*yPosition + c))/Math.sqrt(a*a + b*b);
	}

	/**
	 * Determines where the ball hits the border
	 * @param line - border
	 * @param ball - puck or mallet
	 * @return returns where the ball hits the line
	 */
	public double[] collisionPoint(Line line, Ball ball){
		double x1=line.getXStart(), y1=line.getYStart(), x2=line.getXEnd(), y2=line.getYEnd(), x3=ball.getXPosition(), y3=ball.getYPosition();
		double px = x2-x1, py = y2-y1, dAB = px*px + py*py;
		double u = ((x3 - x1) * px + (y3 - y1) * py) / dAB;
		double x = x1 + u * px, y = y1 + u * py;
		return new double[]{x, y}; //this is D
	}

	/**
	 * deflects the ball
	 * @param hockey the game
	 * @param ball2 ball that it colides
	 * @param collision point of collision
	 * @param isBorder flag if its border collision or ball colision
	 */
	public void deflect( Game hockey, Ball ball2, double[] collision, boolean isBorder)
    {   
		if (!hockey.isSoundMuted()) hockey.playSound(hockey.getBounceSound());

        // The position and speed of each of the two balls in the x and y axis before collision.
        double xPosition2, yPosition2, xSpeed2, ySpeed2;
		double distance;
        if ( isBorder ){
            xPosition2 = collision[0];
            yPosition2 = collision[1];

            xSpeed2 = -1 * this.getXSpeed() * 1.10;
            ySpeed2 = -1 * this.getYSpeed() * 1.10;
			distance = hockey.getBordersThickness()/2;
        }
        else{
            xPosition2 = ball2.getXPosition();
            yPosition2 = ball2.getYPosition();
            xSpeed2 = ball2.getXSpeed();
            ySpeed2 = ball2.getYSpeed();
            if( (this.getXSpeed() != 0 || this.getYSpeed() != 0) && (xSpeed2 == 0 && ySpeed2 == 0) ){
                xSpeed2 = -1 * this.getXSpeed();
                ySpeed2 = -1 * this.getYSpeed();
            }
			distance = ball2.getSize()/2;
        }
		
		double slope = (yPosition2 - this.getYPosition()) / (xPosition2 - this.getXPosition());
		double degree = Math.atan(slope);
		double x = xPosition2 + distance * Math.cos(degree);
		double y = yPosition2 + distance * Math.sin(degree);

		double[] momentumSpeed = deflectionMomentum(hockey, x, y, xSpeed2, ySpeed2);
		this.setSpeed( momentumSpeed[0], momentumSpeed[1] );

		Line colisionLine = getCollisionLine(hockey); 
		if ( isBorder ){
			while( colisionLine == getCollisionLine(hockey) ){
				this.move( this.getXSpeed()/100, this.getYSpeed()/100);
			} 
		}
    }

	private double[] deflectionMomentum(Game hockey, double xPosition2, double yPosition2, double xSpeed2, double ySpeed2){
		// Calculate initial momentum of the balls... We assume unit mass here.
        double p1InitialMomentum = Math.sqrt(this.getXSpeed() * this.getXSpeed() + this.getYSpeed() * this.getYSpeed());
        double p2InitialMomentum = Math.sqrt(xSpeed2 * xSpeed2 + ySpeed2 * ySpeed2);
        // calculate motion vectors
        double[] p1Trajectory = {this.getXSpeed(), this.getYSpeed()};
        double[] p2Trajectory = {xSpeed2, ySpeed2};
        // Calculate Impact Vector
        double[] impactVector = {xPosition2 - this.getXPosition(), yPosition2 - this.getYPosition()};
        double[] impactVectorNorm = normalizeVector(impactVector);
        // Calculate scalar product of each trajectory and impact vector
        double p1dotImpact = Math.abs(p1Trajectory[0] * impactVectorNorm[0] + p1Trajectory[1] * impactVectorNorm[1]);
        double p2dotImpact = Math.abs(p2Trajectory[0] * impactVectorNorm[0] + p2Trajectory[1] * impactVectorNorm[1]);
        // Calculate the deflection vectors - the amount of energy transferred from one ball to the other in each axis
        double[] p1Deflect = { -impactVectorNorm[0] * p2dotImpact, -impactVectorNorm[1] * p2dotImpact };
        double[] p2Deflect = { impactVectorNorm[0] * p1dotImpact, impactVectorNorm[1] * p1dotImpact };
        // Calculate the final trajectories
        double[] p1FinalTrajectory = {p1Trajectory[0] + p1Deflect[0] - p2Deflect[0], p1Trajectory[1] + p1Deflect[1] - p2Deflect[1]};
        double[] p2FinalTrajectory = {p2Trajectory[0] + p2Deflect[0] - p1Deflect[0], p2Trajectory[1] + p2Deflect[1] - p1Deflect[1]};
        // Calculate the final energy in the system.
        double p1FinalMomentum = Math.sqrt(p1FinalTrajectory[0] * p1FinalTrajectory[0] + p1FinalTrajectory[1] * p1FinalTrajectory[1]);
        double p2FinalMomentum = Math.sqrt(p2FinalTrajectory[0] * p2FinalTrajectory[0] + p2FinalTrajectory[1] * p2FinalTrajectory[1]);
        // Scale the resultant trajectories if we've accidentally broken the laws of physics.
        double mag = (p1InitialMomentum + p2InitialMomentum) / (p1FinalMomentum + p2FinalMomentum);
        // Calculate the final x and y speed settings for the two balls after collision.
		double puckSpeedMultiplier = hockey.getPuckSpeedMultiplier();
		return new double[]{p1FinalTrajectory[0] * mag * puckSpeedMultiplier, p1FinalTrajectory[1] * mag * puckSpeedMultiplier};
	}

    /**
    * Converts a vector into a unit vector.
    * Used by the deflect() method to calculate the resultant direction after a collision.
    */
    private static double[] normalizeVector(double[] vec)
    {
        double mag = 0.0;
        int dimensions = vec.length;
        double[] result = new double[dimensions];
        for (int i=0; i < dimensions; i++)
            mag += vec[i] * vec[i];
        
        mag = Math.sqrt(mag);

        if (mag == 0.0){
            result[0] = 1.0;
            for (int i=1; i < dimensions; i++)
            result[i] = 0.0;
        }
        else{
            for (int i=0; i < dimensions; i++)
                result[i] = vec[i] / mag;
        }
        return result;
    }
}