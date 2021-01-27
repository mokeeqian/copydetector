/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/27 19:46
 * @description 作业通知
 */
@Data
public class Inform implements Serializable {

	private Integer id;
	private String title;
	private String publisher;
	private String department;
	private String content;
	private String date;
	private String path;
	private Integer type;

}
