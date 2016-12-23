package org.wowtools.h2.sqlrewriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlRewriterManager {
	private static SqlRewriter[] arr = new SqlRewriter[0];
	private static final Logger logger = LoggerFactory.getLogger(SqlRewriterManager.class);
	/**
	 * 添加一个重写器
	 * @param r
	 */
	public synchronized static void add(SqlRewriter r){
		SqlRewriter[] newArr = new SqlRewriter[arr.length+1];
		int i = 0;
		for(SqlRewriter a:arr){
			newArr[i] = a;
			i++;
		}
		newArr[arr.length] = r;
		arr = newArr;
	}
	
	/**
	 * 重写sql
	 * @param sql
	 * @return
	 */
	public static String rewrite(String sql){
		for(SqlRewriter a:arr){
			if(a.isConform(sql)){
				String newSql = a.rewrite(sql);
				logger.debug("rewrite sql,old:{},new:{}",sql,newSql);
				return newSql;
			}
		}
		return sql;
	}
	
}
