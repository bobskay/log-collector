package wang.wangby.autoconfigure.dao;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.annotation.persistence.Table;
import wang.wangby.dao.BaseDao;
import wang.wangby.utils.ClassUtil;
import wang.wangby.utils.FileUtil;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.template.TemplateUtil;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MapperCreator {

	private TemplateUtil templateUtil;
	DaoProperties properties;
	private String outpurDir;

	public MapperCreator(TemplateUtil templateUtil, DaoProperties properties) {
		this.templateUtil = templateUtil;
		this.properties = properties;
		outpurDir = properties.getOutPutDir();
		if (StringUtil.isNotEmpty(outpurDir)) {
			try {
				Files.createDirectories(Paths.get(outpurDir));
			} catch (IOException e) {
				log.warn("创建目录失败:"+outpurDir,e);
				outpurDir = null;
			}
		}
	}

	//通过传入的baseDao生成映射
	public String getMapperXml(Class<? extends BaseDao> daoClass) throws Exception {
		Type[] ptClass = daoClass.getGenericInterfaces();
		if (ptClass.length != 1) {
			log.warn("忽略class:"+ daoClass.getName());
			return null;
		}
		ParameterizedType type = (ParameterizedType) ptClass[0];
		Type t = type.getActualTypeArguments()[0];
		Class model = (Class) t;

		Map map = toRootMap(daoClass, model);

		log.info("准备生成mapper.xml:" + daoClass.getName() + "<" + model.getName() + ">");
		Table modelAnn = ClassUtil.getAnnotation(model, Table.class);
		if (modelAnn == null) {
			log.info("未设置Table标签,跳过:" + model);
			return null;
		}
		String xml = templateUtil.parseTemplate(properties.getMapperTemplate(), map);
		if (StringUtil.isNotEmpty(outpurDir)) {
			String name =outpurDir + "/" + model.getSimpleName() + "Mapper.xml";
			FileUtil.createFile(name, xml);
			log.info("生成文件:" + name);
		} else {
			log.debug("生成映射文件\n{}", xml);
		}

		return xml;
	}

	private Map toRootMap(Class daoClass, Class modelClass) {
		Map map = new HashMap();
		map.put("daoClass", daoClass.getName());
		map.put("modelClass", modelClass.getName());
		map.put("help", new MapperCreatHelper(modelClass));
		return map;
	}

	public String getBasePackage() {
		return properties.getDaoBasePackage();
	}
	
	public static MapperCreator getDefault(){
		TemplateUtil templateUtil=TemplateUtil.getDefault();
		DaoProperties daoProperties=new DaoProperties();
		MapperCreator creator=new MapperCreator(templateUtil,daoProperties);
		return creator;
	}

}
