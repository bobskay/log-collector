package wang.wangby.logmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.wangby.dao.BaseDao;
import wang.wangby.service.BaseService;
import wang.wangby.logmanager.dao.LogTaskDetailDao;
import wang.wangby.logmanager.model.LogTaskDetail;

@Service
public class LogTaskDetailService extends BaseService<LogTaskDetail> {
    @Autowired
    LogTaskDetailDao logTaskDetailDao;

    public BaseDao defaultDao() {
        return logTaskDetailDao;
    }

    public LogTaskDetail newModel() {
        return new LogTaskDetail();
    }
}
