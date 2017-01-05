package org.wowtools.h2.demo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;
import org.wowtools.dao.SqlUtil;

/**
 * 初始化测试数据，以内存模式启动h2，并构建表:
 * 
 * <pre>
 学生表 STUDENT:
+--------+---------+-----------+
| sid(int 学号) 	| name(varchar 姓名) 	| cid(int 班级id) 	|
| 1 			| 小明					| 1					|
| 2 			| 小红					| 1					|
| 3 			| 小白					| 2					|
+--------+---------+-----------+

 班级表 CLAZZ:
+--------+---------+-----------+
| cid(int 班级编号) 	| name(varchar 班级名) 	|
| 1 				| 三年1班					|
| 2 				| 三年2班					|
+--------+---------+-----------+
 * </pre>
 * 
 * @author liuyu
 * @date 2016年12月23日
 */
public class DemoData {
	private static JdbcConnectionPool connectionPool;
	private static Server webServer;
	private static Server tcpServer;
	
	private static void createTestTable(){
		SqlUtil.executeUpdate(getConnection(), "create table student(sid int,name varchar(32),cid int)");
		ArrayList<Object[]> param = new ArrayList<>();
		param.add(new Object[]{1,"小明",1});
		param.add(new Object[]{2,"小红",1});
		param.add(new Object[]{2,"小白",1});
		SqlUtil.batchUpdate(getConnection(), "insert into student values(?,?,?)",param,true);
		SqlUtil.executeUpdate(getConnection(), "create table clazz(cid int,name varchar(32))");
		param = new ArrayList<>();
		param.add(new Object[]{1,"三年1班"});
		param.add(new Object[]{2,"三年2班"});
		SqlUtil.batchUpdate(getConnection(), "insert into clazz values(?,?)",param,true);
		System.out.println("测试数据构建完成");
	}
	
	public static void init(){
		try {
			String dbName = "test";
			String superDbUserName = "sa";
			String superDbUserPwd = "sa";
			connectionPool = JdbcConnectionPool.create("jdbc:h2:mem:" + dbName + ";MVCC=TRUE", superDbUserName,
					superDbUserPwd);// 连接池
			connectionPool.setMaxConnections(100);
			
			// 初始化库
			webServer = Server.createWebServer(new String[] { "-webPort", "" + 7777, "-webAllowOthers" });
			webServer.start();
			// 新建并启动一个tcp Server
			// jdbc:h2:tcp://localhost:7778/mem:test进行jdbc操作
			tcpServer = Server.createTcpServer(new String[] { "-tcpPort", "" + 7778, "-tcpAllowOthers" });
			tcpServer.start();
			System.out.println("h2数据库启动完成");
			createTestTable();
			
		} catch (Exception e) {
			shutdown();
			throw new RuntimeException(e);
		}
	}

	
	public static Connection getConnection() {
		try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void shutdown(){
		if(null!=webServer){
			webServer.stop();
			webServer.shutdown();
		}
		if(null!=tcpServer){
			tcpServer.stop();
			tcpServer.shutdown();
		}
		connectionPool.dispose();
	}
	

}
