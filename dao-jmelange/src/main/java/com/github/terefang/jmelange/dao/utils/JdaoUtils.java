package com.github.terefang.jmelange.dao.utils;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.dao.JDAO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.codehaus.plexus.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import static com.github.terefang.jmelange.dao.JDAO.DB_TYPE_POSTGRES;
import static com.github.terefang.jmelange.dao.JDAO.DB_TYPE_ORACLE;
import static com.github.terefang.jmelange.dao.JDAO.DB_TYPE_MSSQL;
import static com.github.terefang.jmelange.dao.JDAO.DB_TYPE_ANSI;
import static com.github.terefang.jmelange.dao.JDAO.DB_TYPE_MYSQL;
import static com.github.terefang.jmelange.dao.JDAO.DB_TYPE_SYBASE;
import static com.github.terefang.jmelange.dao.JDAO.DB_TYPE_DB2;
import static com.github.terefang.jmelange.dao.JDAO.DB_TYPE_H2;
import static com.github.terefang.jmelange.dao.JDAO.DB_TYPE_SQLITE;
import static com.github.terefang.jmelange.dao.JDAO.DB_TYPE_CRATE;

@Slf4j
public class JdaoUtils
{
    public static final String PROP_DATASOURCE_CONFIG_SUFFIX = ".ds.properties";

    public static Map<String, DataSource> scanRecursiveDatasourceDirectories(List<String> configDirs, String suffix)
    {
        Map<String, DataSource> ret = new HashMap();
        try
        {
            for(String scanDirs : configDirs)
            {
                for(String scanDir : scanDirs.split(";"))
                {
                    log.info("scanning: "+scanDir);
                    try
                    {
                        processDatasourceDirectory(scanDir.trim(), suffix, ret);
                    }
                    catch(Exception xe)
                    {
                        log.error("scanning: "+scanDir, xe);
                    }
                }
            }
        }
        catch(Exception xe)
        {
            log.error("scanning ds dirs", xe);
        }
        return ret;
    }


    public static void processDatasourceDirectory(String scanDir, final String suffix, Map<String, DataSource> reg)
    {
        try
        {
            File workDir = new File(scanDir);

            if(workDir.isDirectory())
            {
                File[] files = workDir.listFiles(new FileFilter()
                {
                    @Override
                    public boolean accept(File pathname)
                    {
                        return pathname.isFile() &&
                                pathname.getName().endsWith(suffix);
                    }
                });

                for(File file : files)
                {
                    String dsName = file.getName();
                    dsName = dsName.substring(0,dsName.length()-suffix.length());
                    DataSource ds = createDatasourceFromFile(file);
                    reg.put(dsName, ds);
                }
            }
        }
        catch(Exception xe)
        {
            log.warn("Error processing directory: "+scanDir, xe);
        }
    }

    public static DataSource createDatasourceFromFile(File file)
    {
        try
        {
            Properties properties = new Properties();
            FileReader fh = new FileReader(file);
            properties.load(fh);
            fh.close();

            DataSource dataSource = null;
            dataSource = createDataSourceByProperties(file, dataSource, properties);

            return dataSource;
        }
        catch(Exception xe)
        {
            log.warn("Error processing datasource: "+file, xe);
        }
        return null;
    }

    public static DataSource createDataSourceByProperties(File file, DataSource dataSource, Properties properties)
    {
        return createDataSourceByProperties(file.toString(), dataSource, properties);
    }

    public static void adjustPropertiesForEnvParameters(Properties properties)
    {
        for(String key : properties.stringPropertyNames())
        {
            String property = properties.getProperty(key);
            int ofs = 0;
            while((ofs = property.indexOf("$(", ofs)) > 0)
            {
                int efs = property.indexOf(')', ofs);
                String lookupKey = property.substring(ofs+2, efs);
                String lookupProp = System.getProperty(lookupKey,"$("+lookupKey+")");
                property = property.substring(0,ofs)+lookupProp+property.substring(efs+1);
                ofs++;
                properties.setProperty(key, property);
            }
        }
    }

    public static DataSource createDataSourceByProperties(String file, DataSource dataSource, Properties properties)
    {
        try
        {
            adjustPropertiesForEnvParameters(properties);
            if(dataSource==null)
            {
                if(properties.containsKey("jdaoDriverClassName"))
                {
                    Class.forName(properties.getProperty("jdaoDriverClassName"));
                }
                if(properties.containsKey("jdaoDataSourceClassName"))
                {
                    dataSource = (DataSource)Thread.currentThread().getContextClassLoader().loadClass(properties.getProperty("jdaoDataSourceClassName")).newInstance();
                }
                else
                {
                    return (BasicDataSource) BasicDataSourceFactory.createDataSource(properties);
                }
            }
            BeanUtils.populate(dataSource, (Map)properties);
        }
        catch(Exception xe)
        {
            log.warn("Error processing datasource: "+file, xe);
            return null;
        }
        return dataSource;
    }

    public static DataSource createDataSourceByProperties(Class clazz, Properties properties)
    {
        try
        {
            adjustPropertiesForEnvParameters(properties);
            DataSource dataSource = null;
            dataSource = (DataSource)clazz.newInstance();
            BeanUtils.populate(dataSource, (Map)properties);
            return dataSource;
        }
        catch(Exception xe)
        {
            log.warn("Error processing datasource class: "+clazz.getCanonicalName(), xe);
            return null;
        }
    }

    public static DataSource createDataSourceByProperties(String clazz, Properties properties)
    {
        try
        {
            return createDataSourceByProperties(Class.forName(clazz, true, Thread.currentThread().getContextClassLoader()), properties);
        }
        catch(Exception xe)
        {
            log.warn("Error processing datasource class: "+clazz, xe);
            return null;
        }
    }

    public static DataSource createDataSource(String _driver, String _url, String _u, String _p)
    {
        Properties properties = new Properties();
        properties.setProperty("username", _u);
        properties.setProperty("password", _p);
        properties.setProperty("url", _url);
        properties.setProperty("driverClassName", _driver);
        return createDataSourceByProperties(properties);
    }

    public static DataSource createDataSourceByProperties(Properties properties)
    {
        try
        {
            adjustPropertiesForEnvParameters(properties);
            return (BasicDataSource) BasicDataSourceFactory.createDataSource(properties);
        }
        catch(Exception xe)
        {
            log.warn("Error processing datasource ", xe);
            return null;
        }
    }

    public static Connection createConnectionByDriverSpec(String driverclazz, String jdbcUri, String userName, String password)
    {
        try
        {
            if(CommonUtil.isNotBlank(driverclazz))
            {
                Class.forName(driverclazz, true, Thread.currentThread().getContextClassLoader());
            }
            return DriverManager.getConnection(jdbcUri, userName, password);
        }
        catch(Exception xe)
        {
            log.warn("Error : ", xe);
            return null;
        }
    }

    public static Connection createConnectionByDataSourceSpec(String dsclazz, String jdbcUri, String userName, String password)
    {
        try
        {
            if(CommonUtil.isNotBlank(dsclazz))
            {
                DataSource ds = (DataSource)Class.forName(dsclazz, true, Thread.currentThread().getContextClassLoader()).newInstance();
                BeanUtils.setProperty(ds, "url", jdbcUri);
                return ds.getConnection(userName, password);
            }
            return DriverManager.getConnection(jdbcUri, userName, password);
        }
        catch(Exception xe)
        {
            log.warn("Error : ", xe);
            return null;
        }
    }

    public static Map<String,Object> filterFields(Map<String,Object> row, List<String> fieldList) throws Exception
    {
        LinkedHashMap<String,Object> returnRow = new LinkedHashMap();
        for(String key : fieldList)
        {
            if(row.containsKey(key))
            {
                returnRow.put(key, row.get(key));
            }
        }
        return returnRow;
    }

    /**
     * create a set statement-fragment and parameter-list from a column-map.
     *
     */
    public static String buildSet(int dbType, List parm, Map vm)
    {
        StringBuilder qq=new StringBuilder();
        boolean op = true;
        for(Object kv : vm.entrySet())
        {
            String k = ((Map.Entry)kv).getKey().toString();
            Object v = ((Map.Entry)kv).getValue();
            if(op == true)
            {
                qq.append(k+"=?");
                op = false;
            }
            else
            {
                qq.append(", "+k+"=?");
            }
            parm.add(v);
        }
        return(qq.toString());
    }


    public static final int TEMPLATE_TYPE_AUTO = 0;
    public static final int TEMPLATE_TYPE_EQUAL = 1;
    public static final int TEMPLATE_TYPE_NOT_EQUAL = 2;
    public static final int TEMPLATE_TYPE_SUBSTRING = 3;
    public static final int TEMPLATE_TYPE_STARTSWITH = 4;
    public static final int TEMPLATE_TYPE_LIKE = 5;
    public static final int TEMPLATE_TYPE_REGEX = 6;

    public static final int CONSTRAINT_ANY_OF = 0;
    public static final int CONSTRAINT_ALL_OF = 1;

    /**
     * create a where statement-fragment and parameter-list from a column-map and constraint-type based on LIKE.
     *
     */
    public static String buildWhereLike(int dbType, int constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append(" ( ");
        for(String k : vm.keySet())
        {
            String v = vm.get(k).toString();
            if(v!="" && v!="*" && v!="%")
            {
                if(first)
                {
                    sb.append(" ("+likeOpPerDbType(dbType, k, "?", false)+")");
                }
                else if(constraintType==CONSTRAINT_ALL_OF)
                {
                    sb.append(" AND ("+likeOpPerDbType(dbType, k, "?", false)+")");
                }
                else if(constraintType==CONSTRAINT_ANY_OF)
                {
                    sb.append(" OR ("+likeOpPerDbType(dbType, k, "?", false)+")");
                }
                else
                {
                    sb.append(" OR ("+likeOpPerDbType(dbType, k, "?", false)+")");
                }
                param.add(v);
                first=false;
            }
        }
        sb.append(" ) ");

        return(sb.toString());
    }

    public static String likeOpPerDbType(int dbType, String arg1, String arg2, boolean invert)
    {
        switch(dbType)
        {
            case DB_TYPE_POSTGRES:
            {
                return arg1+(invert?" NOT":"")+" ILIKE "+arg2;
            }
            case DB_TYPE_ORACLE:
            case DB_TYPE_MSSQL:
            {
                return "LOWER("+arg1+")"+(invert?" NOT":"")+" LIKE "+arg2;
            }
            case DB_TYPE_ANSI:
            case DB_TYPE_MYSQL:
            case DB_TYPE_SYBASE:
            case DB_TYPE_DB2:
            case DB_TYPE_H2:
            case DB_TYPE_SQLITE:
            case DB_TYPE_CRATE:
            default:
            {
                return arg1+(invert?" NOT":"")+" LIKE "+arg2;
            }
        }
    }

    public static String existsOpPerDbType(int dbType, String arg1, boolean invert)
    {
        switch(dbType)
        {
            case DB_TYPE_POSTGRES:
            case DB_TYPE_ORACLE:
            case DB_TYPE_MSSQL:
            case DB_TYPE_ANSI:
            case DB_TYPE_MYSQL:
            case DB_TYPE_SYBASE:
            case DB_TYPE_DB2:
            case DB_TYPE_H2:
            case DB_TYPE_SQLITE:
            case DB_TYPE_CRATE:
            default:
            {
                return (invert?" NOT":" ")+"(("+arg1+" IS NOT NULL) AND ("+arg1+" !='')) ";
            }
        }
    }

    public static String regexpOpPerDbType(int dbType, String arg1, String arg2, boolean invert)
    {
        switch(dbType)
        {
            case DB_TYPE_CRATE:
            {
                return arg1+(invert?" !":" ")+"~ "+arg2;
            }
            case DB_TYPE_POSTGRES:
            {
                return arg1+(invert?" !":" ")+"~* "+arg2;
            }
            case DB_TYPE_ORACLE:
            {
                return (invert?"NOT ":"")+"REGEXP_LIKE("+arg1+", "+arg2+", 'i')";
            }
            case DB_TYPE_MYSQL:
            {
                return arg1+(invert?" NOT":"")+" RLIKE "+arg2;
            }
            case DB_TYPE_MSSQL:
            case DB_TYPE_SYBASE:
            case DB_TYPE_DB2:
            case DB_TYPE_H2:
            case DB_TYPE_SQLITE:
            case DB_TYPE_ANSI:
            default:
            {
                return arg1+(invert?" NOT":"")+" REGEXP "+arg2;
            }
        }
    }
    /**
     * create a where statement-fragment and parameter-list from a column-map and constraint-type based on REGEXP.
     *
     */
    public static String buildWhereRegexp(int dbType, int constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        sb.append(" ( ");
        for(String k : vm.keySet())
        {
            String v = vm.get(k).toString();
            if(v!="" && v!="*" && v!=".*")
            {
                if(first)
                {
                    sb.append(" ("+regexpOpPerDbType(dbType, k, "?", false)+")");
                }
                else if(constraintType==CONSTRAINT_ALL_OF)
                {
                    sb.append(" AND ("+regexpOpPerDbType(dbType, k, "?", false)+")");
                }
                else if(constraintType==CONSTRAINT_ANY_OF)
                {
                    sb.append(" OR ("+regexpOpPerDbType(dbType, k, "?", false)+")");
                }
                else
                {
                    sb.append(" OR ("+regexpOpPerDbType(dbType, k, "?", false)+")");
                }
                param.add(v);
                first=false;
            }
        }
        sb.append(" ) ");

        return(sb.toString());
    }


    /**
     * create a where statement-fragment and parameter-list from a column-map, template-type and constraint-type.
     *
     */
    public static String buildWhere(int dbType, int templateType, List param, Map<String,Object> template)
    {
        return buildWhere(dbType, templateType, CONSTRAINT_ANY_OF, param, template);
    }

    public static String buildWhere(int dbType, int templateType, int constraintType, List param, Map<String,Object> template)
    {
        switch(templateType)
        {
            case TEMPLATE_TYPE_AUTO:
                return buildWhereAuto(dbType, constraintType, param, template);
            case TEMPLATE_TYPE_EQUAL:
                return buildWhereEqual(dbType, constraintType, param, template);
            case TEMPLATE_TYPE_NOT_EQUAL:
                return buildWhereNotEqual(dbType, constraintType, param, template);
            case TEMPLATE_TYPE_LIKE:
                return buildWhereLike(dbType, constraintType, param, template);
            case TEMPLATE_TYPE_REGEX:
                return buildWhereRegexp(dbType, constraintType, param, template);
            case TEMPLATE_TYPE_STARTSWITH:
                return buildWherePrefix(dbType, constraintType, param, template);
            case TEMPLATE_TYPE_SUBSTRING:
                return buildWhereSubstr(dbType, constraintType, param, template);
            default:
                return buildWhereLike(dbType, constraintType, param, template);
        }
    }


    public static void parseSpec(int dbType, StringBuilder sb, List param, String k, String s)
    {
        if(s==null || s.trim().length()==0)
        {
            sb.append(" TRUE ");
            return;
        }
        boolean invert = false;

        if(s.charAt(0)=='!')
        {
            invert = true;
            s = s.substring(1);
        }

        if(s.charAt(0)=='+' || s.charAt(0)=='-')
        {
            String[] list = s.split("[,;]");

            sb.append(" (");

            boolean first = true;
            for(String item : list)
            {
                if(!first)
                {
                    sb.append(invert ? " AND ": " OR ");
                }

                if(s.charAt(0)=='+')
                {
                    parseSpec_(dbType, sb, param, k, item.substring(1), invert);
                }
                else if(s.charAt(0)=='-')
                {
                    parseSpec_(dbType, sb, param, k, item.substring(1), !invert);
                }
                else
                {
                    parseSpec_(dbType, sb, param, k, item, invert);
                }
                first = false;
            }

            sb.append(")");
        }
        else
        {
            parseSpec_(dbType, sb, param, k, s, invert);
        }
    }

    public static void parseSpec_(int dbType, StringBuilder sb, List param, String k, String s, boolean invert)
    {
        if(s.trim().length()==0)
        {
            return;
        }

        if(s.charAt(0)=='~')
        {
            s=s.substring(1);
            sb.append(" ("+regexpOpPerDbType(dbType, k, "?", invert)+")");
        }
        else if(s.charAt(0)=='^')
        {
            s=s.substring(1);
            sb.append(" ("+likeOpPerDbType(dbType, k, "?", invert)+")");
            param.add(s+'%');
            return;
        }
        else if("*".equals(s) || "%".equals(s))
        {
            sb.append(" ("+existsOpPerDbType(dbType, k, invert)+")");
            return;
        }
        else if(s.indexOf('*')>=0)
        {
            s=s.replace('*', '%');
            sb.append(" ("+likeOpPerDbType(dbType, k, "?", invert)+")");
        }
        else if(s.indexOf('%')>=0)
        {
            sb.append(" ("+likeOpPerDbType(dbType, k, "?", invert)+")");
        }
        else
        {
            if(invert)
            {
                sb.append(" ("+k+"!=?)");
            }
            else
            {
                sb.append(" ("+k+"=?)");
            }
        }
        param.add(s);
    }


    public static String buildWhereAuto(int dbType, List param, String _key, String _value)
    {
        StringBuilder sb = new StringBuilder();

        if(!_value.contentEquals(""))
        {
            parseSpec(dbType, sb, param, _key, _value);
        }
        else
        {
            sb.append(" TRUE ");
        }
        return(sb.toString());
    }

    public static String buildWhereAuto(int dbType, List param, Map<String,Object> vm)
    {
        return buildWhereAuto(dbType, CONSTRAINT_ALL_OF, param, vm);
    }

    public static String buildWhereAuto(int dbType, int constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(String k : vm.keySet())
        {
            Object v = vm.get(k);
            if((v instanceof String) && v.toString()!="")
            {
                if(!first) {
                    if (constraintType == CONSTRAINT_ALL_OF) {
                        sb.append(" AND ");
                    } else {
                        sb.append(" OR ");
                    }
                }
                parseSpec(dbType, sb, param, k, v.toString());
                first = false;
            }
        }
        return(sb.toString());
    }

    public static String buildWhereEqual(int dbType, List param, Map<String,Object> vm)
    {
        return buildWhereEqual(dbType, CONSTRAINT_ALL_OF, param, vm);
    }

    public static String buildWhereEqual(int dbType, int constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(String k : vm.keySet())
        {
            Object v = vm.get(k);
            if(v!=null && !((v instanceof String) && (v.toString().equals(""))))
            {
                if(!first)
                {
                    if(constraintType == CONSTRAINT_ALL_OF)
                    {
                        sb.append(" AND ");
                    }
                    else
                    {
                        sb.append(" OR ");
                    }
                }
                sb.append("("+k+" = ?)");
                param.add(v);
                first = false;
            }
        }
        return(sb.toString());
    }

    public static String buildWhereNotEqual(int dbType, List param, Map<String,Object> vm)
    {
        return buildWhereEqual(dbType, CONSTRAINT_ALL_OF, param, vm);
    }

    public static String buildWhereNotEqual(int dbType, int constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(String k : vm.keySet())
        {
            Object v = vm.get(k);
            if(v!=null && !((v instanceof String) && (v.toString().equals(""))))
            {
                if(!first) {
                    if (constraintType == CONSTRAINT_ALL_OF) {
                        sb.append(" AND ");
                    } else {
                        sb.append(" OR ");
                    }
                }
                sb.append("(" + k + " != ?)");
                param.add(v);
                first = false;
            }
        }
        return(sb.toString());
    }

    public static String buildWhereSubstr(int dbType, List param, Map<String,Object> vm)
    {
        return buildWhereSubstr(dbType, CONSTRAINT_ALL_OF, param, vm);
    }

    public static String buildWhereSubstr(int dbType, int constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(String k : vm.keySet())
        {
            Object v = vm.get(k);
            if(v!=null && !((v instanceof String) && (v.toString().equals(""))))
            {
                if(!first) {
                    if (constraintType == CONSTRAINT_ALL_OF) {
                        sb.append(" AND ");
                    } else {
                        sb.append(" OR ");
                    }
                }
                sb.append("("+likeOpPerDbType(dbType, k, "?", false)+")");

                param.add("%"+v+"%");
                first = false;
            }
        }
        return(sb.toString());
    }

    public static String buildWherePrefix(int dbType, List param, Map<String,Object> vm)
    {
        return buildWherePrefix(dbType, CONSTRAINT_ALL_OF, param, vm);
    }

    public static String buildWherePrefix(int dbType, int constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(String k : vm.keySet())
        {
            Object v = vm.get(k);
            if(v!=null && !((v instanceof String) && (v.toString().equals(""))))
            {
                if(!first) {
                    if (constraintType == CONSTRAINT_ALL_OF) {
                        sb.append(" AND ");
                    } else {
                        sb.append(" OR ");
                    }
                }
                sb.append("("+likeOpPerDbType(dbType, k, "?", false)+")");
                param.add(v+"%");
                first = false;
            }
        }
        return(sb.toString());
    }

    public static String preparseParameters(int dbType, String format, List param, Map vars)
    {
        String prefix = "?{";
        String suffix = "}";
        StringBuilder sb = new StringBuilder();

        int offset = 0;
        int found = -1;
        while((found = format.indexOf(prefix, offset)) >= offset)
        {
            sb.append(format.substring(offset, found));

            if(suffix.length()==0)
            {
                offset = found+prefix.length()+1;
            }
            else
            {
                offset = format.indexOf(suffix, found+prefix.length());
            }

            if(offset > found)
            {
                String tag = format.substring(found+prefix.length(), offset);
                offset += suffix.length();

                sb.append("?");
                if(vars.containsKey(tag))
                {
                    param.add(vars.get(tag));
                }
                else
                if(vars.containsKey(tag.toLowerCase()))
                {
                    param.add(vars.get(tag.toLowerCase()));
                }
                else
                if(vars.containsKey(tag.toUpperCase()))
                {
                    param.add(vars.get(tag.toUpperCase()));
                }
                else
                {
                    param.add("{"+tag.toUpperCase()+"}");
                }
            }
            else
            {
                sb.append(prefix);
                offset = found+prefix.length();
            }
        }
        sb.append(format.substring(offset));

        return sb.toString();
    }


    @SneakyThrows
    public static JDAO daoFromJdbc(String jdbcDriver, String _url, String _user, String _pass)
    {
        return JDAO.createDaoFromConnection(createConnectionByDriverSpec(StringUtils.isNotEmpty(jdbcDriver) ? jdbcDriver : null, _url, _user, _pass), true);
    }

    public static JDAO mysqlDao(boolean mysqlCJvsMariaDb, String _hostPortDb, String _user, String _pass)
    {
        JDAO _dao = daoFromJdbc(mysqlCJvsMariaDb ? "com.mysql.cj.jdbc.Driver" : "org.mariadb.jdbc.Driver", "jdbc:mysql://"+_hostPortDb, _user, _pass);
        _dao.setDbType(JDAO.DB_TYPE_MYSQL);
        return _dao;
    }

    public static JDAO mysqlDao(String _hostPortDb, String _user, String _pass)
    {
        JDAO _dao = daoFromJdbc("com.mysql.jdbc.Driver", "jdbc:mysql://"+_hostPortDb, _user, _pass);
        _dao.setDbType(JDAO.DB_TYPE_MYSQL);
        return _dao;
    }

    public static JDAO sqliteDao(String _hostPortDb, String _user, String _pass)
    {
        JDAO _dao = daoFromJdbc("", "jdbc:sqlite:"+_hostPortDb, _user, _pass);
        _dao.setDbType(JDAO.DB_TYPE_SQLITE);
        return _dao;
    }

    public static JDAO pgsqlDao(String _hostPortDb, String _user, String _pass)
    {
        JDAO _dao = daoFromJdbc("org.postgresql.Driver", "jdbc:postgresql://"+_hostPortDb, _user, _pass);
        _dao.setDbType(JDAO.DB_TYPE_POSTGRES);
        return _dao;
    }

    public static JDAO mariadbDao(String _hostPortDb, String _user, String _pass)
    {
        JDAO _dao = daoFromJdbc("org.mariadb.jdbc.Driver", "jdbc:mariadb://"+_hostPortDb, _user, _pass);
        _dao.setDbType(JDAO.DB_TYPE_MYSQL);
        return _dao;
    }

    public static JDAO mssqlDao(String _hostPortDb, String _user, String _pass)
    {
        JDAO _dao = daoFromJdbc("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://"+_hostPortDb, _user, _pass);
        _dao.setDbType(JDAO.DB_TYPE_MSSQL);
        return _dao;
    }

    public static JDAO jtdsDao(String _hostPortDb, String _user, String _pass)
    {
        JDAO _dao = daoFromJdbc("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:"+_hostPortDb, _user, _pass);
        _dao.setDbType(JDAO.DB_TYPE_SYBASE);
        return _dao;
    }

    public static JDAO xlsxDao(String _filePath)
    {
        JDAO _dao = daoFromJdbc("com.sqlsheet.XlsDriver", "jdbc:xls:file:"+_filePath, "", "");
        _dao.setDbType(DB_TYPE_ANSI);
        return _dao;
    }

}
