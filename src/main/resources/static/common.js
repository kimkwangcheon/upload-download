// onload 
window.onload = function() {
	try{
		documentReady();
	} catch(e) {}
};

// AUIGrid.defaultProps 에 공통적인 속성을 설정합니다.
// AUIGrid.defaultProps 설정은 AUIGrid.js 정의 이후 선언해야 합니다.
// 선언하면 AUIGrid 를 생성 할 때 기본적으로 defaultProps 를 확장하여 생성합니다.
// 따라서 매 페이지의 그리드를 생성할 때 공통적인 사항을 여기서 작성하십시오.
if (typeof window !== 'undefined' && typeof window.AUIGrid !== 'undefined') {
	window.AUIGrid.defaultProps = {
			// 모바일인 경우 자동으로 작은 사이즈의 스크롤을 표시할지 여부
			// 여기서 생성했기 때문에 앞으로 모든 그리드는 autoScrollSize: true 를 상속 받아 적용됨.
			autoScrollSize: true,
	};
}

// 데이터 요청
function requestData(url) {

	// ajax 요청 전 그리드에 로더 표시
	AUIGrid.showAjaxLoader(myGridID);
	
	// ajax (XMLHttpRequest) 로 그리드 데이터 요청
	ajax({
		url : url,
		onSuccess : function(data) {
			
			//console.log(data);
			
			// 그리드에 데이터 세팅
			// data 는 JSON 을 파싱한 Array-Object 입니다.
			AUIGrid.setGridData(myGridID, data);

			// 로더 제거
			AUIGrid.removeAjaxLoader(myGridID);
		},
		onError : function(status, e) {
			alert("데이터 요청에 실패하였습니다.\r\n status : " + status + "\r\nWAS 를 IIS 로 사용하는 경우 json 확장자가 web.config 의 handler 에 등록되었는지 확인하십시오.");
			// 로더 제거
			AUIGrid.removeAjaxLoader(myGridID);
		}
	});
};

function changeLocation(href) {
	location.href = href;
};

// async confirm
function asyncConfirm(text) {
	return new Promise(function (resolve) {
		var wrapper = document.createElement('div');
		wrapper.className = 'popup-layer';
		var popup = document.createElement('div');
		popup.className = 'popup-confirm';
		wrapper.appendChild(popup);

		var textDiv = document.createElement('div');
		textDiv.textContent = text;
		popup.appendChild(textDiv);

		var footerDiv = document.createElement('div');
		footerDiv.className = 'popup-confirm-footer';
		var okBtn = document.createElement('button');
		var cancelBtn = document.createElement('button');
		okBtn.className = 'btn';
		cancelBtn.className = 'btn';
		okBtn.textContent = '확인';
		cancelBtn.textContent = '취소';

		footerDiv.appendChild(okBtn);
		footerDiv.appendChild(cancelBtn);
		popup.appendChild(footerDiv);
		window.document.body.appendChild(wrapper);

		var onClickOk = function () {
			okBtn.removeEventListener('click', onClickOk);
			cancelBtn.removeEventListener('click', onClickCancel);
			resolve(true);
			wrapper.remove();
		};
		
		var onClickCancel = function() {
			okBtn.removeEventListener('click', onClickOk);
			cancelBtn.removeEventListener('click', onClickCancel);
			resolve(false);
			wrapper.remove();
		};
		
		okBtn.addEventListener('click', onClickOk);
		cancelBtn.addEventListener('click', onClickCancel);
	});
};