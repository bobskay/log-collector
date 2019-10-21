package wang.wangby.logmanager.dao;

import org.apache.ibatis.annotations.Mapper;
import wang.wangby.dao.BaseDao;
import wang.wangby.logmanager.model.ServerInfo;

@Mapper
public interface ServerInfoDao extends BaseDao<ServerInfo> {
}
