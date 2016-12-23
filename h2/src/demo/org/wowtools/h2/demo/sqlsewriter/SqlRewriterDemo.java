package org.wowtools.h2.demo.sqlsewriter;

import org.wowtools.dao.SqlUtil;
import org.wowtools.h2.demo.DemoData;
import org.wowtools.h2.sqlrewriter.SqlRewriter;
import org.wowtools.h2.sqlrewriter.SqlRewriterManager;

/**
 * 重写sql示例，将含有CNAMEANDSNAME的sql重写为select s.name,c.name from student s,clazz c
 * where s.cid = c.cid
 * 
 * @author liuyu
 * @date 2016年12月23日
 */
public class SqlRewriterDemo {

	public static void main(String[] args) {
		DemoData.init();
		SqlRewriter r = new SqlRewriter() {

			@Override
			public String rewrite(String sql) {
				return "select s.name,c.name from student s,clazz c where s.cid = c.cid";
			}

			@Override
			public boolean isConform(String sql) {
				return sql.indexOf("CNAMEANDSNAME") > 0;
			}
		};
		SqlRewriterManager.add(r);

		String sql = "select ooxx from CNAMEANDSNAME where testfun()>0";
		SqlUtil.queryWithJdbc(DemoData.getConnection(), (rs) -> {
			String sname = rs.getString(1);
			String cname = rs.getString(2);
			System.out.println(cname + "的" + sname + "同学");
		}, sql);

		DemoData.shutdown();
	}

}
