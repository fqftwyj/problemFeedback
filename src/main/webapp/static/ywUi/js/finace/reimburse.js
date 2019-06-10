layui.define(['table', 'form'], function (exports) {
    var $ = layui.$
        , table = layui.table
        , form = layui.form;

    table.render({
        elem: '#LAY-reimburse-table'
        , url: 'list' //模拟接口
        , cols: [[
            {title: '序号', width: '5%', templet: '#indexTpl'},
            {field: 'reimburseType', width: '10%', title: '报销类别', templet: '#table-reimburse-reimburseType'}
            , {
                field: 'reimburseState', width:  '10%', title: '报销状态', templet: function (d) {
                    if (d.reimburseState == "HASSUBMIT") {
                        return '<span style="color: #58AB58;font-weight: bold;">  已上报</span>';
                    } else if (d.reimburseState == "NOTSUBMIT") {
                        return '<span style="color: #777;font-weight: bold;">  未上报</span>';
                    } else if (d.reimburseState == "RESUBMIT") {
                        return '<span style="color: #C14E4E;font-weight: bold;">  重新上报</span>';
                    }

                }
            }
            , { title: '报销成员', templet: '#memberTipl'}
            , {
                title: '审查状态', templet: function (d) {
                    if (d.review && d.review.reviewState) {
                        if (d.review.reviewState == "NOTREVIEW") {
                            return '<span style="color: #777;font-weight: bold;">  未审查</span>';
                        } else if (d.review.reviewState == "ISREVIEW") {
                            return '<span style="color: #C19E4E;font-weight: bold;">  审查中</span>';
                        } else if (d.review.reviewState == "PASSREVIEW") {
                            return '<span style="color:#58AB58;font-weight: bold;">  审查通过</span>';
                        } else if (d.review.reviewState == "NOTPASSREVIEW") {
                            return '<span style="color: #C14E4E;font-weight: bold;">  审查不通过</span>';
                        }
                    } else {
                        return '<span style="color: #777;font-weight: bold;">  未审查</span>';
                    }


                }
            }
            , {
                title: '审查意见', templet: '#titleTipl'
            }
            , {
                field: 'reimburseDate', title: ' 报销日期', templet: function (d) {
                    if (d.reimburseDate == "1900-01-01") {
                        return "--";
                    } else {
                        return d.reimburseDate;
                    }

                }
            }
            , {title: '提交上报', width: '8%', align: 'center', fixed: 'right', toolbar: '#table-reimburse-submit'}
            , {title: '操作', width: '20%', align: 'center', fixed: 'right', toolbar: '#table-reimburse-operation'}
        ]]
        , page: true
        , limit: 30
        , height: 'full-220'
        , text: {
            none: '暂无数据！'
        }, done: function (res, curr, count) { // 隐藏列


        }
    });

    //监听工具条
    table.on('tool(LAY-reimburse-table)', function (obj) {
        var data = obj.data;
        var bol = data.reimburseState == "RESUBMIT";
        if (bol) {
            posturl = 'update?id=' + data.id + "&type=3";
        } else {
            posturl = 'delete?ids=' + data.id
        }
        if (obj.event === 'del') {
            layer.confirm('确定要删除此报销申请吗', function (index) {
                $.ajax({
                    url: posturl,
                    success: function (e) {
                        if (e.code == 0) {
                            layer.msg(e.msg);
                            obj.del();
                            layer.close(index);
                        } else {
                            layer.msg(e.msg);
                        }
                    }
                });
            });
        } else if (obj.event === 'edit') {
            var tr = $(obj.tr);
            layer.open({
                type: 2
                , title: '编辑流程'
                , content: 'edit?id=' + obj.data.id
                /*    ,maxmin: true*/
                , area: ['100%', '100%']
                , btn: ['确定', '取消']
                , yes: function (index, layero) {
                    var iframeWindow = window['layui-layer-iframe' + index]
                        , submitID = 'LAY-reimburse-edit-submit'
                        , contentId = layero.find('iframe').contents()
                        , submit = layero.find('iframe').contents().find('#' + submitID);
                    //计算车船和出差补贴的总和和其它费用的总和
                    var $form = contentId.find("#reimburseDivForm");
                    //点提交的时候先计算合计的
                    commomFun.autoCalTotal($form);
                    //监听提交
                    iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                        var field = data.field; //获取提交的字段
                        var newData = new Object();
                        var dat = data.field;
                        newData.id = dat.id;
                        newData.reimburseState = dat.reimburseState;
                        newData.officeCode = dat.officeCode;
                        newData.reimburseMembers = dat.reimburseMembers;
                        newData.reimburseDate = dat.reimburseDate;
                        newData.reimburseReason = dat.reimburseReason;
                        newData.reimburseType = dat.reimburseType;
                        newData.staffCode = dat.staffCode;
                        newData.uploadPath = dat.uploadPath;
                        newData.uploadName = dat.uploadName;
                        var json_str = commomFun.assembleFeesData(contentId);
                        newData.reimburseItems = json_str;
                        var totalCarBoatTravel = dat.totalCarBoatTravel;
                        var totalotherFee = dat.totalotherFee;
                        //组装合计的数量
                        var totalFeeData = [];
                        var totalFeeObj = new Object();
                        totalFeeObj.totalCarBoatTravel = totalCarBoatTravel;
                        totalFeeObj.totalotherFee = totalotherFee;
                        totalFeeData.push(totalFeeObj);
                        var totalFeeStr = JSON.stringify(totalFeeData);
                        newData.reimburseCost = totalFeeStr;
                        newData.type = 1;
                        //提交 Ajax 之前确认合计是否正确
                        layer.confirm('您确认你的合计金额是否正确', {
                            btn: ['是', '否'] //按钮
                        }, function () {
                            $.ajax({
                                url: 'update',
                                data: newData,
                                type: 'POST',
                                success: function (e) {
                                    if (e.code == 0) {
                                        table.reload('LAY-reimburse-table');
                                        layer.msg(e.msg);
                                        layer.close(index);  //关闭弹层
                                    } else {
                                        layer.msg(e.msg);
                                    }
                                }
                            });
                        });

                    });
                    submit.trigger('click');
                }
                , success: function (layero, index) {

                }

            });
        } else if (obj.event === 'submit') {
            layer.confirm('确定要上报吗', function (index) {
                $.ajax({
                    url: 'update?id=' + obj.data.id + '&type=2',
                    type: 'POST',
                    success: function (e) {
                        if (e.code == 0) {
                            table.reload('LAY-reimburse-table');
                            layer.msg(e.msg);
                            layer.close(index);  //关闭弹层
                        } else {
                            layer.msg(e.msg);
                        }
                    }
                });
            });


        } else if (obj.event === 'export') {
            window.location = "/finace/reimburse/exportReimburseDetail?id=" + obj.data.id;

        }
    });
    var commomFun = {
        //提交前计算合计费用
        autoCalTotal: function ($form) {
            //车船费
            var carboatfees = $form.find("input[name='carboatfee']");
            var totalcarboatfee = 0.00;
            carboatfees.each(function () {
                var v = Number($(this).val());
                totalcarboatfee += v;
            });
            //出差补贴费
            var travelmoneys = $form.find("input[name='travelmoney']");
            var totaltravelmoneys = 0.00;
            travelmoneys.each(function () {
                var v = Number($(this).val());
                totaltravelmoneys += v;

            });
            //其它费用
            var otherfeemoneys = $form.find("input[name='otherfeemoney']");
            var totalotherfeemoney = 0.00;
            otherfeemoneys.each(function () {
                var v = Number($(this).val());
                totalotherfeemoney += v;

            });
            var totalCarBoatTravel = (totalcarboatfee + totaltravelmoneys).toFixed(2);
            var totalotherFee = totalotherfeemoney.toFixed(2);
            $form.find("#totalCarBoatTravel").val(totalCarBoatTravel);
            $form.find("#totalotherFee").val(totalotherFee);
        }, assembleFeesData: function (contentId) {  //组装复杂数据
            var itemDataObjData = [];
            var itemDataObj = new Object();
            //车船费
            var carboatfeeiItemsData = [];
            var carboatfeeMotherDivs = contentId.find(".carboatfeeMotherDiv:visible");
            carboatfeeMotherDivs.each(function () {
                var carboatfeeitem = new Object();
                carboatfeeitem.departureTime = $(this).find("input[name='departureTime']").val().trim();
                carboatfeeitem.departurePlace = $(this).find("input[name='departurePlace']").val().trim();
                carboatfeeitem.arrivalTime = $(this).find("input[name='arrivalTime']").val().trim();
                carboatfeeitem.arrivalPlace = $(this).find("input[name='arrivalPlace']").val().trim();
                carboatfeeitem.carboatType = $(this).find("select[name='carboatType']").val();
                carboatfeeitem.docNumber = $(this).find("input[name='docNumber']").val().trim();
                carboatfeeitem.carboatfee = $(this).find("input[name='carboatfee']").val().trim();
                carboatfeeiItemsData.push(carboatfeeitem);
            });

            //出差补贴费
            var travelAllowanceItemsData = [];
            var travelStandardMotherDivs = contentId.find(".travelStandardMotherDiv:visible");
            travelStandardMotherDivs.each(function () {
                var travelAllowanceItem = new Object();
                travelAllowanceItem.days = $(this).find("input[name='days']").val().trim();
                travelAllowanceItem.travelStandard = $(this).find("select[name='travelStandard']").val();
                travelAllowanceItem.travelmoney = $(this).find("input[name='travelmoney']").val().trim();
                travelAllowanceItemsData.push(travelAllowanceItem);
            });

            //其它费用
            var otherFeeItemsData = [];
            var otherfeeMotherDivs = contentId.find(".otherfeeMotherDiv:visible");
            otherfeeMotherDivs.each(function () {
                var otherFeeItem = new Object();
                otherFeeItem.itemname = $(this).find("input[name='itemname']").val().trim();
                otherFeeItem.otherdocnumber = $(this).find("input[name='otherdocnumber']").val().trim();
                otherFeeItem.otherfeemoney = $(this).find("input[name='otherfeemoney']").val().trim();
                otherFeeItemsData.push(otherFeeItem);
            });

            itemDataObj.carboatfeeiItemsData = carboatfeeiItemsData;
            itemDataObj.travelAllowanceItemsData = travelAllowanceItemsData;
            itemDataObj.otherFeeItemsData = otherFeeItemsData;
            itemDataObjData.push(itemDataObj);

            return JSON.stringify(itemDataObjData);
        }

    };
    exports('reimburse', {})
});


