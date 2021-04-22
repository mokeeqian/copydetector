/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.controller;

import cn.edu.ahut.copydetector.constant.BasicConstant;
import cn.edu.ahut.copydetector.constant.OtherConstant;
import cn.edu.ahut.copydetector.entity.LayuiDtree;
import cn.edu.ahut.copydetector.entity.PageBean;
import cn.edu.ahut.copydetector.entity.User;
import cn.edu.ahut.copydetector.service.FileService;
import cn.edu.ahut.copydetector.util.Doc2PdfUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/27 23:20
 * @description
 */
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

	@Value("${openoffice.host}")
	private String openOfficeHost;
	@Value("${openoffice.port}")
	private Integer openOfficePort;
	private FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * 展示文件列表
	 */
	@PreAuthorize("hasAnyRole('STUDENT') or hasAnyRole('TEACHER') or hasAnyRole('ADMIN')")
	@GetMapping("/show")
	public Map showList(@RequestParam(required = false) int page
			, @RequestParam(required = false) int limit
			, @RequestParam(required = false) String path) {
		Map<String, Object> res = new HashMap<>();
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			res.put("data", null);
			res.put("code", 0);
			res.put("msg", "身份验证过期，请重新登录");
			res.put("count", 0);
		} else {
			User user = (User) a;
			List<GrantedAuthority> grantedAuthorities = user.getAuthorities();
			if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
				if (path == null) {
					path = "/";
				}
			} else if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_TEACHER"))) {
				if ("\\".equals(path) || "/".equals(path) || path == null) {
					path = user.getUsername() + "_" + user.getRealname() + File.separator;
				} else {
					path = user.getUsername() + "_" + user.getRealname() + File.separator + path;
				}
			} else {
				StringBuffer current = StudentController.current;
				if ("\\".equals(path) || "/".equals(path) || path == null) {
					path = current.toString();
				} else if ("".equals(current.toString())) {
					path = File.separator + "test";
				} else {
					path = current + path;
				}
			}
			PageBean<Map<String, Object>> fileList = fileService.showFileList(path, page, limit);
			if (fileList == null) {
				res.put("data", null);
				res.put("code", 0);
				res.put("msg", "");
				res.put("count", 0);
			} else {
				res.put("data", fileList.getList());
				res.put("code", 0);
				res.put("msg", "");
				res.put("count", fileList.getTotalRecord());
			}
		}
		return res;
	}

	/**
	 * 上传文件
	 */
	@PreAuthorize("hasAnyRole('STUDENT') or hasAnyRole('TEACHER') or hasAnyRole('ADMIN')")
	@PostMapping("/uploadFile")
	public Map upload(HttpServletRequest request, @RequestParam("path") String path) {
		Map<String, Object> json = new HashMap<>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		if (fileMap == null || fileMap.size() == 0) {
			json.put("code", 0);
			json.put("msg", "请选择文件!");
			return json;
		}
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			json.put("data", null);
			json.put("code", 0);
			json.put("msg", "身份验证过期，请重新登录");
			json.put("count", 0);
		} else {
			User user = (User) a;
			List<GrantedAuthority> grantedAuthorities = user.getAuthorities();
			Integer id = user.getId();
			Collection<MultipartFile> files = fileMap.values();
			if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_TEACHER"))) {
				if ("\\".equals(path) || path.equals(user.getUsername() + "_" + user.getRealname() + "\\")) {
					path = user.getUsername() + "_" + user.getRealname() + File.separator;
				} else {
					path = user.getUsername() + "_" + user.getRealname() + File.separator + path;
				}
			} else if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
				StringBuffer current = StudentController.current;
				if ("\\".equals(path) || "/".equals(path) || path == null) {
					path = current.toString();
				} else if ("".equals(current.toString())) {
					path = File.separator + "test";
				} else {
					path = current + path;
				}
			}
			int res = fileService.addFiles(files, path, id);
			if (res == fileMap.size()) {
				json.put("code", 1);
				json.put("msg", "上传成功！");
			} else if (res == 0) {
				json.put("code", 2);
				json.put("msg", "覆盖操作！");
			} else {
				json.put("code", -1);
				json.put("msg", "上传失败！");
			}
		}
		return json;
	}

	/**
	 * 创建文件夹
	 */
	@PostMapping("/newFile")
	public Map newFile(@RequestParam String name, @RequestParam String path) {
		Map<String, Object> json = new HashMap<>();
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			json.put("data", null);
			json.put("code", 0);
			json.put("msg", "身份验证过期，请重新登录");
			json.put("count", 0);
		} else {
			User user = (User) a;
			List<GrantedAuthority> grantedAuthorities = user.getAuthorities();
			if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_TEACHER"))) {
				if ("\\".equals(path) || path.equals(user.getUsername() + "_" + user.getRealname() + "\\")) {
					path = user.getUsername() + "_" + user.getRealname() + File.separator;
				} else {
					path = user.getUsername() + "_" + user.getRealname() + File.separator + path;
				}
			}
			int res = fileService.addDirectory(name, path, user.getId());
			if (res == 1) {
				json.put("code", 1);
				json.put("msg", "创建成功！");
			} else {
				json.put("code", 0);
				json.put("msg", "创建失败！");
			}
		}
		return json;
	}

	/**
	 * 删除文件
	 */
	@PostMapping("/delFile")
	public Map delFile(@RequestParam(value = "name[]") String[] name, @RequestParam String path) {
		Map<String, Object> json = new HashMap<>();
		HashMap<String, Object> param = new HashMap<>();
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			json.put("data", null);
			json.put("code", 0);
			json.put("msg", "身份验证过期，请重新登录");
			json.put("count", 0);
		} else {
			User user = (User) a;
			List<GrantedAuthority> grantedAuthorities = user.getAuthorities();
			if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_TEACHER"))) {
				if ("\\".equals(path) || path.equals(user.getUsername() + "_" + user.getRealname() + "\\")) {
					path = user.getUsername() + "_" + user.getRealname() + File.separator;
				} else {
					path = user.getUsername() + "_" + user.getRealname() + File.separator + path;
				}
			}
			param.put("fileName", name);
			param.put("path", path);
			int res = fileService.deleteFile(param);
			if (res == 1) {
				json.put("code", 1);
				json.put("msg", "创建成功！");
			} else {
				json.put("code", 0);
				json.put("msg", "创建失败！");
			}
		}
		return json;
	}


	/**
	 * 文件下载
	 */
	@GetMapping("/downloadFile")
	public ResponseEntity<byte[]> download(HttpServletRequest request
			, @RequestParam String name
			, @RequestParam String path) throws IOException {
		String real;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities();
		if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			if ("\\".equals(path) || "/".equals(path)) {
				real = OtherConstant.REALPATH;
			} else {
				real = OtherConstant.REALPATH + File.separator + path;
			}
		} else {
			real = OtherConstant.REALPATH + File.separator + user.getUsername() + "_" + user.getRealname() + File.separator + path;
		}
		java.io.File file = new java.io.File(real, name);
		HttpHeaders headers = new HttpHeaders();
		String downloadFileName = null;
		downloadFileName = new String(name.getBytes("UTF-8"), "iso-8859-1");
		headers.setContentDispositionFormData("attachment", downloadFileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}


	/**
	 * 作业查重 接口
	 */
	@PostMapping("/checkFile")
	public Map checkFile(@RequestParam(value = "name[]") String[] name, @RequestParam String path) {
		HashMap<String, Object> resMap = new HashMap<>();
		Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(a.toString())) {
			resMap.put("data", null);
			resMap.put("code", 0);
			resMap.put("msg", "身份验证过期，请重新登录");
			resMap.put("count", 0);
		} else {
			User user = (User) a;
			List<GrantedAuthority> grantedAuthorities = user.getAuthorities();
			// 调用查重service
			List<LayuiDtree> resFiles = fileService.checkMethod(
					name, user.getUsername() + "_" + user.getRealname() + File.separator + path);
			resMap.put("code", 0);
			resMap.put("msg", "查重成功");
			resMap.put("count", resFiles.size());
			resMap.put("data", resFiles);

			log.warn(resFiles.toString());
		}
		return resMap;
	}

	/**
	 * office查看：word转pdf
	 * 参考：https://www.cnblogs.com/ph7seven/p/10158489.html
	 */
	@RequestMapping("/doc2pdf")
	public void doc2pdf(@RequestParam String name
			, @RequestParam String path
			, HttpServletResponse response){
		StringBuffer fileName;
		path = path.replaceAll(OtherConstant.NOT_SEPARATOR, OtherConstant.SEPARATOR);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities();
		if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			if ("\\".equals(path) || "/".equals(path)) {
				fileName = new StringBuffer(OtherConstant.REALPATH + File.separator + name);
			} else {
				fileName = new StringBuffer(OtherConstant.REALPATH + File.separator + path + name);
			}
		} else {
			fileName = new StringBuffer(OtherConstant.REALPATH + File.separator + user.getUsername() + "_" + user.getRealname() + File.separator + path + name);
		}

		java.io.File pdfFile = null;
		OutputStream outputStream = null;
		BufferedInputStream bufferedInputStream = null;

		Doc2PdfUtil doc2PdfUtil = new Doc2PdfUtil(openOfficeHost, openOfficePort);

		try {
			//doc转pdf，返回pdf文件
			pdfFile = doc2PdfUtil.doc2Pdf(fileName,name);
			response.setContentType("application/pdf;charset=UTF-8");
			bufferedInputStream = new BufferedInputStream(new FileInputStream(pdfFile));
			byte[] buffBytes = new byte[1024];
			outputStream = response.getOutputStream();
			int read = 0;
			while ((read = bufferedInputStream.read(buffBytes)) != -1) {
				outputStream.write(buffBytes, 0, read);
			}
		} catch (ConnectException e) {
			log.info("****调用Doc2PdfUtil doc转pdf失败****");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if(outputStream != null){
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bufferedInputStream != null){
				try {
					bufferedInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 重命名文件
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
		} else {
			User user = (User) a;
			List<GrantedAuthority> grantedAuthorities = user.getAuthorities();
			path = user.getUsername() + "_" + user.getRealname() + File.separator + path;
			int index = path.lastIndexOf(File.separator);
			String tmpPath;
			if (index == path.indexOf(File.separator) + 1) {
				tmpPath = path.substring(0, index);
			} else {
				tmpPath = path.substring(0, index) + File.separator;
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
			} else {
				json.put("code", 0);
				json.put("msg", "创建失败！");
			}
		}
		return json;
	}

}
