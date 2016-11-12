package com.gl.base.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
  *<p>类描述：接口调用返回实体</p>
  * @author 李凯 [peter]
  * @version: v1.0.0.1
  * @param <T>
  * @since JDK1.8
  *<p>创建日期：2016年4月29日 下午4:38:30</p>
  * Copyright 【新智泛能网络科技有限公司】 2016 
  */
 
public class ResultMessage<T> implements Serializable {

	
	 /**
	  * serialVersionUID:TODO（用一句话描述这个变量[bian liang]表示什么）
	  *
	  * @since Ver 1.1
	  */
	 
	private static final long serialVersionUID = 1L;
	private int code;//返回码
	private String message;//返回消息
	private T data;//消息实体
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public ResultMessage(){
		
	}
	public ResultMessage(int code,String message,T data){
		
		this.code = code;
		this.message = message;
		this.data = data;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ResultMessage [code = " + code +",message = "+ message +","
				+ "data = "+ data +"]";
	}
	
	
}
