package wang.wangby.dao;

import org.apache.ibatis.annotations.Mapper;
import wang.wangby.test.model.BookInfo;

@Mapper
public interface BookDaoTest extends BaseDao<BookInfo> {
}