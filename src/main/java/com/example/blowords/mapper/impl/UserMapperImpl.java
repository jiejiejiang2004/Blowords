package com.example.blowords.mapper.impl;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.ResultHandler;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blowords.entity.User;
import com.example.blowords.mapper.UserMapper;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

@Repository
public class UserMapperImpl implements UserMapper{
    @Override
    public int insert(User entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int deleteById(User entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int delete(Wrapper<User> queryWrapper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int updateById(User entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int update(User entity, Wrapper<User> updateWrapper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User selectById(Serializable id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<User> selectByIds(Collection<? extends Serializable> idList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void selectByIds(Collection<? extends Serializable> idList, ResultHandler<User> resultHandler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long selectCount(Wrapper<User> queryWrapper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<User> selectList(Wrapper<User> queryWrapper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void selectList(Wrapper<User> queryWrapper, ResultHandler<User> resultHandler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<User> selectList(IPage<User> page, Wrapper<User> queryWrapper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void selectList(IPage<User> page, Wrapper<User> queryWrapper, ResultHandler<User> resultHandler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<User> queryWrapper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void selectMaps(Wrapper<User> queryWrapper, ResultHandler<Map<String, Object>> resultHandler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Map<String, Object>> selectMaps(IPage<? extends Map<String, Object>> page, Wrapper<User> queryWrapper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void selectMaps(IPage<? extends Map<String, Object>> page, Wrapper<User> queryWrapper, ResultHandler<Map<String, Object>> resultHandler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <E> List<E> selectObjs(Wrapper<User> queryWrapper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <E> void selectObjs(Wrapper<User> queryWrapper, ResultHandler<E> resultHandler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
