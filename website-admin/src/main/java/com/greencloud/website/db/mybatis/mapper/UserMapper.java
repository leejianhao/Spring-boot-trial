package com.greencloud.website.db.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.greencloud.website.model.User;


public interface UserMapper {
	@Select("select * from t_user where username=#{username}")
	User selectByName(@Param("username") String username);
	
	User load(@Param("id") Long id);
}