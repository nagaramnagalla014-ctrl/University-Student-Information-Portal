'use strict';

angular.module('portalApp')

// --- Student Dashboard ---
.controller('StudentDashboardController', ['$scope', 'ApiService', 'AuthService',
    function ($scope, ApiService, AuthService) {
        var user = AuthService.getUser();
        $scope.user = user;
        $scope.enrollments = [];
        $scope.stats = { total: 0, graded: 0, avgAttendance: 0, gpa: 0 };

        ApiService.get('/api/students/' + user.userId + '/enrollments').then(function (res) {
            $scope.enrollments = res.data;
            $scope.stats.total = res.data.length;
            var graded = 0, attendanceSum = 0;
            res.data.forEach(function (e) {
                if (e.grade) graded++;
                attendanceSum += (e.attendancePercentage || 0);
            });
            $scope.stats.graded = graded;
            $scope.stats.avgAttendance = res.data.length > 0 ?
                (attendanceSum / res.data.length).toFixed(1) : 0;
        });

        ApiService.get('/api/students/' + user.userId).then(function (res) {
            $scope.stats.gpa = res.data.gpa || 0;
            $scope.student = res.data;
        });

        ApiService.get('/api/notifications').then(function (res) {
            $scope.notifications = res.data.slice(0, 5);
        });
    }
])

// --- Student Courses (Register) ---
.controller('StudentCoursesController', ['$scope', 'ApiService', 'AuthService',
    function ($scope, ApiService, AuthService) {
        var user = AuthService.getUser();
        $scope.available = [];
        $scope.enrolled = [];
        $scope.loading = true;
        $scope.success = null;
        $scope.error = null;
        $scope.searchKeyword = '';

        function loadData() {
            ApiService.get('/api/courses/available').then(function (res) {
                $scope.available = res.data;
                $scope.loading = false;
            });
            ApiService.get('/api/students/' + user.userId + '/enrollments').then(function (res) {
                $scope.enrolled = res.data;
                $scope.enrolledCourseIds = res.data.map(function (e) { return e.course.courseId; });
            });
        }
        loadData();

        $scope.isEnrolled = function (courseId) {
            return $scope.enrolledCourseIds && $scope.enrolledCourseIds.indexOf(courseId) >= 0;
        };

        $scope.enroll = function (course) {
            $scope.error = null;
            ApiService.post('/api/students/' + user.userId + '/enroll/' + course.courseId, {})
                .then(function () {
                    $scope.success = 'Successfully enrolled in ' + course.courseName;
                    loadData();
                    setTimeout(function () { $scope.$apply(function () { $scope.success = null; }); }, 3000);
                })
                .catch(function (err) {
                    $scope.error = (err.data && err.data.message) || 'Enrollment failed';
                });
        };

        $scope.drop = function (enrollment) {
            if (!confirm('Drop ' + enrollment.course.courseName + '?')) return;
            ApiService.delete('/api/students/' + user.userId + '/enroll/' + enrollment.enrollmentId)
                .then(function () { loadData(); })
                .catch(function (err) { $scope.error = err.data.message; });
        };

        $scope.search = function () {
            if ($scope.searchKeyword.length < 2) { loadData(); return; }
            ApiService.get('/api/courses/search', { keyword: $scope.searchKeyword })
                .then(function (res) { $scope.available = res.data; });
        };
    }
])

// --- Student Grades ---
.controller('StudentGradesController', ['$scope', 'ApiService', 'AuthService',
    function ($scope, ApiService, AuthService) {
        var user = AuthService.getUser();
        $scope.enrollments = [];
        $scope.gpa = 0;

        ApiService.get('/api/students/' + user.userId + '/enrollments').then(function (res) {
            $scope.enrollments = res.data;
        });
        ApiService.get('/api/students/' + user.userId).then(function (res) {
            $scope.gpa = res.data.gpa;
            $scope.student = res.data;
        });
    }
])

// --- Student Transcript ---
.controller('StudentTranscriptController', ['$scope', 'ApiService', 'AuthService',
    function ($scope, ApiService, AuthService) {
        var user = AuthService.getUser();

        ApiService.get('/api/students/' + user.userId).then(function (res) {
            $scope.student = res.data;
        });
        ApiService.get('/api/students/' + user.userId + '/enrollments').then(function (res) {
            $scope.enrollments = res.data;
            var totalCredits = 0, earnedPoints = 0;
            res.data.forEach(function (e) {
                if (e.grade && e.gradePoints != null) {
                    totalCredits += (e.course.credits || 0);
                    earnedPoints += (e.course.credits || 0) * e.gradePoints;
                }
            });
            $scope.totalCredits = totalCredits;
            $scope.cgpa = totalCredits > 0 ? (earnedPoints / totalCredits).toFixed(2) : '—';
        });

        $scope.print = function () { window.print(); };
    }
]);
