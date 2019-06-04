layui.define(['table', 'form'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form;
  
  //
    var type=$("#type").val();
    var colsArr;
    if(type==1){
        colsArr= [[
            {title: '序号', width: 80,templet: '#indexTpl'},
            {field: 'staffCode', title: '工号'}
            ,{field: 'reimburseType', title: '报销类别', templet: '#table-review-reimburseType'}
            ,{field: 'reimburseMembers', title: '报销成员'}
            ,{field: 'reimburseDate', title: '报销日期'}
            ,{title: '报销详情', width: 200, align:'center', fixed: 'right', toolbar: '#table-reimburse-detail-operation'}
            ,{title: '操作', width: 200, align:'center', fixed: 'right', toolbar: '#table-review-operation'}
        ]]
    }else{
        colsArr= [[
            {title: '序号', width: 80,templet: '#indexTpl'},
            {field: 'staffCode', title: '工号'}
            ,{field: 'reimburseType', title: '报销类别', templet: '#table-review-reimburseType'}
            ,{ title: '审查状态', templet: function (d) {
                        if (d.reviewState=="PASSREVIEW") {
                            return '<span style="color:#58AB58;font-weight: bold;">  审查通过</span>';
                        } else if ( d.reviewState=="NOTPASSREVIEW") {
                            return '<span style="color: #C14E4E;font-weight: bold;">  审查不通过</span>';
                        }

                }}
            ,{field: 'reimburseMembers', title: '报销成员'}
            ,{field: 'reimburseDate', title: '报销日期'}
            ,{title: '报销详情', width: 200, align:'center', fixed: 'right', toolbar: '#table-reimburse-detail-operation'}
            ,{title: '操作', width: 200, align:'center', fixed: 'right', toolbar: '#table-review-operation'}
        ]]
    }
  table.render({
    elem: '#LAY-review-table'
    ,url: 'list?type='+type //模拟接口
    ,cols:colsArr
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
    if(obj.event === 'dowload'){
        window.location = "/finace/reimburse/exportReimburseDetatl?id=" + obj.data.reimburseId;
    } else if(obj.event === 'detail'){
      var tr = $(obj.tr);
      if(type==1){
          layer.open({
              type: 2
              ,title: '报销详情'
              ,content: 'edit?reimburseId='+obj.data.reimburseId+"&id="+obj.data.id+"&type="+type
              , area: ['100%', '100%']
              ,btn: ['确定', '取消']
              ,yes: function(index, layero){
                  var iframeWindow = window['layui-layer-iframe'+ index]
                      ,submitID = 'LAY-review-edit-submit'
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
      }else{
          layer.open({
              type: 2
              ,title: '报销详情'
              ,content: 'edit?reimburseId='+obj.data.reimburseId+"&id="+obj.data.id+"&type="+type
              , area: ['100%', '100%']
              ,success: function(layero, index){

              }
          });
      }

    }
  });

  exports('review', {})
});


