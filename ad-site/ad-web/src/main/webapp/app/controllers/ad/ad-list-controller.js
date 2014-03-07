app.controller('AdSearchCtrl', ['$scope','filterFilter','$location','CommonFunctions','AdService','$http','possibleTags','possibleBrands','adBrowserWrapper',function($scope, filterFilter, $location, CommonFunctions, AdService, $http, possibleTags, possibleBrands, adBrowserWrapper) {
    $scope.search = function() {
        var params = $scope.filters;
        $.each(params, function(key, val) {
            if (val === undefined || val == null || val.length == 0) {
                delete params[key];
            }
        });
        $location.search(params);
    };
    $scope.changeOrder = function(orderBy) {
        $scope.filters.orderBy = orderBy;
        if ($scope.filters.order == "asc") {
            $scope.filters.order = "desc";
        } else {
            $scope.filters.order = "asc";
        }
    };

    $scope.isInBrandList = function(item) {
        return isInBrandOrTagArray(item, $scope.helper.brandArr);
    };
    $scope.toogleBrand = function(id) {
        toogleTagOrBrand($scope.helper.brandArr, id);
    };
    $scope.isInTagList = function(item) {
        return isInBrandOrTagArray(item, $scope.helper.tagArr);
    };
    $scope.toogleTag = function(id) {
        toogleTagOrBrand($scope.helper.tagArr, id);
    };
    function isInBrandOrTagArray(item, arr) {
        var index = $.inArray("" + item.id, arr);
        if (index >= 0)
            return true;
        return false;
    }

    function toogleTagOrBrand(arr, id) {
        var stringId = "" + id;
        var index = $.inArray(stringId, arr);
        if (index >= 0) {
            arr.splice(index, 1);
        } else {
            arr.push(stringId);
        }
    }

    $scope.$on('$routeUpdate', function(e) {
        $scope.filters = $location.search();
        getAds();
    });
    $scope.selectPage = function(page) {
        $scope.filters.page = page;
        $scope.search();
    };
    init();
    function getAds() {
        AdService.getAdsByUrl(function(data) {
            if (data.ads) {
                $scope.model.ads = data.ads;
                $scope.total = data.total;
            } else
                $scope.model.ads = [data];

        });
    }
    function init() {

        $scope.filters = $location.search();
        $scope.helper = {};
        $scope.helper.brandArr = [];
        $scope.helper.tagArr = [];
        $scope.text = {};
        $scope.possibleTags = possibleTags || [];
        $scope.possibleBrands = possibleBrands || [];
        $scope.possiblePlaces = [{
            label : 'Poczekalnia',
            value : 1
        },{
            label : 'Główna',
            value : 0
        }];
        $scope.model = {};
        $scope.searchOptions = AdService.getSearchOptions();

        $scope.$watch('filters.brandList', function(v) {
            if (v)
                $scope.helper.brandArr = v.split(',');
            else
                $scope.helper.brandArr = [];
        });
        $scope.$watchCollection('helper.brandArr', function(v) {
            if (v)
                $scope.filters.brandList = v.join(',');
        });
        $scope.$watch('filters.tagList', function(v) {
            if (v)
                $scope.helper.tagArr = v.split(',');
            else
                $scope.helper.tagArr = [];
        });
        $scope.$watchCollection('helper.tagArr', function(v) {
            if (v)
                $scope.filters.tagList = v.join(',');
        });
        registerFiltersWatch();
        if (adBrowserWrapper.total !== undefined) {
            $scope.model.ads = adBrowserWrapper.ads;
            $scope.total = adBrowserWrapper.total;
        } else {
            $scope.model.ads = [adBrowserWrapper];
            $scope.model.singleAd = adBrowserWrapper;
        }
    }

    function registerFiltersWatch() {
        if ($scope.searchOptions.filters.rank)
            $scope.$watchCollection('filters.rankFrom + filters.rankTo', function() {
                $scope.text.rank = getRangeString($scope.filters.rankFrom, $scope.filters.rankTo);
            }, true);
        if ($scope.searchOptions.filters.year)
            $scope.$watchCollection('filters.yearFrom + filters.yearTo', function() {
                $scope.text.year = getRangeString($scope.filters.yearFrom, $scope.filters.yearTo);
            }, true);

        if ($scope.searchOptions.filters.votes)
            $scope.$watchCollection('filters.votesFrom + filters.votesTo', function() {
                $scope.text.votes = getRangeString($scope.filters.votesFrom, $scope.filters.votesTo);
            }, true);
        if ($scope.searchOptions.filters.place)
            $scope.$watch('filters.place', function(value) {
                $scope.text.place = '';
                angular.forEach($scope.possiblePlaces, function(elem) {
                    if (value !== "" && value == elem.value) {
                        $scope.text.place = elem.label;
                        return;
                    }
                });
            }, true);
    }

    function getRangeString(from, to) {
        if (from === undefined || from == null || from.length == 0)
            from = '';
        if (to === undefined || to == null || to.length == 0)
            to = '';
        if (from == '' && to != '') {
            return "poniżej " + to;
        } else if (from != '' && to == '') {
            return "powyżej " + from;
        } else if (from == '' && to == '') {
            return "";
        } else {
            return from + " - " + to;
        }
    }

}]);

app.controller('RatingCtrl', ['$scope','AdService','$timeout',function($scope, AdService, $timeout) {
    init();

    function init() {
        $scope.max = 5;
        $scope.isReadonly = false;
        $scope.adModel.tooltip.ranked = false;
        $scope.rate = Math.floor($scope.ad.rank);
        $scope.labelValue = Math.floor($scope.ad.rank);

        $scope.hoveringOver = function(value) {
            $scope.overStar = value;
            $scope.labelValue = value;
        };
        $scope.onLeave = function() {
            $scope.labelValue = Math.floor($scope.ad.rank);
            $scope.overStar = null;
            $scope.rate = Math.floor($scope.ad.rank);
        };

        $scope.$watch('rate', function(value) {
            if ($scope.overStar) {
                var ad = $scope.$parent.ad;
                AdService.rate(ad.id, value, function() {
                    // $scope.adModel.tooltip.ranked = true;
                    $scope.$emit("RANKED", "ranked");
                });
            }
        });
        $scope.$watch('ad.rank', function(value) {
            $scope.rate = Math.floor($scope.ad.rank);
            $scope.labelValue = Math.floor($scope.ad.rank);
        });
        $scope.ratingStates = [{
            stateOn : 'icon-star icon-large',
            stateOff : 'icon-star-empty icon-large'
        }];

    }
}]);

app.controller('AdRegistrationCtrl', ['$scope','possibleBrands','possibleTags','$modal','filterFilter','ErrorFactory','$location','AdService','Auth','$routeParams',function($scope, possibleBrands, possibleTags, $modal, filterFilter, ErrorFactory, $location, AdService, Auth, $routeParams) {

    var brandModalScope = $scope.$new();
    var brandModal = null;
    var tagModalScope = $scope.$new();
    var tagModal = null;

    init();

    function init() {
        $scope.regModel = {};
        $scope.regModel.tags = [];
        $scope.selectedTags = "";
        $scope.possibleTags = possibleTags || [];
        $scope.yearMax = new Date().getFullYear();
        $scope.errorMessages = ErrorFactory.getErrorMessages({
            pattern : {
                url : "Tylko prawidłowe linki youtube"
            }
        });

        $scope.companyUser = Auth.hasRole('company');
        if ($routeParams.contestId) {
            $scope.regModel.contestId = $routeParams.contestId;
            $scope.regModel.brandId = possibleBrands.brand.id;
        }
        if ($routeParams.parentId) {
            $scope.regModel.parentId = $routeParams.parentId;
            $scope.regModel.brandId = possibleBrands.brand.id;
        }
        if ($routeParams.brandId) {
            $scope.regModel.brandId = $routeParams.brandId;
        }
        if ($scope.companyUser) {
            var wistiaProjectId = possibleBrands.brand.wistiaProjectId;
            if ($routeParams.parentId || $routeParams.contestId) {
                $scope.$watch('regModel.videoId', function(value) {
                    if ($scope.regModel.videoId) {
                        AdService.postAd($scope.regModel, function(data) {
                            $location.path("/reklamy/" + data.response + "/" + $scope.regModel.title.replace(/ /g, '-'));
                        });
                    }
                });
            } else {
                $scope.$watch('regModel.videoId', function(value) {
                    if ($scope.regModel.videoId) {
                        AdService.postBrandAd($scope.regModel.brandId, $scope.regModel, function(data) {
                            $location.path("/marki/" + $scope.regModel.brandId + "/reklamy");
                        });
                    }
                });
            }
            var uploadWidgetCb = {
                'uploadSuccess' : function(jsonFile) {
                    $scope.regModel.videoId = jsonFile.hashed_id;
                    $scope.regModel.thumbnail = jsonFile.thumbnail.url;
                    $scope.regModel.duration = Math.floor(jsonFile.duration);
                    $scope.$apply();
                },
                'fileQueued' : function(file) {
                    var ext = file.name.split('.').pop().toLowerCase();
                    if ($.inArray(ext, ['mov','mpg','avi','flv','f4v','mp4','m4v','asf','wmv','vob','mod','3gp','mkv','divx','xvid']) == -1) {
                        this.cancelUpload();
                        this.reset();
                        $(".upload-status").addClass('hidden');
                        $("#upload-file-error").html("Tylko pliki z roższerzeniem mov, mpg, avi, flv, f4v, mp4, m4v, asf, wmv, vob, mod, 3gp, mkv, divx, xvid");
                        $("#upload-file-error").removeClass('hidden');
                    } else {
                        $(".upload-status").removeClass('hidden');
                        $("#upload-file-error").addClass('hidden');
                    }
                }
            };
            var uploadWidget = new wistia.UploadWidget({
                divId : 'wistia-upload-widget',
                publicProjectId : wistiaProjectId,
                buttonText : "Dodaj reklamę",
                callbacks : uploadWidgetCb
            });
        } else {
            $scope.possibleBrands = possibleBrands || [];
            brandModalScope.items = possibleBrands;
            brandModalScope.title = "Wybierz markę";
            brandModalScope.type = 0;
            $scope.openBrandsModal = function() {
                brandModal = $modal.open({
                    templateUrl : 'tagsBrandsPickerModal.html',
                    scope : brandModalScope

                });

            };
            brandModalScope.click = function(item) {
                $scope.selectedBrandName = item.name;
                $scope.regModel.brandId = item.id;
                brandModal.close();
            };
        }

        tagModalScope.items = possibleTags;
        tagModalScope.title = "Wybierz kategorie";
        tagModalScope.type = 1;

        $scope.openTagsModal = function() {
            tagModal = $modal.open({
                templateUrl : 'tagsBrandsPickerModal.html',
                scope : tagModalScope

            });

        };
        tagModalScope.ok = function() {
            tagModal.close();
        };

        $scope.register = function() {
            AdService.postAd($scope.regModel, function(data) {
                $location.path("reklamy/" + data.response + "/" + $scope.regModel.title.replace(/ /g, '-'));
            });
        };

        $scope.click = function(item, $event) {
            item.selected = !item.selected;
            if (item.selected) {
                $scope.regModel.tags.push(item.id);
                $scope.selectedTags = $scope.regModel.tags.join();
            } else {
                var i = $scope.regModel.tags.indexOf(item.id);
                $scope.regModel.tags.splice(i, 1);
                $scope.selectedTags = $scope.regModel.tags.join();
            }
            if ($event) {
                $event.stopPropagation();
            }
        };

    }

}]);

app.controller('TopAdsCtrl', ['$scope','AdService',function($scope, AdService) {
    init();
    function init() {
        $scope.model = {};
        AdService.getAds({
            orderBy : "rank",
            order : "desc",
            page : "1",
            perPage: "5"
        }, function(data) {
            $scope.model.topAds = data.ads;
        });
        $scope.roundRank = function(rank) {
            return Math.floor(rank);
        };
    }
}]);
app.controller('AdAdminPanelCtrl', ['$scope','AdService',function($scope, AdService) {
    init();
    function init() {
        $scope.model = {};
        $scope.changePlace = function(ad, place, placeText) {
            AdService.changeState(ad.id, {
                place : place
            }, function(data) {
                ad.place = placeText;
            });
        };
        $scope.changeApproved = function(ad) {
            AdService.changeState(ad.id, {
                approved : !ad.approved
            }, function(data) {
                ad.approved = !ad.approved;
            });
        };
        $scope.changeAgeProtected = function(ad) {
            AdService.changeState(ad.id, {
                ageProtected : !ad.ageProtected
            }, function(data) {
                ad.ageProtected = !ad.ageProtected;
            });
        };
    }
}]);
app.controller('AdCtrl', ['$scope','AdService','$rootScope',"$route",'$location','$timeout',function($scope, AdService, $rootScope, $route, $location, $timeout) {
    init();
    function init() {
        $scope.adModel = {};
        $scope.adModel.tooltip = {};
        // $scope.model.ad = {};
        // $scope.model.facebookAdThumbnail =
        // $scope.ad.thumbnail.substring(0,$scope.ad.thumbnail.indexOf("?"));
        $scope.tooltip = {};
        $scope.reportAbuse = function() {
            AdService.reportAbuse($scope.ad.id, $scope.adModel.abuseMessage, function() {
                $scope.$emit("REPORT_ABUSE");
                $scope.adModel.abuseMessage = "";
                $timeout(function() {
                    $scope.bottomVideoPanelView = "";
                }, 1000);

            });
        };

        if ($route.current.$$route.writeOGMetaTags) {
            $scope.metatags.title = "Spotnik.pl - reklamy: " + $scope.ad.title;
            $scope.metatags.description = $scope.ad.description.substring(0, 160);
            $scope.metatags.image = $scope.ad.videoData.bigThumbnail;
            $scope.metatags.url = "http://www.spotnik.pl" + "/#!/reklamy/" + $scope.ad.id + "/" + $scope.ad.title.replace(/ /g, '-');
        }
    }
}]);
app.controller("SearchInputCtrl", ["$scope","$location",function($scope, $location) {
    init();
    function init() {
        $scope.model = {};
        $scope.search = function() {
            $location.path("/szukaj/");
            $location.search({});
            $location.search($scope.model);
        };
    }
}]);
