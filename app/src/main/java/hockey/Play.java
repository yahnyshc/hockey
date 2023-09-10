
import airhockey.Game;

public class Play{

    public static void main(String[] args) {
        Game hockey = new Game();

        Thread t1 = new Thread() {
            public void run() {
                hockey.movePuck();
            }
        };
        t1.start();

        Thread t2 = new Thread() {
            public void run() {
                while(true){
                    hockey.moveMallet(hockey.getRedMallet());
                    try { sleep(3); }
		            catch (Exception e) {};
                }
            }
        };
        t2.start();

        Thread t3 = new Thread() {
            public void run() {
                while(true){
                    hockey.moveMallet(hockey.getBlueMallet());
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

        Thread t5 = new Thread() {
            public void run() {
                while(true){
                    if ( ! hockey.goalCelebrationOngoing() ){
                        hockey.checkGoal();
                        try { sleep(140); }
		                catch (Exception e) {};
                    }
                }
            }
        };
        t5.start();
    }
}
