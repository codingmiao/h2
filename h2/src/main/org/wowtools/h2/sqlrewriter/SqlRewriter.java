package org.wowtools.h2.sqlrewriter;
/**
 * sql重写器
 * @author liuyu
 * @date 2016年12月22日
 */
public interface SqlRewriter {
	/**
	 * sql是否满足重写条件
	 * @param sql
	 * @return
	 */
	public boolean isConform(String sql);
	
	/**
	 * 重写sql,重写后的表名、字段、where条件等均可随意修改，但必须符合sql的语法
	 * @param sql
	 * @return
	 */
	public String rewrite(String sql);
}
