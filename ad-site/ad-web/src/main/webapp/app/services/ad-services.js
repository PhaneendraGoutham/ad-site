app.factory("AdFactory", function ($resource) {
    this.Ads = $resource("model/ads.json");
    this.UserAds = $resource("model/ads.json");
    return this;
});


app.service("AdService", ['$http', '$route', '$location', 'CommonFunctions', function ($http, $route, $location, CommonFunctions) {

//    var httpConfig = { method: 'GET', url: '/ad' };
   
    var self = this;
    var searchOptions = {
        search: true,
        filters: {
            tagList: true,
            brandList: true,
            place: true,
            year: true,
            rank: true,
            votes: true,
            order: true,
            orderBy: true,
        }
    };
//    function executeGET(configParam, success, error) {
//        var config = {};
//        $.extend(true, config, httpConfig, configParam);
//        
//        $http(config).success(success).error(error);
//    }
//    function executePOST(configParam, success, error) {
//        var config = {};
//        $.extend(true, config, httpConfig, configParam, { method: "POST" });
//    
//        $http(config).success(success).error(error);
//    }
    this.postAd = function (data, success, error) {
        $http.post("/ad", data).success(success);
    };
    
    this.getAds = function (params, success, error) {
        $http.get("/ad", {params:params}).success(success);
    };

    this.rate = function (id,rank, success, error) {
        $http.post('/ad/'+id+'/rate',$.param({ rank: rank }),CommonFunctions.getPostHeader()).success(success);
    };

    this.getSearchOptions = function() {
        var adSearchOptions = {};
        var routeAdSearchOptions = $route.current.$$route.adSearchOptions;
        $.extend(true, adSearchOptions, searchOptions, routeAdSearchOptions);
        return adSearchOptions;
    };

   
    this.getAdsByUrl = function (success, error) {
        var url = CommonFunctions.getTranslatedUrl();
        var params = $location.search() || {};
        $http.get(url, {params:params}).success(success);
    };

    this.getPossibleTags = function (success, error) {
        $http.get("/tag").success(success);
    };

    this.getPossibleBrands = function (success, error) {
        $http.get("/brand/sm").success(success);
    };

    this.fetchPossibleBrands = function (deferred) {
        self.getPossibleBrands(function (data) {
            deferred.resolve(data);
        }, function (data) {
            deferred.reject(data);
        });
    };
    this.fetchPossibleTags = function (deferred) {
        self.getPossibleTags(function (data) {
            deferred.resolve(data);
        }, function (data) {
            deferred.reject(data);
        });
    };
    this.fetchWistiaProjectId = function (brandId,success,error) {
        $http.get('/brand/'+brandId+'/wistiaproject').success(success);
    };
    this.postBrandAd = function (brandId, data,success,error) {
        $http.post('/brand/'+brandId+'/ad', data).success(success);
    };
    this.changeState = function (adId, state,success,error) {
        $http.post('/ad/'+adId+'/state', $.param(state), CommonFunctions.getPostHeader()).success(success);
    };

}]);