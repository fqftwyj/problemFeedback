layui.define(['table', 'form'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form;

  table.render({
    elem: '#LAY-reimburse-table'
    , url: 'list' //模拟接口
    , cols: [[
      {type: 'checkbox', fixed: 'left'}
      , {field: 'reimburseType', title: '报销类别', templet: '#table-reimburse-reimburseType'}
      , {field: 'reimburseState', title: '报销状态', templet: function(d){
         if( d.reimburseState=="HASSUBMIT"){
              return  '<span style="color: #58AB58;font-weight: bold;">  已上报</span>'
         }else if(d.reimburseState=="NOTSUBMIT"){
              return '<span style="color: #C14E4E;font-weight: bold;">  未上报</span>'
         }

        }}
      , {field: 'reimburseMembers', title: '报销成员'}
      , {field: 'reviewState', title: '审查状态'}
      , {field: 'reviewOpinion', title: '审查意见'}
      , {field: 'reimburseDate', title: ' 报销日期'}
      , {title: '提交上报', width:100, align: 'center', fixed: 'right', toolbar: '#table-reimburse-submit'}
      , {title: '操作', width: 250, align: 'center', fixed: 'right', toolbar: '#table-reimburse-operation'}
    ]]
    , page: true
    , limit: 30
    , height: 'full-220'
    , text: {
      none: '暂无数据！'
    }, done: function (res, curr, count) { // 隐藏列
      debugger
      if(res.data[curr].review.reviewState=='PASSREVIEW'){
        debugger
        $(".layui-icon-download-circle").css("display", "block");
      }else{
        $(".layui-icon-download-circle").css("display", "none");
      }


    }
  });
  
  //监听工具条
  table.on('tool(LAY-reimburse-table)', function(obj){
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
        ,title: '编辑流程'
        ,content: 'edit?id='+obj.data.id
    /*    ,maxmin: true*/
        ,area: ['100%', '100%']
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
          var iframeWindow = window['layui-layer-iframe'+ index]
          ,submitID = 'LAY-reimburse-submit'
           ,contentId= layero.find('iframe').contents()
          ,submit = layero.find('iframe').contents().find('#'+ submitID);
          //监听提交
          iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
            var field = data.field; //获取提交的字段

              var newData=new Object();
              var dat=data.field;
              newData.officeCode=dat.officeCode;
              newData.reimburseMembers=dat.reimburseMembers;
              newData.reimburseDate=dat.reimburseDate;
              newData.reimburseReason=dat.reimburseReason;
              newData.reimburseType=dat.reimburseType;
              newData.staffCode=dat.staffCode;
              var json_str=assembleFeesData(contentId);
              newData.reimburseItems=json_str;
              var totalCarBoatTravel=dat.totalCarBoatTravel;
              var totalotherFee=dat.totalotherFee;
              //组装合计的数量
              var totalFeeObjArray=new Array();
              var totalFeeObj=new Object();
              totalFeeObj.totalCarBoatTravel=totalCarBoatTravel;
              totalFeeObj.totalotherFee=totalotherFee;
              totalFeeObjArray.push(totalFeeObj);
              var totalFeeStr=JSON.stringify(totalFeeObjArray);
              newData.reimburseCost=totalFeeStr;
              newData.type=1;
              //提交 Ajax 成功后，静态更新表格中的数据
              $.ajax({
                  url:'update',
                  data:newData,
                  type:'POST',
                  success: function(e){
                      if(e.code==0){
                          table.reload('LAY-reimburse-table');
                          layer.msg(e.msg);
                          layer.close(index);  //关闭弹层
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
    }else if(obj.event === 'submit'){
      layer.confirm('确定要上报吗', function(index){
        $.ajax({
          url:'update?id='+obj.data.id+'&type=2',
          type:'POST',
          success: function(e){
            if(e.code==0){
              table.reload('LAY-reimburse-table');
              layer.msg(e.msg);
              layer.close(index);  //关闭弹层
            }else{
              layer.msg(e.msg);
            }
          }
        });
      });
    }
  });

  exports('reimburse', {})
});


