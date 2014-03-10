<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" data-ng-app="spotnikApp" id="spotnikApp">
<!--<![endif]-->
<head>
<title>{{metatags.title}}</title>
<meta name="generator"
	content="HTML Tidy for HTML5 (experimental) for Windows https://github.com/w3c/tidy-html5/tree/c63cc39" />
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

<meta name="description" content="{{metatags.description}}" />
<meta name="viewport" content="width=device-width" />
<meta name="fragment" content="!" />

<meta property="og:{{k}}" content="{{v}}"
	data-ng-repeat="(k,v) in metatags" />

<meta name="google-site-verification"
	content="kOM3EvVy70dkyvt64caBxWhYTRoCPIJt2rgQyK0XVoE" />
<!-- stylesheets -->
<link rel="stylesheet" href="resources/css/bootstrap.css" />
<link rel="stylesheet" href="resources/css/font-awesome.css" />
<link rel="stylesheet" href="resources/css/datetimepicker.css" />

<!-- components -->
<script type="text/javascript"
	src="resources/components-min/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="resources/components-min/angular.min.js"></script>
<script type="text/javascript"
	src="resources/components-min/angular-cookies.min.js"></script>
<script type="text/javascript"
	src="resources/components-min/ngShowHide.min.js"></script>
<!-- <script type="text/javascript" src="resources/components-min/ngresource.min.js"></script> -->
<script type="text/javascript"
	src="resources/components-min/localstorage.min.js"></script>
<script type="text/javascript"
	src="resources/components-min/angular-locale_pl-pl.min.js"></script>
<script type="text/javascript"
	src="resources/components-min/upload-widget.js"></script>
<script type="text/javascript"
	src="resources/components-min/ui-bootstrap-custom.min.js"></script>
<script charset="ISO-8859-1"
	src="//fast.wistia.com/assets/external/E-v1.js"></script>
<%
	if (System.getenv("AD_SITE_CONF") == "staging") {
%>
<script type="text/javascript" src="app/all.js"></script>
<%
	} else {
%>
<!-- modules -->
<script type="text/javascript" src="app/modules/common-module.js"></script>
<script type="text/javascript" src="app/modules/st-auth-module.js"></script>

<!-- app sources -->
<script type="text/javascript" src="app/app.js"></script>

<!-- controllers -->
<script type="text/javascript"
	src="app/controllers/ad/ad-list-controller.js"></script>
<script type="text/javascript"
	src="app/controllers/user/user-controllers.js"></script>
<script type="text/javascript"
	src="app/controllers/company/company-controllers.js"></script>
<script type="text/javascript"
	src="app/controllers/contest/contest-controller.js"></script>

<!-- services -->
<script type="text/javascript" src="app/services/ad-services.js"></script>
<script type="text/javascript" src="app/services/company-services.js"></script>
<script type="text/javascript" src="app/services/user-service.js"></script>
<script type="text/javascript" src="app/services/contest-services.js"></script>

<!-- directives -->
<script type="text/javascript" src="app/directives/form-directives.js"></script>
<script type="text/javascript"
	src="app/directives/ad-site-directives.js"></script>

<%
	}
%>
<script src="resources/components-min/jquery.ui.widget.min.js"></script>
<script src="resources/components-min/jquery.iframe-transport.min.js"></script>

<!-- The basic File Upload plugin -->
<script src="resources/components-min/jquery.fileupload.min.js"></script>
<script src="resources/components-min/jquery.fileupload-process.min.js"></script>
<!-- <script src="resources/components-min/jquery.fileupload-validate.min.js"></script> -->
<script src="resources/components-min/jquery.fileupload-angular.min.js"></script>
<script type="text/javascript" src="resources/components/angular-seo.js"></script>

</head>
<body>
	<!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->
	<div id="fb-root"></div>
	<script>
        (function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id))
                return;
            js = d.createElement(s);
            js.id = id;
            js.src = "//connect.facebook.net/pl_PL/all.js#xfbml=1&appId=537711946262789";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));
    </script>
	<div class="navbar navbar-default navbar-static-top">
		<div class="container col-xs-12">
			<div class="navbar-custom-brand">
				<a href="#!/glowna"><img src="resources/img/logo.png" /></a>
			</div>
			<div class="navbar-collapse collapse"
				data-ng-include="'/app/partials/main-menu.html'"></div>
			<!--/.navbar-collapse -->
		</div>
	</div>
	<div class="container col-xs-12">
		<div class="row">
			<div class=" col-md-8">
				<div class="row">
					<div class=" col-sm-12">
						<div alert class="main-alert" data-ng-repeat="alert in alerts"
							type="alert.type" close="closeAlert($index)">{{alert.msg}}</div>
					</div>
				</div>
				<div class="row">
					<section class=" col-md-12 center-section" ng-view></section>
				</div>
			</div>
			<aside class=" col-md-4 aside-section hidden-sm hidden-xs">
				<div class="panel panel-default" data-ng-controller="TopAdsCtrl">
					<!-- Default panel contents -->
					<div class="panel-heading text-center">Popularne</div>
					<!-- List group -->


					<div class="thumbnail clearfix"
						data-ng-repeat="ad in model.topAds track by $index ">
						<a data-st-href="#!/reklamy/{{ad.id}}/{{ad.title}}"
							class="pull-left"> <span> <img class=""
								data-ng-src="{{ad.videoData.smallThumbnail}}" />
						</span>
						</a>
						<div class="caption">
							<div class="wrap-text text-info">
								<a data-st-href="#!/reklamy/{{ad.id}}/{{ad.title}}"
									title="{{ad.title}}">{{ad.title}}</a>
							</div>
							<!-- 							<small>użytkownik: <a -->
							<!-- 								href="#!/uzytkownik/{{ad.user.id}}/reklamy">{{ad.user.displayName}}</a></small> -->
						</div>
						<div class="col-md-12">
							<rating value="roundRank(ad.rank)" max="5"
								state-on="'icon-star '" state-off="'icon-star-empty '"
								readonly="true"></rating>
							<span class="label"
								data-ng-class="{'label-info': labelValue<1,'label-default': ad.rank>=1 && ad.rank<=2, 'label-primary':  ad.rank>=2 && ad.rank<=3, 'label-success':  ad.rank>=3 && ad.rank<=4, 'label-warning': ad.rank >=4 && ad.rank<=5, 'label-danger': ad.rank == 5}"
								data-ng-show="!isReadonly"><small>{{ad.rank |
									number:2}}</small></span>
						</div>
						<div class="col-md-12">
							<span class="text-small vote-count pull-right"
								data-ng-show="ad.voteCount >= 100">{{ad.voteCount}}
								głosów</span> <span
								class="text-small vote-count vote-count-aside-panel pull-right"
								data-ng-show="ad.voteCount < 100 ">mniej niż 100 głosów</span>
						</div>
					</div>
				</div>
			</aside>
		</div>
	</div>
</body>
</html>
