package wang.wangby.utils;

public class Integers {
    private int i;
    public Integers(int i){
        this.i=i;
    }

    public int incrementAndGet(){
        i++;
        return i;
    }

    public int get(){
        return i;
    }
}
