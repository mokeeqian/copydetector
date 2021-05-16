/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.dao;

import cn.edu.ahut.copydetector.entity.SimResult;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SimResultDao {
    List<SimResult> findAll();
    void saveOne(SimResult simResult);

}
