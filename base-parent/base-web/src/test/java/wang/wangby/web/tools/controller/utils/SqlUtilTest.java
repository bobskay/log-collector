package wang.wangby.web.tools.controller.utils;

import org.junit.Test;
import wang.wangby.dao.model.ColumnInfo;
import wang.wangby.dao.model.TableInfo;
import wang.wangby.test.TestBase;

import static org.junit.Assert.*;

public class SqlUtilTest extends TestBase {

    @Test
    public void toTable() {
        String sql = " errorororoororo CREATE table t_logTask\n" +
                "(\n" +
                "   logTaskId            bigint not null Comment '主键',\n" +
                "   logFileId            bigint comment '文件id',\n" +
                "   createTime           datetime comment '创建时间',\n" +
                "   updateTime           datetime comment '最后更新时间',\n" +
                "   logType              varchar(20) comment '日志类型',\n" +
                "   taskState            varchar(10) comment '任务状态',\n" +
                "   runningServer        varchar(100) comment '执行任务的服务器',\n" +
                "   runningPort          int comment '执行任务所用端口',\n" +
                "   runningApp           varchar(255) comment '执行任务的程序',\n" +
                "   primary key (logTaskId)\n" +
                ");";
        TableInfo table=SqlUtil.toTable(sql);
        log.debug("------"+table.getTableName()+"------");
        for(ColumnInfo col:table.getColumns()){
            log.debug(col.getColumnName()+":"+col.getDataType()+":"+col.getColumnComment());
        }
    }
}