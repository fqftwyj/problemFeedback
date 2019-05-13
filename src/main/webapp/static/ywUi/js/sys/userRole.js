layui.define(['table', 'form'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form;
  
  //用户角色关系表
  table.render({
    elem: '#LAY-userRole-table'
    ,url: 'list' //模拟接口
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'userId', title: '用户id'}
      ,{field: 'roleId', title: '角色id'}
      ,{title: '操作', width: 200, align:'center', fixed: 'right', toolbar: '#table-userRole-operation'}
    ]]
    ,page: true
    ,limit: 30
    ,height: 'full-220'
    ,text: {
    	none: '暂无数据！'
    }
  });
  
  //监听工具条
  table.on('tool(LAY-userRole-table)', function(obj){
    var data = obj.data;
    if(obj.event === 'del'){
        layer.confirm('真的删除行么', function(index){
          $.ajax({
	        	url:'delete?ids='+obj.data.id,
	        	success: function(e){
	        		if(e.code==0){
	        			layer.msg(e.msg);
	        			obj.del();
	        	        layer.close(index);
	        		}else{
	        			layer.msg(e.msg);
	        		}
	            }
	      });
         
        });
    } else if(obj.event === 'edit'){
      var tr = $(obj.tr);
      layer.open({
        type: 2
        ,title: '编辑'
        ,content: 'edit?id='+obj.data.id
        ,maxmin: true
        ,area: ['420px', '240px']
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
          var iframeWindow = window['layui-layer-iframe'+ index]
          ,submitID = 'LAY-userRole-submit'
          ,submit = layero.find('iframe').contents().find('#'+ submitID);

          //监听提交
          iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
            var field = data.field; //获取提交的字段
            //提交 Ajax 成功后，静态更新表格中的数据
            $.ajax({
	        	url:'update',
	        	data:field,
	        	type: "post",
	        	success: function(e){
	        		if(e.code==0){
		        		table.reload('LAY-userRole-table');
		        		layer.msg(e.msg);
		        		layer.close(index); //关闭弹层
	        		}else{
	        			layer.msg(e.msg);
	        		}
	            }
	        });
          });  
          
          submit.trigger('click');
        }
        ,success: function(layero, index){
          
        }
      });
    }
  });

  exports('userRole', {})
});


