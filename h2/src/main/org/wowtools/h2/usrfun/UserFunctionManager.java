package org.wowtools.h2.usrfun;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;

import org.wowtools.dao.SqlUtil;

/**
 * 自定义函数注册器
 * @author liuyu
 * @date 2016年12月23日
 */
public class UserFunctionManager {

	/**
	 * 注册一个类中所有@UserFunction注解的方法为自定义函数
	 * 
	 * @param conn
	 * @param clazz
	 * @return 有多少个方法被注册
	 */
	public static int register(Connection conn, Class<?> clazz) {
		Method[] ms = clazz.getMethods();
		StringBuilder sb = new StringBuilder();
		String classPath = clazz.getName() + ".";
		int n = 0;
		for (Method m : ms) {
			int modifiers = m.getModifiers();
			if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
				continue;
			}
			String mName = m.getName();
			UserFunction uf = m.getAnnotation(UserFunction.class);
			if (null == uf) {
				continue;
			}
			String fName = uf.value();
			if (fName.length() == 0) {
				fName = mName;
			}
			// "CREATE ALIAS TopoQuery FOR
			// \"org.wowtools.geoh2.topo.TopoFunction.TopoQuery\";"
			sb.append("CREATE ALIAS ").append(fName).append(" FOR \"").append(classPath).append(mName).append("\";");
			n++;
		}
		if (n == 0) {
			throw new RuntimeException(classPath + "中没有注解为@UserFunction的public static 方法");
		}
		String sql = sb.toString();
		SqlUtil.executeUpdate(conn, sql);
		return n;
	}

}
