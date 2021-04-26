package com.github.terefang.jmelange.dao.utils;

import com.github.terefang.jmelange.dao.utils.JdaoUtils;
import lombok.extern.slf4j.Slf4j;

import javax.naming.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Slf4j
public class JndiUtils
{
    public static final String DEFAULT_JNDI_PATH = "java:ds";

    public static Map<String, DataSource> registerJndiDsFromDirectories(List<String> configDirs, String filesuffix, Context env)
    {
        if(filesuffix==null)
        {
            filesuffix = JdaoUtils.PROP_DATASOURCE_CONFIG_SUFFIX;
        }

        Map<String, DataSource> dsources = JdaoUtils.scanRecursiveDatasourceDirectories(configDirs, filesuffix);

        registerJNDI(env, dsources);

        return dsources;
    }

    public static void registerJNDI(Context env, Map<String, DataSource> dsources)
    {
        for(Map.Entry<String,DataSource> entry : dsources.entrySet())
        {
            String dsName = entry.getKey();
            try
            {
                DataSource dataSource = entry.getValue();

                bind(env, dsName, dataSource);

                log.info("registered ds='"+dsName+"'");
            }
            catch(Exception xe)
            {
                log.info("Error processing datasource: "+dsName, xe);
            }
        }
    }

    public static void unregisterJNDI(Context env, Map<String, DataSource> dsources)
    {
        for(Map.Entry<String,DataSource> entry : dsources.entrySet())
        {
            String dsName = entry.getKey();
            try
            {

                unbind(env, dsName);

                log.info("unregistered ds='"+dsName+"'");
            }
            catch(Exception xe)
            {
                log.error("Error unregister datasource: "+dsName, xe);
            }
        }
    }

    public static Context retrieveContext(String jndi_path)
    {
        InitialContext jndiContext = null;
        Context env = null;
        try
        {
            log.info("resolving "+jndi_path);

            env = jndiContext = new InitialContext();
            env = (Context)jndiContext.lookup(jndi_path);
        }
        catch(Exception xe)
        {
            try
            {
                Name jname = jndiContext.getNameParser(jndi_path).parse(jndi_path);
                Enumeration<String> en = jname.getAll();
                while(en.hasMoreElements())
                {
                    String name = en.nextElement();
                    Context tmp = null;
                    try
                    {
                        tmp = (Context)env.lookup(name);
                        env=(Context)env.lookup(name);
                    }
                    catch(NameNotFoundException nnf)
                    {
                        log.info("creating "+name);
                        env = env.createSubcontext(name);
                    }
                }
            }
            catch(Exception xe2)
            {
                log.error("ERROR: resolving "+jndi_path, xe2);
            }
        }
        return env;
    }

    public static Context unbind (Context ctx, String nameStr)
            throws NamingException
    {
        log.info("unbinding "+nameStr);

        Name name = ctx.getNameParser("").parse(nameStr);

        //no name, nothing to do
        if (name.size() == 0)
            return null;

        Context subCtx = ctx;

        for (int i=0; i < name.size() - 1; i++)
        {
            try
            {
                subCtx = (Context)subCtx.lookup (name.get(i));
            }
            catch (NameNotFoundException e)
            {
                log.warn("Subcontext "+name.get(i)+" undefined", e);
                return null;
            }
        }

        subCtx.unbind(name.get(name.size() - 1));
        log.info("unbound object "+nameStr);
        return subCtx;
    }

    public static Context bind (Context ctx, String nameStr, Object obj)
            throws NamingException
    {
        log.info("binding "+nameStr);

        Name name = ctx.getNameParser("").parse(nameStr);

        //no name, nothing to do
        if (name.size() == 0)
            return null;

        Context subCtx = ctx;

        //last component of the name will be the name to bind
        for (int i=0; i < name.size() - 1; i++)
        {
            try
            {
                subCtx = (Context)subCtx.lookup (name.get(i));
                log.info("Subcontext "+name.get(i)+" already exists");
            }
            catch (NameNotFoundException e)
            {
                subCtx = subCtx.createSubcontext(name.get(i));
                log.info("Subcontext "+name.get(i)+" created");
            }
        }

        subCtx.rebind (name.get(name.size() - 1), obj);
        log.info("Bound object to "+name.get(name.size() - 1));
        return subCtx;
    }

    /**
     * Looks for a DataSource in Jndi
     *
     * @param  name of the datasource
     * @return datasource or null
     */
    public static DataSource lookupDataSourceFromJndi(String name)
            throws Exception
    {
        InitialContext ctx = new InitialContext();
        try
        {
            return lookupDataSourceFromJndi(ctx, name);
        }
        finally
        {
            ctx.close();
        }
    }

    public static DataSource lookupDataSourceFromJndi(Context ctx, String name)
            throws Exception
    {
        return(DataSource)ctx.lookup(name);
    }

    public static Connection lookupConnectionFromJndi(Context ctx, String name)
            throws Exception
    {
        return lookupDataSourceFromJndi(ctx, name).getConnection();
    }


}
