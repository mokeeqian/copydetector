/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service.impl;

import cn.edu.ahut.copydetector.constant.BasicConstant;
import cn.edu.ahut.copydetector.constant.DatabaseConstant;
import cn.edu.ahut.copydetector.constant.OtherConstant;
import cn.edu.ahut.copydetector.dao.FileDao;
import cn.edu.ahut.copydetector.dao.SimResultDao;
import cn.edu.ahut.copydetector.dao.UserDao;
import cn.edu.ahut.copydetector.entity.*;
import cn.edu.ahut.copydetector.service.FileService;
import cn.edu.ahut.copydetector.util.SimHash;
import cn.edu.ahut.copydetector.util.WordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.IconUIResource;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/27 19:51
 * @description
 */
@Service
@Slf4j
@SuppressWarnings("unchecked")/*防止maven打包时警告*/
public class FileServiceImpl implements FileService {

	private FileDao fileDao;
	private static UserDao userDao;
	private SimResultDao simResultDao;

	public FileServiceImpl(FileDao fileDao, UserDao userDao, SimResultDao simResultDao) {
		this.fileDao = fileDao;
		this.simResultDao = simResultDao;
		FileServiceImpl.userDao = userDao;
	}

	@Override
	public PageBean<File> selectOldSubmitted(Integer submitter, int page, int limit) {
		HashMap<String, Object> pageMap = new HashMap<>(3);
		PageBean<File> pageBean = new PageBean<>();
		pageBean.setCurrentPage(page);
		pageBean.setPageSize(limit);
		Integer count = fileDao.countBySubmitter(submitter);
		pageBean.setTotalRecord(count);
		pageMap.put("start", (page - 1) * limit);
		pageMap.put("limit", pageBean.getPageSize());
		pageMap.put("submitter", submitter);
		List<File> files = fileDao.selectFileBySubmitter(pageMap);
		pageBean.setList(files);
		return pageBean;
	}

	@Override
	public PageBean<File> selectRecent(Integer owner, int page, int limit, String modern) {
		HashMap<String, Object> pageMap = new HashMap<>(3);
		PageBean<File> pageBean = new PageBean<>();
		pageBean.setCurrentPage(page);
		pageBean.setPageSize(limit);
		Integer count = fileDao.countByOwner(owner);
		pageBean.setTotalRecord(count);
		pageMap.put("start", (page - 1) * limit);
		pageMap.put("limit", pageBean.getPageSize());
		pageMap.put("owner", owner);
		if (modern != null) {
			pageMap.put("modern", modern);
		}
		List<File> files = fileDao.selectFileByOwner(pageMap);
		Iterator<File> iterator = files.iterator();
		while (iterator.hasNext()){
			File current = iterator.next();
			if (current.getType().equals(DatabaseConstant.File.DIRECTORY_FILE.getFlag())){
				iterator.remove();
			}
		}
		pageBean.setList(files);
		return pageBean;
	}

	@Override
	public PageBean<Map<String, Object>> showFileList(String path, int page, int limit) {
		List<Map<String, Object>> fileList = new ArrayList<>();
		path = path.replaceAll(OtherConstant.NOT_SEPARATOR, OtherConstant.SEPARATOR);
		//利用nio包的文件相关接口，此处生成一个对应目录下所有文件的Path集合，在for循环中遍历至自定义的结果集PageBean<T>中
		//PageBean<T>是分页实体类，包装了总页数、总记录数和数据集合等信息
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(new java.io.File(OtherConstant.REALPATH).getAbsolutePath() ,path))) {
			//此处的File为自定义实体类，须和io下的File区分，用于查询数据库中，同一目录下所有File记录
			List<File> files = fileDao.checkFiles(new java.io.File(OtherConstant.REALPATH).getAbsolutePath() + java.io.File.separator + path);
			for (Path pathObj : directoryStream) {
				//遍历每个Path对象，同readAttributes()读取基础属性，存进单个Map中
				BasicFileAttributes attrs = Files.readAttributes(pathObj, BasicFileAttributes.class);
				Map<String, Object> fileItem = new HashMap<>(4);
				fileItem.put("name", pathObj.getFileName().toString());
				fileItem.put("date", OtherConstant.DATE_FORMAT.format(new Date(attrs.lastModifiedTime().toMillis())));
				fileItem.put("size", attrs.size());
				fileItem.put("type", attrs.isDirectory() ? "dir" : "file");
//				log.info(fileItem.toString());
				//读取对应数据库中文件记录的status字段，即文件查阅状态
				for (File file : files) {
					if (file.getName().equals(pathObj.getFileName().toString())) {
						User user = userDao.selectUserById(file.getSubmitter());
//						log.info(userDao.selectUserById(file.getSubmitter()).toString());
						fileItem.put("status", file.getStatus());
						if ( user != null ) {
							fileItem.put("submittername", user.getRealname());		// 姓名
							fileItem.put("submitterid", user.getUsername());		// 学号
							fileItem.put("submitter", file.getSubmitter());		// 自增的id
						} else {
							fileItem.put("submittername", "NAN");
							fileItem.put("submitterid", "NAN");
							fileItem.put("submitter", file.getSubmitter());
						}
						break;
					}
				}
//				log.info(fileItem.toString());
				fileList.add(fileItem);
			}
			Collections.sort(fileList, (o1, o2) -> {
				String name1 =(String)o1.get("name");
				String name2= (String)o2.get("name");
				return name1.compareTo(name2);
			});
			int start = (page - 1) * limit;
			int end = Math.min((start + limit), fileList.size());
			List<Map<String, Object>> newFileList = fileList.subList(start, end);
			PageBean<Map<String, Object>> pageBean = new PageBean<>();
			pageBean.setList(newFileList);
			pageBean.setTotalRecord(fileList.size());
			pageBean.setPageSize(limit);
			pageBean.setCurrentPage(page);
			return pageBean;
		} catch (IOException e) {
			log.error("文件遍历失败", e);
			return null;
		}
	}

	@Override
	public int newTeacherFile(String username, String realname, Integer id) {
		File teacherFile = new File();
		/*教师文件夹名字由工号+姓名组成*/
		String teacherRoot = username + "_" + realname;
		teacherFile.setName(teacherRoot);
		teacherFile.setOwner(id);
		teacherFile.setPath(new java.io.File(OtherConstant.REALPATH).getAbsolutePath() + java.io.File.separator);
		teacherFile.setPermission("-rw--rwx-rwx");
		teacherFile.setType(DatabaseConstant.File.DIRECTORY_FILE.getFlag());
		teacherFile.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date()));
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(new java.io.File(OtherConstant.REALPATH).getAbsolutePath(), "/"))) {
			for (Path pathObject : directoryStream) {
				if (pathObject.getFileName().toString().equals(teacherRoot)) {
					log.info("已有同名文件夹:" + pathObject.getFileName().toString());
					return 0;
				}
			}
			Files.createDirectory(Paths.get(new java.io.File(OtherConstant.REALPATH).getAbsolutePath() ,teacherRoot));
			return fileDao.addFile(teacherFile);
		} catch (IOException e) {
			log.error("教师:" + username + "创建文件夹失败!", e);
		}
		return -1;
	}

	@Override
	public int addFiles(Collection<MultipartFile> files, String path, Integer submitter) {
		Iterator<MultipartFile> fileIterator = files.iterator();
		String ownerUsername ;
		path = path.replaceAll(OtherConstant.NOT_SEPARATOR, OtherConstant.SEPARATOR);
		if("\\".equals(path) || "/".equals(path)){
			ownerUsername = "root";
			path = "";
		}else if (!path.matches("^[0-9]{4}_{1}\\S{0,}$")){
			ownerUsername = "root";
		}else {
			ownerUsername = path.substring(0, 4);
		}
		if (files.size() == 1) {
			File preFile = addMethod(fileIterator.next(), new File(), new java.io.File(OtherConstant.REALPATH).getAbsolutePath() + java.io.File.separator + path, ownerUsername, submitter);
			File isExist = fileDao.checkFile(preFile.getName(), preFile.getPath());
			if (isExist == null) {
				return (preFile != null) ? fileDao.addFile(preFile) : -1;
			} else {
				return (preFile != null) ? 0 : -1;
			}

		} else {
			List<File> preFiles = new ArrayList<>();
			while (fileIterator.hasNext()) {
				File current = addMethod(fileIterator.next(), new File(), new java.io.File(OtherConstant.REALPATH).getAbsolutePath() + java.io.File.separator + path, ownerUsername, submitter);
				if (current == null) {
					preFiles.clear();
					break;
				}
				preFiles.add(current);
			}
			return (preFiles.size() != 0) ? fileDao.addFiles(preFiles) : -1;
		}
	}

	/**
	 * 在磁盘添加文件或文件夹，并生成一个File对象
	 */
	private static File addMethod(MultipartFile paramFile, File destFile, String path, String ownerUsername, Integer submitter) {
		String fileName = paramFile.getOriginalFilename();
		path = path.replaceAll(OtherConstant.NOT_SEPARATOR, OtherConstant.SEPARATOR);
		if (fileName != null) {
			java.io.File dest = new java.io.File(path, fileName);
			/*判断是file还是dir,dest对象还没在服务器上,所以用isFile()或isDirectory()都会返回false*/
			if (dest.exists()) {
				log.info("添加覆盖操作");
				boolean res = dest.delete();
				if (!res) {
					log.error("覆盖失败，请检查文件夹是否非空");
					return null;
				}
			}
			destFile.setType(DatabaseConstant.File.WORD_FILE.getFlag());
			destFile.setPermission("-rwx-rwx-r-x");
			destFile.setSubmitter(submitter);
			destFile.setStatus(DatabaseConstant.File.UNCHECKED.getFlag());
			try {
				paramFile.transferTo(dest);
			} catch (IOException e) {
				log.error("添加保存失败", e);
				return null;
			}
		}
		destFile.setName(fileName);
		destFile.setPath(path);
		destFile.setSize(paramFile.getSize());
		destFile.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date()));
		destFile.setOwner(userDao.selectUserByUsername(ownerUsername).getId());
		return destFile;
	}

	@Override
	public Integer addDirectory(String name, String path, Integer owner) {
		Path direction;
		File tempFile = new File();
		path = path.replaceAll(OtherConstant.NOT_SEPARATOR, OtherConstant.SEPARATOR);
		if("\\".equals(path) || "/".equals(path)){
			direction = Paths.get(new java.io.File(OtherConstant.REALPATH).getAbsolutePath(), name);
			tempFile.setPath(new java.io.File(OtherConstant.REALPATH).getAbsolutePath() + java.io.File.separator);
		}else {
			direction = Paths.get(new java.io.File(OtherConstant.REALPATH).getAbsolutePath(), path, name);
			tempFile.setPath(new java.io.File(OtherConstant.REALPATH).getAbsolutePath() + java.io.File.separator + path);
		}
		try {
			Files.createDirectory(direction);
		} catch (IOException e) {
			log.error("创建文件夹失败", e);
			return 0;
		}
		tempFile.setName(name);
		tempFile.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date()));
		tempFile.setOwner(owner);
		tempFile.setType(DatabaseConstant.File.DIRECTORY_FILE.getFlag());
		tempFile.setPermission("-rwx-rwx-rw-");
		return fileDao.addFile(tempFile);
	}

	@Override
	public int updateFiles(HashMap<String, Object> updateParam, String action) {
		if (BasicConstant.FileAction.RENAME.getString().equals(action)) {
			log.info("修改信息如下："+updateParam.toString());
			String path = (String) updateParam.get("resourcePath");			// 源文件的路径
			path = path.replaceAll(OtherConstant.NOT_SEPARATOR, OtherConstant.SEPARATOR);
//			log.info("修改后的path: " + path);
			String oldName = (String) updateParam.get("resourceName");		// 源文件的原名
//			log.info("绝对路径： " + new java.io.File(OtherConstant.REALPATH).getAbsolutePath());
//			Path resource = Paths.get(new java.io.File(OtherConstant.REALPATH).getAbsolutePath(), path, oldName);
			Path resource = Paths.get(path.substring(0,path.length()-1), oldName);
			log.info("resource: " + resource.toString());
			String newName = (String) updateParam.get("newName");			// 源文件的新名字
			try {
				if (Files.exists(resource)) {
					Map<String, Object> param = new HashMap<>();
					Files.move(resource, resource.resolveSibling(newName));
					param.put("name", newName);
					param.put("updateTime", OtherConstant.DATE_FORMAT.format(new Date()));
					param.put("resourcePath", new java.io.File(OtherConstant.REALPATH).getAbsolutePath() + java.io.File.separator + path);
					param.put("resourceName", oldName);
					log.info("if待修改参数: " + param);
					return fileDao.updateFile(param);
				}
			} catch (IOException e) {
				log.error("重命名失败", e);
				return -1;
			}

		}/*else if (BasicConstant.FileAction.MOVE.getString().equals(action)){
            Path newDir = (Path) updateParam.get("newDir");
            if (updateParam.get("resource") instanceof ArrayList<?>){
                List<Path> resources = (ArrayList<Path>) updateParam.get("resource");
                List<File> files = new ArrayList<>();
                for (Path current : resources){
                    String fileName = current.getFileName().toString();
                    File newObject = moveMethod(current, newDir, fileName);
                    if (newObject == null){
                        files.clear();
                        break;
                    }
                    files.add(newObject);
                }
                return (files.size() != 0) ? fileDao.updateFiles(files) : -1;
            }else {
                Path resource = (Path) updateParam.get("resource");
                String fileName = resource.getFileName().toString();
                File newObject = moveMethod(resource, newDir, fileName);
                return (newObject != null) ? fileDao.updateFile(newObject) : -1;
            }

        }*//*else if (BasicConstant.FileAction.CORRECT.getString().equals(action)){
            Integer status = (Integer) updateParam.get("status");
            long time = System.currentTimeMillis();
            FileTime fileTime = FileTime.fromMillis(time);
            if (updateParam.get("resource") instanceof ArrayList<?>){
                List<File> files = new ArrayList<>();
                List<Path> resources = (ArrayList<Path>) updateParam.get("resource");
                for (Path resource : resources){
                    try {
                        Files.setAttribute(resource, "basic:lastModifiedTime", fileTime);
                    }catch (IOException e){
                        log.error("修改更新时间失败",e);
                        files.clear();
                        break;
                    }
                    File file = new File();
                    file.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date(fileTime.toMillis())));
                    file.setStatus(status);
                    files.add(file);
                }
                return (files.size() != 0) ? fileDao.updateFiles(files) : -1;
            }*/
		else {
			long time = System.currentTimeMillis();
			FileTime fileTime = FileTime.fromMillis(time);
			String path = (String) updateParam.get("resourcePath");
			path = path.replaceAll(OtherConstant.NOT_SEPARATOR, OtherConstant.SEPARATOR);
			String name = (String) updateParam.get("resourceName");
			Path resource = Paths.get(new java.io.File(OtherConstant.REALPATH).getAbsolutePath(), path, name);
			Integer status = (Integer) updateParam.get("status");
			try {
				Files.setAttribute(resource, "basic:lastModifiedTime", fileTime);
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
			Map<String, Object> param = new HashMap<>();
			param.put("status", status);
			param.put("updateTime", OtherConstant.DATE_FORMAT.format(new Date()));
			param.put("resourcePath", path);
			param.put("resourceName", name);
			log.info("else待修改参数: " + param);
			return fileDao.updateFile(param);
		}
		return 0;
	}

	/**
	 * 判断是移动还是移动覆盖操作，并生成File对象
	 */
	private static File moveMethod(Path source, Path newDir, String filename) {
		File file = new File();
		//覆盖
		if (Files.exists(Paths.get(newDir.toString(), filename))) {
			try {
				//size
				file.setSize((Long) Files.getAttribute(source, "basic:size"));
			} catch (IOException e) {
				log.error("覆盖操作获取文件大小失败", e);
				return file;
			}
		}
		try {
			if (Files.exists(source)) {
				Path current = Files.move(source, newDir, StandardCopyOption.REPLACE_EXISTING);
				//path，updateTime
				file.setPath(newDir.getParent().toString());
				FileTime ft = (FileTime) Files.getAttribute(current, "basic:lastModifiedTime");
				file.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date(ft.toMillis())));
			}
		} catch (IOException e) {
			log.error("移动失败", e);
			file = null;
		}
		return file;
	}


	@Override
	public int deleteFile(HashMap<String, Object> param) {
		String path = (String) param.get("path");
		path = path.replaceAll(OtherConstant.NOT_SEPARATOR, OtherConstant.SEPARATOR);
		String[] name = (String[]) param.get("fileName");
		Integer delRes = 0;
		if ("\\".equals(path) || "/".equals(path)) {
			path = "";
		}
		for (int i = 0; i < name.length; i++) {
			File current = deleteMethod(new java.io.File(OtherConstant.REALPATH).getAbsolutePath() + java.io.File.separator + path, name[i]);
			delRes += fileDao.deleteFile(current);
		}
		return delRes;
	}

	/**
	 * 删除文件
	 */
	private static File deleteMethod(String path, String fileName) {
		Path current = Paths.get(path, fileName);
		File file = new File();
		try {
			Files.delete(current);
		} catch (IOException e) {
			log.error("删除文件" + fileName + "失败", e);
			return null;
		}
		file.setName(fileName);
		file.setPath(path);
		return file;
	}

	@Override
	public int deleteTeacherFiles(Integer owner) {
		return fileDao.deleteFileByOwner(owner);
	}

	@Override
	public int deleteUnPassedFiles(Integer submitter, Integer status) {
		return fileDao.deleteFilesByStatus(submitter, status);
	}

	/**
		@param names 文件名数组
	 	@param path 文件路径
	 	// TODO: 2021/4/18
	 */
	@Override
	public List<LayuiDtree> checkMethod(String[] names, String path) {
		path = path.replaceAll(OtherConstant.NOT_SEPARATOR, OtherConstant.SEPARATOR);
		ArrayList<File> files = new ArrayList<>();
		ArrayList<SimHash> hashes = new ArrayList<>();
		String pathParam = new java.io.File(OtherConstant.REALPATH).getAbsolutePath()
				+ java.io.File.separator + path;
		for (String name : names) {
			//根据传参的n个文件名和1个路径，分别生成每个文件的签名（sign）和检查状态，入库
			//将生产的simHash加到集合，以便后面求海明距离
			File tmpFile = fileDao.checkFile(name, pathParam);                // 从数据库中查找文件

//			System.out.println(tmpFile.toString());

			try {
				SimHash tmpHash = new SimHash(
						WordUtil.readWord(pathParam + java.io.File.separator + name),
						64);
				String sign = tmpHash.strSimHash;
				tmpFile.setSign(sign);
				tmpFile.setStatus(DatabaseConstant.File.CHECKED.getFlag());
				hashes.add(tmpHash);
				files.add(tmpFile);
			} catch (IOException e) {
				log.error("simHash出错,检查文件是否存在", e);
			}
		}
		//更新sign字段
		Integer updRes = fileDao.updateFiles(files);
		//为每个File对象存放海明距离集合
		for (int i = 0; i < files.size() - 1; i++) {
			List<HaiMingDistance> distanceList = new ArrayList<>();
			String currentSign = files.get(i).getSign().toString();
			for (int j = i + 1; j < files.size(); j++) {
				String compareSign = files.get(j).getSign().toString();
				// 计算海明距离
				int distance = hashes.get(i).getDistance(currentSign, compareSign);
				HaiMingDistance haiMingDistance = new HaiMingDistance();
				haiMingDistance.setFilename(files.get(j).getName());
				haiMingDistance.setDistance(distance);
				distanceList.add(haiMingDistance);
			}
			// 为当前文件设置和其他文件对比 的海明距离
			files.get(i).setDistances(distanceList);
		}
		//新建一个LayuiDtree的结果集,父节点只有高相似->中相似->低相似
		List<LayuiDtree> res = new ArrayList<>();
		LayuiDtree top = new LayuiDtree();
		LayuiDtree mid = new LayuiDtree();
		LayuiDtree low = new LayuiDtree();

		top.setId("1");
		//列表展开属性
		top.setSpread(true);
		mid.setId("2");
		low.setId("3");

		List<LayuiDtree> topChildren = new ArrayList<>();
		List<LayuiDtree> midChildren = new ArrayList<>();
		List<LayuiDtree> lowChildren = new ArrayList<>();

		int topId = 1;
		int midId = 1;
		int lowId = 1;

		List<SimResult> simResultList = simResultDao.findAll();
		for (File current : files) {
			List<HaiMingDistance> distanceList = current.getDistances();
			if (distanceList != null) {
				for (HaiMingDistance distance : distanceList) {
					LayuiDtree childTree = new LayuiDtree();
//					childTree.setTitle(current.getName() + "——" + distance.getFilename());
					User currentUser = userDao.selectUserById(current.getSubmitter());
					File tmpFile = fileDao.checkFile(distance.getFilename(), current.getPath());
					User distanceUser = userDao.selectUserById(tmpFile.getSubmitter());

					// 相似度
					double sim = (1/Math.sqrt(2*Math.PI*0.16))
							* Math.exp((-(Math.pow(0.01*distance.getDistance()-0.01, 2))/(2*0.0459*0.0458)));
					BigDecimal bigDecimal = new BigDecimal(sim*100);
					double simVal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

					childTree.setTitle(currentUser.getUsername() + " " + currentUser.getRealname() +
							"   <-- " + simVal + "%  -->   "
							+ distanceUser.getUsername() + " " + distanceUser.getRealname());

					// 结果入库
					SimResult simResult = new SimResult(currentUser.getRealname(), distanceUser.getRealname() ,simVal);
					if (!simResultList.contains(simResult))
						simResultDao.saveOne(simResult);

					// 海明距离<=3，可认为高相似度
					if (distance.getDistance() <= 3) {
						childTree.setId(String.valueOf(Integer.parseInt(top.getId()) * 1000 + topId));
						topId++;
						topChildren.add(childTree);
					} else if (distance.getDistance() >= 4 && distance.getDistance() <= 6) {
						childTree.setId(String.valueOf(Integer.parseInt(mid.getId()) * 1000 + midId));
						midId++;
						midChildren.add(childTree);
					} else {
						childTree.setId(String.valueOf(Integer.parseInt(low.getId()) * 1000 + lowId));
						lowId++;
						lowChildren.add(childTree);
					}
				}
			}
		}
		top.setTitle("<label style=\"color:#FF5722\">高相似（相似度 ≥ 70%）—— 共"+(topId-1)+"份</label>");
		mid.setTitle("<label style=\"color:#FFB800\">中相似（40% ≤ 相似度 < 70%）—— 共"+(midId-1)+"份</label>");
		low.setTitle("<label style=\"color:#5FB878\">低相似（相似度 < 40%）—— 共"+(lowId-1)+"份</label>");
		top.setChildren(topChildren);
		mid.setChildren(midChildren);
		low.setChildren(lowChildren);
		res.add(top);
		res.add(mid);
		res.add(low);
		return res;
	}
}
