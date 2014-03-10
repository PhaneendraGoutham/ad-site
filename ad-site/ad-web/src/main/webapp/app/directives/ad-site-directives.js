app.directive("wistiaEmbedded", ["$http",function($http) {
    return {
        restrict : 'A',
        link : function(scope, htmlElem, attributes) {
            var uri = "http://home.wistia.com/medias/" + scope.ad.videoData.videoId + "?embedType=seo&videoWidth=620&videoHeight=349";
            var encodedUri = encodeURIComponent(uri);
            var url = "http://fast.wistia.com/oembed.json?url=" + encodedUri;
            $http.get(url).success(function(data) {
                htmlElem.html("");
                var htmlToApped = data.html.replace('<script charset="ISO-8859-1" src="\/\/fast.wistia.com\/assets\/external\/E-v1.js"><\/script>', "");
                htmlElem.append(htmlToApped);
                scope.htmlReady();
            });
        }
    };

}]);
app.directive('embedSrc', function() {
    return {
        restrict : 'A',
        link : function(scope, element, attrs) {
            var current = element;
            scope.$watch(function() {
                return attrs.embedSrc;
            }, function() {
                var clone = element.clone().attr('src', attrs.embedSrc);
                current.replaceWith(clone);
                current = clone;
            });
        }
    };
});
app.directive('clickIframe', function() {
    return {
        restrict : 'A',
        template : '<div class="iframe" data-ng-if="adModel.iframe"><iframe width="620" height="349" data-ng-src="{{ad.videoData.iframeVideoUrl}}" frameborder="0" allowfullscreen></iframe></div><div data-ng-if="!adModel.iframe" data-ng-click="clickIframe()" style="cursor:pointer"><img data-ng-src="{{ad.videoData.bigThumbnail}}" /><img src="resources/img/play-button.png"/></div>',
        replace : false,
        link : function(scope, element, attrs) {
            scope.$watch('ad.id', function() {
                scope.adModel.iframe = false;
            });
            scope.clickIframe = function() {
                scope.adModel.iframe = true;
            };
        }
    };
});