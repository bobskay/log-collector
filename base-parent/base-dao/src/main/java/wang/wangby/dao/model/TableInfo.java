package wang.wangby.dao.model;

import java.util.List;

import lombok.Data;
import wang.wangby.annotation.Remark;

@Data
@Remark("表信息")
public class TableInfo  {
	
	@Remark("表名")
	private String tableName;
	@Remark("表注释")
	private String tableComment;
	@Remark("对应数据信息")
	private DatabaseInfo databaseInfo;
	@Remark("包含的列")
	private List<ColumnInfo> columns;
	
}
