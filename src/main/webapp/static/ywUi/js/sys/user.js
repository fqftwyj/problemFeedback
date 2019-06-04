layui.define(['table', 'form'], function (exports) {
    var $ = layui.$
        , table = layui.table
        , form = layui.form;

    //用户表
    table.render({
        elem: '#LAY-user-table'
        , url: 'list' //模拟接口
        , cols: [[
            {type: 'checkbox', fixed: 'left'}
            ,{title: '序号', width: '5%', templet: '#indexTpl'}
            , {field: 'userName', title: '用户名'}
            , {field: 'status', title: '用户状态', templet: "#table-user-status"}
            , {title: '操作', width: 300, align: 'center', fixed: 'right', toolbar: '#table-user-operation'}
/*            , {field: 'createdTime', title: '创建时间'}
            , {field: 'updatedTime', title: '更新时间'}*/
        ]]
        , page: true
        , limit: 30
        , height: 'full-220'
        , text: {
            none: '暂无数据'
        }
    });

    var roleIdsStr = "";
    //监听工具条
    table.on('tool(LAY-user-table)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('真的删除行吗?', function (index) {
                $.ajax({
                    url: 'delete?ids=' + obj.data.id,
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
                , title: '编辑'
                , content: 'edit?id=' + obj.data.id
                , maxmin: true
                , area: ['420px', '240px']
                , btn: ['确定', '取消']
                , yes: function (index, layero) {
                    var iframeWindow = window['layui-layer-iframe' + index]
                        , submitID = 'LAY-user-submit'
                        , submit = layero.find('iframe').contents().find('#' + submitID);
                    roleIdsStr = "";
                    layero.find('iframe').contents().find('.xm-select-this').each(function (e) {
                        roleIdsStr = roleIdsStr + "," + $(this).attr('lay-value');
                    })
                    //监听提交
                    iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                        var field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        $.ajax({
                            url: 'update?roleIdsStr=' + roleIdsStr.substring(1),
                            data: field,
                            type: "post",
                            success: function (e) {
                                if (e.code == 0) {
                                    table.reload('LAY-user-table');
                                    layer.msg(e.msg);
                                    layer.close(index); //关闭弹层
                                } else {
                                    layer.msg(e.msg);
                                }
                            }
                        });
                    });

                    submit.trigger('click');
                }
                , success: function (layero, index) {

                }
            });
        }
        if (obj.event === 'reset') {
            layer.confirm('重置默认密码为123456,确定重置吗？', function (index) {
                $.ajax({
                    url: 'resetPassword?id=' + obj.data.id,
                    success: function (e) {
                        if (e.code == 0) {
                            layer.msg(e.msg);
                            layer.close(index);
                        } else {
                            layer.msg(e.msg);
                        }
                    }
                });

            });
        }
        if (obj.event === 'enable') {
            var userId = obj.data.id;
            //提交 Ajax 成功后，静态更新表格中的数据
            $.ajax({
                url: 'updateStatus',
                data: {"userId": userId, "status": "ENABLED"},
                type: "post",
                success: function (e) {
                    if (e.code == 0) {
                        table.reload('LAY-user-table');
                        layer.msg("启用成功");
                    } else {
                        layer.msg("启用失败");
                    }
                }
            });
        }
        if (obj.event === 'disable') {
            var userId = obj.data.id;
            //提交 Ajax 成功后，静态更新表格中的数据
            $.ajax({
                url: 'updateStatus',
                data: {"userId": userId, "status": "DISABLED"},
                type: "post",
                success: function (e) {
                    if (e.code == 0) {
                        table.reload('LAY-user-table');
                        layer.msg("禁用成功");
                    } else {
                        layer.msg("禁用失败");
                    }
                }
            });
        }
        if (obj.event === 'distribute') {
            var tr = $(obj.tr);
            layer.open({
                type: 2
                , title: '角色分配'
                , content: 'distribute?id=' + obj.data.id
                , maxmin: true
                , area: ['400px', '250px']
                , btn: ['确定', '取消']
                , yes: function (index, layero) {
                    var iframeWindow = window['layui-layer-iframe' + index]
                        , submitID = 'LAY-distribute-submit'
                        , submit = layero.find('iframe').contents().find('#' + submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                        var roleIdsStr = '';
                        //获取当前选择的权限项
                        var roleIdsObj = iframeWindow.getRoleIds();
                        roleIdsObj.each(function (i) {
                            roleIdsStr += roleIdsObj[i].value + ',';
                        })
                        if (roleIdsStr != '') {
                            roleIdsStr = roleIdsStr.substring(0, roleIdsStr.length - 1);
                        }
                        var userId = obj.data.id;
                        //提交 Ajax 成功后，静态更新表格中的数据
                        $.ajax({
                            url: 'saveRoleDistribute',
                            data: {"userId": userId, "roleIdsStr": roleIdsStr},
                            type: "post",
                            success: function (e) {
                                if (e.code == 0) {
                                    table.reload('LAY-user-table');
                                    layer.msg(e.msg);
                                    layer.close(index); //关闭弹层
                                } else {
                                    layer.msg(e.msg);
                                }
                            }
                        });
                    });

                    submit.trigger('click');
                }
                , success: function (layero, index) {

                }
            });
        }
    });

    exports('user', {})
});


