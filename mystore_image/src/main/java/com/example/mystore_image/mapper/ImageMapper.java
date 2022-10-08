package com.example.mystore_image.mapper;

import com.example.mystore_image.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
Human selectHumanByNameAndSex(@Param("name") String username, Integer sex);

 <select id="selectHumanByNameAndSex" resultType="Human">
 select * from m_human where name=#{name} and sex=#{sex}
 </select>
 */
@Repository
public interface ImageMapper {

    /**
     * 根据uid更新用户的头像
     * @param uid 用户的id
     * @param avatar 新头像的路径
     * @param modifiedUser 修改执行人
     * @param modifiedTime 修改时间
     * @return 受影响的行数
     */
    Integer updateAvatarByUid(
            @Param("uid") Integer uid,
            @Param("avatar") String avatar,
            @Param("modifiedUser") String modifiedUser,
            @Param("modifiedTime") Date modifiedTime);
    /**
     * 根据用户id查询用户数据
     * @param uid 用户id
     * @return 匹配的用户数据，如果没有匹配的用户数据，则返回null
     */

    User findByUid(Integer uid);
}
