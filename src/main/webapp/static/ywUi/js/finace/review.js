layui.define(['table', 'form'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form;
  
  //
  table.render({
    elem: '#LAY-review-table'
    ,url: 'list' //模拟接口
    ,cols: [[
      {field: 'reviewOpinion', title: '审查意见'}
      ,{field: 'reviewState', title: '审查类别'}
      ,{field: 'createTime', title: '创建时间'}
      ,{field: 'updateTime', title: '更新时间'}
      ,{title: '操作', width: 200, align:'center', fixed: 'right', toolbar: '#table-review-operation'}
    ]]
    ,page: true
    ,limit: 30
    ,height: 'full-220'
    ,text: {
    	none: '暂无数据！'
    }
  });
  
  //监听工具条
  table.on('tool(LAY-review-table)', function(obj){
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
          ,submitID = 'LAY-review-submit'
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
		        		table.reload('LAY-review-table');
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

  exports('review', {})
});


