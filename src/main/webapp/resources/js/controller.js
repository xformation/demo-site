/**
 * Created by dario on 27/10/2016.
 */

var cartApp = angular.module("cartApp", []);

cartApp.controller("cartCtrl", function ($scope, $http) {

    $scope.refreshCart = function () {
        $http.get("/demo.site/rest/cart/" + $scope.cartId).success(function (data) {
        	console.log("data: " + JSON.stringify(data));
            $scope.cart = data;
            $scope.calGrandTotal();
        });
    };
    $scope.clearCart = function () {
        $http.delete('/demo.site/rest/cart/' + $scope.cartId).success($scope.refreshCart());
    };
    $scope.initCartId = function (cart_id) {
        $scope.cartId = cart_id;
        $scope.refreshCart(cart_id);
    };
    $scope.addToCart = function (custId, product_id) {
        $http.post("/demo.site/rest/cart/add/" + custId + "/" + product_id).success(function () {
            $scope.refreshCart();
        });
    };
    $scope.removeFromCart = function (cartItemId) {
        $http.post("/demo.site/rest/cart/remove/" +  $scope.cartId + "/" + cartItemId)
        			.success(function (data) {
            $scope.refreshCart();
        });
    };
    $scope.calGrandTotal = function () {
        var grandTotal = 0;
        if (typeof($scope.cart) !== 'undefined') {
        	if ($scope.cart.items !== null) {
	            for (var i = 0; i < $scope.cart.items.length; i++) {
	                grandTotal += $scope.cart.items[i].totalPrice;
	            }
        	}
            return grandTotal;
        }else {
            return 0;
        }

    }

});

