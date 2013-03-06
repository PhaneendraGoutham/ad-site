<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>My Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="http://code.jquery.com/jquery-1.7.1.js"></script>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<script src="/ad-web/resources/jquery.ui.widget.js"></script>
<script src="/ad-web/resources/jquery.iframe-transport.js"></script>
<script src="/ad-web/resources/jquery.fileupload.js"></script>
<body>
<div id="upload_container">
    <form id="franek" enctype="multipart/form-data">
<input name="file" type="file" />
</form>
<progress></progress>
</div>
<spring:url value="/secure/ad" var="ad"/>
	<form:form method="POST" action="${ad}" id="ad-form" commandName="adPostDto">
		<form:input path="brandId" />
		<form:input path="title" />
		<form:textarea path="description" />
		<form:hidden path="type" />
		<form:hidden path="url" id="dailymotion-url" />
		
	</form:form>
	<input type="button" id="ad-form-submit-button"/>
</body>
<script>
$("#ad-form-submit-button").click(function(){
    var formData = new FormData($('#franek')[0]);
	 $.ajax({
	        url: '<spring:url value="${dailymotionUploadUrl}" />',  
	        		//server script to process data
	        type: 'POST',
	        xhr: function() {  // custom xhr
	            myXhr = $.ajaxSettings.xhr();
	            if(myXhr.upload){ // check if upload property exists
	                myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // for handling the progress of the upload
	            }
	            return myXhr;
	        },
	        //Ajax events
	        success: completeHandler,
	        // Form data
	        data: formData,
	        //Options to tell JQuery not to process data or worry about content-type
	        cache: false,
	        contentType: false,
	        processData: false
	    });
});
function completeHandler(data){
	$("#dailymotion-url").val(data.url);
	$("#ad-form").submit();
}
function progressHandlingFunction(e){
    if(e.lengthComputable){
        $('progress').attr({value:e.loaded,max:e.total});
    }
}
</script>
</html>