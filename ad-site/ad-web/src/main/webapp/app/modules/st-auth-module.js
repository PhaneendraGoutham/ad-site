angular.module('st-auth-module', ['ngCookies','angularLocalStorage']).provider('CommonFunctions', function() {
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
                return self.wordMappings[word] || word;
            },
            translateUrl : function(url) {
                var parts = url.split('/');
                if (parts[0] == '')
                    delete parts[0];
                var translated = "/";
                var translateUrlObject = this;
                angular.forEach(parts, function(value) {
                    translated += translateUrlObject.translateWord(value) || value;
                    translated += "/";
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
}).provider('Auth', function() {
    var self = this;

    this.loginUrl = "/user/login";
    this.userStatusUrl = "/user/login/status";
    this.usernameExistUrl = '/user/username';
    this.emailExistUrl = '/user/email';
    this.userRoles = {
        public : 'public',
        user : 'user',
        admin : 'admin',
    };
    this.protectedResourcesList = [];
    this.setProtectedResourcesList = function(protectedResourcesList) {
        angular.forEach(protectedResourcesList, function(val) {
            val.url = val.url.replace(/\//g, "\\/").replace(/\*/g, "[^ ]*");
            val.url = new RegExp(val.url, "i");
        });
        this.protectedResourcesList = protectedResourcesList;

    }
    this.setLoginUrl = function(loginUrl) {
        this.loginUrl = loginUrl;
    }

    this.setUserStatusUrl = function(userStatusUrl) {
        this.userStatus = userStatusUrl;
    }
    this.setUserRoles = function(userRoles) {
        this.userRoles = userRoles;
    }
    this.setUsernameExistUrl = function(usernameExistUrl) {
        this.usernameExistUrl = usernameExistUrl;
    }
    this.setEmailExistUrl = function(emailExistUrl) {
        this.emailExistUrl = emailExistUrl;
    }

    this.$get = ['$injector','$cookieStore','storage','$rootScope',function($injector, $cookieStore, storage, $rootScope) {
        $rootScope.currentUser = storage.get('loggedInUser') || {
            roles : [this.userRoles.public]
        };

        function changeUser(user) {
            angular.extend($rootScope.currentUser, user);
        }
        var deleteTokens = false;
        return {
            setDeleteTokens : function() {
                deleteTokens = true;
            },
            usernameExist : function(username, success, error) {
                var $http = $injector.get("$http");
                $http({
                    method : 'GET',
                    url : self.usernameExistUrl + "?username=" + username
                }).success(function(data) {
                    success(data.response);
                }).error(error);
            },
            emailExist : function(email, success, error) {
                var $http = $injector.get("$http");
                $http({
                    method : 'GET',
                    url : self.emailExistUrl + "?email=" + email
                }).success(function(data) {
                    success(data.response);
                }).error(error);
            },
            login : function(user, success, error) {
                var $http = $injector.get("$http");
                var data = user;
                if (deleteTokens) {
                    data.deleteTokens = true;
                }
                $http.post(self.loginUrl, $.param(data), {
                    headers : {
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).success(function(data) {
                    storage.set('authToken', {
                        series : data.series,
                        token : data.token
                    });
                    changeUser(data.principal);
                    storage.set('loggedInUser', $rootScope.currentUser);
                    deleteTokens = false;
                    if (success)
                        success(data);
                });
            },
            changeUser : function(user) {
                changeUser(user);
            },
            updateLoggedInUser : function(success, error) {
                var $http = $injector.get("$http");
                $http({
                    method : "GET",
                    url : self.userStatusUrl
                }).success(function(data) {
                    changeUser(data);
                    storage.set('loggedInUser', $rootScope.currentUser);
                    if (success)
                        success(data);
                }).error(error);
            },
            updateAuthToken : function(authToken) {
                storage.set('authToken', authToken);
            },
            logout : function() {
                storage.remove('authToken');
                storage.remove('loggedInUser');
                $rootScope.currentUser = {};
                $rootScope.currentUser = {
                    roles : [self.userRoles.public]
                };
            },
            getCredentials : function() {
                return storage.get('authToken');
            },
            getAuthHeader : function() {
                var cred = this.getCredentials();
                if (cred) {
                    return {
                        "AuthToken" : cred.series + ":" + cred.token
                    }
                }
                return {};
            },
            isLoggedIn : function() {
                var authToken = storage.get('authToken');
                return authToken ? true : false;
            },
            hasRole : function(demandedRole) {
                var authorized = false;
                angular.forEach($rootScope.currentUser.roles, function(value) {
                    if (value === demandedRole) {
                        authorized = true;
                    }
                });
                return authorized;
            },
            isAuthorized : function(access, params, onlyRole) {
                access = self.userRoles[access];
                var $q = $injector.get("$q");
                var deferred = $q.defer();
                var url = null;
                var demandedRole = null;
                if (typeof access == 'string' || access instanceof String) {
                    demandedRole = access;
                } else {
                    demandedRole = access.role;
                    url = access.url;
                }
                var authorized = false;
                angular.forEach($rootScope.currentUser.roles, function(value) {
                    if (value === demandedRole) {
                        authorized = true;
                    }
                });

                if (url && authorized && !(onlyRole)) {
                    var $http = $injector.get("$http");

                    if (params === undefined)
                        params = $injector.get("$routeParams");

                    var urlParams = url.match(/:[^\/]*/g);
                    angular.forEach(urlParams, function(elem) {
                        url = url.replace(elem, params[elem.substring(1)]);
                    });
                    $http.get(url).success(function(data) {
                        if (data.response)
                            return deferred.resolve(data.response && authorized);
                        else
                            return deferred.reject(data.response && authorized);
                    });
                    return deferred.promise;
                }
                if (authorized) {
                    deferred.resolve(authorized);
                } else {
                    deferred.reject(authorized);
                }
                return deferred.promise;
            },
            protectedResourcesList : this.protectedResourcesList,
        }

    }];
}).provider('stAuthInterceptor', function() {
    var self = this;
    this.loginUrl = '/login';
    this.baseUrl = 'http://localhost:8080';
    this.setLoginUrl = function(loginUrl) {
        this.loginUrl = loginUrl;
    }

    this.accessDeniedUrl = '/accessDenied';

    this.setAccessDeniedUrl = function(accessDeniedUrl) {
        this.accessDeniedUrl = accessDeniedUrl;
    }
    this.$get = ['Auth','Base64','$q','$location','$log','CommonFunctions',function(Auth, Base64, $q, $location, $log, CommonFunctions) {

        function getProtectedResource(resource, method) {
            var protectedResource = null;
            angular.forEach(Auth.protectedResourcesList, function(value) {
                if (resource.match(value.url) && method === value.method) {
                    protectedResource = value;
                    return;
                }

            });
            return protectedResource;
        }
        function updateAuthToken(headers) {
            var AuthToken = headers('AuthToken');
            if (AuthToken && AuthToken.indexOf(":") != -1) {
                var tokenParts = AuthToken.split(":");
                Auth.updateAuthToken({
                    series : tokenParts[0],
                    token : tokenParts[1]
                });
            }
        }
        return {
            request : function(config) {
                if (config.url.substring(0, 3) != "app" && config.url.substring(0, 8) != "template") {
                    var protectedResource = getProtectedResource(config.url, config.method);
                    if (protectedResource) {
                        var userCredentials = Auth.getCredentials();
                        if (userCredentials) {
                            if (protectedResource.access) {
                                $q.when(Auth.isAuthorized(protectedResource.access, null, true)).then(null, function() {
                                    return $q.reject({
                                        config : config,
                                        status : 403
                                    });
                                });
                            }
                            var credentialsToEncode = userCredentials.series + ":" + userCredentials.token;
                            // var encodedCredentials =
                            // Base64.encode(credentialsToEncode);
                            angular.extend(config.headers, {
                                'AuthToken' : credentialsToEncode
                            });

                        } else {
                            return $q.reject({
                                config : config,
                                status : 401
                            });
                        }
                    }
                    if (!config.url.match('.html'))
                        config.url = self.baseUrl + config.url;
                }
                return config || $q.when(config);
            },
            responseError : function(rejection) {
                if (rejection.headers)
                    updateAuthToken(rejection.headers);
                switch (rejection.status) {
                case 401:
                    if (rejection.config.headers['AuthToken']) {
                        CommonFunctions.pushAlert('danger', "Twoja sesja wygasła proszę zaloguj się jeszcze raz");
                        $location.path(self.loginUrl);
                    } else if (rejection.config.data.match("login")) {
                        CommonFunctions.pushAlert('danger', "Błędny login lub hasło")
                    } else {
                        CommonFunctions.pushAlertByType("login");
                        $location.path(self.loginUrl);
                    }
                    return $q.reject(rejection);
                    break;
                case 403:
                    CommonFunctions.pushAlertByType("accessDenied");
                    return $q.reject(rejection);
                    break;
                case 400:
                    CommonFunctions.pushAlertByType("badRequest");
                    return $q.reject(rejection);
                    break;
                case 441:
                    CommonFunctions.pushAlert('danger', "Ktoś prawdopodobnie włamał się na Twoje konto. Zaloguj się ponownie i zmień hasło jak najszybciej!");
                    Auth.setDeleteTokens();
                    $location.path(self.loginUrl);
                    return $q.reject(rejection);
                case 404:
                    CommonFunctions.pushAlert('danger', "Strona której szukasz nie istnieje.");
                    return $q.reject(rejection);
                default:
                    return $q.reject(rejection);

                }
            },
            response : function(response) {
                updateAuthToken(response.headers);
                return response || $q.when(response);
            },
        }
    }];
}).factory('Base64', function() {
    var keyStr = 'ABCDEFGHIJKLMNOP' + 'QRSTUVWXYZabcdef' + 'ghijklmnopqrstuv' + 'wxyz0123456789+/' + '=';
    return {
        encode : function(input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            do {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);

                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;

                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }

                output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2) + keyStr.charAt(enc3) + keyStr.charAt(enc4);
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);

            return output;
        },

        decode : function(input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            // remove all characters that are not A-Z, a-z, 0-9, +, /,
            // or =
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                alert("There were invalid base64 characters in the input text.\n" + "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" + "Expect errors in decoding.");
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            do {
                enc1 = keyStr.indexOf(input.charAt(i++));
                enc2 = keyStr.indexOf(input.charAt(i++));
                enc3 = keyStr.indexOf(input.charAt(i++));
                enc4 = keyStr.indexOf(input.charAt(i++));

                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;

                output = output + String.fromCharCode(chr1);

                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }

                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";

            } while (i < input.length);

            return output;
        }
    };
}).directive('stAccessShow', ['$animator','Auth','$rootScope','$q',function($animator, Auth, $rootScope, $q) {
    return {
        restrict : 'A',

        link : function(scope, elem, attrs) {
            var animate = $animator(scope, attrs);
            scope.hasRole = Auth.hasRole;
            scope.isAuthorized = Auth.isAuthorized;
            var registerWatch = scope.$eval(attrs.stRegisterWatch);
//            if (registerWatch) {
                scope.$watchCollection('currentUser', function() {
                    check();
                });
//            }else{
//                check();
//            }
            function check(){
                if (attrs.stAccessShow.match("hasRole")) {
                    var authorized = scope.$eval(attrs.stAccessShow);
                    animate[authorized ? 'show' : 'hide'](elem);
                } else {
                    var pPromise = scope.$eval(attrs.stAccessShow);
                    $q.when(pPromise).then(function(authorized) {
                        animate[authorized ? 'show' : 'hide'](elem);
                    }, function(authorized) {
                        animate['hide'](elem);
                    });
                }
            }
        }
    }
}]);