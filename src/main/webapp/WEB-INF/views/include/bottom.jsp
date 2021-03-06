<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<div class="footer">
	<div class="footer-inner">
		<div class="footer-content">
			<span class="bigger-120">
				PPT는 제공된 정보에 의한 투자결과에 대한 법적인 책임을 지지 않습니다. 게시된 정보를 무단으로 배포할 수 없습니다. &nbsp;
				<br/><i>Copyright &copy; 2017 by Jiman Kim, Ikhyeon Cho, Dongjoo Hwang. All rights reserved.</i>
			</span>
		</div>
	</div>
</div>
</div>
		<script src="${context}/resources/assets/js/bootstrap.min.js"></script>
		<!-- page specific plugin scripts -->
		<script src="${context}/resources/assets/js/jquery-ui.min.js"></script>
		<script src="${context}/resources/assets/js/jquery.ui.touch-punch.min.js"></script>
		<!-- ace scripts -->
		<script src="${context}/resources/assets/js/ace-elements.min.js"></script>
		<script src="${context}/resources/assets/js/ace.min.js"></script>
		<script src="${context}/resources/loading.js"></script>
<script>
jQuery(function($) {
	$( "#id-btn-signOut" ).on('click', function(e) {
		e.preventDefault();
	
		$( "#dialog-signOut" ).removeClass('hide').dialog({
			resizable: false,
			width: '320',
			modal: true,
			title: "     탈퇴하기",
			title_html: true,
			buttons: [
				{
					html: "<i class='ace-icon fa fa-trash-o bigger-110'></i>&nbsp; 탈퇴하기",
					"class" : "btn btn-danger btn-minier",
					click: function() {
						$( this ).dialog( "close" );
						location.href = "${context}/signOut.do";
					}
				}
				,
				{
					html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; 취소",
					"class" : "btn btn-minier",
					click: function() {
						$( this ).dialog( "close" );
					}
				}
			]
		});
	});
});
</script>
		<!-- inline scripts related to this page -->
