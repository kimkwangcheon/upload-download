<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upload Test Page</title>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script>
    function ajaxUpload(inputId, formId, beUrl) {
        let upFile = $(inputId)[0];
        if (upFile) {
            upFile.addEventListener('change', function() {
                let form = $(formId)[0];
                let frmData = new FormData(form);

                $.ajax({
                    enctype: 'multipart/form-data',
                    type: 'POST',
                    url: beUrl,
                    processData: false,
                    contentType: false,
                    cache: false,
                    data: frmData,
                    success: function(data) {
                        console.log(data);
                    },
                    error: function(xhr, status, error) {
                        console.log("Status: " + status);
                        console.log("Error: " + error);
                        console.log("Response: " + xhr.responseText);
                        alert('엑셀파일 업로드 실패');
                    }
                });
            });
        } else {
            console.error('upFile 요소를 찾을 수 없습니다.');
        }
    }

    $(document).ready(function() {
        ajaxUpload('#upFile', '#uploadForm', '/upload');
    });
    </script>
</head>
<body>
    <h1>엑셀 대용량 업로드 테스트 페이지입니다</h1>
    <div style="border: 1px solid #000; padding: 10px; width: 380px;">
        <p><b>엑셀 - 하나의 sheet당 추가할 수 있는 최대 행수</b></p>
        <ul>
            <li>확장자 xls &nbsp;: &ensp;&emsp;65,536 행</li>
            <li>확장자 xlsx : 1,048,576 행</li>
        </ul>
        <br>
        <p><b>대용량 엑셀 파일을 선택해주세욥 (H2 DB에 INSERT됨)</b></p>
    </div>
    <div style="border: 1px solid #000; padding: 10px; width: 380px;">
        <p><b>[XSSF] upload</b>: H2 DB - 일반 foreach문</p>
        <form id="uploadForm" method="post" action="/upload" enctype="multipart/form-data">
            <p><input type="file" name="upFile" id="upFile" accept=".xlsx" /></p>
        </form>
    </div>
</body>
</html>