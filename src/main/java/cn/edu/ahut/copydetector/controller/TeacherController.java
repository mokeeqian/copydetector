/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.controller;

import cn.edu.ahut.copydetector.common.TableResult;
import cn.edu.ahut.copydetector.constant.BasicConstant;
import cn.edu.ahut.copydetector.constant.DatabaseConstant;
import cn.edu.ahut.copydetector.constant.OtherConstant;
import cn.edu.ahut.copydetector.entity.*;
import cn.edu.ahut.copydetector.service.*;
import cn.edu.ahut.copydetector.util.ExcelUtil;
import cn.edu.ahut.copydetector.util.PageBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/29 10:52
 * @description
 */
@Controller
@Slf4j
@RequestMapping("/teacher")
@SuppressWarnings("unchecked")
public class TeacherController {

	private User user = new User();
	private FileService fileService;
	private UserService userService;
	private InformService informService;
	private SimResultService simResultService;
	private CourseService courseService;

	public TeacherController(FileService fileService, UserService userService,
							 InformService informService, SimResultService simResultService,
							 CourseService courseService) {
		this.fileService = fileService;
		this.userService = userService;
		this.informService = informService;
		this.simResultService = simResultService;
		this.courseService = courseService;
	}

	/**
	 * 教师页面跳转
	 */
	@RequestMapping("/index")
	public String index(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "teacher/index";
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
			return "teacher/submitions";
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
					return "teacher/update";
				}
			}else {
				user = (User) a;
				model.addAttribute("current", user);
				return "teacher/update";
			}
		}
	}
	@RequestMapping("/informs")
	public String informs(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "teacher/informs";
		}
	}
	@RequestMapping("/informs2")
	public String informs2(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "teacher/informs2";
		}
	}
	@RequestMapping("/myFolder")
	public String myFolder(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "teacher/myFolder";
		}
	}

	@RequestMapping(value = "/students")
	public String students(Model model) {
		Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())){
			return "redirect:logout";
		}else {
			user = (User) a;
			model.addAttribute("current", user);
			return "teacher/students";
		}
	}

	@RequestMapping("/search")
	public String search(Model model){
		Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())){
			return "redirect:logout";
		}else {
			user = (User) a;
			model.addAttribute("current", user);
			return "teacher/search";
		}
	}

	/**
	 * 作业点名页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/attendance")
	public String attendance(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "teacher/attendance";
		}
	}

	/**
	 * 3D散点图
	 * @param model
	 * @return
	 */
	@RequestMapping("/scatter3D")
	public String scatter3D(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "teacher/scatter3D";
		}
	}
	@RequestMapping("/table")
	public String table(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			model.addAttribute("current", user);
			return "teacher/table";
		}
	}

	@RequestMapping("/courses")
	public String courses(Model model) {
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			return "redirect:logout";
		} else {
			user = (User) a;
			log.info("当前用户: "+ user.toString());
			model.addAttribute("current", user);
			return "teacher/courses";
		}
	}

	/**
	 * 检查谁没交作业
	 * @param ids
	 * @return
	 */
	@PreAuthorize("hasAnyRole('TEACHER') or hasAnyRole('ADMIN')")
	@PostMapping(value = "/checkSubmittion")
	@ResponseBody
	public Map checkSubmittion(@RequestParam(value="ids[]") List<Integer> ids) {
		HashMap<String, Object> res = new HashMap<>();
		List<User> allStudents = userService.selectAllStudents();
		Iterator<User> iterator = allStudents.iterator();
		while(iterator.hasNext()) {
			User user = iterator.next();
			for (Integer id : ids) {
				if (user.getId().equals(id)) {
					iterator.remove();
				}
			}
		}
		List<Map<String, Object>> submittionResultList = new ArrayList<>();
		for (User user : allStudents) {
			Map<String, Object> tmpMap = new HashMap<>();
			tmpMap.put("username", user.getUsername());
			tmpMap.put("realname", user.getRealname());
			submittionResultList.add(tmpMap);
		}
		res.put("code", 0);
		res.put("msg", "");
		res.put("data", submittionResultList);
		res.put("count", submittionResultList.size());
		return res;
	}

	/**
	 * 	//TODO: 催学生提交作业
	 * @param usernames
	 * @return
	 */
	@PostMapping(value = "/noticeForSubmitting")
	@ResponseBody
	public String askForSubmitting(@RequestParam(value = "usernames[]") List<Integer> usernames) {
		String code = "1";
		log.info("users: " + usernames.size());
		return code;
	}


	@GetMapping(value = "/getScatter3D")
	@ResponseBody
	public Map<String, Object> getScatter3D() {
		Map<String, Object> res = new HashMap<>();
		List<SimResult> simResultList = simResultService.getAllSimresult();
		List<Object> datas = new ArrayList<>();
		for ( SimResult it : simResultList ) {
			List<Object> item = new ArrayList<>();
			item.add(it.getUser1());
			item.add(it.getUser2());
			item.add(it.getSim());
			datas.add(item);
		}

		res.put("code", 0);
		res.put("msg", "");
		res.put("data", datas);
		res.put("count", datas.size());
		return res;
	}

	@RequestMapping(value = "/getTableResult", method = RequestMethod.GET)
	@ResponseBody
	public TableResult<SimResult> getTableResult(@RequestParam("page") int page,
												 @RequestParam("limit") int limit) {
		TableResult<SimResult> tableResult = new TableResult<>();
		PageBean<SimResult> simResultPageBean = simResultService.getPageResult(page, limit);
		tableResult.setCode(0);
		tableResult.setMsg("");
		tableResult.setData(simResultPageBean.getList());
		tableResult.setCount(simResultPageBean.getTotalRecord());

		return tableResult;
	}

	/**
	 * Excel表格处理器
	 */
	@PostMapping("/excelAdd")
	@ResponseBody
	public Map upload(HttpServletRequest request, @RequestParam("role")int role) {
		Map<String, Object> json = new HashMap<>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		List<User> existUsers = new ArrayList<>();
		for (String string : fileMap.keySet()){
			ExcelUtil<User> userExcelUtil = new ExcelUtil<>(User.class);
			MultipartFile currentFile = fileMap.get(string);
			String fileName = currentFile.getOriginalFilename();
			String suffixName = fileName.substring(fileName.lastIndexOf("."));
			java.io.File upload = new java.io.File(OtherConstant.REALPATH, "excel临时文件夹");
			if(!upload.exists()){
				upload.mkdirs();
			}
			fileName = UUID.randomUUID() + suffixName;
			java.io.File dest = new java.io.File(upload.getAbsolutePath() + java.io.File.separator + fileName);
			try {
				currentFile.transferTo(dest);
				List<User> studentList = userExcelUtil.explain(dest.getAbsolutePath());
				Map<String, Object> resMap = userService.addUsersByExcel(studentList, role);
				if (resMap.get("exist") != null && resMap.get("exist") instanceof List<?>){
					existUsers.addAll((List<User>) resMap.get("exist"));
				}
			} catch (IOException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
				log.error("解析Excel发生错误，控制器处理异常",e);
				json.put("code", -1);
				json.put("msg", "上传失败！");
				return json;
			}
		}
		if (existUsers.size() == 0){
			json.put("code", 0);
			json.put("msg", "上传成功！");
			return json;
		}else {
			json.put("code", 1);
			json.put("exist", existUsers);
			json.put("msg", "上传成功！");
			return json;
		}
	}


	/**
	 * 教师搜索学生用户
	 * @param page
	 * @param limit
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseBody
	public TableResult<User> users(
			@RequestParam("page") int page,
			@RequestParam("limit") int limit,
			@RequestParam(required = false, value = "role") Integer role) {
		TableResult<User> tableResult = new TableResult<>();
		PageBean<User> res = userService.selectUsersByRole(page, limit, role, new HashMap<>());
		tableResult.setData(res.getList());
		tableResult.setCode(0);
		tableResult.setCount(res.getTotalRecord());
		tableResult.setMsg("");
		return tableResult;
	}


	/**
	 * 搜索用户
	 */
	@RequestMapping("/search/{type}/{keyword}")
	@ResponseBody
	public Map searchRes(@PathVariable String type, @PathVariable String keyword){
		HashMap<String, Object> res = new HashMap<>();
		User user = userService.selectUserBySort(type, keyword);
		List<User> users = new ArrayList<>();
		users.add(user);
		if (user == null){
			res.put("code",1);
			res.put("msg","用户不存在");
			res.put("count",0);
			res.put("data",users);
			return res;
		}else {
			res.put("code",0);
			res.put("msg","");
			res.put("count",1);
			res.put("data",users);
			return res;
		}
	}

	/**
	 * 批量管理用户
	 */
	@RequestMapping(value = "/manageUsers", method = RequestMethod.POST)
	@ResponseBody
	public Map addUsers(@RequestBody String jsonUsers, @RequestParam Integer roleId)
			throws InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
		Map<String, Object> res = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(jsonUsers, User.class);
		List<User> singleUser = new ArrayList<>();
		singleUser.add(user);
		Map<String, Object> addRes = userService.addUsersByExcel(singleUser, roleId);
		if (addRes.get("exist") != null){
			res.put("msg","该用户已存在");
		}else {
			res.put("msg","添加成功");
		}
		res.put("code",0);
		return res;
	}
	@RequestMapping(value = "/deleteUsers", method = RequestMethod.POST)
	@ResponseBody
	public Map deleteUsers(@RequestBody String jsonUsers) throws JsonProcessingException {
		Map<String, Object> res = new HashMap<>();
		List<Integer> ids = new ArrayList<>();
		if(jsonUsers.startsWith("[")){
			ObjectMapper mapper = new ObjectMapper();
			List<User> beanList = mapper.readValue(jsonUsers, new TypeReference<List<User>>() {});
			for (User user : beanList){
				ids.add(user.getId());
			}
		}else {
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.readValue(jsonUsers, User.class);
			ids.add(user.getId());
		}
		Integer delRes = userService.deleteUsers(ids);
		res.put("code",1);
		res.put("count",delRes);
		return res;
	}
	@RequestMapping(value = "/updateUsers", method = RequestMethod.POST)
	@ResponseBody
	public Map updateUsers(@RequestBody String jsonUsers, @RequestParam(required = false) Integer roleId) throws JsonProcessingException {
		Map<String, Object> res = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(jsonUsers, User.class);
		List<User> users = new ArrayList<>();
		users.add(user);
		Integer updRes = userService.updateUsers(users);
		res.put("code",1);
		res.put("count",updRes);
		return res;
	}

	/**
	 * 修改密码（后续优化：将旧密码的确认做成异步）
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
	 * 重命名文件
	 * FIXME: 修复文件重命名bug
	 */
	@PostMapping("/renameFile")
	@ResponseBody
	public Map renameFile(@RequestParam String name, @RequestParam String path) {
		Map<String, Object> json = new HashMap<>();
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			json.put("data", null);
			json.put("code", 0);
			json.put("msg", "身份验证过期，请重新登录");
			json.put("count", 0);
			return json;
		} else {
			String user_tag = user.getUsername() + "_" + user.getRealname();
			;                // "123_王老师"
			path = new java.io.File(OtherConstant.REALPATH).getAbsolutePath()
					+ java.io.File.separator + user_tag
					+ java.io.File.separator + path;
			log.info("******path: " + path);
			int index = path.lastIndexOf(java.io.File.separator);
			String tmpPath;
			if (index == path.indexOf(java.io.File.separator) + 1) {
				tmpPath = path.substring(0, index);
			} else {
				tmpPath = path.substring(0, index) + java.io.File.separator;
			}
			String tmpName = path.substring(index + 1);
			HashMap<String, Object> param = new HashMap<>();
			param.put("newName", name);
			param.put("resourcePath", tmpPath);
			param.put("resourceName", tmpName);
			int res = fileService.updateFiles(param, BasicConstant.FileAction.RENAME.getString());
			if (res == 1) {
				json.put("code", 1);
				json.put("msg", "创建成功！");
				return json;
			} else {
				json.put("code", 0);
				json.put("msg", "创建失败！");
				return json;
			}
		}
	}


	/**
	 * 个人中心展示最近未批改
	 */
	@RequestMapping(value = "/recentWorks", method = RequestMethod.GET)
	@ResponseBody
	public Map recentWorks(@RequestParam int page, @RequestParam int limit) {
		Map<String, Object> json = new HashMap<>();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		PageBean<File> pageBean = fileService.selectRecent(user.getId(), page, limit, "update_time");
		PageBean<File> pageBean = fileService.selectRecent(1, page, limit, "update_time");
		List<File> list = pageBean.getList();
		Iterator<File> iterator = list.iterator();
		while (iterator.hasNext()){
			File current = iterator.next();
			if (!current.getStatus().equals(DatabaseConstant.File.UNCHECKED.getFlag())){
				iterator.remove();
			}
		}
		json.put("data", list);
		json.put("code", 0);
		json.put("msg", "");
		json.put("count", pageBean.getTotalRecord());
		log.info("最近未批改: "+json.toString());
		return json;
	}


	/**
	 * 个人中心最近通知
	 */
	@RequestMapping(value = "/recentInform", method = RequestMethod.GET)
	@ResponseBody
	public Map recentInform(@RequestParam int limit) {
		Map<String, Object> json = new HashMap<>();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PageBean<Inform> pageBean = informService.selectInforms(user.getUsername(), 1, limit);
		json.put("data", pageBean.getList());
		json.put("code", 0);
		json.put("msg", "");
		json.put("count", pageBean.getTotalRecord());
		return json;
	}


	/**
	 * 个人中心最近提交owner,倒数id
	 */
	@RequestMapping(value = "/recentSubmit", method = RequestMethod.GET)
	@ResponseBody
	public Map recentSubmit(@RequestParam int limit) {
		Map<String, Object> json = new HashMap<>();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		PageBean<File> pageBean = fileService.selectRecent(user.getId(), 1, limit, "id");
		PageBean<File> pageBean = fileService.selectRecent(1, 1, limit, "id");
		json.put("data", pageBean.getList());
		json.put("code", 0);
		json.put("msg", "");
		json.put("count", pageBean.getTotalRecord());
		log.info("最近提交: " + json.toString());
		return json;
	}


	/**
	 * 教师端通知发布
	 */
	@RequestMapping(value = "/push", method = RequestMethod.POST)
	@ResponseBody
	public Map pushInform(Inform inform) {
		HashMap<String, Object> resMap = new HashMap<>();
		List<Inform> informs = new ArrayList<>();
		informs.add(inform);
		Integer updRes = informService.addInforms(informs);
		if (updRes == 1) {
			resMap.put("code", 0);
			resMap.put("msg", "发布成功");
		} else {
			resMap.put("code", 1);
			resMap.put("msg", "发布失败");
		}
		return resMap;
	}

	/**
	 * 教师端通知编辑
	 */
	@RequestMapping(value = "/editInform", method = RequestMethod.POST)
	@ResponseBody
	public Map editInform(Inform inform) {
		HashMap<String, Object> resMap = new HashMap<>();
		Integer updRes = informService.updateInform(inform);
		if (updRes == 1) {
			resMap.put("code", 0);
			resMap.put("msg", "更新成功");
		} else {
			resMap.put("code", 1);
			resMap.put("msg", "更新失败");
		}
		return resMap;
	}

	/**
	 * 教师端通知删除
	 */
	@RequestMapping(value = "/delInform", method = RequestMethod.POST)
	@ResponseBody
	public Map delInform(@RequestParam(name = "idParam[]") String[] idParam) {
		HashMap<String, Object> resMap = new HashMap<>();
		List<Integer> ids = new ArrayList<>();
		for (String id : idParam) {

			ids.add(Integer.valueOf(id));
		}
		// FIXME: 删除inform 后，自动删除对应的文件夹
		int delRes = informService.deleteInforms(ids);
		if (delRes == idParam.length) {
			resMap.put("code", 0);
			resMap.put("msg", "删除成功");
		} else if (delRes < idParam.length && delRes > 0) {
			resMap.put("code", 1);
			resMap.put("msg", "删除成功，部分删除不成功");
		} else {
			resMap.put("code", -1);
			resMap.put("msg", "删除失败");
		}
		return resMap;
	}

	/**
	 * 个人中心最近通知(查看更多)
	 */
	@RequestMapping(value = "/recentInforms", method = RequestMethod.GET)
	@ResponseBody
	public Map recentInforms(@RequestParam int page, @RequestParam int limit) {
		Map<String, Object> res = new HashMap<>();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PageBean<Inform> informs = informService.selectInforms(user.getUsername(), page, limit);
		res.put("code", 0);
		res.put("count", informs.getTotalRecord());
		res.put("data", informs.getList());
		res.put("msg", "");
		return res;
	}


	/**
	 * 个人中心最近提交(查看更多)
	 */
	@RequestMapping(value = "/recentSubmits", method = RequestMethod.GET)
	@ResponseBody
	public Map recentSubmits() {
		Map<String, Object> json = new HashMap<>();
		return json;
	}


	/**
	 * office展示
	 */
	@RequestMapping(value = "/showOffice", method = RequestMethod.POST)
	public void showOffice() {
	}

	@RequestMapping(value = "/getMyCourses", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getMyCourses(HttpServletRequest request) {
		Map<String, Object> res = new HashMap<>();
		User user = (User) request.getSession().getAttribute("user");
		List<Course> courseList = courseService.getCoursesByTno(user.getUsername());
//		log.info(courseList.toString());
		res.put("code", 0);
		res.put("msg", "");
		res.put("data", courseList);
		res.put("count", courseList.size());
		return res;
	}



}

