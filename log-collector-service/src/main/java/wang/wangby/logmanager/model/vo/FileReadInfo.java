package wang.wangby.logmanager.model.vo;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.logmanager.model.LogTaskDetail;
import wang.wangby.utils.shell.file.FileInfo;

@Data
@Remark("文件读取情况")
public class FileReadInfo {
    private FileInfo fileInfo;
    private LogTaskDetail logTaskDetail;

}
