layui.define(['table', 'form'], function (exports) {
    var $ = layui.$
        , table = layui.table
        , form = layui.form;

    //角色表
    table.render({
        elem: '#LAY-role-table'
        , url: 'list' //模拟接口
        , cols: [[
            {type: 'checkbox', fixed: 'left'}
            ,{title: '序号', width: '5%', templet: '#indexTpl'}
            , {field: 'name', title: '角色名'}
            , {
                field: 'addPrivileges',
                title: '权限分配',
                width: 200,
                align: 'center',
                fixed: 'right',
                toolbar: '#permission-distribute'
            }
            , {field: 'description', title: '描述'}
            , {title: '操作', width: 200, align: 'center', fixed: 'right', toolbar: '#table-role-operation'}
           /* , {field: 'createdTime', title: '创建时间'}
            , {field: 'updatedTime', title: '更新时间'}*/
        ]]
        , page: true
        , limit: 30
        , height: 'full-220'
        , text: {
            none: '暂无数据！'
        }
    });

    //监听工具条
    table.on('tool(LAY-role-table)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('真的删除行么', function (index) {
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
                        , submitID = 'LAY-role-submit'
                        , submit = layero.find('iframe').contents().find('#' + submitID);

                    //监听提交
                    iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                        var field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        $.ajax({
                            url: 'update',
                            data: field,
                            type: "post",
                            success: function (e) {
                                if (e.code == 0) {
                                    table.reload('LAY-role-table');
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
        //权限分配
        if (obj.event === 'distribute') {
            var tr = $(obj.tr);
            layer.open({
                type: 2
                , title: '权限分配'
                , content: 'distribute?id=' + obj.data.id
                , maxmin: true
                , area: ['400px', '600px']
                , btn: ['确定', '取消']
                , yes: function (index, layero) {
                    var iframeWindow = window['layui-layer-iframe' + index]
                        , submitID = 'LAY-distribute-submit'
                        , submit = layero.find('iframe').contents().find('#' + submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                        var privilegeStr = '';
                        //获取当前选择的权限项
                        var zTree1 = iframeWindow.getTree();
                        var nodes1 = zTree1.getCheckedNodes();

                        for (var i = 0; i < nodes1.length; i++) {
                            privilegeStr += nodes1[i].id + ',';
                        }
                        if (privilegeStr != '') {
                            privilegeStr = privilegeStr.substring(0, privilegeStr.length - 1);
                        }
                        var roleId = obj.data.id;
                        //提交 Ajax 成功后，静态更新表格中的数据
                        $.ajax({
                            url: 'permissionDistribute',
                            data: {"roleId": roleId, "privilegeStr": privilegeStr},
                            type: "post",
                            success: function (e) {
                                if (e.code == 0) {
                                    table.reload('LAY-role-table');
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

    exports('role', {})
});


