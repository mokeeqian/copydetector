/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/27 19:46
 * @description
 */
@Data
public class LayuiDtree {
	private String id;
	private String title;
	private String field;
	private String href;
	private Boolean checked;
	private Boolean spread;
	private Boolean disabled;
	private List<LayuiDtree> children;
}