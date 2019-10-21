package wang.wangby.utils.threadpool.batch;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import wang.wangby.annotation.Remark;

import java.util.Date;
import java.util.Queue;

@Data
@Slf4j
public class BatchResult {
    @Remark("创建时间")
    private Date createTime;
    @Remark("总开始时间ns")
    private long create;
    @Remark("实际执行开始时间ns")
    private long runBegin;
    private long runEnd;
    private int count;
    private Queue<EachResult> eachResults;

}
