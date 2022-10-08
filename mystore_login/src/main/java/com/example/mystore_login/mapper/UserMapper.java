package com.example.mystore_login.mapper;

import com.example.mystore_login.entity.User;
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
public interface UserMapper {
    Integer insert(User user);
    User findByUsername(String username);
}
