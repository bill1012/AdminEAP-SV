package com.eap.admin.mapper;

import com.eap.admin.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UserMapper extends Mapper<User> {

    List<User> selectUsersWithRoleId(@Param("roleId") String roleId, @Param("name") String name);

    List<User> selectUsersWithoutRoleId(@Param("roleId") String roleId, @Param("name") String name);


    void insertOrgUser(Map<String, String> map);

    User selectUserById(Object id);

    void deleteRlOrgByUid(String id);

    List<User> getUsersByProjectId(String projectId);

    List<User> selectByProId(@Param("projectId") String projectId,@Param("name")String name);


    List<User> selectUserNoPro(@Param("projectId") String projectId,@Param("name")String name);

    User selectUserByUProId(@Param("projectId") String projectId,@Param("userId")String userId);
    List<User> selectUsersByProjectIdN(@Param("projectId") String projectId,@Param("name")String name);

    Integer selectByProjectId(@Param("projectId") String projectId,@Param("userId")String userId);
}
