package com.joolun.common.sql;

import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.github.pagehelper.parser.defaults.DefaultCountSqlParser;
import net.sf.jsqlparser.statement.select.Select;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SQL 解析器兼容性护栏。
 *
 * <p>PageHelper 6.1.x 是按 JSqlParser 4.x 的 API 编译的：它调用
 * {@code Select.getASTNode()} 并把返回值当成 {@code SimpleNode} 使用。</p>
 *
 * <p>JSqlParser 5.4 把该方法返回类型改成了 {@code Node}，编译期完全看不出来，
 * 只有真正执行分页查询时才会抛 {@code NoSuchMethodError}。
 * 这组测试把“编译期不可见、运行期才暴露”的约束固化成断言，
 * 依赖一升级就能在 {@code mvn test} 阶段直接失败。</p>
 *
 * @author www.joolun.com
 */
class SqlParserCompatibilityTest {

	private static final String SQL = "select u.user_id, u.user_name from sys_user u "
			+ "left join sys_dept d on u.dept_id = d.dept_id "
			+ "where u.del_flag = '0' order by u.create_time desc";

	/**
	 * PageHelper 依赖的 AST 方法签名必须是 4.x 形态。
	 * 升级到 JSqlParser 5.x 时这里会先失败，避免运行期才暴露。
	 */
	@Test
	void jsqlparserKeepsSimpleNodeAstSignature() throws NoSuchMethodException {
		String returnType = Select.class.getMethod("getASTNode").getReturnType().getName();
		assertEquals("net.sf.jsqlparser.parser.SimpleNode", returnType,
				"JSqlParser 版本与 PageHelper 6.1.x 不兼容，请使用 mybatis-plus-jsqlparser-4.9");
	}

	/**
	 * PageHelper 能把普通查询改写成 count 查询。
	 */
	@Test
	void pageHelperCanBuildCountSql() {
		String countSql = assertDoesNotThrow(() -> new DefaultCountSqlParser().getSmartCountSql(SQL));
		assertTrue(countSql.toLowerCase().contains("count("), "count SQL 生成结果异常：" + countSql);
	}

	/**
	 * MyBatis-Plus 自己的解析入口也要能正常工作。
	 */
	@Test
	void mybatisPlusCanParseSql() {
		assertNotNull(assertDoesNotThrow(() -> JsqlParserGlobal.parse(SQL)));
	}
}
