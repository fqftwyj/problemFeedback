 $(function(){ 
//			var explorer = window.navigator.userAgent.toLowerCase() ;
//		    if(explorer.indexOf("chrome")!=-1){ 
//			    /*licenceMsg(0);*/
//            }else{
//                location.href = "index";
//            }
	 if(window.parent.window!=window){
		 window.top.location="login";
	 }
			  
});
var akey = randomString();//随机生成key和iv，更安全。
var aiv = randomString();
function encrypt(data){
	 var key = CryptoJS.enc.Latin1.parse(akey);//不可缺少的一步
	 var iv = CryptoJS.enc.Latin1.parse(aiv);//不可缺少的一步
	 return CryptoJS.AES.encrypt(data,key,{iv:iv,mode:CryptoJS.mode.CBC,padding:CryptoJS.pad.ZeroPadding}).toString();
}
       
function randomString() {
	 var chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
	 var length = chars.length;
	 var pwd = '';
	 for ( var i = 0; i < 16; i++) {
		pwd += chars.charAt(Math.floor(Math.random() * length));
	 }
	 return pwd;
}
//回车提交
document.onkeydown=function(event){
     var e = event || window.event || arguments.callee.caller.arguments[0];
          
     if(e && e.keyCode==13){ // enter 键
          //要做的事情
          checkUser();
     }
};
//登录验证
function checkUser(){
   	var info='${licenceMsg.status}';
   	/*if(info != "2"){
   		licenceMsg(1);
   	}else{*/
	var name =$("#username").val();
    var pwd = $("#password").val();
    if(name == ''){
    	$('.alertMes-tip').show().find('label').text("用户名不能为空");
	}else if(pwd == ''){
		$('.alertMes-tip').show().find('label').text("密码不能为空");
	}else{
		$('.alertMes-tip').hide().find('label').empty();
		//name = encrypt(name);
		//pwd = encrypt(pwd);
	$.ajax({
       	type:"post",
       	url:"dologin",
        data:{'name' : name, 'password' : hex_md5(pwd)},
        success:function(data){
	        if(data.code=="-2"){
	            var errormsg = "，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
	            cdialog.info(errormsg,null,null,true, 'center');
	        }else if(data.code=="0"){
	        	debugger
				sessionStorage.removeItem("cpFlag");
				window.location = "../indexMenu";
			}else{
				$('.alertMes-tip').show().find('label').text(data.msg);
			}
                     		
           }
          });
		}
    	/*}*/
    	
    }
    
	    
	/*******授权提示信息***********/
	//flag==0:刷新登录页，flag==1:执行登录
	function licenceMsg(flag){
		var info='${licenceMsg.status}';
		var timeMsg='';
		if(info!= "2"){
			if(info=="1"){
				timeMsg = "您使用的系统还未经授权，硬件序列号：" + '${licenceMsg.deviceserial}' + "，请与富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}else{
				timeMsg = '${licenceMsg.msgInfo}' + "，硬件序列号：" + '${licenceMsg.deviceserial}' + "，请与富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}
			/* if(info=="4"){
				timeMsg = "授权文件读取错误，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}
			if(info=="5"){
				 timeMsg = "授权文件不存在，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}
			if(info=="7"){
				timeMsg = "当前硬件序列号和导入的授权文件硬件序列号不一致，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}
			if(info=="8"){
				timeMsg = "产品编号不一致，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}
			if(info=="9"){
				timeMsg = "授权绑定模式错误，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}
			if(info=="101"){
				timeMsg = "许可证的使用次数为0，许可证已失败，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}
			if(info=="102"){
				timeMsg = "已到达许可证的累计运行时间，许可证已失败，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}
			if(info=="103"){
				timeMsg = "许可证时间不在有效期内，许可证已失败，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}
			if(info=="104"){
				timeMsg = "系统时间被篡改，许可证已失败，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			}
			if(info=="105"){
				timeMsg = "没有发现许可证文件，请及时和富阳第一人民医院信息科联系。 &nbsp;&nbsp;<a style='color:red;cursor:pointer;' onclick='licenceImport()'>授权导入<a>";
			} */
		}else{
			if(flag == 0){
				var days='${licenceMsg.betweendays}';
				if(parseInt(days) < 30 && parseInt(days) > 0){
					if(parseInt(days) == 1){
						timeMsg = "授权将于当日过期，为保证产品正常使用，请及时和富阳第一人民医院信息科联系。";
					}else{
						timeMsg = "离授权到期还剩"+days+"天，为保证产品正常使用，请及时和富阳第一人民医院信息科联系。";
					}
				}
			}
		}
		if(timeMsg != ""){
			var  msg = timeMsg;
			//var  msgTitle="授权提醒";
			cdialog.info(msg,null,null,true, 'center');
		}
	}
	function licenceImport(){
	   $('.t-dialog-mask').remove();
		cdialog.pop('licence','授权导入',600,'200','center',function(){
			 $('.t-dialog-mask').remove();
		},true,0);
	}