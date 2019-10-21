package wang.wangby.mquery;

import wang.wangby.model.dao.BaseModel;
import wang.wangby.service.BaseService;

import java.util.List;

public class Mquery {

    private BaseService baseService;
    private BaseModel query;

    public Mquery(BaseService baseService){
        this.baseService=baseService;
        query=baseService.newModel();
    }

    public Condition attr(String name){
        Condition condition=new Condition(this,query,name);
        return condition;
    }
    public List list(){
        return baseService.list(query);
    }
}
