package wang.wangby.model.dao;

import wang.wangby.annotation.persistence.Id;
import wang.wangby.model.Setter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class BaseModel implements Serializable,Cloneable {

	//传自定义属性的时候用,例如mybatis查询
	private transient Map<String,Object> ext=new HashMap();

	public Map getExt() {
		return ext;
	}

	public Long id(){
		return GetMethodHolder.get(this.getClass()).apply(this);
	}

	public void id(Long id){
		SetMethodHolder.get(this.getClass()).set(this,id);
	}

	public static class GetMethodHolder{
		private static Map<Class,Function<BaseModel,Long>> functionMap=new ConcurrentHashMap<>();

		public static Function<BaseModel,Long> get(Class<? extends BaseModel> clazz){
			Function<BaseModel,Long> function=functionMap.get(clazz);
			if(function!=null){
				return function;
			}
			synchronized (clazz){
				if(function!=null){
					return function;
				}
			}

			for(Field f:clazz.getDeclaredFields()){
				Id id=f.getAnnotation(Id.class);
				if(id!=null){
					f.setAccessible(true);
					function= model->getId(f,model);
					functionMap.put(clazz,function);
					return function;
				}
			}
			throw new RuntimeException(clazz.getName()+"未设置主键");
		}

		public static Long getId(Field field,Object model){
			try {
				return (Long) field.get(model);
			} catch (IllegalAccessException e) {
				return null;
			}
		}
	}


	public static class SetMethodHolder{
		private static Map<Class,Setter<BaseModel,Long>> settMapper=new ConcurrentHashMap<>();

		public static Setter<BaseModel,Long> get(Class<? extends BaseModel> clazz){
			Setter<BaseModel,Long> function=settMapper.get(clazz);
			if(function!=null){
				return function;
			}
			synchronized (clazz){
				if(function!=null){
					return function;
				}
			}

			for(Field f:clazz.getDeclaredFields()){
				Id id=f.getAnnotation(Id.class);
				if(id!=null){
					f.setAccessible(true);
					function= (model,idValue)->setId(f,model,idValue);
					settMapper.put(clazz,function);
					return function;
				}
			}
			throw new RuntimeException(clazz.getName()+"未设置主键");
		}

		public static void setId(Field field,Object model,Long value){
			try {
				 field.set(model,value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
