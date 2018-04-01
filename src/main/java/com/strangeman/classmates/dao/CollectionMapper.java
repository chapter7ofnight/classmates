package com.strangeman.classmates.dao;

import com.strangeman.classmates.bean.Collection;
import com.strangeman.classmates.bean.CollectionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectionMapper {
    long countByExample(CollectionExample example);

    int deleteByExample(CollectionExample example);

    int insert(Collection record);

    int insertSelective(Collection record);

    List<Collection> selectByExample(CollectionExample example);

    int updateByExampleSelective(@Param("record") Collection record, @Param("example") CollectionExample example);

    int updateByExample(@Param("record") Collection record, @Param("example") CollectionExample example);
}