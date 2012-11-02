<%@page isELIgnored="false" contentType="text/html" pageEncoding="windows-1252"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>
<!--Force IE6 into quirks mode with this comment tag-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>poetryfeeder</title>
		<link href="http://poetryfeeder.com/poetryfeeder.css" rel="stylesheet" type="text/css" media="screen" />
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
		
		<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
		<!--
		function pop(url)
		{
		    newwindow1=window.open(url,'popwindow','height=840,width=840,scrollbars=yes,,top=20,left=20');
		    if (window.focus) {newwindow1.focus()}
		}
		//-->
		</SCRIPT>
		
		
		</head>

	<body>

		<div id="framecontentLeft">
			<div class="innertube">
				<div id="home">
					<ul>
						<li><h1><a href="recent"><spring:message code="link.home"/></a></h1></li>
					</ul>

					<div id="colofon">
						<a href="about"><spring:message code="link.about"/></a>
					</div>
					
					<!--******  i18n  ****************-->
					<br><br>
					<div>
						<div class="lang">
					            <c:url var="englishLocaleUrl" value="write">
					                <c:param name="locale" value="en" />
					            </c:url>
					            <c:url var="dutchLocaleUrl" value="write">
					                <c:param name="locale" value="nl" />
					            </c:url>

					            <a href='<c:out value="${englishLocaleUrl}"/>'><spring:message code="link.locale.english"/></a> | <a href='<c:out value="${dutchLocaleUrl}"/>'><spring:message code="link.locale.dutch"/></a>
					    </div>
					</div>
				    <!--**********************-->
				</div>
			</div>
		</div>

		<div id="framecontentRight">

		</div>

		<div id="maincontent">
			<div class="innertubemain">
				<h3><spring:message code="lable.header.write"/></h3>
				<div class="poem">
					<spring:message code="text.write.main"/>
				</div>
			 </div>
		</div>

	</body>
</html>
