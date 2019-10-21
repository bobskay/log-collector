package wang.wangby.utils.serialize.json.vo;

import lombok.Data;

import java.util.Date;

@Data
public class Person {
    private Long id;
    private String name;
    private Date birthday;
    private String comment;
}
