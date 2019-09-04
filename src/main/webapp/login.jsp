<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript" src="js/jquery-1.8.3.js"></script>
<style type="text/css">
td {
	padding: 10px;
	text-align: left;
}
</style>
<script>
/**
 *点击获取验证码按钮后将手机号传到后台获取验证码
 */
function getVerifyCode() {
    var phoneNumber = $("#fphoneNumber");
    if(phoneNumber.val()==""){
    	alert("手机号码不能为空,请输入手机号码 ");
    }else{
    	$.post("send/sendMessage",{"phoneNumber":phoneNumber.val()},function(data){
    		
    		if(data == 'fail'){
                alert("发送验证码失败");
                return ;
            }else{
                alert("验证码发送成功")
            }
    		
    	},"json")
    	
    }
}


/**
*输入验证码后提交到后台是否输入合法
*/

</script>
<script type="text/javascript">
	function dologin(){
		
	 	var fverifyCode = $("#fverifyCode");
	 	
	 	if(fverifyCode.val()==""){
	 		alert("验证码不能为空");
	 	}else{
	 		
	 		$.post("send/login?verifyCode="+fverifyCode.val(),{},function(obj){
	 			if(obj.data=='登录成功'){
	 				window.location.href="index.jsp";
	 			}else{
	 				window.location.href="login.jsp";
	 			}
	 		},"json") 
	 		
	 	}
	   
	
	}
</script>


</head>
<body>

	<table align="center" border="1px solid gray" cellspacing="0">
		<tr>
			<td><input type="text" value="18672829892" id="fphoneNumber"
				name="phoneNumber" placeholder="请输入手机号" style="margin-top: 10px;">
				<span></span></td>
		</tr>
		<tr>
			<td><input type="text" id="fverifyCode" value=""
				placeholder="请输入验证码" name="verifyCode">
				<button type="button" onclick="getVerifyCode()">获取</button></td>
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">
				<button type="button" onclick="dologin()">登录</button>
			</td>
		</tr>

	</table>

</body>
</html>