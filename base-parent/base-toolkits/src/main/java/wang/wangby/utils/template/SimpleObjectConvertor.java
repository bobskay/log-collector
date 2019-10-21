package wang.wangby.utils.template;

//将对象转为特定字符串的方法,需要在系统初始化时将SimpleObject.CONVERTOR,替换为具体实现类
public interface SimpleObjectConvertor{

	//将对象转为json
	public String toJson(Object target);

	//过滤html字符
	public String htmlEscape(String str);
}
