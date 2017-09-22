var Tree = {
    createNew: function (url) {
        var tree = {};
        var setting = {
            data: {
                simpleData: {
                    enable: true
                }
            },
            check: {
                enable: true
            },
            callback: {
                onClick: onClick,
                onCheck: onCheck
            }
        };
        tree.init = function () {
            $.get(url, function (data) {
                var zNodes = eval(data);
                var zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                zTreeObj.expandAll(true);
            })
        };
        return tree;
    }
}
function onCheck(event, treeId, treeNode) {
    console.log(treeNode.id + ", " + treeNode.name + "," + treeNode.checked);
    if (treeNode.checked) {
        appendHidden(treeNode.id);
        $(".fileUploadBtton").attr("data-id", treeNode.id);
        $(".spanStation").html(treeNode.name);
    } else {
        removeHidden(treeNode.id);
    }
};
function appendHidden(id) {
    var hiddenString = '<input type="hidden" name="allocation" value="' + id + '">';
    $("#hiddenBox").append(hiddenString);
}
function removeHidden(id) {
    $("#hiddenBox>input").each(function (index, element) {
        if ($(this).val() == id) {
            $(this).remove();
        }
        if (isContains($(this).val(), id)) {
            $(this).remove();
        }
    });
}
function isContains(str, substr) {
    return str.indexOf(substr) >= 0;
}
