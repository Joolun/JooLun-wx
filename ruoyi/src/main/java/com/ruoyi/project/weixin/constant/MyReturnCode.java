package com.ruoyi.project.weixin.constant;

/**
 * 全局返回码
 * 小程序用6开头，例60001
 * @author JL
 * 2019年7月25日
 */
public enum MyReturnCode {

	ERR_60000(60000, "系统错误，请稍候再试"){},//其它错误
	ERR_60001(60001, "登录超时，请重新登录"){},
	;

	MyReturnCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private int code;
	private String msg;

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

	@Override
	public String toString() {
		return "MyReturnCode{" + "code='" + code + '\'' + "msg='" + msg + '\'' + '}';
	}

}
