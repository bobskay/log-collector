package wang.wangby.utils.shell.windows;

import wang.wangby.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class OutputString {
	private String data;
	private String delimiter= StringUtil.REG_BLANK;

	public static OutputString getInstance(String data, String delimiter) {
		OutputString str = new OutputString();
		str.data = data.trim();
		str.delimiter = delimiter;
		return str;
	}
	
	public static OutputString getInstance(String data) {
		OutputString str = new OutputString();
		str.data = data.trim();
		return str;
	}

	public List<String> get(int index) {
		try {
			List list=new ArrayList();
			String[] lines=data.split("\n");
			for(String line :lines) {
				String[] col=line.trim().split(delimiter);
				list.add(col[index]);
			}
			return list;
		}catch(Exception ex) {
			throw new RuntimeException("无法获取第"+(index+1)+"列数据:"+data,ex);
		}
	}

	public String getUnique(int index) {
		if(StringUtil.isEmpty(index)) {
			throw new RuntimeException("接收到的内容为空");
		}
		return get(index).get(0);
	}
	
	public String toString() {
		return data;
	}
}
