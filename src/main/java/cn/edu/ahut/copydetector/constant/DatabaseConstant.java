/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.constant;

public interface DatabaseConstant {
	/**
	 * 系统的角色枚举
	 */
	enum Role implements DatabaseConstant{
		//三个角色
		ROLE_ADMIN("ROLE_ADMIN"),
		ROLE_TEACHER("ROLE_TEACHER"),
		ROLE_STUDENT("ROLE_STUDENT");
		private String role;

		Role(String role) {
			this.role = role;
		}

		public String getRole() {
			return role;
		}
	}

	enum Authority implements DatabaseConstant{
		//角色权限
		MANAGE_USER("MANAGE_USER"),
		MANAGE_FILE("MANAGE_FILE"),
		MANAGE_ROLE("MANAGE_ROLE"),
		BASIC("BASIC");
		private String authority;

		Authority(String authority) {
			this.authority = authority;
		}

		public String getAuthority() {
			return authority;
		}
	}

	enum File implements DatabaseConstant{
		//文件夹和word文档
		DIRECTORY_FILE(0),
		WORD_FILE(1),

		//文档文件的status字段
		UNCHECKED(0),
		PASSED(1),
		CHECKED(2),
		UNPASSED(-1);

		private Integer flag;

		File(Integer flag) {
			this.flag = flag;
		}

		public Integer getFlag() {
			return flag;
		}
	}

	enum Inform implements DatabaseConstant{
		//通知类型
		COMPULSORY(1),		// 必修课
		ELECTIVE(0);		// 选修课

		private Integer type;

		Inform(Integer type) {
			this.type = type;
		}

		public Integer getType() {
			return type;
		}
	}


}
