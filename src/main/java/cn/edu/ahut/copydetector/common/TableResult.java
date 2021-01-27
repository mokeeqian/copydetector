/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.common;

import java.util.List;

/**
 * @author mokeeqian
 * @version 1.0
 * @date 2021/1/26 18:48
 * @description layui数据接口接受的数据结构
 */
public class TableResult<T> {
	private int code;
	private String msg;
	private long count;
	private List<T> data;


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
