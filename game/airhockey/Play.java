
public class Play{

    public static void main(String[] args) {
        Game hockey = new Game();

        hockey.getPuck().setXSpeed(0);
        hockey.getPuck().setYSpeed(0);
        Thread t1 = new Thread() {
            public void run() {
                hockey.movePuck();
            }
        };
        t1.start();

        Thread t2 = new Thread() {
            public void run() {
                while(true){
                    hockey.moveRedMallet();
                    try { sleep(3); }
		            catch (Exception e) {};
                }
            }
        };
        t2.start();

        Thread t3 = new Thread() {
            public void run() {
                while(true){
                    hockey.moveBlueMallet();
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
}
