layui.define(['form', 'upload'], function(exports){
  var $ = layui.$
  ,layer = layui.layer
  ,laytpl = layui.laytpl
  ,setter = layui.setter
  ,view = layui.view
  ,admin = layui.admin
  ,form = layui.form
  ,upload = layui.upload;

  var $body = $('body');
  
  //设置密码
  form.on('submit(setmypass)', function(obj){
    $.ajax({
       url: '/sys/users/setPassword'
      ,data: obj.field
      ,success: function(data){
    	  layer.msg(data.msg);
      }
    });
    return false;
  });
  
  //对外暴露的接口
  exports('setPassword', {});
});