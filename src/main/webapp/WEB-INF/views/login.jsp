<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PPT-제법 쓸만한 예측 툴</title>

<!-- Main import -->
<meta name="description" content="overview &amp; stats" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<!-- bootstrap & fontawesome -->
<link rel="stylesheet" href="${context}/resources/assets/css/bootstrap.min.css" />
<link rel="stylesheet" href="${context}/resources/assets/css/jquery-ui.min.css" />
<link rel="stylesheet" href="${context}/resources/assets/font-awesome/4.5.0/css/font-awesome.min.css" />
<!-- text fonts -->
<link rel="stylesheet" href="${context}/resources/assets/css/fonts.googleapis.com.css" />
<!-- ace styles -->
<link rel="stylesheet" href="${context}/resources/assets/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style" />
<link rel="stylesheet" href="${context}/resources/assets/css/ace-skins.min.css" />
<link rel="stylesheet" href="${context}/resources/assets/css/ace-rtl.min.css" />
<!-- ace settings handler -->
<script src="${context}/resources/assets/js/ace-extra.min.js"></script>
<script src="${context}/resources/assets/js/jquery-2.1.4.min.js"></script>
<!------------------------------------------------------------------------------------------------------------------------->
</head>
<body class="no-skin">
	<jsp:include page="include/top-menu.jsp"></jsp:include>
	<jsp:include page="include/side-menu.jsp"></jsp:include>
	<div class="main-content">
		<div class="main-content-inner">
			<div class="breadcrumbs ace-save-state breadcrumbs-fixed" id="breadcrumbs">
				<ul class="breadcrumb">
					<li><i class="ace-icon fa fa-home home-icon"></i> <a href="${context}/hello.do">Home</a></li>
					<li class="active">로그인</li>
				</ul>
				<!-- /.breadcrumb -->
			</div>
			
			<div class="page-content">
				<div class="row">
					<div class="col-sm-12">
							<div align="center">
								<img src="${context }/resources/logo.png" width="20%"><br/><br/>
							</div>
					</div>
				</div>
				<div class="row margin-b-5">
					<div class ="col-sm-4 col-sm-offset-4">
						<form name="loginForm" action="${context}/login.do" method="post" onSubmit="return checkForm()">
							<input type="email" style="width:100%; height:50px" placeholder="&nbsp;&nbsp;아이디" autofocus name="email" value="${cookie.savedId.value}"><br/><br/>
							<input type="password" style="width:100%; height:50px" placeholder="&nbsp;&nbsp;패스워드" name="password"  value="${cookie.savedPassword.value}"><br/><br/>
							<div id="loginFail"></div>
							<input type="checkbox" id="remember" class="ace" name="remember" ${cookie.savedCheck.value }>
							<span class="lbl"><label for="remember">&nbsp;로그인 상태 유지</label><br/><br/></span>
							<input type="submit" value="로그인" id="btnLogin" class="btn btn-info" style="font-size:20px; width:100%">
						</form>
					<hr/>
					</div>
				</div>
				<div class="row">
					<div align="center" >
						<div class="col-sm-12" align="center">
							<a href="#" id="id-btn-dialog2">아이디를 잊으셨나요?</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
							<a href="#" id="id-btn-dialog1">비밀번호를 잊으셨나요?</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
							<a href="join.do">회원가입</a>
						</div>
					</div>
				</div>
			</div>
		<!-- jQuery UI -->
		<div id="dialog-message" class="hide">
			<p>
				This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.
			</p>

			<div class="hr hr-12 hr-double"></div>

			<p>
				Currently using
				<b>36% of your storage space</b>.
			</p>
		</div><!-- #dialog-message -->

		<div id="dialog-confirm" class="hide">
			<div class="alert alert-info bigger-110">
				These items will be permanently deleted and cannot be recovered.
			</div>

			<div class="space-6"></div>

			<p class="bigger-110 bolder center grey">
				<i class="ace-icon fa fa-hand-o-right blue bigger-120"></i>
				Are you sure?
			</p>
		</div><!-- #dialog-confirm -->
	<!-- jQuery UI -->
		</div>
	</div>
</div>
	<!--========== FOOTER ==========-->
	<footer>
		<jsp:include page="include/bottom.jsp"></jsp:include>
	</footer>
<!-- page specific plugin scripts -->
<script src="${context}/resources/assets/js/jquery-ui.min.js"></script>
<script src="${context}/resources/assets/js/jquery.ui.touch-punch.min.js"></script>
<script>
	
	function isNull(obj, msg){
		if(obj.value == ""){
			$('#loginFail').text(msg).css({
				'color' : 'red'
			});
			obj.focus();
			return true;
		}
		return false;
	}
	
	function checkForm() {
		var form = document.loginForm;
		var email = form.email;
		var password = form.password;
		
		if(isNull(form.email,"아이디를 입력하시오.")){
			return false;
		}
		if(isNull(form.password, '패스워드를 입력하세요')){
			return false;
		}
		if(!/^[a-zA-Z0-9]{4,15}$/.test(form.password.value)){
			$('#loginFail').text('암호는 숫자와 영문자 조합으로 4~15자리를 사용해야 합니다.').css({
				'color' : 'red'
			});
			return false;
			///^(?=.*[a-zA-Z])((?=.*\d)|(?=.*\W)).{6,20}$/ : 영어, 숫자, 최소 한개 특수문자
		}
		return true;
	}
	
jQuery(function($) {
	$( "#id-btn-dialog1" ).on('click', function(e) {
		e.preventDefault();

		var dialog = $( "#dialog-message" ).removeClass('hide').dialog({
			modal: true,
			title: "<div class='widget-header widget-header-small'><h4 class='smaller'><i class='ace-icon fa fa-check'></i> jQuery UI Dialog</h4></div>",
			title_html: true,
			buttons: [ 
				{
					text: "Cancel",
					"class" : "btn btn-minier",
					click: function() {
						$( this ).dialog( "close" ); 
					} 
				},
				{
					text: "OK",
					"class" : "btn btn-primary btn-minier",
					click: function() {
						$( this ).dialog( "close" ); 
					} 
				}
			]
		});
	});


	$( "#id-btn-dialog2" ).on('click', function(e) {
		e.preventDefault();
	
		$( "#dialog-confirm" ).removeClass('hide').dialog({
			resizable: false,
			width: '320',
			modal: true,
			title: "<h4>비밀번호 찾기</h4>",
			title_html: true,
			buttons: [
				{
					html: "<i class='ace-icon fa fa-trash-o bigger-110'></i>&nbsp; Delete all items",
					"class" : "btn btn-danger btn-minier",
					click: function() {
						$( this ).dialog( "close" );
					}
				}
				,
				{
					html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; Cancel",
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
</body>
</html>