package com.github.terefang.jmelange.beans;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Slf4j
public class BeanUtil
{
    @SneakyThrows
    public static <T> T mapToSimpleBean(Map<String, ? extends Object> _res, Class<T> _clazz)
    {
        T _obj = _clazz.newInstance();
        for(String _k : _res.keySet())
        {
            try
            {
                BeanUtils.setProperty(_obj, _k, _res.get(_k));
            }
            catch (InvocationTargetException | IllegalAccessException e)
            {
                log.warn("failed to set property '"+_k+"' -- "+e.getMessage(), e);
            }
        }
        try
        {
            BeanUtils.populate(_obj, _res);
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {
            log.warn("failed to set properties -- "+e.getMessage(), e);
        }
        return _obj;
    }
}
