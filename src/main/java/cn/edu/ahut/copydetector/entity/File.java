/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/27 19:47
 * @description 用户文件
 */
@Data
public class File implements Serializable {

	private Integer id;
	/**
	 * 文件名
	 */
	private String name;
	/**
	 * 文件真实物理路径
	 */
	private String path;
	private Long size;
	private String updateTime;
	/**
	 * 文件类型，dir或doc、docx
	 */
	private Integer type;
	private String permission;
	private Integer owner;
	private Integer submitter;
	private Integer status;
	private String sign;
	/**
	 * 海明距离{{"filename":"text.docx"},{"distance":1000001010101}}
	 */
	private List<HaiMingDistance> distances;

}
