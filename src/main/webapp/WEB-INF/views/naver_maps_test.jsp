<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upload Test</title>
    <style type="text/css">
        /* 네이버지도 관련 css */
        .naver-map {
            width: 50%;
            height: 400px;
            position: relative;
            overflow: hidden;
            background: rgb(248, 249, 250);
        }
        .naver-map .buttons {
            position: absolute;
            top: 0;
            left: 0;
            z-index: 1000;
            padding: 5px;
        }
        .naver-map .buttons .control-btn {
            margin: 0 5px 5px 0;
            padding: 2px 6px;
            background: #fff;
            border: solid 1px #333;
            cursor: pointer;
            -webkit-border-radius: 5px;
            border-radius: 5px;
            box-shadow: 2px 2px 1px 1px rgba(0, 0, 0, 0.5);
        }
        .naver-map .buttons .control-on {
            background-color: #dfdfdf;
            color: #555;
        }
    </style>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <!-- ncpClientId는 등록 환경에 따라 일반(ncpClientId), 공공(govClientId), 금융(finClientId)으로 나뉩니다. 사용하는 환경에 따라 키 이름을 변경하여 사용하세요. 참고: clientId(네이버 개발자 센터)는 지원 종료 -->
    <script type="text/javascript" src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=83bfuniegk"></script>
    <script>
    /****************************************
     * 전역변수
    ****************************************/
    var map = null;

    /****************************************
     * 로딩
    ****************************************/
    // 제이쿼리 로드 후 실행
    $(document).ready(function() {
        initMap();      // 지도 초기화
        initEvent();    // 이벤트 초기화
    });

    /****************************************
     * 초기화
    ****************************************/
    function initMap() {
        var mapOptions = {
            center: new naver.maps.LatLng(37.3595704, 127.105399), // 지도의 초기 중심 좌표
            zoom: 13,               // 지도의 초기 줌 레벨
            minZoom: 7,             // 지도의 최소 줌 레벨
            zoomControl: true,      // 줌 컨트롤의 표시 여부
            zoomControlOptions: {   // 줌 컨트롤의 옵션
                position: naver.maps.Position.TOP_RIGHT
            }
        };

        map = new naver.maps.Map('map', mapOptions);

        // 핀(마커) 꽂기
        new naver.maps.Marker({
            position: map.getCenter(),
            map: map
        });
    }

    /****************************************
     * 이벤트
    ****************************************/
    function initEvent() {
        // 네이버지도 - [옵션] 인터랙션
        $("#interaction").on("click", function(e) {
            e.preventDefault();

            if (map.getOptions("draggable")) {
                map.setOptions({ //지도 인터랙션 끄기
                    draggable: false,
                    pinchZoom: false,
                    scrollWheel: false,
                    keyboardShortcuts: false,
                    disableDoubleTapZoom: true,
                    disableDoubleClickZoom: true,
                    disableTwoFingerTapZoom: true
                });

                $(this).removeClass("control-on");
            } else {
                map.setOptions({ //지도 인터랙션 켜기
                    draggable: true,
                    pinchZoom: true,
                    scrollWheel: true,
                    keyboardShortcuts: true,
                    disableDoubleTapZoom: false,
                    disableDoubleClickZoom: false,
                    disableTwoFingerTapZoom: false
                });

                $(this).addClass("control-on");
            }
        });

        // 네이버지도 - 인증 실패 시 호출되는 함수
        window.navermap_authFailure = function () {
            // 인증 실패 시 처리 코드 작성
            $("#map").html('<div style="text-align: center; line-height: 400px; font-size: 25px; color: #f99;">네이버지도 인증 실패</div>');
        }
    }
    </script>
</head>
<body>
    <h1>네이버지도 테스트 페이지입니다</h1>
    <div class="naver-map" id="map">
        <div class="buttons">
            <input id="interaction" type="button" name="지도 인터렉션" value="지도 인터렉션" class="control-btn control-on">
        </div>
    </div>
</body>
</html>