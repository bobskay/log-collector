package wang.wangby.utils.template;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.test.model.BookInfo;
import wang.wangby.utils.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;

@Slf4j
public class TemplateUtilTest extends TestBase {
	
	TemplateUtil templateUtil;

	@Before
	public void init() {
		VelocityEngine velocityEngine = new VelocityEngine();
		VelocityConfig config=new VelocityConfig();
		Properties p = new Properties();
		p.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName());
		p.setProperty("userdirective", TrimAllDirective.class.getName());
		p.setProperty("input.encoding", config.getEncoding());
		p.setProperty("output.encoding", config.getEncoding());
		p.setProperty("runtime.log", config.getLog());
		velocityEngine.init(p);
		String root="/templates";
		templateUtil=new TemplateUtil(velocityEngine,root);
	}
	
	//以字符串为模板
	@Test
	public void testParseText() {
		String text="hello ${velocity}";
		Map map=new HashMap();
		map.put("velocity", "world");
		
		String actual=templateUtil.parseText(text, map);
		log.debug("解析文本结果{}",actual);
		Assert.assertThat(actual, equalTo("hello world"));
	}

	//以文件为模板
	@Test
	public void testParseTemplate() {
		Map map=new HashMap();
		map.put("velocity", "world");
		
		String template="hello.vm";
		String result=templateUtil.parseTemplate(template, map);
		
		String expect="hello world";
		Assert.assertThat(result, equalTo(expect));
	}
	
	
	//测试trimAll标签
	@Test
	public void testTrim() {
		String template="testTrim.vm";
		String result=templateUtil.parseTemplate(template, null);
		String expected="将 所有 回车和空格 都替换成1个";
		Assert.assertThat(result, equalTo(expected));
	}
	
	//测试给简单对象自定义的方法
	@Test
	public void testSimpleObject() {
		String template="testSimple.vm";
		Map map=new HashMap();
		
		BookInfo book=new BookInfo();
		book.setBookId(1L);
		book.setBookName("这是一个很长的字符串");
		book.setPrice(10);
		book.setPublication(new DateTime("2018-01-31").toDate());
		book.setCreateTime(new DateTime("2018-01-31 12:13:14").toDate());
		book.setValid(false);
		map.put("book", book);
		
		String real=templateUtil.parseTemplate(template, map);
		log.debug(real);
	}
	
	
}
