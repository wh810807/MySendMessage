package com.wuheng.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.wuheng.pojo.JSONResult;

@RestController
@RequestMapping("/send")
public class sendMessageController {
	
	@RequestMapping("/sendMessage")
	@ResponseBody
	public Object sendMessage(HttpServletRequest httpServletRequest,String phoneNumber) {
		try {
			//生成6位验证码
			String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
			//设置超时时间(不必修改)
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			//(不必修改)
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");
			//初始化ascClient，("***"分别填写自己的AccessKey ID和Secret)
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4Fgcs6aaFQ8tkMA5a6hm", "erIaLF6d5oCqoFp5fXhHavEEKmPvEn");
			//(不必修改)
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
			//(不必修改)
			IAcsClient acsClient = new DefaultAcsClient(profile);
			//组装请求对象(不必修改)
			SendSmsRequest request = new SendSmsRequest();
			//****处填写接收方的手机号码
			request.setPhoneNumbers(phoneNumber);
			//****填写已申请的短信签名
			request.setSignName("CRM系统");
			//****填写获得的短信模版CODE
			request.setTemplateCode("SMS_173342087");
			//笔者的短信模版中有${code}, 因此此处对应填写验证码
			request.setTemplateParam("{\"code\":\""+verifyCode+"\"}");
			//不必修改
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

			//将生成的验证码和创建时间放到session中，后面验证从session中取
			HttpSession session = httpServletRequest.getSession();
			session.setAttribute("verifyCode",verifyCode);
			session.setAttribute("verifyCodeCreateTime",System.currentTimeMillis());
			System.out.println("verifyCode:"+verifyCode);
			return "success";

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@RequestMapping(value = "/login",method = RequestMethod.POST)
	@ResponseBody
	public Object login(@RequestParam("verifyCode") String fverifyCode, HttpSession session) {
		
		JSONResult jsonResult=new JSONResult();
		
		System.out.println("session:"+session.getAttribute("verifyCode"));
		System.out.println("fverifyCode:"+fverifyCode);
		System.out.println("flag:"+fverifyCode.equals(session.getAttribute("verifyCode")));
		
		if(session.getAttribute("verifyCodeCreateTime")!=null && (System.currentTimeMillis()-Long.valueOf(String.valueOf(session.getAttribute("verifyCodeCreateTime"))))>1000*60) {
			session.removeAttribute("verifyCode");
			session.removeAttribute("verifyCodeCreateTime");
			jsonResult.setData("验证码过期");
		}else if(session.getAttribute("verifyCode") != null && fverifyCode.equals(session.getAttribute("verifyCode"))) {
			session.removeAttribute("verifyCode");
			session.removeAttribute("verifyCodeCreateTime");
			jsonResult.setData("登录成功");
		}else {
			jsonResult.setData("验证码错误");
		}
		System.out.println(jsonResult);
		
		return jsonResult;
	}
	
}
