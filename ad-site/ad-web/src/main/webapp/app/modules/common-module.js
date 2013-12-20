angular.module('st-common-module', ['ngCookies','angularLocalStorage'])

.controller("PagingCtrl", ["$scope","$location",function($scope, $location) {
    init();
    function init() {
        $scope.filters.page = parseInt($scope.filters.page) || 1;
    }
    $scope.$watch('filters.page', function(v) {
        $scope.currentPage = parseInt($scope.filters.page) || 1;
    });
}]).provider('CommonFunctions', function() {
    var self = this;

    this.wordMappings = {};

    this.setWordMappings = function(wordMappings) {
        this.wordMappings = wordMappings;
    }

    this.$get = ['$rootScope','$location',function($rootScope, $location) {
        $rootScope.alerts = [];

        $rootScope.closeAlert = function(index) {
            $rootScope.alerts.splice(index, 1);
        }

        return {
            pushAlert : function(type, message) {
                $rootScope.alerts.push({
                    type : type,
                    msg : message
                });
            },
            pushAlertByType : function(type) {
                switch (type) {
                case "login":
                    this.pushAlert('danger', "Musisz się najpierw zalogować.");
                    break;
                case "accessDenied":
                    this.pushAlert('danger', "Nie masz wystarczających uprawnień.");
                    break;
                case "badRequest":
                    this.pushAlert('danger', "Ups, coś poszło nie tak. Proszę, postępuj zgodnie z podpowiedziami");
                    break;
                }
            },
            getPostHeader : function() {
                return {
                    headers : {
                        "Content-Type" : "application/x-www-form-urlencoded"
                    }
                };
            },
            translateWord : function(word) {
                return self.wordMappings[word];
            },
            translateUrl : function(url) {
                var parts = url.split('/');
                if (parts[0] == '')
                    delete parts[0];
                var translated = "/";
                var translateUrlObject = this;
                angular.forEach(parts, function(value) {
                    var word = translateUrlObject.translateWord(value);
                    if (word == "")
                        translated += word;
                    else {
                        translated += word || value;
                        translated += "/";
                    }
                });
                if (translated.length > 1)
                    translated = translated.slice(0, translated.length - 1);
                return translated;
            },
            getTranslatedUrl : function() {
                return this.translateUrl($location.path());
            },
        }
    }];
}).factory('ErrorFactory', function() {
    this.errorMessages = {
        pattern : {
            def : "Niewłaściwe znaki"
        },
        minlength : {
            def : 'Min {{data-ng-minlength}} znaków'
        },
        maxlength : {
            def : 'Max {{data-ng-maxlength}} znaków'
        },
        fieldMatch : {
            def : 'Pola nie są takie same'
        },
        email : {
            def : 'Niepoprawny email'
        },
        dateValid : {
            def : 'Niepoprawna data'
        },
        min : {
            def : 'Podana liczba musi być większa od {{data-min}}'
        },
        max : {
            def : 'Podana liczba musi być mniejsza od {{data-max}}'
        },
    }
    this.getErrorMessages = function(customErrorMessages) {
        return $.extend(true, this.errorMessages, customErrorMessages);
    }
    return this;
});