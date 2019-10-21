package wang.wangby.dao;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

//根据条件更新某个字段
@Data
public class UpdateField {
    private String fieldName;
    private List oldValues;
    private Object newValue;
    private List<Long> ids;


}
