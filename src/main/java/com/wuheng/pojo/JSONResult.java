package com.wuheng.pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONResult {
	
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	private Object data;

	private boolean success;
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String toJSONP(String callback) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("success", this.success);
		jsonObject.add("data", new JsonParser().parse(gson.toJson(this.data)));
		return callback + "(" + jsonObject.toString() + ");";
	}
	public String toJSON() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("success", this.success);
		jsonObject.add("data", new JsonParser().parse(gson.toJson(this.data)));
		return jsonObject.toString();
	}

	public static JSONResult create(boolean success, Object data) {
		JSONResult result = new JSONResult();
		result.setData(data);
		result.setSuccess(success);
		return result;
	}
}
