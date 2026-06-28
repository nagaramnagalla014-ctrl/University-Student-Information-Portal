'use strict';

angular.module('portalApp')

.controller('AuthController', ['$scope', '$location', 'AuthService',
    function ($scope, $location, AuthService) {

        // Redirect if already logged in
        var user = AuthService.getUser();
        if (user && user.authenticated) {
            redirectByRole(user.role);
        }

        $scope.credentials = { email: '', password: '', role: 'STUDENT' };
        $scope.loading = false;
        $scope.error = null;

        $scope.login = function () {
            $scope.loading = true;
            $scope.error = null;
            AuthService.login($scope.credentials.email, $scope.credentials.password, $scope.credentials.role)
                .then(function (res) {
                    if (res.data.success) {
                        redirectByRole(res.data.role);
                    } else {
                        $scope.error = res.data.message || 'Login failed. Check credentials.';
                    }
                })
                .catch(function (err) {
                    $scope.error = (err.data && err.data.message) || 'Server error. Please try again.';
                })
                .finally(function () { $scope.loading = false; });
        };

        function redirectByRole(role) {
            if (role === 'STUDENT')      $location.path('/student/dashboard');
            else if (role === 'FACULTY') $location.path('/faculty/dashboard');
            else if (role === 'ADMIN')   $location.path('/admin/dashboard');
        }
    }
]);
