/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector;

import cn.edu.ahut.copydetector.constant.DatabaseConstant;
import cn.edu.ahut.copydetector.constant.OtherConstant;
import cn.edu.ahut.copydetector.entity.User;
import cn.edu.ahut.copydetector.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/27 21:13
 * @description 初始化文件路径
 */
@Component
@Slf4j
public class initializationBean {

	@Autowired
	private UserService userService;

	@PostConstruct
	public void init(){
		List<User> root = new ArrayList<>(1);
		User user = new User();
		user.setUsername("root");
		root.add(user);
		// 首次使用本系统，自动创建admin
		userService.addUsersByExcel(root, DatabaseConstant.Role.ROLE_ADMIN.ordinal()+1);
		File programRoot = new File(OtherConstant.REALPATH);
		log.info("文件系统根目录为:"+programRoot.getAbsolutePath());
		// 若文件目录不存在，则创建
		if ( !programRoot.exists() ){
			// programRoot.mkdir();
			if ( programRoot.mkdir() ) {
				log.info("文件根目录创建成功");
			} else {
				log.error("文件根目录创建失败");
			}
		} else {
			log.info("文件根目录已存在，不做处理");
		}
	}
}
