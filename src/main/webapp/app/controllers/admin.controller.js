'use strict';

angular.module('portalApp')

// --- Admin Dashboard ---
.controller('AdminDashboardController', ['$scope', 'ApiService',
    function ($scope, ApiService) {
        $scope.stats = {};
        ApiService.get('/api/admin/stats').then(function (res) {
            $scope.stats = res.data;
        });
        ApiService.get('/api/admin/students').then(function (res) {
            $scope.recentStudents = res.data.slice(0, 5);
        });
    }
])

// --- Admin Students ---
.controller('AdminStudentsController', ['$scope', 'ApiService',
    function ($scope, ApiService) {
        $scope.students = [];
        $scope.departments = [];
        $scope.semesters = [];
        $scope.newStudent = {};
        $scope.success = null;
        $scope.error = null;
        $scope.searchKeyword = '';

        function loadData() {
            ApiService.get('/api/admin/students').then(function (res) { $scope.students = res.data; });
        }
        ApiService.get('/api/admin/departments').then(function (res) { $scope.departments = res.data; });
        ApiService.get('/api/admin/semesters').then(function (res) { $scope.semesters = res.data; });
        loadData();

        $scope.createStudent = function () {
            ApiService.post('/api/admin/students', $scope.newStudent)
                .then(function () {
                    $scope.success = 'Student registered successfully';
                    $scope.newStudent = {};
                    loadData();
                    setTimeout(function () { $scope.$apply(function () { $scope.success = null; }); }, 3000);
                })
                .catch(function (err) { $scope.error = err.data && err.data.message; });
        };

        $scope.deleteStudent = function (student) {
            if (!confirm('Deactivate ' + student.firstName + ' ' + student.lastName + '?')) return;
            ApiService.delete('/api/admin/students/' + student.studentId)
                .then(function () { loadData(); })
                .catch(function (err) { $scope.error = err.data && err.data.message; });
        };

        $scope.filter = function () {
            if ($scope.searchKeyword.length < 2) { loadData(); return; }
            $scope.students = $scope.students.filter(function (s) {
                var kw = $scope.searchKeyword.toLowerCase();
                return (s.firstName + ' ' + s.lastName).toLowerCase().indexOf(kw) >= 0 ||
                    s.studentCode.toLowerCase().indexOf(kw) >= 0;
            });
        };
    }
])

// --- Admin Courses ---
.controller('AdminCoursesController', ['$scope', 'ApiService',
    function ($scope, ApiService) {
        $scope.courses = [];
        $scope.departments = [];
        $scope.faculty = [];
        $scope.semesters = [];
        $scope.newCourse = { credits: 3, maxStudents: 60 };
        $scope.success = null;
        $scope.error = null;

        function loadCourses() {
            ApiService.get('/api/courses').then(function (res) { $scope.courses = res.data; });
        }
        ApiService.get('/api/admin/departments').then(function (res) { $scope.departments = res.data; });
        ApiService.get('/api/admin/faculty').then(function (res) { $scope.faculty = res.data; });
        ApiService.get('/api/admin/semesters').then(function (res) { $scope.semesters = res.data; });
        loadCourses();

        $scope.createCourse = function () {
            ApiService.post('/api/courses', $scope.newCourse)
                .then(function () {
                    $scope.success = 'Course created successfully';
                    $scope.newCourse = { credits: 3, maxStudents: 60 };
                    loadCourses();
                    setTimeout(function () { $scope.$apply(function () { $scope.success = null; }); }, 3000);
                })
                .catch(function (err) { $scope.error = err.data && err.data.message; });
        };

        $scope.deleteCourse = function (course) {
            if (!confirm('Delete course ' + course.courseName + '?')) return;
            ApiService.delete('/api/courses/' + course.courseId)
                .then(function () { loadCourses(); })
                .catch(function (err) { $scope.error = err.data && err.data.message; });
        };
    }
]);
