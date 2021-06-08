/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.util;

import lombok.Data;

import java.util.List;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/24 14:27
 * @description 分页bean
 */
@Data
public class PageBean<T> {
	private Integer totalRecord;
	private Integer totalPage;
	private Integer currentPage;
	private Integer pageSize;
	private List<T> list;
}
