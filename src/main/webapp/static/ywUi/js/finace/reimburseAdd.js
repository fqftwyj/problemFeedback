layui.define(['table', 'form','laydate','upload'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form,
   laydate=layui.laydate,
   upload=layui.upload;
    //常规用法
    var  $snObj= $("input[name='specialName']");
    var val=$("select[name='foundSource']").val();
    if(val=='KEYDISCIPLINES'|| val=='RESEARCHTOPICS' || val=='TALENTTRAIN' ){

        $("#specialNameId").css("display","inline-block");
        $snObj.attr("lay-verify","required");
    }else{
        $snObj.val("");
        $snObj.removeAttr("lay-verify");
        $("#specialNameId").css("display","none");
    }
    //监听下拉框选中事件
    form.on('select(foundSourceSel)', function (data) {

        if(data.value=='KEYDISCIPLINES'|| data.value=='RESEARCHTOPICS' || data.value=='TALENTTRAIN' ){
            $("#specialNameId").css("display","inline-block");
            $snObj.attr("lay-verify","required");
        }else{
            $snObj.val("");
            $snObj.removeAttr("lay-verify");
            $("#specialNameId").css("display","none");
        }
        form.render();

    });

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

            }).on('click', 'i.'+iId, function () {

                //计算车船和出差补贴的总和和其它费用的总和
                var $form=$("#reimburseDivForm");
                //车船费
                var carboatfees=$form.find("input[name='carboatfee']");
                var totalcarboatfee=0.00;
                carboatfees.each(function () {
                    var v=Number($(this).val());
                    totalcarboatfee+=v;
                });
                //出差补贴费
                var travelmoneys=$form.find("input[name='travelmoney']");
                var totaltravelmoneys=0.00;
                travelmoneys.each(function () {
                    var v=Number($(this).val());
                     totaltravelmoneys+=v;

                });
                //其它费用
                var otherfeemoneys=$form.find("input[name='otherfeemoney']");
                var totalotherfeemoney=0.00;
                otherfeemoneys.each(function () {
                    var v=Number($(this).val());
                     totalotherfeemoney+=v;

                });
                var totalCarBoatTravel=(totalcarboatfee+totaltravelmoneys).toFixed(2);
                var totalotherFee=totalotherfeemoney.toFixed(2);
                $("#totalCarBoatTravel").val(totalCarBoatTravel);
                $("#totalotherFee").val(totalotherFee);
                //请再次确认你的合计金额是否正确

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
                    var v=data.value*days;
                    if(!v || isNaN(v)){
                        v="";
                    }
                    $moneyObj.val(v);

                }
            });
            //绑定天数键盘输入或的时候出差补贴自动计算金额
            $(".days").mouseout(function() {
                reimburseFuns.autoCaculate($(this));
            }).mouseleave(function() {
                reimburseFuns.autoCaculate($(this));
            }).blur(function() {
                reimburseFuns.autoCaculate($(this));
            }).keydown(function() {
                reimburseFuns.autoCaculate($(this));
            });

        },autoCaculate:function($obj){
            var $mobj=$obj.parents(".travelStandardMotherDiv");
            var standard=$mobj.find("select[name='travelStandard']").val();
            var days=$obj.val();
            var $moneyObj= $mobj.find("input[name='travelmoney']");
            var v=standard*days;
            if(!v || isNaN(v)){
                v="";
            }
            $moneyObj.val(v);
            $moneyObj.val(v);
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
    //合计绑定
    reimburseFuns.bindConAddDel('totalCaculate');

    var index;//上传loading
    //上传文件

    //组装删除后的文件路径或文件名字字符串
    function afterDelStr(curName,names,nameArr,curPath,paths,pathArr){
        var targetNameArr=[];
        var targetPathArr=[];
        for(j = 0; j < pathArr.length; j++) {
           if(pathArr[j]!=curPath){
               targetPathArr.push(pathArr[j]);
               targetNameArr.push(nameArr[j]);
           }
        }

        return targetPathArr.join(",")+";"+targetNameArr.join(",");
    }


    var funs={delEditFun:function(){
            $(".demo-delete-edit").off("click").on("click",function (data){

                //重组文件路径相关
                var curPath=$(this).attr("txt");
                var paths=  $("#imgInput").val();
                var pathArr=paths.split(",");

                //重组文件名称相关
                var curName=$(this).attr("txtNM");
                var names= $("#imgInputName").val();
                var nameArr=names.split(",");
                var lastPathNameArr=afterDelStr(curName,names,nameArr,curPath,paths,pathArr).split(";");
                var lastPathStr="";
                var lastNameStr="";
                if(lastPathNameArr.length>1){
                     lastPathStr=lastPathNameArr[0];
                     lastNameStr=lastPathNameArr[1];
                }

                var delFilePath=$("#delFilePath").val();
                if(delFilePath){
                    delFilePath+=","+curPath;
                }else{
                    delFilePath=curPath;

                }
                $("#delFilePath").val(delFilePath);
                $("#imgInput").val(lastPathStr);
                $("#imgInputName").val(lastNameStr);
                //删除文件的请求
                $(this).parent().parent().remove();

            });
        }};
       funs.delEditFun();


        //多文件列表示例
        var demoListView = $('#imgList');
        var totalArray = new Array();

    var uploadListIns =upload.render({
            elem: '#upload'
            ,url: '/upload/fileUpload'
             ,accept: 'file' //允许上传的文件类型
            , size: 1024*100        // 最大允许上传的文件大小  单位 KB
            , auto: false //选择文件后不自动上传
            , bindAction: '#startUpload' //指向一个按钮触发上传
            , multiple: true   // 开启多文件上传
             ,data: {
                 module: function(){
                     return $('#module').val();
                 }
             }
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
              /*  layer.load(); //上传loading*/

            }
        /*    , number: 6    //  同时上传文件的最大个数*/
            , choose: function (obj) {
                var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
                var arr = Object.keys(files);
                totalArray = totalArray.concat(arr);
                // 检查上传文件的个数
              /*  if (totalArray.length <= 6) {*/
                    //读取本地文件
                    obj.preview(function (index, file, result) {
                        var tr = $(['<tr id="upload-' + index + '">'
                            , '<td>' + file.name + '</td>'
                            , '<td>' + (file.size / 1014).toFixed(0) + 'kb</td>'
                            , '<td>等待上传</td>'
                            , '<td>'
                            , '<button class="layui-btn demo-reload layui-hide">重传</button>'
                            , '<button class="layui-btn layui-btn-danger demo-delete">删除</button>'
                            , '</td>'
                            , '</tr>'].join(''));
                         console.log(tr)
                        //单个重传
                        tr.find('.demo-reload').on('click', function () {
                            obj.upload(index, file);
                        });

                        //删除
                        tr.find('.demo-delete').on('click', function () {
                            delete files[index]; //删除对应的文件
                            tr.remove();
                            uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                        });

                        demoListView.append(tr);
                    });
              /*  } else {
                    // 超出上传最大文件
                    layer.msg("上传文件最大不超过6个")
                }*/

            }
            , done: function (res, index, upload) {
              /* layer.closeAll('loading'); //关闭loading*/
                console.log("res", res);
                if (res.code == 0) { //上传成功
                    // 上传成功后将图片路径拼接到input中，多个路径用","分割
                    var dat=res.data.split(";");
                    var inputVal =  $("#imgInput").val();
                    var imgInputNameVal =$("#imgInputName").val() ;
                    var valDataPath = "";
                    var valData1Name = "";
                    if (inputVal) {
                        valDataPath = inputVal + "," + dat[0];
                        valData1Name = imgInputNameVal + "," + dat[1];
                    } else {
                        valDataPath =  dat[0];
                        valData1Name = dat[1];
                    }
                    $("#imgInput").val(valDataPath);
                    $("#imgInputName").val(valData1Name);

                    console.log( $("#imgInput").val()+"=="+ $("#imgInputName").val());
                    var tr = demoListView.find('tr#upload-' + index)
                        , tds = tr.children();
                    var module=$('#module').val();
                    tds.eq(0).html('<a  data-name=\"'+dat[1]+'\" data-path=\"'+dat[0]+'\" class=\"downloadA\" data-module=\"'+module+'\" onclick=\"fqdownload(\'/upload/fileDownload?\',this)\">'+dat[1]+'</a>');
                    tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                    tds.eq(3).html('<button class=\"layui-btn layui-btn-danger demo-delete-edit\"   txt=\"'+dat[0]+'\" txtNM=\"'+dat[1]+'\">删除</button>'); //清空操作
                    funs.delEditFun();
                    return delete this.files[index]; //删除文件队列已经上传成功的文件

                }
                this.error(index, upload);
            }
            , error: function (index, upload) {
                var tr = demoListView.find('tr#upload-' + index)
                    , tds = tr.children();
                tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
            }
        });

    //多文件上传结束
    function fqdownload(url,ele){
        var $this=$(ele);
        var name=$this.data("name");
        var path=$this.data("path");
        var module=$this.data("module");
        window.location=url+"fileName="+encodeURIComponent(name)+"&filePath="+encodeURIComponent(path)+"&module="+module;
    }
  exports('reimburseAdd', {})
});


