'use strict';

angular.module('portalApp')

// --- Faculty Dashboard ---
.controller('FacultyDashboardController', ['$scope', 'ApiService', 'AuthService',
    function ($scope, ApiService, AuthService) {
        var user = AuthService.getUser();
        $scope.user = user;
        $scope.courses = [];
        $scope.notifications = [];

        ApiService.get('/api/faculty/' + user.userId).then(function (res) {
            $scope.faculty = res.data;
        });
        ApiService.get('/api/faculty/' + user.userId + '/courses').then(function (res) {
            $scope.courses = res.data;
        });
        ApiService.get('/api/notifications').then(function (res) {
            $scope.notifications = res.data.slice(0, 5);
        });
    }
])

// --- Faculty Upload Grades ---
.controller('FacultyGradesController', ['$scope', 'ApiService', 'AuthService',
    function ($scope, ApiService, AuthService) {
        var user = AuthService.getUser();
        $scope.courses = [];
        $scope.selectedCourse = null;
        $scope.enrollments = [];
        $scope.success = null;
        $scope.error = null;

        $scope.grades = ['O', 'A+', 'A', 'B+', 'B', 'C+', 'C', 'D', 'F'];
        $scope.gradePoints = { 'O': 10, 'A+': 9, 'A': 8, 'B+': 7, 'B': 6, 'C+': 5, 'C': 4, 'D': 3, 'F': 0 };

        ApiService.get('/api/faculty/' + user.userId + '/courses').then(function (res) {
            $scope.courses = res.data;
        });

        $scope.selectCourse = function (course) {
            $scope.selectedCourse = course;
            $scope.enrollments = [];
            ApiService.get('/api/faculty/courses/' + course.courseId + '/students').then(function (res) {
                $scope.enrollments = res.data;
            });
        };

        $scope.updateGrade = function (enrollment) {
            var payload = {
                enrollmentId: enrollment.enrollmentId,
                grade: enrollment.grade,
                gradePoints: $scope.gradePoints[enrollment.grade] || 0,
                remarks: enrollment.remarks
            };
            ApiService.put('/api/faculty/grades', payload)
                .then(function () {
                    $scope.success = 'Grade updated for ' + enrollment.student.firstName;
                    setTimeout(function () { $scope.$apply(function () { $scope.success = null; }); }, 3000);
                })
                .catch(function (err) { $scope.error = err.data && err.data.message; });
        };
    }
])

// --- Faculty Attendance ---
.controller('FacultyAttendanceController', ['$scope', 'ApiService', 'AuthService',
    function ($scope, ApiService, AuthService) {
        var user = AuthService.getUser();
        $scope.courses = [];
        $scope.selectedCourse = null;
        $scope.enrollments = [];
        $scope.success = null;
        $scope.error = null;

        ApiService.get('/api/faculty/' + user.userId + '/courses').then(function (res) {
            $scope.courses = res.data;
        });

        $scope.selectCourse = function (course) {
            $scope.selectedCourse = course;
            ApiService.get('/api/faculty/courses/' + course.courseId + '/students').then(function (res) {
                $scope.enrollments = res.data;
            });
        };

        $scope.updateAttendance = function (enrollment) {
            ApiService.put('/api/faculty/attendance', {
                enrollmentId: enrollment.enrollmentId,
                attendancePercentage: enrollment.attendancePercentage
            })
            .then(function () {
                $scope.success = 'Attendance updated for ' + enrollment.student.firstName;
                setTimeout(function () { $scope.$apply(function () { $scope.success = null; }); }, 3000);
            })
            .catch(function (err) { $scope.error = err.data && err.data.message; });
        };
    }
]);
