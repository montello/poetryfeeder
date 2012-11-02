<%@ page session="false" %>
<%@ page import="java.util.List,
                 java.util.Iterator,
                 com.fraise.domain.PoetryTweet,
                 com.fraise.domain.Poem" %>
<%@page isELIgnored="false" contentType="text/html" pageEncoding="windows-1252"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>poetryfeeder</title>
		<link href="http://poetryfeeder.com/poetryfeeder.css" rel="stylesheet" type="text/css" />
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
		<script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.min.js" /> "></script>
		<script type="text/javascript" src="<c:url value="/scripts/json.min.js" /> "></script>		
	</head>

	<body>

<!-- //// FRAME LEFT -->
		<div id="framecontentLeft">
			<div class="innertube">
				<div id="home">
					<ul>
						<li><h1><a href="recent"><spring:message code="link.home"/></a></h1></li>
					</ul>
					<div id="colofon">
						<a href="about"><spring:message code="link.about"/></a>
					</div>
				</div>
			</div>
		</div>
<!--  FRAME LEFT \\\\ -->
	
		<div id="maincontent">
			<div class="innertubemain" id="innertubemain">
			 	<h3>001</h3>	
				<%
				List poems = (List) request.getAttribute("archivedPoems");
				
				Iterator it= poems.iterator();
				while(it.hasNext()){
				%><div class="archivepoem"><%
					Poem poem = (Poem)it.next();
					List poemLines = (List) poem.getLines();
					Iterator lineIter= poemLines.iterator();
					while(lineIter.hasNext()){ 
						PoetryTweet line = (PoetryTweet)lineIter.next(); %>
						  <div class="single">
						  	<%= line.getText() %> 
							<div class="meta">
								<spring:message code="label.author"/>:&nbsp;<%= line.getAuthor() %>,&nbsp;<spring:message code="label.authoredOn"/>:&nbsp;<%= line.getAuthoredOn() %>
							</div>
						  </div>
					 <%}%>
					 </div>
				<%}%>
				<%
				String bookmarkNext = (String)request.getAttribute("bookmarkNext");
				if(null != bookmarkNext){%>
					<p><a href="javascript:;" id="next">Next></a></p>
					<input id="nextBookmark" type="hidden" value="<%=bookmarkNext%>"/>
				<%}%>
			</div>
		</div>
		
		<script type="text/javascript">

			$(document).ready(function() {
			    // check name availability on focus lost
			    $('#next').click(function() {
			        checkAvailability();
			    });
			});
			
			function checkAvailability() {
				var bookmarkVar = $("#nextBookmark").attr("value"); 
				$.getJSON("archive/next", { bookmark: bookmarkVar }, function(result) {
					$.each(result.poems, function(i, poem) {
						$('#innertubemain').append(
						        '<div id="archivePoem" class="archivepoem">'
						    );
					    $.each(poem.lines,function(j, line) {
						    $('#archivePoem').append(
						        '<div class="single">'
						        + line.text
						        + '<div class="meta">'
						        + line.author + line.authoredOn
						        + '</div></div>'
						    );
    					});
    					$('#innertubemain').append(
						        '</div>'
						    );
					});    
				});
			}

		</script>

<!-- //// FRAME RIGHT -->
		<div id="framecontentRight">
			<div class="write">    
				<ul>            	 
					<li><a href="write"><spring:message code="link.write"/></a></li>
				</ul>
			</div>
			
			<div class="innertube">
				<%
				String currentBookmark = (String)request.getAttribute("currentBookmark");
				%>
				<ul>
					<li><a href="archive?sort=author&bookmark=<%=currentBookmark%>"><spring:message code="link.sort.author"/></a></li>
					<li><a href="archive?sort=chronological&bookmark=<%=currentBookmark%>"><spring:message code="link.sort.chronological"/></a></li>
					<li><a href="archive?sort=alphabetical&bookmark=<%=currentBookmark%>"><spring:message code="link.sort.alphabetical"/></a></li>
					<li><a href="cinquain"><spring:message code="link.poem.cinquain"/></a></li>
				</ul>				
	
				<!--******  i18n  ****************-->
				<br><br>
				<div>
					<div class="lang">
				            <c:url var="englishLocaleUrl" value="archive">
				                <c:param name="locale" value="en" />
				            </c:url>
				            <c:url var="dutchLocaleUrl" value="archive">
				                <c:param name="locale" value="nl" />
				            </c:url>
							
				            <a href='<c:out value="${englishLocaleUrl}"/>'><spring:message code="link.locale.english"/></a>|<a href='<c:out value="${dutchLocaleUrl}"/>'><spring:message code="link.locale.dutch"/></a>
				    </div>
			    <!--**********************-->
				</div>
			</div>
		</div>
<!-- FRAME RIGHT \\\\ -->


	</body>
</html>
