function showDescription(e){
	var d= $(".video-description");
	if(d.hasClass("hidden")){
		d.removeClass("hidden");
	}else{
		d.addClass("hidden");
	}
	alert(d);
}
