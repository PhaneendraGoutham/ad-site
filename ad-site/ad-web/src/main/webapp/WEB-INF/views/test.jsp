
<!DOCTYPE html>
<html>
<head>
<title>My Page</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="http://code.jquery.com/jquery-1.7.1.js"></script>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<script type="text/javascript" src="http://crypto-js.googlecode.com/svn/tags/2.5.4/build/crypto-sha256-hmac/crypto-sha256-hmac.js"></script>
<script type="text/javascript" src="http://crypto-js.googlecode.com/svn/tags/2.5.4/build/crypto/crypto-min.js"></script>

</head>
<body>
<div id="upload_container">
    <form enctype="multipart/form-data">
<input name="file" type="file" />
<input type="button" value="Upload" />
</form>
<progress></progress>
</div>
<form id="fb_signin" action="http://89.78.121.121:2100/ad-web/signin/facebook" method="POST"><input
                  type="hidden" name="scope" value="publish_stream,offline_access,email,user_birthday"><a
                  href="javascript:document.forms.fb_signin.submit()" title="Log In With Facebook">login</a></form>

</body>
<script>
	$(function() {
		
// // 		USer
// 		userRegForm = new Object();
// 		userRegForm['password'] = "password";
// 		userRegForm["confirmPassword"] = "password";
// 		userRegForm["username"] = "stallon";
// 		user = new Object();
// 		userData = new Object();
// 		userData["birthDate"] = "1990-05-21";
// 		userData["name"] = "szymon";
// 		userData["surname"] = "konicki";
// 		userData["phone"] = "3344555";
// 		userData["sex"] = "FEMALE";
// 		userRegForm["mail"] = "konik32s2@gmail.com";
// 		userRegForm["userData"] = userData;
// 		address = new Object();
// 		address["city"] = "Warszawa";
// 		address["street"] = "Aleje";
// 		address["homeNr"] = "8";

// 		address["name"] = "domowy";
// 		address["type"] = "HOME";
// 		address['zip'] = "33-300";
// 		company = new Object();
// 		company["id"] = 552;
// 		company["name"] = "warszawska";
// 		company["nip"] = "7343312760";
// 		company["phone"] = "32234223423";
// 		company["address"] = address;

// 		userRegForm["address"] = address;
// 		passwordForm = new Object();
// 		passwordForm["oldPassword"] = "password";
// 		passwordForm["newPassword"] ="franeczek";
// 		passwordForm["confirmPassword"] = "franeczek";
// 		var message = JSON.stringify(userRegForm);
// 		var url = "user/";


// BRAND

// 		var brand = new Object();
// 		brand["description"] = "Cocacola byla z coca";
// 		brand["name"] = "CocaCola";
// 		brand["logo"] = "http:///www.cocacola.com.pl/_img/data/coca-cola-new-logo.png";
// 		var message = JSON.stringify(brand);
// 		var url = "brand/";

		var comment = new Object();
		comment["message"] = "menda";
		var c = new Object();
		c["id"]  =1;
		comment["parent"] = c;
		var url = "ad/1/comment";
		var message = JSON.stringify(comment);
		$.ajax({
			url : "http://89.78.121.121:2100/ad-web/"+url,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : message,
			success : function(json) {
				alert(json);
			}
		});
	});
// var id = 1;
// 		$("#upload_html_form").submit(function(e){
// 			if(id == 2)
// 				return;
// 			id = 2;
// 			e.preventDefault();
// 	 		$.ajax({
// 			url : "http://77.254.210.34:2100/ad-web/ad/upload",
// 			type : "POST",
// 			dataType : "json",
// 			contentType : "application/json",
// 			success : function(json) {
// 				$("#upload_html_form").attr("action", json);
// 				$("#upload_submit").click();
// 			}
// 		});
// 		});
// $(':button').click(function(){
//     $.ajax({
// 			url : "http://89.78.121.121:2100/ad-web/video/ad",
// 			type : "GET",
// 			dataType : "json",
// 			contentType : "application/json",
// 			success : onUploadUrlReceived
// 		});
// });
// function onUploadUrlReceived(json){
//     var formData = new FormData($('form')[0]);
// 	 $.ajax({
// 	        url: json.url,  //server script to process data
// 	        type: 'POST',
// 	        xhr: function() {  // custom xhr
// 	            myXhr = $.ajaxSettings.xhr();
// 	            if(myXhr.upload){ // check if upload property exists
// 	                myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // for handling the progress of the upload
// 	            }
// 	            return myXhr;
// 	        },
// 	        //Ajax events
// 	        success: completeHandler,
// 	        // Form data
// 	        data: formData,
// 	        //Options to tell JQuery not to process data or worry about content-type
// 	        cache: false,
// 	        contentType: false,
// 	        processData: false
// 	    });
// }
// function completeHandler(data){
// 	var requestData = new Object();
// 	requestData["url"] = data.url;
// 	requestData["title"] = "Marysia se idzie";
// 	requestData["description"] = "piknie se idzie";
//     $.ajax({
// 		url : "http://89.78.121.121:2100/ad-web/brand/1/video/ad",
// 		type : "POST",
// 		data : JSON.stringify(requestData),
// 		dataType : "json",
// 		contentType : "application/json",
// 		success : function(json){
// 			alert(json);
// 		}
// 	});
// }
// function progressHandlingFunction(e){
//     if(e.lengthComputable){
//         $('progress').attr({value:e.loaded,max:e.total});
//     }
// }
// $.ajax({
// 	url : "http://89.78.121.121:2100/ad-web/user/login",
// 		type : "GET",
// 		success : function(json){
// 			alert(json);
// 		}
// });
</script>
</html>