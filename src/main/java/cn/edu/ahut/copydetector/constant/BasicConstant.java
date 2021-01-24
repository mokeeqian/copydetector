/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.constant;

public interface BasicConstant {

	/**
	 * 用户实体属性
	 */
	enum User implements BasicConstant{
		//实体类的字段
		USERNAME("username"),
		PASSWORD("password"),
		REAL_NAME("realname"),
		DEPARTMENT("department"),
		MAJOR("major"),
		CREATE_TIME("createTime"),
		LAST_LOGIN_TIME("lastLoginTime"),

		ACCOUNT_NON_EXPIRED("accountNonExpired"),
		ACCOUNT_NON_LOCKED("accountNonLocked"),
		CREDENTIALS_NON_EXPIRED("credentialsNonExpired"),
		ENABLED("enabled"),

		AUTHORITIES("authorities");

		private String string;

		User(String string) {
			this.string = string;
		}

		public String getString() {
			return string;
		}
	}

	/**
	 * 文件操作行为
	 */
	enum FileAction implements BasicConstant{
		//重命名
		RENAME("RENAME"),
		//移动
		MOVE("MOVE"),
		//批改
		CORRECT("CORRECT"),
		//复制
		COPY("COPY");

		private String string;

		public String getString() {
			return string;
		}

		FileAction(String string) {
			this.string = string;
		}
	}

}
