package com.leo.medical.mapper;

import com.leo.medical.annotation.AutoFill;
import com.leo.medical.entity.User;
import com.leo.medical.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    User getById(String id);

    /**
     * 根据openid获取当前用户
     * @param openid
     * @return
     */
    User getByOpenId(@Param("openid") String openid);

    /**
     * 创建新用户
     * @param user
     */
    @AutoFill(OperationType.INSERT)
    void insert(User user);

    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
