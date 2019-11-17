package com.eap.common.util;

import com.eap.common.constant.BaseConstants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;


/**
 * 实体类相关工具类
 * 解决问题： 1、快速对实体的常驻字段，如：crtUser、crtHost、updUser等值快速注入
 */
public class EntityUtil {
    /**
     * 快速将bean的crtUser、crtHost、crtTime、updUser、updHost、updTime附上相关值
     *
     * @param entity 实体bean
     */
    public static <T> void setCreatAndUpdatInfo(T entity) {
        setCreateInfo(entity);
        setUpdatedInfo(entity);
    }

    /**
     * 快速将bean的crtUser、crtHost、crtTime附上相关值
     *
     * @param entity 实体bean
     */
    public static <T> void setCreateInfo(T entity) {
        // 默认属性
        String[] fields = {"createName", "createUser", "createHost", "createTime"};
        Field field = ReflectionUtil.getAccessibleField(entity, "createTime");
        // 默认值
        Object[] value = null;
        if (field != null && field.getType().equals(Date.class)) {
            value = getBaseInfo();
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
    }


    public static Object[] getBaseInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String hostIp = "";
        String name = "";
        String id = "";
        if (request != null) {
            hostIp = String.valueOf(request.getSession().getAttribute(BaseConstants.REQUEST_HEADER_HOST));
            name = String.valueOf(request.getSession().getAttribute(BaseConstants.REQUEST_HEADER_UNAME));
            id = String.valueOf(request.getSession().getAttribute(BaseConstants.REQUEST_HEADER_UID));
        }
        return new Object[]{name, id, hostIp, new Date()};
    }

    /**
     * 快速将bean的updUser、updHost、updTime附上相关值
     *
     * @param entity 实体bean
     */
    public static <T> void setUpdatedInfo(T entity) {

        // 默认属性
        String[] fields = {"updateName", "updateUser", "updateHost", "updateTime"};
        Field field = ReflectionUtil.getAccessibleField(entity, "updateTime");
        Object[] value = null;
        if (field != null && field.getType().equals(Date.class)) {
            value = getBaseInfo();
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
    }

    /**
     * 依据对象的属性数组和值数组对对象的属性进行赋值
     *
     * @param entity 对象
     * @param fields 属性数组
     * @param value  值数组
     */
    private static <T> void setDefaultValues(T entity, String[] fields, Object[] value) {
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if (ReflectionUtil.hasField(entity, field)) {
                ReflectionUtil.invokeSetter(entity, field, value[i]);
            }
        }
    }

    /**
     * 根据主键属性，判断主键是否值为空
     *
     * @param entity
     * @param field
     * @return 主键为空，则返回false；主键有值，返回true
     */
    public static <T> boolean isPKNotNull(T entity, String field) {
        if (!ReflectionUtil.hasField(entity, field))
            return false;
        Object value = ReflectionUtil.getFieldValue(entity, field);
        return value != null && !"".equals(value);
    }
}
