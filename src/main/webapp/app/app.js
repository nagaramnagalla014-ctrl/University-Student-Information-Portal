'use strict';

angular.module('portalApp', ['ngRoute'])

.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {

    $routeProvider
        .when('/login', {
            templateUrl: 'app/views/login.html',
            controller: 'AuthController'
        })
        // Student routes
        .when('/student/dashboard', {
            templateUrl: 'app/views/student/dashboard.html',
            controller: 'StudentDashboardController',
            resolve: { auth: ['AuthGuard', function (g) { return g.requireRole('STUDENT'); }] }
        })
        .when('/student/courses', {
            templateUrl: 'app/views/student/courses.html',
            controller: 'StudentCoursesController',
            resolve: { auth: ['AuthGuard', function (g) { return g.requireRole('STUDENT'); }] }
        })
        .when('/student/grades', {
            templateUrl: 'app/views/student/grades.html',
            controller: 'StudentGradesController',
            resolve: { auth: ['AuthGuard', function (g) { return g.requireRole('STUDENT'); }] }
        })
        .when('/student/transcript', {
            templateUrl: 'app/views/student/transcript.html',
            controller: 'StudentTranscriptController',
            resolve: { auth: ['AuthGuard', function (g) { return g.requireRole('STUDENT'); }] }
        })
        // Faculty routes
        .when('/faculty/dashboard', {
            templateUrl: 'app/views/faculty/dashboard.html',
            controller: 'FacultyDashboardController',
            resolve: { auth: ['AuthGuard', function (g) { return g.requireRole('FACULTY'); }] }
        })
        .when('/faculty/grades', {
            templateUrl: 'app/views/faculty/grades.html',
            controller: 'FacultyGradesController',
            resolve: { auth: ['AuthGuard', function (g) { return g.requireRole('FACULTY'); }] }
        })
        .when('/faculty/attendance', {
            templateUrl: 'app/views/faculty/attendance.html',
            controller: 'FacultyAttendanceController',
            resolve: { auth: ['AuthGuard', function (g) { return g.requireRole('FACULTY'); }] }
        })
        // Admin routes
        .when('/admin/dashboard', {
            templateUrl: 'app/views/admin/dashboard.html',
            controller: 'AdminDashboardController',
            resolve: { auth: ['AuthGuard', function (g) { return g.requireRole('ADMIN'); }] }
        })
        .when('/admin/students', {
            templateUrl: 'app/views/admin/students.html',
            controller: 'AdminStudentsController',
            resolve: { auth: ['AuthGuard', function (g) { return g.requireRole('ADMIN'); }] }
        })
        .when('/admin/courses', {
            templateUrl: 'app/views/admin/courses.html',
            controller: 'AdminCoursesController',
            resolve: { auth: ['AuthGuard', function (g) { return g.requireRole('ADMIN'); }] }
        })
        .otherwise({ redirectTo: '/login' });
}])

// Auth guard factory
.factory('AuthGuard', ['$q', '$location', 'AuthService', function ($q, $location, AuthService) {
    return {
        requireRole: function (role) {
            var deferred = $q.defer();
            var user = AuthService.getUser();
            if (user && user.authenticated && (role === 'ANY' || user.role === role)) {
                deferred.resolve(user);
            } else {
                $location.path('/login');
                deferred.reject('Not authorized');
            }
            return deferred.promise;
        }
    };
}])

// Root controller: handles navbar, notifications, logout
.controller('RootController', ['$scope', '$rootScope', '$location', 'AuthService', 'ApiService',
    function ($scope, $rootScope, $location, AuthService, ApiService) {

        $scope.currentUser = AuthService.getUser() || {};
        $scope.unreadCount = 0;

        $rootScope.$on('userLoggedIn', function (e, user) {
            $scope.currentUser = user;
            loadUnreadCount();
        });

        $rootScope.$on('userLoggedOut', function () {
            $scope.currentUser = {};
            $scope.unreadCount = 0;
        });

        function loadUnreadCount() {
            if ($scope.currentUser && $scope.currentUser.authenticated) {
                ApiService.get('/api/notifications/unread-count')
                    .then(function (res) { $scope.unreadCount = res.data.count; });
            }
        }

        $scope.isActive = function (path) {
            return $location.path() === path;
        };

        $scope.logout = function () {
            AuthService.logout().then(function () {
                $rootScope.$emit('userLoggedOut');
                $location.path('/login');
            });
        };

        // Initial check
        if ($scope.currentUser.authenticated) {
            loadUnreadCount();
        } else {
            $location.path('/login');
        }
    }
])

// Attach root controller to body via ng-controller (referenced from index.html via ng-app bootstrap)
.run(['$rootScope', '$location', 'AuthService', function ($rootScope, $location, AuthService) {
    $rootScope.$on('$routeChangeStart', function (e, next) {
        var user = AuthService.getUser();
        if (!user || !user.authenticated) {
            if (next && next.$$route && next.$$route.templateUrl !== 'app/views/login.html') {
                $location.path('/login');
            }
        }
    });
}]);
