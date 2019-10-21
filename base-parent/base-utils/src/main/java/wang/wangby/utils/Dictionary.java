package wang.wangby.utils;

public interface Dictionary {
    default  String getKey(){
        return this.toString();
    }

    default String getValue(){
        return this.toString();
    }

    default String getText(){
        return this.toString();
    }
}
