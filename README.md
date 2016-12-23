# wowtools h2
魔改版h2,主要针对sql的自定义重写。
原h2项目地址[https://github.com/h2database/h2database][1]

扩展功能：
=====
 - sql重写

有时候，我们需要利用某些软件查询h2中的数据，但直接去修改软件源码工作量较大(例如在geoserver中的一个图层，我们需要按照自定义的数据来出图)。
此时，可利用sql重写功能，重写sql后查询我们想要的数据，例如：

有数据表如下：

>  学生表 STUDENT:
+--------+---------+-----------+
| sid(int 学号) 	| name(varchar 姓名) 	| cid(int 班级id) 	|
| 1 			| 小明					| 1					|
| 2 			| 小红					| 1					|
| 3 			| 小白					| 2					|
+--------+---------+-----------+
+
 班级表 CLAZZ:
+--------+---------+-----------+
| cid(int 班级编号) 	| name(varchar 班级名) 	|
| 1 				| 三年1班					|
| 2 				| 三年2班					|
+--------+---------+-----------+

我们可以把sql拦截一下，替换成连接查询而直接获得学生所属班级：

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

然后，当我们输入sql比如select ooxx from CNAMEANDSNAME时，就会被替换成代码中的连接查询语句啦~~

这个例子看起来好像没什么卵用？我们再来看h2自带的一个功能：

> **Using a Function as a Table**
A function that returns a result set can be used like a table. However, in this case the function is called at least twice: first while parsing the statement to collect the column names (with parameters set to null where not known at compile time). And then, while executing the statement to get the data (maybe multiple times if this is a join). If the function is called just to get the column list, the URL of the connection passed to the function is jdbc:columnlist:connection. Otherwise, the URL of the connection is jdbc:default:connection. 

    public static ResultSet getMatrix(Connection conn, Integer size) throws SQLException {
		SimpleResultSet rs = new SimpleResultSet();
		rs.addColumn("X", Types.INTEGER, 10, 0);
		rs.addColumn("Y", Types.INTEGER, 10, 0);
		String url = conn.getMetaData().getURL();
		if (url.equals("jdbc:columnlist:connection")) {
			return rs;
		}
		for (int s = size.intValue(), x = 0; x < s; x++) {
			for (int y = 0; y < s; y++) {
				rs.addRow(x, y);
			}
		}
		return rs;
	}
		CREATE ALIAS MATRIX FOR"org.h2.samples.Function.getMatrix";
		SELECT * FROM MATRIX(4) ORDER BY X, Y;

直接吧java代码写在自定义函数里，然后通过sql重写来调用自定义函数，就能返回任意格式、任意来源的数据啦~~这样一看是不是很管用了呢^_^
 

  [1]: https://github.com/h2database/h2database