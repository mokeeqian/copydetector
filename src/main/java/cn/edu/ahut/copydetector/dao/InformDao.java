/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.dao;

import cn.edu.ahut.copydetector.entity.Inform;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@Mapper
public interface InformDao {

	/**
	 * 单个通知
	 * @param id
	 * @return
	 */
	Inform selectInform(Integer id);

	/**
	 * 查询通知
	 * @param publisher
	 * @return
	 */
	List<Inform> selectInforms(String publisher);

	/**
	 * 查询同一发布者通知
	 * @param param
	 * @return
	 */
	List<Inform> selectInformsByPublisher(HashMap<String, Object> param);

	/**
	 * 查询所有通知，分页
	 * @param param
	 * @return
	 */
	List<Inform> selectAllInforms(HashMap<String, Object> param);

	/**
	 * 根据必修或选修查询通知
	 * @param param
	 * @return
	 */
	List<Inform> selectInformsByType(HashMap<String, Object> param);

	/**
	 * 根据条件查询通知
	 * @param param
	 * @return
	 */
	List<Inform> selectInformsBySort(HashMap<String, Object> param);

	/**
	 * 统计
	 * @return
	 */
	Integer count();

	/**
	 * 根据通知类型返回统计
	 * @param type
	 * @return
	 */
	Integer countByType(Integer type);

	/**
	 * 统计同发布者
	 * @param publisher
	 * @return
	 */
	Integer countByPublisher(String publisher);

	/**
	 * 根据条件返回统计
	 * @param inform
	 * @return
	 */
	Integer countBySort(Inform inform);

	/**
	 * 批量发布通知
	 * @param list
	 * @return
	 */
	Integer addInforms(List<Inform> list);

	/**
	 * 修改通知
	 * @param inform
	 * @return
	 */
	Integer updateInform(Inform inform);

	/**
	 * 批量删除通知
	 * @param ids
	 * @return
	 */
	Integer deleteInforms(List<Integer> ids);
}
