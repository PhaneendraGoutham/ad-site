﻿app.controller('AdSearchCtrl', ['AdFactory','$scope','filterFilter','$location','CommonFunctions','AdService','$http','possibleTags','possibleBrands','adBrowserWrapper',function(AdFactory, $scope, filterFilter, $location, CommonFunctions, AdService, $http, possibleTags, possibleBrands, adBrowserWrapper) {

    $scope.search = function() {
        var params = $scope.filters;
        // var tags = filterFilter($scope.possibleTags, {
        // selected : true
        // });
        // params.tagList = $.map(tags, function(o) {
        // return o["id"];
        // });
        // params.tagList = params.tagList.join(",");
        // var brands = filterFilter($scope.possibleBrands, {
        // selected : true
        // });
        // params.brandList = $.map(brands, function(o) {
        // return o["id"];
        // });
        // params.brandList = params.brandList.join(",");
        $.each(params, function(key, val) {
            if (val === undefined || val == null || val.length == 0) {
                delete params[key];
            }
        });
        // if ($scope.searchOptions.search) {
        // registerFiltersWatch();
        // }
        // userChangedParams = true;
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
        // if (!userChangedParams)
        // setFiltersFromUrlParams();
        // else
        // userChangedParams = false;
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

        // setFiltersFromUrlParams();

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
        $scope.model.ads = adBrowserWrapper.ads;
        $scope.total = adBrowserWrapper.total;
        // $scope.search();
        // userChangedParams = false;
        // getAds();
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
    //
    // function setFiltersFromUrlParams() {
    // if ($scope.searchOptions.search) {
    // var params = {};
    // angular.copy($location.search(), params);
    //
    // // if ($scope.searchOptions.filters.tags && $scope.possibleTags.length >
    // 0) {
    // // setTagsOrBrandsFromUrlParams($location.search().tagList,
    // $scope.possibleTags);
    // // }
    // // if ($scope.searchOptions.filters.brands &&
    // $scope.possibleBrands.length > 0) {
    // // setTagsOrBrandsFromUrlParams($location.search().brandList,
    // $scope.possibleBrands);
    // // }
    // // delete params.tags;
    // // delete params.brands;
    // angular.forEach($scope.filters, function(i, e) {
    // delete $scope.filters[e];
    // });
    // angular.forEach(params, function(paramV, paramK) {
    // angular.forEach($scope.searchOptions.filters, function(filterV, filterK)
    // {
    // if (paramK.match(filterK) && filterV) {
    // $scope.filters[paramK] = paramV;
    // return;
    // }
    // });
    // });
    // }
    // }

    // function setTagsOrBrandsFromUrlParams(array, possibles) {
    // if (array !== undefined) {
    // array = array instanceof Array ? array : array.split(',');
    // $.each(possibles, function(i, elem) {
    // $.each(array, function(ino, possible) {
    // if (possible == elem.id)
    // elem.selected = true;
    // });
    // });
    // } else {
    // $.each(possibles, function(i, elem) {
    // elem.selected = false;
    // });
    // }
    // }

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

    // return {
    // resolve : {
    // possibleTags : ['$http','$q',function($http, $q) {
    // fetchPossibleTags();
    // }],
    // }
    // };
}]);

app.controller('RatingCtrl', ['$scope','AdService',function($scope, AdService) {
    init();

    function init() {
        $scope.max = 5;
        $scope.isReadonly = false;
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

        $scope.onClick = function(ad, value) {
            alert(ad.id + " " + value);
            AdService.rate(ad.id, value, success);
        };
        $scope.$watch('rate', function(value) {
            if ($scope.overStar) {
                var ad = $scope.$parent.ad;
                AdService.rate(ad.id, value, function() {

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
        },];

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
            },
        });

        $scope.companyUser = Auth.hasRole('company');
        if ($routeParams.contestId) {
            $scope.regModel.contestId = $routeParams.contestId;
        }
        if (!$scope.companyUser) {
            $scope.possibleBrands = possibleBrands || [];
            brandModalScope.items = possibleBrands;
            brandModalScope.title = "Wybierz markę";
            brandModalScope.type = 0;
            $scope.openBrandsModal = function() {
                brandModal = $modal.open({
                    templateUrl : 'tagsBrandsPickerModal.html',
                    scope : brandModalScope,

                });

            };
            brandModalScope.click = function(item) {
                $scope.selectedBrandName = item.name;
                $scope.regModel.brandId = item.id;
                brandModal.close();
            };
        } else {
            $scope.regModel.brandId = $routeParams.id;
            $scope.$watch('regModel.videoId', function(value) {
                if ($scope.regModel.videoId) {
                    AdService.postBrandAd($scope.regModel.brandId, $scope.regModel, function(data) {
                        $location.path("/marki/" + $scope.regModel.brandId + "/reklamy");

                    });
                }
            });
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
                },

            };
            var uploadWidget = new wistia.UploadWidget({
                divId : 'wistia-upload-widget',
                publicProjectId : possibleBrands,
                buttonText : "Dodaj reklamę",
                callbacks : uploadWidgetCb
            });
        }

        tagModalScope.items = possibleTags;
        tagModalScope.title = "Wybierz kategorie";
        tagModalScope.type = 1;

        $scope.openTagsModal = function() {
            tagModal = $modal.open({
                templateUrl : 'tagsBrandsPickerModal.html',
                scope : tagModalScope,

            });

        };
        tagModalScope.ok = function() {
            tagModal.close();
        };

        $scope.register = function() {
            AdService.postAd($scope.regModel, function(data) {
                $location.path("reklamy/" + data.response);
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

app.controller("PagingCtrl", ["$scope", "$location", function($scope, $location){
    init();
    function init(){
        $scope.filters.page = parseInt($scope.filters.page) || 1;
    }
    $scope.$watch('filters.page', function(v){
        $scope.currentPage = parseInt($scope.filters.page) || 1;
    });
}]);