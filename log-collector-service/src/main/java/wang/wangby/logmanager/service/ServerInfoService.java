package wang.wangby.logmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.wangby.dao.BaseDao;
import wang.wangby.logmanager.dao.ServerInfoDao;
import wang.wangby.logmanager.model.ServerInfo;
import wang.wangby.service.BaseService;

@Service
public class ServerInfoService extends BaseService<ServerInfo> {

    @Autowired
    ServerInfoDao serverInfoDao;

    public BaseDao defaultDao() {
        return serverInfoDao;
    }

    public ServerInfo newModel() {
        return new ServerInfo();
    }
}
