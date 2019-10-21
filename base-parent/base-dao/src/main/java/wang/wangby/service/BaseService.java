package wang.wangby.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.wangby.dao.BaseDao;
import wang.wangby.exception.Message;
import wang.wangby.model.Setter;
import wang.wangby.model.dao.BaseModel;
import wang.wangby.model.dao.Pagination;
import wang.wangby.mquery.Mquery;
import wang.wangby.utils.CollectionUtil;
import wang.wangby.utils.IdWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


//对数据库单表操作的基础server
abstract  public class BaseService<T extends BaseModel>{
    protected Logger log= LoggerFactory.getLogger(this.getClass());

    public int updateField(String fieldName,Object newValue,Object oldValue,long id){
        return updateField(fieldName,newValue,oldValue, CollectionUtil.singleList(id));
    }

    //更新单个字段
    public int updateField(String fieldName, Object newValue, Object oldValue, List<Long> ids){
        Map map=new HashMap<>();
        map.put("fieldName",fieldName);
        map.put("newValue",newValue);
        map.put("oldValues",CollectionUtil.singleList(oldValue));
        map.put("ids",ids);
        return this.defaultDao().updateField(map);
    }

    //setId,设置id的方法
    public  Integer insert(T t, Setter<T,Long> setId){
        setId.set(t,newId());
        return defaultDao().insert(t);
    }


    public T get(Long id) {
        if(id==null){
            throw new Message("通过主键查询时传入的ID为null");
        }
       return (T) defaultDao().get(id);
    }

    public List<T> get(List<Long> ids){
        return defaultDao().getById(ids);
    }
    public int delete(Long[] id){
        return defaultDao().deleteById(id);
    }

    //获取全部
    public List<T> getAll(){
        return list(newModel());
    }

    public List<T> list(Consumer<T> consumer){
        T t=newModel();
        return this.defaultDao().list(t,consumer);
    }

    public T unique(Consumer<T> consumer){
        T t=newModel();
        List<T> list= this.defaultDao().list(t,consumer);
        if(list.size()==0){
            return null;
        }
        return list.get(0);
    }

    public List<T> list(T t){
        return this.defaultDao().list(t,null);
    }

    public int update(T t){
        return defaultDao().updateById(t);
    }

    abstract public BaseDao defaultDao();

    abstract public T newModel();

    public void insert(T t) {
        if(t.id()==null){
            t.id(newId());
        }
        defaultDao().insert(t);
    }

    public List<T> select(T query,Integer offset, Integer limit) {
        query.getExt().put("offset", offset);
        query.getExt().put("limit", limit);
        List list = this.defaultDao().select(query);
        return list;
    }

    public Pagination selectPage(T query,Integer offset, Integer limit) {
        BaseDao baseDao=this.defaultDao();
        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = Pagination.DEFAULT_SIZE;
        }
        long count = baseDao.getCount(query);
        if (offset == null || offset > count) {
            return new Pagination(count, new ArrayList(), offset, limit);
        }
        query.getExt().put("offset", offset);
        query.getExt().put("limit", limit);
        List list = baseDao.select(query);
        return new Pagination(count, list, offset, limit);
    }


    public void updateById(T t) {
        defaultDao().updateById(t);
    }

    public Integer deleteById(Long[] ids) {
        return defaultDao().deleteById(ids);
    }

    public Integer insertBatch(List<T> list){
        return defaultDao().insertBatch(list);
    }

    public Long newId() {
        return IdWorker.nextLong();
    }

    public Mquery $(BaseService service){
        return new Mquery(service);
    }


}
