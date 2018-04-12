package com.mircoservice.mybatis.dao.mapper;

import com.mircoservice.mybatis.dao.entity.FeeBase;

public interface FeeBaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FeeBase record);

    int insertSelective(FeeBase record);

    FeeBase selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FeeBase record);

    int updateByPrimaryKey(FeeBase record);
}