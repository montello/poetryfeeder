<%@ page session="false" %>
<%@ page import="java.util.List,
                 java.util.Iterator,
                 com.fraise.domain.PoetryTweet" %>
<%@page isELIgnored="false" contentType="text/html" pageEncoding="windows-1252"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>poetryfeeder</title>
		<link href="http://poetryfeeder.com/poetryfeeder.css" rel="stylesheet" type="text/css" media="screen"/>
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

<!-- //// FRAME LEFT -->
		<div id="framecontentLeft">
			<div class="innertube">
				<div id="home">
					<ul>
						<li><h1><a href=""><spring:message code="link.home"/></a></h1></li>
					</ul>
					<div id="colofon">
						<a href="about"><spring:message code="link.about"/></a>
					</div>
					<!--******  i18n  ****************-->
					<br><br>
					<div>
						<div class="lang">
					            <c:url var="englishLocaleUrl" value="/recent">
					                <c:param name="locale" value="en" />
					            </c:url>
					            <c:url var="dutchLocaleUrl" value="/recent">
					                <c:param name="locale" value="nl" />
					            </c:url>

					            <a href='<c:out value="${englishLocaleUrl}"/>'><spring:message code="link.locale.english"/></a> | <a href='<c:out value="${dutchLocaleUrl}"/>'><spring:message code="link.locale.dutch"/></a>
					    </div>
					</div>
				    <!--**********************-->
				</div>
			</div>
		</div>
<!--  FRAME LEFT \\\\ -->

<!-- //// FRAME RIGHT -->
		<div id="framecontentRight">
			<div class="write">    
				<ul>            	 
					<li><a href="write"><spring:message code="link.write"/></a></li>
				</ul>
			</div>
			
			<div class="innertube">
				<ul>
					<li><a href="recent?sort=author"><spring:message code="link.sort.author"/></a></li>
					<li><a href="recent?sort=chronological"><spring:message code="link.sort.chronological"/></a></li>
					<li><a href="recent?sort=alphabetical"><spring:message code="link.sort.alphabetical"/></a></li>
					<li><a href="cinquain"><spring:message code="link.poem.cinquain"/></a></li>
					<li><a href="archive"><spring:message code="link.poem.archive"/></a></li>
				</ul>

				
				</div>
			</div>
		</div>
<!-- FRAME RIGHT \\\\ -->

<!-- //// MAIN CONTENT -->
		<div id="maincontent">
			<div class="innertubemain">
			  <h3><spring:message code="text.recent.tweet"/></h3>
			<%
			List poemLines = (List) request.getAttribute("recentList");
			Iterator it= poemLines.iterator();
			while(it.hasNext()){
			PoetryTweet line = (PoetryTweet)it.next();%>
			  <div class="poem">
			  	<%= line.getText() %> 
				<div class="meta">
					<spring:message code="label.author"/>:&nbsp;<%= line.getAuthor() %>,&nbsp;<spring:message code="label.authoredOn"/>:&nbsp;<%= line.getAuthoredOn() %>
				</div>
			  </div>
			 <%}%>
			</div>
		</div>
<!-- MAIN CONTENT \\\\ -->

	</body>
</html>
