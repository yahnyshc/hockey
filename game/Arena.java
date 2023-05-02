import airhockey.*;

public class Arena {
    private GameArena arena;
    private Ball mallet1;
    private Ball mallet2;
    private Ball puck;

    public GameArena getArena(){
        return arena;
    }

    public Ball getMallet1(){
        return this.mallet1;
    }

    public Ball getMallet2(){
        return this.mallet2;
    }

    public Ball getPuck(){
        return this.puck;
    }

    public void setArena(GameArena arena){
        this.arena = arena;
    }

    public void setMallet1(Ball mallet){
        this.mallet1 = mallet;
    }

    public void setMallet2(Ball mallet){
        this.mallet2 = mallet;
    }

    public void setPuck(Ball puck){
        this.puck = puck;
    }

}