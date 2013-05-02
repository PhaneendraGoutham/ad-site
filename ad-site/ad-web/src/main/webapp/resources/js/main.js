function toggleClassHidden(target) {
	if (target.hasClass("hidden")) {
		target.removeClass("hidden");
	} else {
		target.addClass("hidden");
	}
}
function showUserDropdownMenu(e) {
	var d = $("#logged-user-holder > ul");
	toggleClassHidden(d);
}
function toggleAdminButton(target) {
	if (target.hasClass("button-green")) {
		target.removeClass("button-green");
		target.addClass("button-blue");
	} else {
		target.removeClass("button-blue");
		target.addClass("button-green");
	}
}
$(function() {
	jQuery.validator.addMethod("pattern", function(value, element, param) {
		if (this.optional(element)) {
			return true;
		}
		if (typeof param === 'string') {
			param = new RegExp('^(?:' + param + ')$');
		}
		return param.test(value);
	}, "Nieprawidłowe znaki");
	
	
	$(".admin-panel button").click(function() {
		var $this = $(this);
		var target = $this.data("target");
		var adId = $this.data("id");
		var data = new Object();
		if ($this.hasClass("button-blue")) {
			data[target] = false;
		} else {
			data[target] = true;
		}
		if (target == "place") {
			if (data[target] == true) {
				data[target] = 0;
			} else
				data[target] = 1;
		}
		$.ajax({
			url : basePath + "ad/" + adId + "/state",
			data : data,
			method : "POST",
			success : function() {
				toggleAdminButton($this);
			}
		});
	});
	$("#login-dialog").dialog({
		autoOpen : false,
		height : 135,
		width : 180,
		buttons : [ {
			text : "OK",
			click : function() {
				$(this).dialog("close");
			}
		} ]
	});

	showSelectedFilters();
	$(".rating-wrapper .star").click(function() {
		$this = $(this);
		var d = $this.parent().children(".rating-panel");
		if (d.hasClass("hidden")) {
			d.removeClass("hidden");
		} else {
			d.addClass("hidden");
		}
	});
	$(".rating-panel").raty({
		starOn : basePath + 'resources/img/small-star.png',
		starOff : basePath + 'resources/img/small-star-white.png',
		width : '125px',
		click : function(score, event) {
			var adId = $(this).data("ad-id");
			var dataObject = new Object();
			$this = $(this);
			dataObject['vote'] = score;
			$.ajax({
				url : basePath + "ad/" + adId + "/vote",
				type : "POST",
				data : dataObject,
				success : function() {
					$this.addClass("hidden");
				},
				error : function() {
					$("#login-dialog").dialog("open");
				}
			});
		}
	});

	$(".video-footer-nav li > a").on('click', function(e) {
		$this = $(this);
		e.preventDefault();
		var target = $this.data("target");
		var adId = $this.data("id");
		var wrapper = $("#ad-wrapper-" + adId + " > .bottom-video-panel");
		wrapper.children().addClass("hidden");
		var hadClass = $this.parent().hasClass("on");
		$this.parent().parent().children().removeClass("on");
		if (!hadClass) {
			$this.parent().addClass("on");
			wrapper.removeClass("hidden");
		} else {
			wrapper.addClass("hidden");
		}
		wrapper.children(target).removeClass("hidden");
		if (target == '.comments') {
			var commentsWrapper = wrapper.children(target);
			var attr = commentsWrapper.attr('data-loaded');
			if (typeof attr === 'undefined' || attr === false) {
				loadComments(commentsWrapper, adId);
			}

		}
	});
	$(".comments").on(
			'click',
			'.show-answer-box',
			function(e) {
				$this = $(this);
				var postId = $this.data("id");
				e.preventDefault();
				var target = $this.data("target");
				var adId = $this.data("ad-id");
				var attr = $(this).attr('data-loaded');
				if (typeof attr === 'undefined' || attr === false) {
					var box = $(".comments").children(
							".comment-box-wrapper:first").clone();

					box.prependTo($(target).children(".post-children"));
					$this.attr("data-loaded", 'true');
					var commentButton = $(target).children(".post-children")
							.children(".comment-box-wrapper").find("button");
					commentButton.attr("data-post-id", postId);
					commentButton.attr("data-ad-id", adId);
				} else if (attr == 'true') {
					$this.attr("data-loaded", 'false');
					$(target + "> .post-children > .comment-box-wrapper")
							.addClass("hidden");
				} else {
					$this.attr("data-loaded", 'true');
					$(target + "> .post-children > .comment-box-wrapper")
							.removeClass("hidden");
				}
			});
	$(".comments").on(
			'click',
			'.inform',
			function(e) {
				$this = $(this);
				var postId = $this.data("post-id");
				e.preventDefault();
				var target = $this.data("target");
				var attr = $(this).attr('data-loaded');
				if (typeof attr === 'undefined' || attr === false) {
					var box = $(".inform").children(".inform-box-holder:first")
							.clone();

					box.prependTo($(target).children(".post-children"));
					$this.attr("data-loaded", 'true');
					var informButton = $(target).children(".post-children")
							.children(".inform-box-holder").find("button");
					informButton.attr("data-post-id", postId);
					informButton.removeAttr("data-ad-id");
				} else if (attr == 'true') {
					$this.attr("data-loaded", 'false');
					$(target + "> .post-children > .inform-box-holder")
							.addClass("hidden");
				} else {
					$this.attr("data-loaded", 'true');
					$(target + "> .post-children > .inform-box-holder")
							.removeClass("hidden");
				}
			});
	$(".comments").on(
			'click',
			'.comment-button',
			function(e) {
				$this = $(this);
				var id = $this.data("post-id");
				var adId = $this.data("ad-id");
				var dataObject = new Object();
				dataObject['message'] = $this.parent().children(".comment-box")
						.val();
				dataObject['postId'] = id;
				$.ajax({
					url : basePath + "ad/" + adId + "/comment",
					type : "POST",
					data : dataObject,
					success : function(html, status, xhr) {
						var commentsWrapper = $("#ad-wrapper-" + adId
								+ " > .bottom-video-panel > .comments");
						commentsWrapper.html(html);
						commentsWrapper.attr("data-loaded", true);
					},
					error : function() {
						$("#login-dialog").dialog("open");
					}
				});
			});
	$(".comments,.inform").on(
			'click',
			'.inform-button',
			function(e) {
				$this = $(this);

				var id = $this.data("post-id");
				var adId = $this.data("ad-id");
				var dataObject = new Object();
				dataObject['message'] = $this.parent().children(".comment-box")
						.val();
				if (adId === undefined) {
					var url = basePath + "post/" + id + "/inform";
					$this.parent().addClass("hidden");
					$this.parent().parent().parent().find("a.inform").attr(
							"data-loaded", false);
				} else {
					var url = basePath + "ad/" + adId + "/inform";
				}
				$.ajax({
					url : url,
					type : "POST",
					data : dataObject,
				});
				$this.parent().find(".comment-box").val("");
			});

	$(".filters-item-list > li > span").click(function() {
		if ($(this).parent().hasClass("on")) {
			return;
		}
		var target = $(this).data("target");
		var id = $(this).data("id");
		var uiName = $(this).data("ui-name");
		$(this).parent().addClass("on");
		addItemChecked(target, id);
		displayFilter(target, id, uiName);
	});
	$("li").on('click', '.close', function() {
		var target = $(this).data("target");
		var id = $(this).data("id");
		removeItemChecked(target, id);
		$("#" + target + "-filter-item-" + id).removeClass("on");
		hideFilter(target, id);
	});
});
function loadComments(commentsWrapper, adId) {
	$.ajax({
		url : basePath + "ad/" + adId + "/comment",
		type : "GET",
		success : function(html) {
			commentsWrapper.html(html);
			commentsWrapper.attr("data-loaded", true);
		}
	});
}
function toggleFilters(target) {
	var li = $("#" + target + "-menu-filter");
	if (li.hasClass("on")) {
		li.removeClass("on");
		toggleClassHidden($("#" + target + "-filters"));
	} else {
		$("#filters-menu > li.on").removeClass("on");
		li.addClass("on");
		$("#filters").children().each(function() {
			if (!$(this).hasClass("hidden")) {
				$(this).addClass("hidden");
			}
		});
		toggleClassHidden($("#" + target + "-filters"));
	}
}
function displayFilter(target, id, uiName) {
	var fil = $("#" + target + "-selected-filters");
	if (target == 'tag' || target == 'brand') {
		if (!fil.hasClass("on")) {
			fil.addClass("on");
		}
		var close = $(document.createElement('div'));
		var li = $(document.createElement('li'));
		close.attr("data-target", target);
		close.attr("data-id", id);
		close.addClass("close");
		close.append("x");
		li.attr("id", target + "-selected-filter-item-" + id);
		li.addClass("on");
		li.append('<span>' + uiName + '</span>').append(close);
		fil.children("ul").append(li);
	} else {
		var text = "-";
		if (target == 'place' || target == 'year') {
			var select = $("#" + target + "-select");
			text = select.find(":selected").text();
		} else if (target == 'rank') {
			var from = $("#" + target + "-select-from").find(":selected")
					.text();
			var to = $("#" + target + "-select-to").find(":selected").text();
			text = getRangeString(from, to);
		} else {
			var from = $("#" + target + "-input-from").val();
			var to = $("#" + target + "-input-to").val();
			text = getRangeString(from, to);
		}
		if (text != '-') {
			if (!fil.hasClass("on")) {
				fil.addClass("on");
			}
			$("#" + target + "-selected-filters").children("span.value").html(
					text);
		} else {
			hideFilter(target);
		}
	}
}

function getRangeString(from, to) {
	if (from == '-')
		from = '';
	if (to == '-')
		to = '';
	if (from == '' && to != '') {
		return "poniżej " + to;
	} else if (from != '' && to == '') {
		return "powyżej " + from;
	} else if (from == '' && to == '') {
		return "-";
	} else {
		return from + " - " + to;
	}
}
function showSelectedFilters() {
	$(".filters-item-list li.on").each(
			function() {
				var span = $(this).children("span");
				displayFilter(span.data("target"), span.data("id"), span
						.data("ui-name"));
			});
	displayFilter('year');
	displayFilter('place');
	displayFilter('rank');
	displayFilter('vote');
	// setSortUi($("#order-by-input").val(),$("#order-input").val());
}
function hideFilter(target, id, uiName) {
	var li = $("#" + target + "-selected-filter-item-" + id);
	li.remove();
	var fil = $("#" + target + "-selected-filters");
	var ul = fil.children("ul");
	if (ul.children().length === 0) {
		fil.removeClass("on");
	}
}
function setSelectDefault(select) {
	select[0].selectedIndex = 0;
}
function removeItemChecked(target, id) {
	if (target == 'tag' || target == 'brand') {
		$("#" + target + "-" + id).attr("checked", false);
	} else if (target == 'place' || target == 'year') {
		setSelectDefault($("#" + target + "-select"));
	} else if (target == 'rank') {
		setSelectDefault($("#" + target + "-select-from"));
		setSelectDefault($("#" + target + "-select-to"));
	} else {
		$("#" + target + "-input-from").val('');
		$("#" + target + "-input-to").val('');
	}
}
function addItemChecked(target, id) {
	var targetObject = $("#" + target + "-" + id);
	targetObject.attr("checked", true);
}

function handleSortClick(target) {
	var li = $("#" + target + "-sort");
	var arrow = li.children("div");
	if (li.hasClass("on")) {
		if (arrow.hasClass("arrow-down-white")) {
			arrow.removeClass();
			arrow.addClass("arrow-up-white");
			setOrderInputs(target, "asc");
		} else {
			arrow.removeClass();
			arrow.addClass("arrow-down-white");
			setOrderInputs(target, "desc");
		}
	} else {
		li.parent().children("li").removeClass("on");
		li.addClass("on");
		arrow.removeClass();
		arrow.addClass("arrow-down-white");
		setOrderInputs(target, "desc");
	}
}
// function setSortUi(target, order) {
// var li = $("#" + target + "-sort");
// var arrow = li.children("div");
// if (li.hasClass("on")) {
// if (order == "asc") {
// arrow.removeClass();
// arrow.addClass("arrow-up-white");
// setOrderInputs(target, "asc");
// } else {
// arrow.removeClass();
// arrow.addClass("arrow-down-white");
// setOrderInputs(target, "desc");
// }
// } else {
// li.parent().children("li").removeClass("on");
// li.addClass("on");
// if (order == "asc") {
// arrow.removeClass();
// arrow.addClass("arrow-up-white");
// setOrderInputs(target, "asc");
// } else {
// arrow.removeClass();
// arrow.addClass("arrow-down-white");
// setOrderInputs(target, "desc");
// }
// }
// }
function setOrderInputs(orderBy, order) {
	var o = $("#order-by-input");
	$("#order-by-input").val(orderBy);
	$("#order-input").val(order);
}
