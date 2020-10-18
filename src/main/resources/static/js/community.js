/**
 * 点赞
 */
//js获取项目根路径，如： http://localhost:8887/test
function getRootPath() {
    //获取当前网址，如： http://localhost:8887/test/index
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： /test/index
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8887
    var localhostPaht = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/test
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPaht + projectName);
}

function like(e) {
    var commentId = e.getAttribute("data-id");
    var status = e.getAttribute("data-status");
    var subLike = $("#sub-like-" + commentId);
    var spanLike = $("#span-like-" + commentId);
    $.ajax({
        type: "POST",
        url: getRootPath() + "/like",
        contentType: "application/json",
        data: JSON.stringify({
            "commentId": commentId,
            "status": status
        }),
        success: function (response) {
            if (response.code == 200) {
                // var comment = jQuery.parseJSON(response);
                var retStatus = response.data.likeStatus;
                e.setAttribute("data-status", retStatus);
                if (retStatus == 1) {
                    spanLike.addClass("active");
                } else {
                    spanLike.removeClass("active");
                }
                console.log(response.data.likeCount);
                subLike.text(response.data.likeCount);

            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        var clientId = e.getAttribute("client_id");
                        window.location.href = "https://github.com/login/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + getRootPath() + "/callback&scope=user&state=1";
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

/**
 * 提交回复
 */
function post(e) {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    var clientId = e.getAttribute("client_id");
    comment2target(questionId, 1, content, clientId);
}

function comment2target(targetId, type, content, clientId) {
    if (!content) {
        alert("不能回复空内容～～");
        return;
    }
    $.ajax({
        type: "POST",
        url: getRootPath() + "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.location.href = "https://github.com/login/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + getRootPath() + "/callback&scope=user&state=1";
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    var clientId = e.getAttribute("client_id");
    comment2target(commentId, 2, content, clientId);
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    var spanComment = $("#span-comment-" + id);
    console.log(spanComment);

    //获取一下二级评论展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        spanComment.removeClass("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            //标记二级评论状态
            e.setAttribute("data-collapse", "in");
            spanComment.addClass("active");
        } else {
            $.getJSON(getRootPath() + "/comment/" + id, function (data) {
                console.log(data);
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left",
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body",
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        html: comment.user.name
                    })).append($("<div/>", {
                        html: comment.content
                    })).append($("<div/>", {
                        "class": "menu",
                    })).append($("<span/>", {
                        "class": "pull-right second-date-style",
                        html: moment(comment.gmtCreate).format('YYYY-MM-DD')
                    }));

                    var mediaElement = $("<div/>", {
                        "class": "media",
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });

                //展开二级评论
                comments.addClass("in");
                //标记二级评论状态
                e.setAttribute("data-collapse", "in");
                spanComment.addClass("active");
            });
        }
    }
}

function hideSelectTag() {
    $("#select-tag").hide();
}

function showSelectTag() {
    $("#select-tag").show();
}

function selectTag(e) {
    //定义开关
    var flag = true;
    //页面输入的标签
    var value = e.getAttribute("data-tag");
    //输入框中的标签
    var previous = $("#tag").val();
    //将输入框中的标签按,分割得到标签数组
    var psplits = previous.split(",");
    //循环数组与输入的标签值进行比较
    for (var i = 0; i < psplits.length; i++) {
        if (psplits[i] == value) {
            flag = false;
        }
    }
    //如果没有重复元素的话,再添加
    if (flag) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}

