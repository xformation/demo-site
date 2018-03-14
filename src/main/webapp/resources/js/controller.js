/**
 * Created by dario on 27/10/2016.
 */

var cartApp = angular.module("cartApp", []);

cartApp.controller("cartCtrl", function ($scope, $http) {

    $scope.refreshCart = function () {
        $http.get("/rest/cart/" + $scope.cart_id).success(function (data) {
            $scope.cart = data;
            $scope.calGrandTotal();
        });
    };
    $scope.clearCart = function () {
        $http.delete('/rest/cart/' + $scope.cart_id).success($scope.refreshCart());
    };
    $scope.initCartId = function (cart_id) {
        $scope.cart_id = cart_id;
        $scope.refreshCart(cart_id);
    };
    $scope.addToCart = function (producto_id) {
        $http.put("/rest/cart/add/" + producto_id).success(function () {
            $scope.refreshCart();
        });
    };
    $scope.removeFromCart = function (producto_id) {
        $http.put("/rest/cart/remove/" + producto_id).success(function (data) {
            $scope.refreshCart();
        });
    };
    $scope.calGrandTotal = function () {
        var grandTotal = 0;
        if (typeof($scope.cart) !== 'undefined') {
            for (var i = 0; i < $scope.cart.cart_items.length; i++) {
                grandTotal += $scope.cart.cart_items[i].precio_total;
            }
            return grandTotal;
        }else {
            return 0;
        }

    }

});

