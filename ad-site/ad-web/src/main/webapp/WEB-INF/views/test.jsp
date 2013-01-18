
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
</body>
<script>
	$(function() {
		userRegForm = new Object();
		userRegForm['password'] = "password";
		userRegForm["confirmPassword"] = "password";
		userRegForm["username"] = "stallon";
		user = new Object();
		userData = new Object();
		userData["birthDate"] = "1990-05-21";
		userData["name"] = "szymon";
		userData["surname"] = "konicki";
		userData["phone"] = "3344555";
		userData["sex"] = "FEMALE";
		userRegForm["mail"] = "konik32s2@gmail.com";
		userRegForm["userData"] = userData;
		address = new Object();
		address["city"] = "Warszawa";
		address["street"] = "Aleje";
		address["homeNr"] = "8";

		address["name"] = "domowy";
		address["type"] = "HOME";
		address['zip'] = "33-300";
		company = new Object();
		company["id"] = 552;
		company["name"] = "warszawska";
		company["nip"] = "7343312760";
		company["phone"] = "32234223423";
		company["address"] = address;

		userRegForm["address"] = address;
		passwordForm = new Object();
		passwordForm["oldPassword"] = "password";
		passwordForm["newPassword"] ="franeczek";
		passwordForm["confirmPassword"] = "franeczek";
		var message = JSON.stringify(userRegForm);
		$.ajax({
			url : "http://localhost:8080/ad-web/user/",
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : message,
			success : function(json) {
				alert(json);
			}
		});
	});

</script>
</html>