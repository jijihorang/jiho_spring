<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="../includes/header.jsp" %>

<style>
    .uploadResult {
        width: 100%;
        background-color: gray;
    }
    .uploadResult ul {
        display: flex;
        flex-flow: row;
        justify-content: center;
        align-items: center;
    }
    .uploadResult ul li {
        list-style: none;
        padding: 10px;
        align-content: center;
        text-align: center;
    }
    .uploadResult ul li img {
        width: 100px;
    }
    .uploadResult ul li span {
        color: white;
    }

    .bigPictureWrapper {
        position: absolute;
        display: none;
        justify-content: center;
        align-items: center;
        top: 0%;
        width: 100%;
        height: 100%;
        background-color: gray;
        z-index: 100;
        background: rgba(255, 255, 255, 0.5);
    }
    .bigPicture {
        position: relative;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .bigPicture img {
        width: 600px;
    }
</style>

<div class="row">
    <div class="container-fluid">
        <div class="col-lg-12">
            <h1 class="page-header">Board Modify Page</h1>
        </div>
        <!--  /.col-lg-12 -->
    </div>
</div>
<!-- /.row -->

<div class="row">
    <div class="container-fluid">
        <div class="col-lg-12">
            <div class="panel panel-default card mb-4">
                <div class="panel-heading card-header">Board Modify Page</div>
                <!--  /.panel-heading -->
                <div class="panel-body card-body">

                    <form role="form" action="/board/modify" method="post">

                        <input type="hidden" name="pageNum" value="${paging.pageNum}">
                        <input type="hidden" name="amount" value="${paging.amount}">

                        <input type="hidden" name="type" value="${paging.type}">
                        <input type="hidden" name="keyword" value="${paging.keyword}">

                        <div class="form-group">
                            <label>Bno</label>
                            <input class="form-control" name="bno" value="${board.bno}" readonly> <%-- readonly는 읽기 전용이라 수정 x --%>
                        </div>

                        <div class="form-group">
                            <label>Title</label>
                            <input class="form-control" name="title" value="${board.title}"> <%-- 수정헤야 하기 떄문에 readonly 사용 x --%>
                        </div>

                        <div class="form-group">
                            <label>Content</label>
                            <textarea class="form-control" rows="3" name="content">${board.content}</textarea> <%-- 수정헤야 하기 떄문에 readonly 사용 x --%>
                        </div>

                        <div class="form-group">
                            <label>Writer</label>
                            <input class="form-control" name="writer" value="${board.writer}" readonly> <%-- readonly는 읽기 전용이라 수정 x --%>
                        </div>

                        <button type="submit"  data-oper="modify" class="btn btn-warning">Modify</button> <%-- 수정 확정 버튼 --%>
                        <button type="submit"  data-oper="remove" class="btn btn-danger">Remove</button> <%-- 삭제 버튼 --%>
                        <button type="submit"  data-oper="list" class="btn btn-info">List</button> <%-- 리스트로 돌아가는 버튼 --%>
                    </form>

                </div>
                <!--  end panel-body -->
            </div>
            <!--  end panel-body -->
        </div>
        <!-- end panel -->
    </div>
</div>
<!--  /.row -->

<div class="bigPictureWrapper">
    <div class="bigPicture"></div>
</div>
<div class="row">
    <div class="container-fluid">
        <div class="col-lg-12">
            <div class="panel panel-default card mb-4">
                <div class="panel-heading card-header">Files</div>
                <!-- /.panel-heading -->
                <div class="panel-body card-body">
                    <div class="form-group uploadDiv">
                        <input type="file" name="uploadFile" multiple>
                    </div>
                    <div class="uploadResult">
                        <ul></ul>
                    </div>

                </div>
                <!-- end panel-body -->
            </div>
            <!-- end panel -->
        </div>
    </div>
</div>
<!-- /.row -->

<script>
    $(document).ready(function () {

        var bno = ${board.bno};

        $.getJSON("/board/getAttachList", {bno: bno}, function(arr) {
            var str = "";

            $(arr).each(function(i, attach) {
                if(!attach.fileType) {    // 일반 첨부파일일 경우
                    var fileCallPath = encodeURIComponent(attach.uploadPath + "/" + attach.uuid + "_" + attach.fileName);
                    str += "<li data-path='" + attach.uploadPath + "'";
                    str += " data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "'>";
                    str += "	<div>";
                    str += "		<span>" + attach.fileName + "</span>";
                    str += "		<button type='button' data-file=\'" + fileCallPath + "\' data-type='file' " +
                        "class='btn btn-warning btn-circle'>" +
                        "<i class='fa fa-times'></i></button><br>";
                    str += "		<img src='/resources/img/attach.png'>";
                    str += "	</div>";
                    str += "</li>";
                } else {            // 이미지 파일일 경우
                    var fileCallPath = encodeURIComponent(attach.uploadPath + "/s_" +attach.uuid + "_" + attach.fileName);

                    str += "<li data-path='" + attach.uploadPath + "'";
                    str += " data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType+ "'>";
                    str += "	<div>";
                    str += "		<span>" + attach.fileName + "</span>";
                    str += "		<button type='button' data-file=\'" + fileCallPath + "\' data-type='image' " +
                        "class='btn btn-warning btn-circle'>" +
                        "<i class='fa fa-times'></i></button><br>";
                    str += "		<img src='/display?fileName=" + fileCallPath + "'>";
                    str += "	</div>";
                    str += "</li>";
                }
            });
            $(".uploadResult ul").html(str);
        });


        // 확대 이미지 닫기
        $(".bigPictureWrapper").on("click", function () {
            $(".bigPicture").animate({width: '0%', height: '0%'}, 1000);
            setTimeout(function () {
                $(".bigPictureWrapper").hide();
            }, 1000);
        });
    });
</script>

<script>
    $(document).ready(function () {
        var formObj = $("form"); /* form 가져옴 */

        $("button").on("click", function (e) { /* 3개 button type이 submit으로 동작함 */
           e.preventDefault();  /* submit 동작을 막는다는 의미 */

            var operation = $(this).data("oper");

            if(operation === "remove") { /* 삭제 버튼 누를 시 */
                if(confirm("정말 삭제하시겠습니까?")) { /* confirm 창 띄우기 */
                    formObj.attr("action", "/board/remove"); /* /board/remove 이동 */
                } else {
                    return false;
                }
            } else if (operation === "list") { /* 리스트 버튼 누를 시 */
                formObj.attr("action", "/board/list").attr("method", "get"); /* submit 안하고 /board/list 이동 */

                var pageNumTag = $("input[name=pageNum]").clone();
                var amountTag = $("input[name=amount]").clone();

                var typeTag = $("input[name=type]").clone();
                var keywordTag = $("input[name=keyword]").clone();

                formObj.empty();
                formObj.append(pageNumTag);
                formObj.append(amountTag);

                formObj.append(typeTag);
                formObj.append(keywordTag);

            } else if (operation === "modify") {
                var str = "";

                // 첨부파일 있을 경우에만 hidden 태그 추가
                if($(".uploadResult ul li").length > 0) { /* 첨부파일이 없을 경우 */
                    $(".uploadResult ul li").each(function (i, obj) {
                        var jobj = $(obj);

                        str += "<input type='hidden' name='attachList["+i+"].fileName' value='" + jobj.data("filename") + "'>";
                        str += "<input type='hidden' name='attachList["+i+"].uuid' value='" + jobj.data("uuid") + "'>";
                        str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='" + jobj.data("path") + "'>";
                        str += "<input type='hidden' name='attachList["+i+"].fileType' value='" + jobj.data("type") + "'>";
                    });
                    formObj.append(str);
                }
            }
            formObj.submit();
        });

        // 정규표현식으로 업로드를 제한할 확장자 지정
        var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");

        // 파일 크기 제한
        var maxSize = 5242880; // 5MB

        function checkExtension(fileName, fileSize) {
            if (fileSize >= maxSize) {
                alert("파일 사이즈 초과");
                return false;
            }
            if (regex.test(fileName)) {
                alert("해당 종류의 파일은 업로드할 수 없습니다.");
            }
            return true;
        }

        var uploadResult = $(".uploadResult ul");

        // 업로드 리스트를 보여주는 함수
        function showUploadedFile(uploadResultArr) {
            var str = "";

            $(uploadResultArr).each(function(i, obj) {
                if(!obj.image) {    // 일반 첨부파일일 경우
                    var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
                    str += "<li data-path='" + obj.uploadPath + "'";
                    str += " data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.image + "'>";
                    str += "	<div>";
                    str += "		<span>" + obj.fileName + "</span>";
                    str += "		<button type='button' data-file=\'" + fileCallPath + "\' data-type='file' " +
                        "class='btn btn-warning btn-circle'>" +
                        "<i class='fa fa-times'></i></button><br>";
                    str += "		<img src='/resources/img/attach.png'>";
                    str += "	</div>";
                    str += "</li>";
                } else {            // 이미지 파일일 경우
                    var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);

                    str += "<li data-path='" + obj.uploadPath + "'";
                    str += " data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.image + "'>";
                    str += "	<div>";
                    str += "		<span>" + obj.fileName + "</span>";
                    str += "		<button type='button' data-file=\'" + fileCallPath + "\' data-type='image' " +
                        "class='btn btn-warning btn-circle'>" +
                        "<i class='fa fa-times'></i></button><br>";
                    str += "		<img src='/display?fileName=" + fileCallPath + "'>";
                    str += "	</div>";
                    str += "</li>";
                }
            });
            uploadResult.append(str);
        }

        // 첨부 파일 삭제 버튼 클릭 이벤트
        $(".uploadResult").on("click", "button", function () {
            var targetFile = $(this).data("file");   // 썸네일 파일명
            var type = $(this).data("type");         // 파일인지 이미지인지 구분
            var target_li = $(this).closest("li");   // x 버튼에서 가장 가까운 <li> 태그 지정

            if (!confirm("정말 삭제하시겠습니까?")) {
                return false;
            }

            $.ajax({
                url: '/deleteFile',
                data: {fileName: targetFile, type: type},
                dataType: 'text',
                type: 'POST',
                success: function (result) {
                    // 삭제한 파일을 첨부파일 목록에서 제거
                    target_li.remove();
                    alert(result);
                }
            });
        });

        // 파일에 대한 변화 있을 시 이벤트
        $("input[type=file]").change(function () {
            var formData = new FormData();
            var inputFile = $("input[name=uploadFile]");
            var files = inputFile[0].files;
            console.log(files);

            // add fileData to formData
            for (var i = 0; i < files.length; i++) {
                if (!checkExtension(files[i].name, files[i].size)) {
                    return false;
                }
                formData.append("uploadFile", files[i]);
            }

            // processData, contentType는 반드시 FALSE 해야만 파일 전송 가능 !!!!!!!!!!
            $.ajax({
                url: "/uploadAjaxAction", /* 컨트롤러에 등록시켜야 함 */
                processData: false,
                contentType: false,
                data: formData,
                type: "post",
                dataType: "json",
                success: function (result) {
                    showUploadedFile(result);

                    $(".uploadDiv").find("input[type=file]").val("");
                }
            });
        });
    });
</script>

<%@ include file="../includes/footer.jsp" %>