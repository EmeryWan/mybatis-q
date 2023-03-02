package cn.letout.mybatis.executor.statement;

import cn.letout.mybatis.executor.Executor;
import cn.letout.mybatis.executor.parameter.ParameterHandler;
import cn.letout.mybatis.executor.resultset.ResultSetHandler;
import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.session.ResultHandler;
import cn.letout.mybatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 语句处理器抽象基类
 *
 * 将参数信息、结果信息 进行封装处理
 */
public abstract class BaseStatementHandler implements StatementHandler {

    protected final Configuration configuration;

    protected final Executor executor;

    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;

    protected final ParameterHandler parameterHandler;

    protected final ResultSetHandler resultSetHandler;

    protected final RowBounds rowBounds;

    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        this.boundSql = boundSql;

        this.parameterObject = parameterObject;
        this.parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, rowBounds, resultHandler, boundSql);
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            // 实例化 statement
            statement = instantiateStatement(connection);
            // 参数设置（可以被抽取，提供配置）
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;
        } catch (Exception e) {
            throw new RuntimeException("Error preparing statement. Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;

}
