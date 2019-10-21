package wang.wangby.utils.validate;

import java.lang.reflect.Field;

import wang.wangby.annotation.Property;
import wang.wangby.exception.Message;
import wang.wangby.utils.StringUtil;

//验证工具类,只实现了非空
public enum ValidateUtil {

	notNull;

	// 通过某个规则验证字段值是否正确
	public static String valiate(String validate, Object value) {
		for (ValidateUtil va : ValidateUtil.values()) {
			if (va.name().equalsIgnoreCase(validate)) {
				String error = va.validate(value);
				if (error != null) {
					return error;
				}
			}
		}
		return null;
	}

	//根据对象配置的property字段验属性值是否有效
	public static void validateBean(Object o) throws Exception {
		for (Field f : o.getClass().getDeclaredFields()) {
			Property p = f.getAnnotation(Property.class);
			if (p == null) {
				continue;
			}
			if (StringUtil.isEmpty(p.validate())) {
				continue;
			}
			f.setAccessible(true);
			validateField(p, f.get(o));
		}
	}

	private static void validateField(Property p, Object value) {
		String[] validate = p.validate().split(",");
		String name = p.value();
		for (String s : validate) {
			s=s.trim();
			if(StringUtil.isEmpty(s)) {
				continue;
			}
			String error = ValidateUtil.valiate(s, value);
			if (error != null) {
				throw new Message(String.format(error, name));
			}
		}
	}


	public String validate(Object value) {
		switch (this) {
		case notNull:
			if (StringUtil.isEmpty(value)) {
				return "%s不能为空";
			}
			break;
		default:
			break;
		}
		return null;
	}
}
