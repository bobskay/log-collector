package wang.wangby.mquery;

import wang.wangby.model.dao.BaseModel;
import wang.wangby.utils.StringUtil;

import java.util.List;

public class Condition {

    private String field;
    private  BaseModel baseModel;
    private Mquery mquery;
    public Condition(Mquery mquery,BaseModel baseModel,String field){
        this.field=field;
        this.baseModel=baseModel;
        this.mquery=mquery;
    }

    public Mquery in(List list){
       String sql=SqlUtil.in(field,list);
       String old=(String)baseModel.getExt().get("condition");
       if(!StringUtil.isEmpty(old)){
           sql= old+" and ("+sql+")";
       }
        baseModel.getExt().put("condition",sql);
       return mquery;
    }
}
