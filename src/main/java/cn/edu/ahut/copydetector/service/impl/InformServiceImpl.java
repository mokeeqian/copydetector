/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service.impl;

import cn.edu.ahut.copydetector.constant.OtherConstant;
import cn.edu.ahut.copydetector.dao.InformDao;
import cn.edu.ahut.copydetector.entity.Inform;
import cn.edu.ahut.copydetector.entity.PageBean;
import cn.edu.ahut.copydetector.entity.User;
import cn.edu.ahut.copydetector.service.FileService;
import cn.edu.ahut.copydetector.service.InformService;
import cn.edu.ahut.copydetector.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/27 20:02
 * @description
 */
@Service
@Slf4j
public class InformServiceImpl implements InformService {

	private InformDao informDao;

	// 新建通知，自动创建该通知所接受的文件目录
	private FileService fileService;
	private UserService userService;

	public InformServiceImpl(InformDao informDao, FileService fileService, UserService userService) {
		this.informDao = informDao;
		this.fileService = fileService;
		this.userService = userService;
	}



	@Override
	public Inform selectInform(Integer id) {
		return informDao.selectInform(id);
	}

	@Override
	public PageBean<Inform> selectInforms(String publisher, int page, int limit) {
		HashMap<String, Object> map = new HashMap<>();
		PageBean<Inform> pageBean = new PageBean<>();
		pageBean.setCurrentPage(page);
		pageBean.setPageSize(limit);
		Integer count = informDao.countByPublisher(publisher);
		pageBean.setTotalRecord(count);

		map.put("start", (page-1)*limit);
		map.put("limit", pageBean.getPageSize());
		map.put("publisher", publisher);
		List<Inform> informs = informDao.selectInformsByPublisher(map);
		pageBean.setList(informs);
		return pageBean;
	}

	@Override
	public PageBean<Inform> selectAllInforms(int currentPage) {
		HashMap<String, Object> map = new HashMap<>();
		PageBean<Inform> pageBean = new PageBean<>();
		pageBean.setCurrentPage(currentPage);
		int pageSize = 15;
		pageBean.setPageSize(pageSize);
		Integer total = informDao.count();
		pageBean.setTotalRecord(total);

		if (total%pageSize == 0){
			pageBean.setTotalPage(total/pageSize);
		}else {
			pageBean.setTotalPage((total/pageSize)+1);
		}

		map.put("start", (currentPage-1)*pageSize);
		map.put("limit", pageBean.getPageSize());
		List<Inform> informs = informDao.selectAllInforms(map);
		pageBean.setList(informs);
		return pageBean;
	}

	@Override
	public PageBean<Inform> selectInformsBySort(Integer type, int currentPage, int limit, String ... department) {
		HashMap<String, Object> map = new HashMap<>();
		PageBean<Inform> pageBean = new PageBean<>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setPageSize(limit);
		if (department.length == 0) {
			int total = informDao.countByType(type);
			pageBean.setTotalRecord(total);
			if (total % limit == 0) {
				pageBean.setTotalPage(total / limit);
			} else {
				pageBean.setTotalPage((total / limit) + 1);
			}

			map.put("start", (currentPage - 1) * limit);
			map.put("limit", pageBean.getPageSize());
			map.put("type", type);
			List<Inform> informs = informDao.selectInformsByType(map);
			pageBean.setList(informs);

		}else {
			String tmpDepartment = department[0];
			map.put("department", tmpDepartment);
			Inform tmpInform = new Inform();
			tmpInform.setType(type);
			tmpInform.setDepartment(tmpDepartment);
			int total = informDao.countBySort(tmpInform);
			pageBean.setTotalRecord(total);
			if (total % limit == 0) {
				pageBean.setTotalPage(total / limit);
			} else {
				pageBean.setTotalPage((total / limit) + 1);
			}

			map.put("start", (currentPage - 1) * limit);
			map.put("limit", pageBean.getPageSize());
			map.put("type", type);
			List<Inform> informs = informDao.selectInformsBySort(map);
			pageBean.setList(informs);
		}
		return pageBean;
	}

	@Override
	public Integer addInforms(List<Inform> list) {
		for (Inform current : list) {
			if (current.getPath() != null && !"".equals(current.getPath())) {
				log.info("你输入的相对路径："+current.getPath());

				User user = (User) userService.loadUserByUsername(current.getPublisher());
				String name = user.getUsername() + "_" + user.getRealname();	// 123_王老师
				String filename = current.getPath();
				current.setPath(
						new java.io.File(OtherConstant.REALPATH).getAbsolutePath()
						+ java.io.File.separator
								+ name + File.separator
						+ current.getPath().replaceAll(
								OtherConstant.NOT_SEPARATOR, OtherConstant.SEPARATOR)
				);
				log.info("当前inform的接受文件路径为："+ current.getPath());

				// 创建文件夹
				int res = this.fileService.addDirectory(
						filename,
						name + File.separator,
						user.getId());
				if (res == 1) {
					log.info("===inform文件夹创建成功===");
				} else {
					log.error("===inform文件夹创建失败===");
				}
			}
			current.setDate(OtherConstant.DATE_FORMAT.format(new Date()));
		}
		return informDao.addInforms(list);
	}

	@Override
	public Integer updateInform(Inform inform) {
		return informDao.updateInform(inform);
	}

	@Override
	public Integer deleteInforms(List<Integer> ids) {
		return informDao.deleteInforms(ids);
	}
}
