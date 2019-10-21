package wang.wangby.logmanager.dao;

import org.apache.ibatis.annotations.Mapper;
import wang.wangby.dao.BaseDao;
import wang.wangby.logmanager.model.LogFile;

@Mapper
public interface LogFileDao extends BaseDao<LogFile> {
}
