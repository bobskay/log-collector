package wang.wangby.utils.threadpool.batch;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.utils.DateTime;

import java.util.Date;
import java.util.List;

@Data
public class BatchReport {
    @Remark
    private Date createTime;
    @Remark("创建时间ns")
    private Long create;
    @Remark("开始执行")
    private Long runBegin;
    @Remark("结束执行")
    private Long runEnd;
    @Remark("总数")
    private int taskCount;
    @Remark("执行个数")
    private int execCount;
    @Remark("成功个数")
    private int success;
    @Remark("失败格式")
    private int failure;
    @Remark("成功用时")
    private long successTotal;
    @Remark("失败用时")
    private long failureTotal;
    @Remark("最快用时")
    private long fastest;
    @Remark("最慢用时")
    private long slowest;

    public String getSuccessAvg() {
        if (success == 0) {
            return "";
        }
        double time = successTotal;
        time = time / success;
        return DateTime.showNs(time);
    }

    public String getFailureAvg() {
        if (failure == 0) {
            return "";
        }
        double time = failureTotal;
        time = time / failure;
        return DateTime.showNs(time);
    }

    public String getTotalTimes() {
        return DateTime.showNs(runBegin,runEnd);
    }

    //准备时间
    public String getPrepare() {
        return DateTime.showNs(create,runBegin);
    }

    public static BatchReport getReport(List<? extends InvokeBean> resultList,Long create, Long runBegin, Long runEnd) {
        BatchReport report = new BatchReport();
        report.setCreateTime(new Date());
        report.create=create;
        report.runBegin=runBegin;
        report.setRunEnd(runEnd);
        report.setTaskCount(resultList.size());

        int success = 0;
        int failue = 0;
        long successTotal = 0;
        long failueTotal = 0;
        long fast = Long.MAX_VALUE;
        long slow = 0l;
        for (InvokeBean eachResult : resultList) {
            if(eachResult.getInvokeBegin()==null){
                continue;
            }

            InvokeBean realResult = getRealResult(eachResult);
            Long invokeBegin = eachResult.getInvokeBegin();
            Long invokeEnd = eachResult.getInvokeEnd();


            if (realResult.getInvokeBegin() != null && realResult.getInvokeEnd() != null) {
                invokeBegin = realResult.getInvokeBegin();
                invokeEnd = realResult.getInvokeEnd();
            }
            long time = invokeEnd - invokeBegin;
            if (realResult.getResult() instanceof Exception) {
                failue++;
                failueTotal += time;
            } else {
                success++;
                successTotal += time;
                if (time > slow) {
                    slow = time;
                }
                if (time < fast) {
                    fast = time;
                }
            }
        }
        if (fast == Long.MAX_VALUE) {
            fast = 0;
        }
        report.setSuccess(success);
        report.setSuccessTotal(successTotal);
        report.setFailure(failue);
        report.setFailureTotal(failueTotal);
        report.setFastest(fast);
        report.setSlowest(slow);
        report.setExecCount(success+failue);
        return report;
    }

    //调用结果的返回值可能是另一个调用结果,获取最底层的调用信息
    private static InvokeBean getRealResult(InvokeBean eachResult) {
        Object o = eachResult.getResult();
        if (o instanceof InvokeBean) {
            return getRealResult((InvokeBean) o);
        }
        return eachResult;
    }
}
