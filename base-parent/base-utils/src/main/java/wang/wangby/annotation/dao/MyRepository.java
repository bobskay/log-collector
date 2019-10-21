package wang.wangby.annotation.dao;

import wang.wangby.model.dao.BaseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

//持久化数据的基本方法
public interface MyRepository<T extends BaseModel> {
    // 新增记录
    int insert(T model) throws  Exception;

    // 通过主键删除记录
    int delete(Class<T> clazz, Long id) throws  Exception;

    //更新数据
    int update(T model) throws  Exception;

    //遍历数据
    void iterator(Function<T, Boolean> visitor) throws  Exception;

    T get(Class<T> clazz, Long id) throws  Exception;

    default List getAll(Class clazz) throws Exception {
        List list=new ArrayList();
        iterator(t->{
            list.add(t);
            return true;
        });
        return list;
    }
}
