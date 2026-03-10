package com.example.blowords.User.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blowords.User.entity.User;

/**
 * <p>
 *  Mapper 接口
 * </p>
*
* @author 杰杰酱
* @since 2026-02-12
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
