package wang.wangby.autoconfigure.dao;

import lombok.Data;
import wang.wangby.annotation.Remark;

@Data
public class DaoProperties {

	@Remark("生成映射文件的模板")
	private String mapperTemplate = "codetemplate/mapper.xml";
	@Remark("mybatis的映射文件路径")
	private String   mybatisMapperLocations= "classpath:mybatis/mapper/**/*.xml";
	//生成mapping文件扫描的跟路径路径
	private String daoBasePackage = "wang.wangby";

	private String outPutDir = "/opt/temp";

	private Integer machineNo = 0;

	public Integer getMachineNo() {
		return machineNo;
	}

	//机器号,一个集群里面需要为机器分配唯一序列号用于生成UUID
	public void setMachineNo(Integer machineNo) {
		this.machineNo = machineNo;
	}

}
