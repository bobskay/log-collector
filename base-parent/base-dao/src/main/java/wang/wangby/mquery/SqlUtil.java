package wang.wangby.mquery;

import wang.wangby.exception.Message;
import wang.wangby.utils.StrBuilder;

import java.util.List;

public class SqlUtil {

    //TODO 只支持字符串和数字
    public static String in(String name, List values){
        if(values.size()==0){
            return "1<>1";
        }
        StrBuilder sb=new StrBuilder();
        sb.append(name +" in (");
        for(Object o:values){
            if(o.getClass()==String.class){
                sb.append("'"+o+"',");
                continue;
            }
            if(o.getClass().getName().startsWith("java.lang")){
                throw new Message("目前不支持通过该类型查询:"+o.getClass());
            }
            sb.append(o+",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.append(")").toString();
    }
}
