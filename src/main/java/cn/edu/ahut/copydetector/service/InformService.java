/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service;

import cn.edu.ahut.copydetector.entity.Inform;
import cn.edu.ahut.copydetector.util.PageBean;

import java.util.List;

public interface InformService {
	Inform selectInform(Integer id);
	PageBean<Inform> selectInforms(String publisher, int page, int limit);
	PageBean<Inform> selectAllInforms(int currentPage);
	PageBean<Inform> selectInformsBySort(Integer type, int currentPage, int limit, String ... department);
	Integer addInforms(List<Inform> list);
	Integer updateInform(Inform inform);
	Integer deleteInforms(List<Integer> ids);
}
