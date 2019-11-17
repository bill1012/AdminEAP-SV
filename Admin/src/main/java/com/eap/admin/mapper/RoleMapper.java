package com.eap.admin.mapper;

import com.eap.admin.entity.Role;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleMapper extends Mapper<Role> {

    String selectMaxLevelCodeByParentId(@Param("parentId") String parentId);

    void deleteRoleUser(@Param("roleId") String roleId, @Param("userId") String userId);

    void insertRoleUser(@Param("roleId") String roleId, @Param("userId") String userId);


    void batchDeleteRoleUser(@Param("roleId") String roleId, @Param("userId") String[] userIds);

    void batchInsertRoleUser(@Param("roleId") String roleId, @Param("userId") String[] userIds);

    void deleteByRoleIdAndResourceType(@Param("roleId") String roleId, @Param("resourceType") String resourceType);

    void deleteByRoleIdAndResourceId(@Param("roleId") String roleId, @Param("resourceId") String resourceId);

    List<String> selectResourceByRoleIdAndType(@Param("roleId") String roleId, @Param("resourceType") String resourceType);

    void insertResourceElement(@Param("roleId") String roleId, @Param("elementId") String elementId,
                               @Param("menuId") String menuId, @Param("resourceType") String resourceType);

    void deleteByRoleIdAndParentId(@Param("roleId") String roleId, @Param("menuId") String menuId);

    void insertResourceElements(@Param("roleId") String roleId, @Param("elementIds") String[] elementIds,
                                @Param("menuId") String menuId, @Param("resourceType") String resourceType);


    void insertResourceMenus(@Param("roleId") String roleId, @Param("menuIds") String[] menuIds,
                               @Param("resourceType") String resourceType);

    List<String> selectRoleCodesByUserId(@Param("userId") String userId);
    List<String> selectRoleIdsByUserId(@Param("userId") String userId);

    int deleteRoleResourceByResourceId(String resourceId);
}