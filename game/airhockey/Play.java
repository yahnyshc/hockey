
public class Play{

    public static void main(String[] args) {
        Game hockey = new Game();

        hockey.getPuck().setXSpeed(0);
        hockey.getPuck().setYSpeed(0);
        Thread t1 = new Thread() {
            public void run() {
                while(true){
                    movePuck(hockey);
                    //hockey.getArena().pause();
                    try { sleep(10); }
		            catch (Exception e) {};
                }
            }
        };
        t1.start();

        Thread t2 = new Thread() {
            public void run() {
                while(true){
                    moveRedMallet(hockey);
                    try { sleep(3); }
		            catch (Exception e) {};
                }
            }
        };
        t2.start();

        Thread t3 = new Thread() {
            public void run() {
                while(true){
                    moveBlueMallet(hockey);
                    try { sleep(3); }
		            catch (Exception e) {};
                }
            }
        };
        t3.start();
    }

    public static void movePuck(Game hockey){
        double[] borderCollision = {0,0};
        double friction = 0.99973;
        while(true){
            //checkGoal(hockey);
            borderCollision[0] = 0;
            borderCollision[1] = 0;
            hockey.getPuck().move(hockey.getPuck().getXSpeed(), hockey.getPuck().getYSpeed());
            borderCollision = hockey.getPuck().collidesBorders(hockey, true);
            if (! (borderCollision[0] == 0 && borderCollision[1] == 0) ){
                deflect(hockey.getPuck(), hockey.getPuck(), borderCollision, true);
            }
            else if ( hockey.getPuck().collides( hockey.getBlueMallet() ) ){
                deflect(hockey.getPuck(), hockey.getBlueMallet(), borderCollision, false);
            }
            else if( hockey.getPuck().collides( hockey.getRedMallet() ) ){
                deflect(hockey.getPuck(), hockey.getRedMallet(), borderCollision, false);
            }

            hockey.getPuck().setXSpeed(hockey.getPuck().getXSpeed() * friction);
            //System.out.println(speedX);
            hockey.getPuck().setYSpeed(hockey.getPuck().getYSpeed() * friction);
            //System.out.println(speedY);
            try { Thread.sleep(1); }
		    catch (Exception e) {};
        }
    }

    public static void checkGoal(Game hockey){
        if (hockey.getPuck().getXPosition() + hockey.getPuck().getSize()/2 < hockey.getArenaGoalLimits()[0] || 
            hockey.getPuck().getXPosition() - hockey.getPuck().getSize()/2 > hockey.getArenaGoalLimits()[1]){

            String[] colours = hockey.getBorderColours();

            for(int i = 0; i < 16; i++){
                String last = colours[3];
                for(int j = 3; j > 0; j--){
                    colours[j] = colours[j-1];
                }
                colours[0] = last;
                hockey.setBorderColours(colours);
                try { Thread.sleep(200);}
                catch (Exception e) {};
            }
            
            int xPosition = hockey.getWidth()/2;
            if (hockey.getPuck().getXPosition() + hockey.getPuck().getSize()/2 < hockey.getArenaGoalLimits()[0]){
                xPosition -= 150; 
            }
            else{
                xPosition += 150; 
            }
            hockey.getPuck().setXPosition(xPosition);
            hockey.getPuck().setYPosition(hockey.getHeight()/2);
            hockey.getPuck().setXSpeed(0);
            hockey.getPuck().setYSpeed(0);

            hockey.getRedMallet().setXPosition(hockey.getRedMalletStartingPos()[0]);
            hockey.getRedMallet().setYPosition(hockey.getBlueMalletStartingPos()[1]);
            hockey.getBlueMallet().setXPosition(hockey.getBlueMalletStartingPos()[0]);
            hockey.getBlueMallet().setYPosition(hockey.getBlueMalletStartingPos()[1]);
        }
    }

    public static void moveRedMallet(Game hockey){
        boolean w = hockey.getArena().letterPressed('W');
        boolean a = hockey.getArena().letterPressed('A');
        boolean s = hockey.getArena().letterPressed('S');
        boolean d = hockey.getArena().letterPressed('D');
        
        int xMove = 0;
        if (d){
            xMove += 1;
        }
        if (a){
            xMove -= 1;
        }

        int yMove = 0;
        if (w){
            yMove -= 1;
        }
        if (s){
            yMove += 1;
        }

        hockey.getRedMallet().setXSpeed(xMove);
        hockey.getRedMallet().setYSpeed(yMove);

        hockey.getRedMallet().move( xMove, yMove );
        
        double[] borderCollision = hockey.getRedMallet().collidesBorders(hockey, true);
        if( ! (borderCollision[0] == 0 && borderCollision[1] == 0) ){
            hockey.getRedMallet().move( -xMove, -yMove );
        }
        else if( hockey.getRedMallet().getXPosition() + hockey.getBlueMallet().getSize()/2 >= hockey.getWidth()/2 ){
            hockey.getRedMallet().move( -xMove, -yMove );
        }
    }

    public static void moveBlueMallet(Game hockey){
        boolean up = hockey.getArena().upPressed();
        boolean left = hockey.getArena().leftPressed();
        boolean right = hockey.getArena().rightPressed();
        boolean down = hockey.getArena().downPressed();

        int xMove = 0;
        if (right){
            xMove += 1;
        }
        if (left){
            xMove -= 1;
        }

        int yMove = 0;
        if (up){
            yMove -= 1;
        }
        if (down){
            yMove += 1;
        }
        hockey.getBlueMallet().setXSpeed(xMove);
        hockey.getBlueMallet().setYSpeed(yMove);

        hockey.getBlueMallet().move( xMove, yMove );
        double[] borderCollision = hockey.getBlueMallet().collidesBorders(hockey, true);
        if( ! (borderCollision[0] == 0 && borderCollision[1] == 0) ){
            hockey.getBlueMallet().move( -xMove, -yMove );
        }
        else if( hockey.getBlueMallet().getXPosition() - hockey.getBlueMallet().getSize()/2 <= hockey.getWidth()/2 ){
            hockey.getBlueMallet().move( -xMove, -yMove );
        }
    }

    public static void deflect(Ball ball1, Ball ball2, double[] borderCollision, boolean isBorder)
    {   
        // The position and speed of each of the two balls in the x and y axis before collision.
        // YOU NEED TO FILL THESE VALUES IN AS APPROPRIATE...
        double xPosition1, xPosition2, yPosition1, yPosition2;
        double xSpeed1, xSpeed2, ySpeed1, ySpeed2;
        xPosition1 = ball1.getXPosition();
        yPosition1 = ball1.getYPosition();
        xSpeed1 = ball1.getXSpeed();
        ySpeed1 = ball1.getYSpeed();
        if ( isBorder ){
            xPosition2 = borderCollision[0];
            yPosition2 = borderCollision[1];
            xSpeed2 = -1 * xSpeed1 * 1.5;
            ySpeed2 = -1 * ySpeed1 * 1.5;
        }
        else{
            xPosition2 = ball2.getXPosition();
            yPosition2 = ball2.getYPosition();
            xSpeed2 = ball2.getXSpeed();
            ySpeed2 = ball2.getYSpeed();
            if( (xSpeed1 != 0 || ySpeed1 != 0) && (xSpeed2 == 0 && ySpeed2 == 0) ){
                xSpeed2 = -1 * xSpeed1;
                ySpeed2 = -1 * ySpeed1;
            }
        }
        // Calculate initial momentum of the balls... We assume unit mass here.
        double p1InitialMomentum = Math.sqrt(xSpeed1 * xSpeed1 + ySpeed1 * ySpeed1);
        double p2InitialMomentum = Math.sqrt(xSpeed2 * xSpeed2 + ySpeed2 * ySpeed2);
        // calculate motion vectors
        double[] p1Trajectory = {xSpeed1, ySpeed1};
        double[] p2Trajectory = {xSpeed2, ySpeed2};
        // Calculate Impact Vector
        double[] impactVector = {xPosition2 - xPosition1, yPosition2 - yPosition1};
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
        double slowDown = 0.6;
        ball1.setXSpeed( p1FinalTrajectory[0] * mag * slowDown);
        ball1.setYSpeed( p1FinalTrajectory[1] * mag * slowDown);
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
