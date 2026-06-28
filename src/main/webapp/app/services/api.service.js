'use strict';

angular.module('portalApp')

.factory('ApiService', ['$http', function ($http) {
    var baseUrl = '';  // same origin, relative URLs

    return {
        get: function (url, params) {
            return $http.get(baseUrl + url, { params: params, withCredentials: true });
        },
        post: function (url, data) {
            return $http.post(baseUrl + url, data, { withCredentials: true });
        },
        put: function (url, data) {
            return $http.put(baseUrl + url, data, { withCredentials: true });
        },
        delete: function (url) {
            return $http.delete(baseUrl + url, { withCredentials: true });
        }
    };
}])

.factory('AuthService', ['$http', '$rootScope', function ($http, $rootScope) {
    var USER_KEY = 'portal_user';

    function saveUser(user) {
        localStorage.setItem(USER_KEY, JSON.stringify(user));
    }

    function getUser() {
        var str = localStorage.getItem(USER_KEY);
        return str ? JSON.parse(str) : null;
    }

    function clearUser() {
        localStorage.removeItem(USER_KEY);
    }

    return {
        login: function (email, password, role) {
            return $http.post('/api/auth/login', { email: email, password: password, role: role }, { withCredentials: true })
                .then(function (res) {
                    if (res.data.success) {
                        var user = {
                            authenticated: true,
                            userId: res.data.userId,
                            role: res.data.role,
                            name: res.data.name,
                            email: res.data.email
                        };
                        saveUser(user);
                        $rootScope.$emit('userLoggedIn', user);
                    }
                    return res;
                });
        },
        logout: function () {
            return $http.post('/api/auth/logout', {}, { withCredentials: true })
                .then(function () {
                    clearUser();
                });
        },
        getUser: getUser
    };
}]);
