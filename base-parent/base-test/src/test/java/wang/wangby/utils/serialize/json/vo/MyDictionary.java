package wang.wangby.utils.serialize.json.vo;

import wang.wangby.utils.Dictionary;

public enum MyDictionary implements Dictionary {
    dic1("字典枚举1"),dic12("字典枚举2"),dic3("字典枚举3");
    private String text;
    MyDictionary(String text){
        this.text=text;
    }

    public String getKey() {
        return "my "+this.toString();
    }
}
