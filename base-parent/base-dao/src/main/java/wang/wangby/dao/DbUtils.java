package wang.wangby.dao;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import wang.wangby.dao.model.DatabaseInfo;
import wang.wangby.dao.model.MetaInfo;
import wang.wangby.utils.BeanUtil;
import wang.wangby.utils.LogUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DbUtils {
	private Map<String, DataSource> datasourceMap = new ConcurrentHashMap();
	
	private DataSource ds;

	public DbUtils(DataSource ds) {
		this.ds = ds;
	}

	public DbUtils() {
	}
	
	@SneakyThrows
	public <T> List<T> select(String sql, Class<T> clazz) {
		log.debug("准备执行sql:"+sql);
		@Cleanup
		Connection conn = ds.getConnection();
		@Cleanup
		Statement st = conn.createStatement();
		@Cleanup
		ResultSet rs = st.executeQuery(sql);
		return toList(rs, clazz);

	}

	// 将ResultSet转为list
	private <T> List<T> toList(ResultSet rs, Class<T> clazz) throws Exception {
		List list = new ArrayList();
		List<MetaInfo> columns = null;
		while (rs.next()) {
			if (columns == null) {
				columns = getMapping(rs);
			}
			Object o = clazz.newInstance();
			for (int i = 0; i < columns.size(); i++) {
				Object value = columns.get(i).getValue(rs, i+1);
				if(!BeanUtil.set(o, columns.get(i).getName(), value)) {
					log.info("跳过字段:"+columns.get(i).getName()+":"+o.getClass().getName());
				}
			}
			list.add(o);
		}
		return list;
	}

	// 通过ResultSet的metadate获得对应的java映射
	private List<MetaInfo> getMapping(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		List list = new ArrayList();
		for (int i = 0; i < meta.getColumnCount(); i++) {
			MetaInfo info = new MetaInfo();
			info.setType(meta.getColumnType(i+1));
			info.setName(meta.getColumnLabel(i+1));
			list.add(info);
		}
		return list;
	}

	

	@SneakyThrows
	public <T> List<T> select(DatabaseInfo info, String sql, Class<T> clazz) {
		DataSource ds = getDatasource(info);

		DbUtils util = new DbUtils(ds);
		List list = util.select(sql, clazz);
		datasourceMap.put(info.getKey(), ds);
		return list;
	}

	//获得数据源
	private DataSource getDatasource(DatabaseInfo info) throws SQLException, Exception {
		DataSource ds = datasourceMap.get(info.getKey());
		if (ds == null) {
			log.info("新建连接池:"+info.getKey());
			@SuppressWarnings("resource")//数据源不能close,如果close了getConnection就出错
			DruidDataSource dd = new DruidDataSource();
			dd.setUsername(info.getUsername());
			dd.setPassword(info.getPassword());
			dd.setDriverClassName("com.mysql.jdbc.Driver");
			dd.setUrl(info.getUrl());
			dd.setMaxActive(3);
			dd.setMaxWait(2000);//默认为-1,会一直尝试
			try {
				dd.getConnection();
			}catch(Exception ex) {
				dd.close();
				log.error(ex.getMessage(),ex);
				throw new RuntimeException("获取数据库链接失败:"+LogUtil.getCause(ex).getMessage());
			}
			ds = dd;
		}
		return ds;
	}
	

}
