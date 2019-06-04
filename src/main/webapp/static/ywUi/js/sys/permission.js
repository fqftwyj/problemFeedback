layui.define(['table', 'form'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form;
  
  //权限表
  table.render({
    elem: '#LAY-permission-table'
    ,url: 'list' //模拟接口
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{title: '序号', width: '8%', templet: '#indexTpl'}
      ,{field: 'permissionName', title: '权限名'}
      ,{field: 'permissionCode', title: '编号'}
      ,{field: 'permissionType', title: '权限类型',templet: '#table-permission-permissionType'}
      ,{field: 'permissionUrl', title: '权限url'}
      ,{field: 'permissionIcon', title: '权限图标'}
      ,{field: 'description', title: '描述'}
      ,{field: 'sort', title: '排序'}
      ,{title: '操作', width: 150, align:'center', fixed: 'right', toolbar: '#table-permission-operation'}
    ]]
    ,page: true
    ,limit: 30
    ,height: 'full-220'
    ,text: {
    	none: '暂无数据！'
    }
  });
  
  //监听工具条
  table.on('tool(LAY-permission-table)', function(obj){
    var data = obj.data;
    if(obj.event === 'del'){
        layer.confirm('真的删除行吗?', function(index){
          $.ajax({
	        	url:'delete?ids='+obj.data.id,
	        	success: function(e){
	        		if(e.code==0){
	        			layer.msg(e.msg);
	        			obj.del();
	        	        layer.close(index);
		        		if($("#LAY-permission-table").next().find("table").find("tbody tr").length==0){
	                    	zTree.reAsyncChildNodes(zTree.getSelectedNodes()[0],"refresh");
		        			zTree.getSelectedNodes()[0].isParent=false;
		        		}else{
	                    	zTree.reAsyncChildNodes(zTree.getSelectedNodes()[0],"refresh");
	                    }
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
        ,area: ['420px', '500px']
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
          var iframeWindow = window['layui-layer-iframe'+ index]
          ,submitID = 'LAY-permission-submit'
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
		        		table.reload('LAY-permission-table');
		        		layer.msg(e.msg);
		        		layer.close(index); //关闭弹层
                        zTree.reAsyncChildNodes(zTree.getSelectedNodes()[0],"refresh");
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

  exports('permission', {})
});


