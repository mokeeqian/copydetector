/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.controller;

import cn.edu.ahut.copydetector.common.TableResult;
import cn.edu.ahut.copydetector.constant.OtherConstant;
import cn.edu.ahut.copydetector.entity.*;
import cn.edu.ahut.copydetector.service.InformService;
import cn.edu.ahut.copydetector.service.UserService;
import cn.edu.ahut.copydetector.util.ExcelUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivateKey;
import java.util.*;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/23 13:51
 * @description 管理员映射
 */
@Controller
@Slf4j
@RequestMapping("/admin")
@SuppressWarnings("unchecked")
public class AdminController {
	private User user = new User();
	private UserService userService;
	private InformService informService;


	public AdminController(UserService userService, InformService informService) {
		this.userService = userService;
		this.informService = informService;
	}

	@RequestMapping(value = "/index")
	public String index(Model model) {
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// 判断当前用户
		if ( "anonymousUser".equals(o.toString()) ) {
			return "redirect:/logout";
		} else {
			user = (User) o;
			model.addAttribute("current", user);
			return "admin/index";
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
			return "admin/students";
		}
	}
	@RequestMapping(value = "/teachers")
	public String teachers(Model model) {

		Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())){
			return "redirect:logout";
		}else {
			user = (User) a;
			model.addAttribute("current", user);
			return "admin/teachers";
		}
	}
	@RequestMapping("/authorities")
	public String authorities(Model model){
		Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())){
			return "redirect:logout";
		}else {
			user = (User) a;
			model.addAttribute("current", user);
			return "admin/authorities";
		}
	}
	// TODO: 文件管理
	@RequestMapping("/files")
	public String files(Model model){
		Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())){
			return "redirect:logout";
		}else {
			user = (User) a;
			model.addAttribute("current", user);
			return "admin/files";
		}
	}
	@RequestMapping("/informs")
	public String informs(Model model){
		Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())){
			return "redirect:logout";
		}else {
			user = (User) a;
			model.addAttribute("current", user);
			return "admin/informs";
		}
	}
	@RequestMapping("/roles")
	public String roles(Model model){
		Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())){
			return "redirect:logout";
		}else {
			user = (User) a;
			model.addAttribute("current", user);
			return "admin/roles";
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
			return "admin/search";
		}
	}
	@RequestMapping("/systemInfo")
	public String Info(Model model){
		Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())){
			return "redirect:logout";
		}else {
			user = (User) a;
			model.addAttribute("current", user);
			return "admin/systemInfo";
		}
	}
	@RequestMapping("/loginLog")
	public String log(Model model){
		Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())){
			return "redirect:logout";
		}else {
			user = (User) a;
			model.addAttribute("current", user);
			return "admin/loginLog";
		}
	}
	@RequestMapping("/manageMenu")
	public String menu(Model model){
		Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())){
			return "redirect:logout";
		}else {
			user = (User) a;
			model.addAttribute("current", user);
			return "admin/manageMenu";
		}
	}



	/**
	 * 数据接口,根据roleid来分页查询用户
	 * @param page
	 * @param limit
	 * @param role 用户角色id
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
			java.io.File dest = new java.io.File(upload.getAbsolutePath() + File.separator + fileName);
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
	 * 管理员端展示通知列表
	 */
	@RequestMapping(value = "/informType", method = RequestMethod.GET)
	@ResponseBody
	public Map inform(@RequestParam int page
			,@RequestParam int limit
			,@RequestParam(required = false) Integer type) {
		Map<String, Object> informMap = new HashMap<>(4);
		PageBean<Inform> res = informService.selectInformsBySort(type, page, limit);
		informMap.put("data",res.getList());
		informMap.put("code",0);
		informMap.put("msg","");
		informMap.put("count",res.getTotalRecord());
		return informMap;
	}


	/**
	 * 管理员端展示角色列表
	 */
	@RequestMapping(value = "/showRoles",method = RequestMethod.GET)
	@ResponseBody
	public Map roles(@RequestParam(value = "username", required = false) String username){
		Map<String, Object> roleMap = new HashMap<>(4);
		if (username == null) {
			List<Role> roles = userService.selectAllRole();
			roleMap.put("data", roles);
			roleMap.put("code", 0);
			roleMap.put("msg", "");
			roleMap.put("count", roles.size());
		}else {
			Role role = userService.selectRoleByUsername(username);
			roleMap.put("data", role);
			roleMap.put("code", 0);
			roleMap.put("msg", "");
			roleMap.put("count", 1);
		}
		return roleMap;
	}

	/**
	 * 管理员端展示角色列表
	 */
	@RequestMapping(value = "/showAuthorities",method = RequestMethod.GET)
	@ResponseBody
	public Map authority(@RequestParam(value = "username", required = false) String username){
		Map<String, Object> authorityMap = new HashMap<>(4);
		if (username == null) {
			List<Authority> authorities = userService.selectAllAuthority();
			authorityMap.put("data", authorities);
			authorityMap.put("code", 0);
			authorityMap.put("msg", "");
			authorityMap.put("count", authorities.size());
		}else {
			List<Authority> authorities = userService.selectAuthoritiesByUsername(username);
			authorityMap.put("data", authorities);
			authorityMap.put("code", 0);
			authorityMap.put("msg", "");
			authorityMap.put("count", authorities.size());
		}
		return authorityMap;
	}


	/**
	 * 管理员端搜索用户
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
	 * 管理员端批量管理用户
	 */
	@RequestMapping(value = "/manageUsers", method = RequestMethod.POST)
	@ResponseBody
	public Map addUsers(@RequestBody String jsonUsers, @RequestParam Integer roleId) throws InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
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
	 * 管理员端管理角色（未完成）
	 */
	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	@ResponseBody
	public Map addRole(@RequestParam String jsonUsers){
		Map<String, Object> res = new HashMap<>();
		return res;
	}


}
