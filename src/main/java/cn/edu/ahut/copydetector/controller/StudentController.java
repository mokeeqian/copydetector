/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.controller;

import cn.edu.ahut.copydetector.constant.DatabaseConstant;
import cn.edu.ahut.copydetector.entity.File;
import cn.edu.ahut.copydetector.entity.Inform;
import cn.edu.ahut.copydetector.entity.PageBean;
import cn.edu.ahut.copydetector.entity.User;
import cn.edu.ahut.copydetector.service.FileService;
import cn.edu.ahut.copydetector.service.InformService;
import cn.edu.ahut.copydetector.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/27 23:17
 * @description
 */
@Controller
@Slf4j
@RequestMapping("/student")
public class StudentController {

	static StringBuffer current;
	private UserService userService;
	private FileService fileService;
	private InformService informService;
	private User user = new User();

	public StudentController(UserService userService, FileService fileService, InformService informService) {
		this.userService = userService;
		this.fileService = fileService;
		this.informService = informService;
	}

	/**
	 * 学生页面的一些跳转
	 */
	@RequestMapping("/index")
	public String index(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "student/index";
		}
	}

	@RequestMapping("/checkInforms")
	public String checkInforms(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "student/checkInforms";
		}
	}

	@RequestMapping("/aboutme")
	public String aboutme(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "student/aboutme";
		}
	}




	@PostMapping("/upload")
	@ResponseBody
	public Map redirectUploadByInform(Inform detailJson) {
		HashMap<String, Object> resMap = new HashMap<>();
		resMap.put("code", 1);
		resMap.put("id", detailJson.getId());
		resMap.put("username", detailJson.getPublisher());
		return resMap;
	}

	@PostMapping("/uploadByTeacher")
	@ResponseBody
	public Map redirectUploadByUser(User data) {
		HashMap<String, Object> resMap = new HashMap<>();
		resMap.put("code", 1);
		resMap.put("username", data.getUsername());
		return resMap;
	}

	@RequestMapping("/upload/{id}")
	public String upload(@PathVariable Integer id, Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			Inform inform = informService.selectInform(id);
			model.addAttribute("current", user);
			current = new StringBuffer(inform.getPath().substring(inform.getPath().indexOf("root") + 5));
			return "student/upload";
		}
	}

	@RequestMapping("/uploadByTeacher/{username}")
	public String upload(@PathVariable String username, Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			HashMap<String, Object> res = userService.selectUser(username);
			model.addAttribute("current", user);
			User user = (User) res.get("user");
			current = new StringBuffer(user.getUsername() + "_" + user.getRealname() + java.io.File.separator);
			return "student/upload";
		}
	}


	@RequestMapping("/submitions")
	public String submitions(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "student/submitions";
		}
	}

	@RequestMapping("/update")
	public String update(Model model) {
		if (SecurityContextHolder.getContext().getAuthentication() == null){
			return "redirect:logout";
		}
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			if (user != null){
				User newUser = (User) userService.selectUser(user.getUsername()).get("user");
				if (!user.getPassword().equals(newUser.getPassword())) {
					return "redirect:../logout";
				}else {
					user = (User) a;
					model.addAttribute("current", user);
					return "student/update";
				}
			}else {
				user = (User) a;
				model.addAttribute("current", user);
				return "student/update";
			}
		}
	}

	@RequestMapping("/search")
	public String teachers(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "student/teachers";
		}
	}

	@RequestMapping("/search/{username}")
	public String teacher(Model model, @PathVariable String username) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			User searchRes = (User) userService.selectUser(username).get("user");
			model.addAttribute("current", user);
			model.addAttribute("res", searchRes);
			return "student/teachers";
		}
	}

	/**
	 * 学生端搜索教师
	 */
	@RequestMapping(value = "/searchTeacher", method = RequestMethod.GET)
	@ResponseBody
	public Map searchTeacher(String username) {
		HashMap<String, Object> des = new HashMap<>();
		des.put("code", 1);
		des.put("msg", "转发");
		des.put("username", username);
		return des;
	}


	/**
	 * 通知列表，可筛选
	 */
	@RequestMapping(value = "/showInforms", method = RequestMethod.GET)
	@ResponseBody
	public Map showInforms(@RequestParam(value = "type", required = false) int type
			, @RequestParam(value = "department", required = false) String department
			, @RequestParam(value = "realname", required = false) String realname
			, @RequestParam(value = "page") int page
			, @RequestParam(value = "limit") int limit) {
		HashMap<String, Object> informMap = new HashMap<>(4);
		PageBean<Inform> pageBean = informService.selectInformsBySort(type, page, limit, department);
		if (realname != null) {
			User teacher = (User) userService.selectUserByRealname(realname.trim()).get("user");
			if (teacher == null) {
				pageBean.getList().clear();
				pageBean.setTotalRecord(0);
			} else {
				List<Inform> informs = pageBean.getList();
				Iterator<Inform> iterator = informs.iterator();
				while (iterator.hasNext()) {
					Inform current = iterator.next();
					if (!current.getPublisher().equals(teacher.getUsername())) {
						iterator.remove();
					}
				}
				pageBean.setList(informs);
				pageBean.setTotalRecord(informs.size());
			}
		}
		informMap.put("data", pageBean.getList());
		informMap.put("msg", "");
		informMap.put("code", 0);
		informMap.put("count", pageBean.getTotalRecord());
		return informMap;
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	@ResponseBody
	public Map updatePassword(@RequestBody Map jsonUsers) {
		Map<String, Object> res = new HashMap<>(2);
		String oldPassword = (String) jsonUsers.get("oldPassword");
		String newPassword = (String) jsonUsers.get("newPassword");
		User oldUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int updRes = userService.updatePassword(newPassword, oldPassword, oldUser);
		if (updRes == 0) {
			res.put("code", 0);
			res.put("msg", "error");
		} else if (updRes == -1) {
			res.put("code", -1);
			res.put("msg", "old password error");
		} else {
			res.put("code", 1);
			res.put("msg", "success");
		}
		return res;
	}


	/**
	 * 教师列表
	 */
	@RequestMapping(value = "/showTeachers", method = RequestMethod.GET)
	@ResponseBody
	public Map showTeachers(@RequestParam int page
			, @RequestParam int limit
			, @RequestParam(required = false) String department
			, @RequestParam(required = false) String username) {
		Map<String, Object> map = new HashMap<>();
		HashMap<String, String> param = new HashMap<>();
		if (department != null) {
			param.put("department", department);
		}
		if (username != null) {
			param.put("username", username);
		}
		// 枚举类型默认从0开始计数，数据库中是从1开始字段标记
		PageBean<User> res = userService.selectUsersByRole(page, limit, DatabaseConstant.Role.ROLE_TEACHER.ordinal() + 1, param);
		map.put("data", res.getList());
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", res.getList().size());
		return map;
	}

	/**
	 * 以往作业列表
	 */
	@RequestMapping(value = "/showOldWorks", method = RequestMethod.GET)
	@ResponseBody
	public Map showOldWorks(@RequestParam int page
			, @RequestParam int limit) {
		Map<String, Object> map = new HashMap<>();
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			map.put("data", "");
			map.put("code", 1);
			map.put("msg", "验证信息失效，请重新登录");
			map.put("count", 0);
			return map;
		} else {
			User user = (User) a;
			PageBean<File> res = fileService.selectOldSubmitted(user.getId(), page, limit);
			map.put("data", res.getList());
			map.put("code", 0);
			map.put("msg", "查询成功");
			map.put("count", res.getTotalRecord());
			return map;
		}
	}


	/**
	 * 最近选择的教师
	 */
	@RequestMapping(value = "/recentTeacher", method = RequestMethod.GET)
	@ResponseBody
	public Map recentTeacher(@RequestParam int page
			, @RequestParam int limit) {
		Map<String, Object> map = new HashMap<>();
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			map.put("data", "");
			map.put("code", 1);
			map.put("msg", "验证信息失效，请重新登录");
			map.put("count", 0);
			return map;
		} else {
			User user = (User) a;
			List<File> res = fileService.selectOldSubmitted(user.getId(), page, limit).getList();
			HashMap<Integer,Integer> record = new HashMap<>();
			for (File current : res){
				if (!record.containsKey(current.getOwner())){
					record.put(current.getOwner(),1);
				}else {
					record.put(current.getOwner(),record.get(current.getOwner())+1);
				}
			}
			ArrayList<Integer> ids = new ArrayList<>();
			for (Integer id : record.keySet()){
				ids.add(id);
			}
			if (ids.size() == 0){
				map.put("data", null);
				map.put("code", 0);
				map.put("msg", "查询成功");
				map.put("count", 0);
			}else {
				List<User> teachers = userService.selectUsersByIds(ids);
				map.put("data", teachers);
				map.put("code", 0);
				map.put("msg", "查询成功");
				map.put("count", teachers.size());
			}
			return map;
		}
	}
}
