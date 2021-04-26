package com.github.terefang.jmelange.dao.rsh;

import com.github.terefang.jmelange.dao.JBAO;
import org.apache.commons.dbutils.BeanProcessor;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IBeanProcessor<T> extends BeanProcessor
{
    private Class<T> type;

    public static <T> IBeanProcessor<T> of(Class<T> type)
    {
        IBeanProcessor abp = new IBeanProcessor();
        abp.type = type;
        return abp;
    }

    private IBeanProcessor()
    {
    }

    protected int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props) throws SQLException
    {
        int cols = rsmd.getColumnCount();
        int[] columnToProperty = new int[ cols+1];

        Arrays.fill(columnToProperty, -1);

        List<Field> fieldList = getFieldsListWithAnnotation(this.type, JBAO.IBeanField.class);
        for(int col = 1; col <= cols; ++col)
        {
            String columnName = rsmd.getColumnLabel(col);
            if(null == columnName || 0 == columnName.length())
            {
                columnName = rsmd.getColumnName(col);
            }

            for(Field f : fieldList)
            {
                String fName = f.getAnnotation(JBAO.IBeanField.class).value();
                String pName = f.getName();
                if(fName.equalsIgnoreCase(columnName))
                {
                    for(int i = 0; i < props.length; ++i)
                    {
                        String propName = props[i].getName();
                        if(pName.equalsIgnoreCase(propName))
                        {
                            columnToProperty[col] = i;
                            break;
                        }
                    }
                    break;
                }
            }
        }

        return columnToProperty;
    }

    public static List<Field> getFieldsListWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls)
    {
        List<Field> allFields = getAllFieldsList(cls);
        List<Field> annotatedFields = new ArrayList();
        for(Field field : allFields)
        {
            if (field.getAnnotation(annotationCls) != null) {
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }

    public static List<Field> getAllFieldsList(Class<?> cls)
    {
        List<Field> allFields = new ArrayList();

        for(Class currentClass = cls; currentClass != null; currentClass = currentClass.getSuperclass()) {
            Field[] declaredFields = currentClass.getDeclaredFields();
            for(Field field : declaredFields)
            {
                allFields.add(field);
            }
        }

        return allFields;
    }

    public static Object readField(final Field field, final Object target, final boolean forceAccess) throws IllegalAccessException {
        if (forceAccess && !field.isAccessible())
        {
            field.setAccessible(true);
        }
        return field.get(target);
    }
}
