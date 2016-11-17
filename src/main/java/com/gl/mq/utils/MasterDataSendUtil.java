package com.gl.mq.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecej.mq.handler.ProducerHandler;
import com.ecej.utils.common.enums.BusinessType;
import com.ecej.utils.common.message.MessageObject;

/**
 * 
 * 创建日期:2016年8月5日 Title: 主数据相关的消息发送 Description：对本文件的详细描述，原则上不能少于50字
 * 
 * @author caowuchao
 * @mender：（文件的修改者，文件创建者之外的人） @version 1.0
 */
public abstract class MasterDataSendUtil {

	public static void send(BusinessType type, Object... params) {

		try {
			JSONArray jsonArray = new JSONArray();
			for (Object obj : params) {
				jsonArray.add(obj);
			}
			MessageObject messageObject = new MessageObject();
			messageObject.setJsonArrays(jsonArray);
			messageObject.setBusinessType(type);
			String jsonString = JSONObject.toJSONString(messageObject);
			ProducerHandler.sendMessageByQueue(jsonString, "common_master_data", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
