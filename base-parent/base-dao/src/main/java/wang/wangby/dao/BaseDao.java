package wang.wangby.dao;

import wang.wangby.model.dao.BaseModel;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface BaseDao<T extends BaseModel> {

    // 新增记录
    int insert(T model);

    // 通过主键删除记录
    int deleteById(Long[] id);

    // 通过主键更新记录,如果要将字段更新为null,用updateFull
    int updateById(T model);

    /**
     * 根据条件更新单个字段
     *
     * @param map.field     要更新的字段名称
     * @param map.ids       要更新数据的主键,list
     * @param map.newValue  新值
     * @param map.oldValues 旧值,list
     */
    int updateField(Map map);

    // 通过主键更新记录,所有字段都更新
    int updateFull(T model);

    // 查询列表,如果model.ext.start ,model.ext.size 有值,就分页
    List<T> select(T model);

    // 获得记录条数
    long getCount(T model);

    // 通过主键获得记录
    T get(long id);

    // 通过主键获得记录
    List<T> getById(List<Long> id);

    //批量插入
    int insertBatch(List<T> list);

    Object max(T model);

    default Integer maxInteger(String fieldName, T model, Consumer<T> consumer) {
        consumer.accept(model);
        model.getExt().put("fieldName", fieldName);
        return (Integer) this.max(model);
    }

    default List<T> list(T model, Consumer<T> consumer) {
        if (consumer != null) {
            consumer.accept(model);
        }
        return this.select(model);
    }


}
