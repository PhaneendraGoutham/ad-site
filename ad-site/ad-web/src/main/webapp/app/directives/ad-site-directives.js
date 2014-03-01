app.directive("wistiaEmbedded", ["$http",function($http) {
    return {
        restrict : 'A',
        link : function(scope, htmlElem, attributes) {
            var uri = "http://home.wistia.com/medias/" + scope.ad.videoData.videoId + "?embedType=seo&videoWidth=620&videoHeight=349";
            var encodedUri = encodeURIComponent(uri);
            var url = "http://fast.wistia.com/oembed.json?url=" + encodedUri;
            $http.get(url).success(function(data) {
                htmlElem.html("");
                var htmlToApped = data.html.replace('<script charset="ISO-8859-1" src="\/\/fast.wistia.com\/assets\/external\/E-v1.js"><\/script>',"");
                htmlElem.append(htmlToApped);
                
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