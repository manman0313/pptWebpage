<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${context}/resources/assets/css/bootstrap.min.css" />
<!-- page specific plugin styles -->
<!-- text fonts -->
<link rel="stylesheet" href="${context}/resources/assets/css/fonts.googleapis.com.css" />
<link rel="stylesheet" href="${context}/resources/assets/font-awesome/4.5.0/css/font-awesome.min.css" />
<!-- ace styles -->
<link rel="stylesheet" href="${context}/resources/assets/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style" />
<link rel="stylesheet" href="${context}/resources/assets/css/ace-skins.min.css" />
<link rel="stylesheet" href="${context}/resources/assets/css/ace-rtl.min.css" />

<script src="${context}/resources/assets/js/jquery-2.1.4.min.js"></script>
<script src="${context}/resources/assets/js/bootstrap.min.js"></script>
</head>
<body style="margin:auto;background-color:white">
<div class="main-content">
	<div class="main-content-inner">
	<div class="space-24"></div>
	<div class="space-24"></div>
			<c:forEach items="${rList}" var="recommend" varStatus="rStatus">
				<c:choose>
					<c:when test="${rStatus.first }">
						<div class="row">
							<div class="col-xs-6 col-xs-offset-3 center">
								<div >${recommend.comName }</div>
								<div id="chart${rStatus.index}" class="easy-pie-chart percentage" data-percent="${recommend.value}" data-color="#D15B47">
									<span class="percent">${recommend.value}</span>%
								</div>
							</div>
						</div>
						<div class="space-12"></div>
					</c:when>
					<c:otherwise>
						<div class="col-xs-6  center">
							<div >${recommend.comName }</div>
							<div id="chart${rStatus.index}" class="easy-pie-chart percentage" data-percent="${recommend.value}" data-color="#D15B47">
								<span class="percent">${recommend.value}</span>%
							</div>
						</div>
					</c:otherwise>
				</c:choose>
			</c:forEach>
	</div>
	</div>
<!-- ace scripts -->
<script src="${context}/resources/assets/js/ace-elements.min.js"></script>
<script src="${context}/resources/assets/js/ace.min.js"></script>
<script src="${context}/resources/assets/js/jquery.easypiechart.min.js"></script>
<script>
var color = ["#D15B47", "#87CEEB", "#87B87F"];

jQuery(function($) {
	<c:forEach var="i" begin="0" end="2">
		$("#chart${i}").attr("data-color",color[${i}]);
	</c:forEach>
	$('.easy-pie-chart.percentage').each(function(){
		$(this).easyPieChart({
			barColor: $(this).data('color'),
			trackColor: '#EEEEEE',
			scaleColor: false,
			lineCap: 'butt',
			lineWidth: 8,
			animate: ace.vars['old_ie'] ? false : 1000,
			size:75
		}).css('color', $(this).data('color'));
	});
	
	
});
</script>
</body>
</html>