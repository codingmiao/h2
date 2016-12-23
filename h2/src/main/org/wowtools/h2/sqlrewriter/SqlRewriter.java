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
	 * 重写sql
	 * @param sql
	 * @return
	 */
	public String rewrite(String sql);
}
