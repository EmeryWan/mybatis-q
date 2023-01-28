package cn.letout.mybatis.builder.xml;

import cn.letout.mybatis.builder.BaseBuilder;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.mapping.SqlCommandType;
import cn.letout.mybatis.mapping.SqlSource;
import cn.letout.mybatis.scripting.LanguageDriver;
import cn.letout.mybatis.session.Configuration;
import org.dom4j.Element;

import java.util.Locale;

/**
 * XML 语句构建器
 */
public class XMLStatementBuilder extends BaseBuilder {

    private String currentNamespace;

    private Element element;

    public XMLStatementBuilder(Configuration configuration, Element element, String currentNamespace) {
        super(configuration);
        this.element = element;
        this.currentNamespace = currentNamespace;
    }

    // 解析语句(select|insert|update|delete)
    // <select
    //   id="selectPerson"
    //   parameterType="int"
    //   parameterMap="deprecated"
    //   resultType="hashmap"
    //   resultMap="personResultMap"
    //   flushCache="false"
    //   useCache="true"
    //   timeout="10000"
    //   fetchSize="256"
    //   statementType="PREPARED"
    //   resultSetType="FORWARD_ONLY"
    // >
    //   SELECT * FROM PERSON WHERE ID = #{id}
    //</select>
    public void parseStatementNode() {
        String id = element.attributeValue("id");

        // 参数类型
        String parameterType = element.attributeValue("parameterType");
        Class<?> parameterTypeClass = resolveAlias(parameterType);

        // 结果类型
        String resultType = element.attributeValue("resultType");
        Class<?> resultTypeClass = resolveAlias(resultType);

        // 命令类型 select / insert / update / delete
        String nodeName = element.getName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

        // 获取默认语言驱动器
        Class<?> langClass = configuration.getLanguageRegistry().getDefaultDriverClass();
        LanguageDriver langDriver = configuration.getLanguageRegisty().getDriver(langClass);

        SqlSource sqlSource = langDriver.createSqlSource(configuration, element, parameterTypeClass);

        MappedStatement mappedStatement = new MappedStatement.Builder(
                configuration,
                currentNamespace + "." + id,
                sqlCommandType,
                sqlSource,
                resultTypeClass
        ).build();

        // 添加解析 SQL
        configuration.addMappedStatement(mappedStatement);
    }

}
