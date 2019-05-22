layui.define(['table', 'form','laydate'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form,
   laydate=layui.laydate;
    //常规用法


    //鼠标悬停提示特效
    //车船费报销
    $("#carboatInfo").hover(function() {
        openMsg(1);
    }, function() {
        layer.close(subtips);
    });
    //出差标准
    $("#travelStandardInfo").hover(function() {
        openMsg(2);
    }, function() {
        layer.close(subtips);
    });
   //出差人
    $("#travelPeopleInfo").hover(function() {
        openMsg(3);
    }, function() {
        layer.close(subtips);
    });
    //其它费用
    $("#otherfeeInfo").hover(function() {
        openMsg(4);
    }, function() {
        layer.close(subtips);
    });
    function openMsg(type) {
        if(type==1){
            id="carboatInfo";
            tips='车船类别&nbsp; &nbsp; &nbsp; &nbsp;     注意事项<br>'+
                '            汽车 &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;         不含滴滴车、出租车，市内公交车在其他栏填报<br>'+
                '           火车  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;          二等座按实报销，二等软座经院领导批准<br>'+
                '            飞机  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;          经济舱经院领导批准   <br>'+
                '            轮船  &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;            按三等舱位报销<br>';
        }else if(type==2){
            id="travelStandardInfo";
            tips='  伙食费补助标准&nbsp; &nbsp; &nbsp; &nbsp;     说明<br>'+
                '25&nbsp; &nbsp; &nbsp; &nbsp;   1、工作人员离开本市到其他地区实（见）习、到外地挂职锻炼或支援工作(特殊规定除外)等，在外地工作期间的伙食补助费每人每天补助标准为25元（休息天除外）。<br>2、参加本系统和其他有关部门举办的培训学习班，在培训学习期间的伙食补助费每人每天补助标准为25元，按实际培训学习天数计算。<br>3、具有会议性质或伙食非自理的培训学习班不发伙食补助费；培训学习班所在地人员不发伙食补助费。<br>' +
                '' +
                '55 &nbsp; &nbsp; &nbsp; &nbsp;    进修人员培训时间在3个月（含3个月）以下' +
                '的：住宿费补贴30元/天+伙食补助25元/天。<br>' +
                '42.5 &nbsp; &nbsp; &nbsp; &nbsp; 进修人员培训时间在3个月（不含3个月）以上的，超过部分天数的伙食补助按本款上述标准减半发放。住宿费补贴30元/天+伙食补助12.5元/天。<br>';

        }else if(type==3){
             id='travelPeopleInfo';
             tips='数人一起出差使用一张差旅费报销单时，要将每个人的姓名都填上<br>';
        }else if(type==4){
            id='otherfeeInfo';
            tips='  住宿费<br>'+
                ' 出差人员住宿标准170元/人日、340元/人间（住宿条件：标间），两人应住一个标准间，单人出差或男、女出差人员为单数的，其单数人员可一人住一个标准间。<br>出差人员无住宿发票，一律不予报销。<br>有下列情况的人员不能享受出差有关补助：已享受会议、培训伙食者；调干待分配工作者；在常驻地参加各种临时办公室、指挥部、领导小组等机构工作者；已享受讲课补贴、野外津贴、下海津贴和其他作业津贴者。<br>';
        }
        subtips = layer.tips(tips, '#'+id,{tips:[1,'#FF5722'],time: 50000,area: 'auto',maxWidth:500});
    }

    //绑定新增，删除事件
    var $form = $('#reimburseDivForm');
    var reimburseFuns={
        bindConAddDel:function(iId){
            //克隆隐藏的对象
            var $table = $('#'+iId+'MotherHideDiv', $form);
            $( $form).on('click', 'i.'+iId+'Add', function () {
                var $this = $(this);
                var $new = $table.clone();
                $new.find("input.datetime").removeAttr("lay-key");
                var $lastObj=$this.parent().parent();
                $lastObj.append($new);
                //表单重新渲染
                form.render();
                //日期重新渲染
                reimburseFuns.initdate();
                reimburseFuns.bindAutoCaculate();
                $this.parent().parent().find('.'+iId+'MotherDiv:last').css('display','block');
            }).on('click', 'i.'+iId+'Del', function () {
                var $this = $(this);
                $this.parents('.'+iId+'MotherDiv').remove();

            });
        },initdate:function () {
            laydate.render({
                elem: '#reimburse_date',
                type: 'date'
            });
            //同时绑定多个
            lay('.datetime').each(function(){
                laydate.render({
                    elem: this
                        ,type:'date'
                    ,trigger: 'click'
                });
            });

        },bindAutoCaculate:function(){
            //绑定标准下拉的时候出差补贴自动计算金额
            form.on('select(travelStandard)', function(data){
                var $mobj=$(data.elem).parents(".travelStandardMotherDiv");
                var days=$mobj.find("input[name='days']").val();
                var $moneyObj= $mobj.find("input[name='travelmoney']");
                //如果不是正整数的时候结果为“”
                if(days==''){
                    $moneyObj.val("");
                }else{
                    $moneyObj.val(data.value*days=='NAN'?"":data.value*days);
                }
            });
            //绑定天数键盘输入或的时候出差补贴自动计算金额
            $(".days").mouseout(function() {
                var $mobj=$(this).parents(".travelStandardMotherDiv");
                var standard=$mobj.find("select[name='travelStandard']").val();
                var days=$(this).val();
                var $moneyObj= $mobj.find("input[name='travelmoney']");
                $moneyObj.val(standard*days=='NAN'?"":standard*days);
            });

        }
    };
    reimburseFuns.initdate();
    //车船费绑定
    reimburseFuns.bindConAddDel('carboatfee');
    //出差补贴绑定
    reimburseFuns.bindConAddDel('travelStandard');
    //其它费用绑定
    reimburseFuns.bindConAddDel('otherfee');
    //绑定出差补贴自动计
    reimburseFuns.bindAutoCaculate();
    //上传文件
    layui.use('upload', function(){
        var $ = layui.jquery
            ,upload = layui.upload;

        //上传文件
        upload.render({
            elem: '#selectFile'
            ,url: 'finace/reimburse//reimburseFilepload.do'
            ,accept: 'file'
            ,auto: false
            ,bindAction: '#startUploadFile'
            ,size: 1024*40 //限制文件大小，单位 KB
            ,done: function(result){
                if(result.msg != true){
                    layer.msg(result.msg, {icon:1});
                }else if(result.msg == true){
                    layer.msg("上传成功!",{icon:1});
                }else{
                    layer.msg("上传失败!",{icon:1});
                }
            }
        });

    });




  exports('reimburseAdd', {})
});


