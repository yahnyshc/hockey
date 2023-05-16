
public class Play{

    public static void main(String[] args) {
        Game hockey = new Game();

        hockey.getPuck().setXSpeed(0);
        hockey.getPuck().setYSpeed(0);
        Thread t1 = new Thread() {
            public void run() {
                movePuck(hockey);
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

        Thread t4 = new Thread() {
            public void run() {
                while(true){
                    hockey.checkCheat();
                }
            }
        };
        t4.start();
    }

    public static void movePuck(Game hockey){
        double[] borderCollision = {0,0};
        double friction = 0.99973;
        double prevXpos = 0;
        double prevYpos = 0;
        double prevXspd = 0;
        double prevYspd = 0;

        while(true){
            // safety checks
            if (Double.isNaN(hockey.getPuck().getXSpeed())
            || Double.isNaN(hockey.getPuck().getYSpeed())){
                hockey.getPuck().setXPosition(prevXpos);
                hockey.getPuck().setYPosition(prevYpos);
                hockey.getPuck().setXSpeed(-(prevXspd+0.1));
                hockey.getPuck().setYSpeed(-(prevYspd+0.1));
            }
            prevXpos = hockey.getPuck().getXPosition();
            prevYpos = hockey.getPuck().getYPosition();
            prevXspd = hockey.getPuck().getXSpeed();
            prevYspd = hockey.getPuck().getYSpeed();
            
            hockey.checkGoal();

            borderCollision[0] = 0;
            borderCollision[1] = 0;
            hockey.getPuck().move(hockey.getPuck().getXSpeed(), hockey.getPuck().getYSpeed());

            borderCollision = hockey.getPuck().collidesBorders(hockey, true);
            if (! (borderCollision[0] == 0 && borderCollision[1] == 0) ){
                hockey.getPuck().deflect(hockey, hockey.getPuck(), borderCollision, true);
                if ( hockey.getPuck().collides( hockey.getRedMallet()) ){
                    hockey.getRedMallet().move( hockey.getPuck().getXSpeed()*5, hockey.getPuck().getYSpeed()*5 );
                }
                if( hockey.getPuck().collides( hockey.getBlueMallet()) ){
                    hockey.getBlueMallet().move( hockey.getPuck().getXSpeed()*5, hockey.getPuck().getYSpeed()*5 );
                }
            }
            else if ( hockey.getPuck().collides( hockey.getBlueMallet()) && hockey.getPuck().collides( hockey.getRedMallet())){
                hockey.getRedMallet().move( hockey.getBlueMallet().getXSpeed()*5, hockey.getBlueMallet().getYSpeed()*5 );
                hockey.getBlueMallet().move( hockey.getRedMallet().getXSpeed()*5, hockey.getRedMallet().getYSpeed()*5 );
            }
            else if ( hockey.getPuck().collides( hockey.getBlueMallet() ) ){
                hockey.getPuck().deflect(hockey, hockey.getBlueMallet(), borderCollision, false);
            }
            else if( hockey.getPuck().collides( hockey.getRedMallet() ) ){
                hockey.getPuck().deflect(hockey, hockey.getRedMallet(), borderCollision, false);
            }

            hockey.getPuck().setXSpeed(hockey.getPuck().getXSpeed() * friction);
            hockey.getPuck().setYSpeed(hockey.getPuck().getYSpeed() * friction);
            try { Thread.sleep(1); }
		    catch (Exception e) {};
        }
    }

    public static void moveRedMallet(Game hockey){
        boolean w = hockey.getArena().letterPressed('W');
        boolean a = hockey.getArena().letterPressed('A');
        boolean s = hockey.getArena().letterPressed('S');
        boolean d = hockey.getArena().letterPressed('D');
        
        int xMove = 0;
        xMove += d ? 1 : 0;
        xMove += a ? -1 : 0;

        int yMove = 0;
        yMove += s ? 1 : 0;
        yMove += w ? -1 : 0;

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
        else if( hockey.getRedMallet().collides(hockey.getPuck()) ){
            double[] borderC = hockey.getPuck().collidesBorders(hockey, false);
            if ( borderC[0]!= 0 && borderC[1]!=0 ){
                hockey.getRedMallet().move( -xMove, -yMove );
            }
        }

    }

    public static void moveBlueMallet(Game hockey){
        boolean up = hockey.getArena().upPressed();
        boolean left = hockey.getArena().leftPressed();
        boolean right = hockey.getArena().rightPressed();
        boolean down = hockey.getArena().downPressed();

        int xMove = 0;
        xMove += right ? 1 : 0;
        xMove += left ? -1 : 0;

        int yMove = 0;
        yMove += down ? 1 : 0;
        yMove += up ? -1 : 0;

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
        else if( hockey.getBlueMallet().collides(hockey.getPuck()) ){
            double[] borderC = hockey.getPuck().collidesBorders(hockey, false);
            if ( borderC[0]!= 0 && borderC[1]!=0 ){
                hockey.getBlueMallet().move( -xMove, -yMove );
            }
        }
    }
}
