/**
 @Name：公共自定义表单验证
 @Author：陈荣俊
 */
layui.define('form', function (exports) {
  var form = layui.form;

  //自定义验证
  form.verify({
    //验证格式为正整数
    int: function (value, item) { //value：表单的值、item：表单的DOM对象
      if (!(/^[1-9]\d*$/.test(value) || value == '')) {
        return '必须是正整数';
      }
    },
    //验证格式为手机或者座机
    telphone: function (value, item) {
      if (!(/^1(3|4|5|7|8)\d{9}$/.test(value) || /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(value) || value == '')) {
        return '请输入正确的联系电话';
      }
    },
    //验证金额
    money:function(value, item){
      if (!(/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/.test(value) || value == '')) {
        return '请输入正确的金额，最多保留两位小数';
      }


    },selectcheck : function(value, item) {
        if (value == '') {
          return '请输入';
        }
      }
    });

  //对外暴露的接口
  exports('verfy', {});
});