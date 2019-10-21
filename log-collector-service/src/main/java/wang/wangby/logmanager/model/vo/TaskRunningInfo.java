package wang.wangby.logmanager.model.vo;

import lombok.Data;
import wang.wangby.annotation.Remark;

import java.util.List;

//程序执行状态
@Data
public class TaskRunningInfo {

    @Remark("登录信息")
    private String loginInfo;

    @Remark("端口占用情况,通过lsof -:xx")
    private String portInfo;

    @Remark("文件信息")
    private String fileInfo;

    @Remark("文件读取情况")
    private List<FileReadInfo> fileReadInfos;

}
