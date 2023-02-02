package cn.letout.mybatis.mapping;

import cn.letout.mybatis.session.Configuration;
import lombok.Getter;
import lombok.Setter;

/**
 * 映射语句类
 * 用于记录 SQL 信息：SQL 类型、SQL 语句、入参类型、出参类型
 */
@Getter
@Setter
public class MappedStatement {

    private Configuration configuration;

    private String id;  // 接口的全限定名 cn.letout.mybatis.dao.IUserDao.queryUserInfoById

    private SqlCommandType sqlCommandType;  // enum -> SELECT

    private SqlSource sqlSource;  // SQL 信息 eg: staticSqlSource

    private Class<?> resultType;  // 返回类型

    // 禁用构造
    MappedStatement() {
    }

    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, SqlSource sqlSource, Class<?> resultType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.sqlSource = sqlSource;
            mappedStatement.resultType = resultType;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public Class<?> getResultType() {
        return resultType;
    }

}
