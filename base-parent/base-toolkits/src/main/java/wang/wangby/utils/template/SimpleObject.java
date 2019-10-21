package wang.wangby.utils.template;

import wang.wangby.utils.DateTime;
import wang.wangby.utils.StringUtil;

import java.util.Date;

public class SimpleObject {
	private static SimpleObjectConvertor CONVERTOR;

	// 在调用toJson和reamrk之前,需要先初始化CONVERTOR
	public static void init(SimpleObjectConvertor convertor) {
		CONVERTOR = convertor;
	}

	private Object target;

	public SimpleObject(Object value) {
		this.target = value;
	}

	public String getDate() {
		if (target instanceof Date) {
			String s = new DateTime((Date) target, DateTime.Format.YEAR_TO_DAY).toString();
			return s;
		}
		if (target instanceof Long) {
			Long time = (Long) target;
			if (time == 0) {
				return "";
			}
			return new DateTime(time).toString(DateTime.Format.YEAR_TO_DAY);
		}
		return toString();
	}

	public String toJson() {
		return CONVERTOR.toJson(target);
	}

	public String showNs(){
		if(target instanceof Long){
			return DateTime.showNs(((Long) target).longValue());
		}
		return "";
	}

	//返回包括毫秒的日期
	public String toMsTime(){
		if(target instanceof Long){
			String s = new DateTime((Long) target, DateTime.Format.YEAR_TO_MILLISECOND).toString();
			return s;
		}
		return "";
	}

	public String getDateTime() {
		if (target instanceof Date) {
			String s = new DateTime((Date) target, DateTime.Format.YEAR_TO_SECOND).toString();
			return s;
		}
		if (target instanceof Long) {
			Long time = (Long) target;
			if (time == 0) {
				return "";
			}
			return new DateTime(time).toString(DateTime.Format.YEAR_TO_SECOND);
		}
		return toString();
	}

	public String remark(int length) {
		if (target == null) {
			return "";
		}
		String text = target + "";
		if (text.length() <= length || length <= 3) {
			return html(text);
		}
		return html(text.substring(0, length - 3) + "...");
	}

	// 用html格式化
	private String html(String str) {
		return CONVERTOR.htmlEscape(str);
	}

	// 用html格式化
	public String getHtml() {
		if (target == null) {
			return "";
		}
		return CONVERTOR.htmlEscape(target + "");
	}

	// 用于在title里显示,每间隔length插入一个回车
	public String title(int length) {
		StringBuilder sb = new StringBuilder(target.toString());
		for (int i = 1; i < 100; i++) {
			if (sb.length() > i * length) {
				sb.insert(i * length, "\n");
			}
		}
		return sb.toString();
	}

	public String substring(int length) {
		if (target == null) {
			return "";
		}
		String text = target + "";
		if (text.length() <= length) {
			return text;
		}
		return text.substring(0, length) + "...";
	}

	public String yesOrNo() {
		if (StringUtil.isEmpty(target)) {
			return "";
		}
		if (target instanceof Boolean) {
			if ((Boolean) target) {
				return "是";
			}
			return "否";
		}
		if ((target + "").equals("0")) {
			return "否";
		}
		return "是";
	}

	public String toString() {
		if (target == null) {
			return null;
		}
		return target + "";
	}

	// 判断某个值是否是简单对象
	public static boolean isSimple(Object value) {
		if (value instanceof Date) {
			return true;
		}
		String className = value.getClass().getName();
		return className.startsWith("java.lang") || className.indexOf('.') == -1;
	}
}
