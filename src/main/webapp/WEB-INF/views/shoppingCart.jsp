<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@include file="/WEB-INF/views/templates/header.jsp"%>

<div class="container-wrapper">
	<div class="container" ng-app="cartApp" ng-cloak>
		<div ng-controller="cartCtrl" ng-init="initCartId('${cart_id}')">
			<br>
			<section>
				<div class="row text">
					<div class="col-md-8">
						<h1 style="text-shadow: 1px 1px 1px dodgerblue">
							<strong>Shopping Cart </strong>
						</h1>
						<h3 style="text-shadow: 1px 1px 1px darkorchid">List of
							products in your shopping cart:</h3>
					</div>
				</div>
			</section>
			<hr>
			<br>
			<section class="container">
				<div>
					<div class="table-responsive">
						<table class="table table-hoover table-condensed table-custom">
							<td></td>
							<thead>
								<tr class="bg-success">
									<td colspan="2">First name</td>
									<td>Price</td>
									<td>Quantity</td>
									<td>Total</td>
									<td>
										<a class="btn btn-danger" ng-click="clearCart()">Cancel Shopping </a>
									</td>
								</tr>
							</thead>
							<tr ng-repeat="item in cart.cart_items">
								<td colspan="2">
									<a ng-href="/products/productDetails/{{item.product.product_id}}">{{item.product.name}}
								</a></td>
								<td>{{item.product.price | currency : $}}</td>
								<td>{{item.quantity}}</td>
								<td class="precio">{{item.price_total | currency : $}}</td>
								<td>
									<a href="#" class="btn btn-warning" ng-click="removeFromCart(item.product.product_id)">
										Remove product<span class="glyphicon glyphicon-remove"></span>
									</a>
								</td>
							</tr>
							<tr>
								<td colspan="4"></td>
								<th>Total of your Purchases</th>
								<th class="precio">{{ calGrandTotal() | currency : $}} MXN</th>
								<td></td>
							</tr>
							<tr>
								<td>
									<a href="<spring:url value="/products"/>" class="btn btn-primary">
										Add products
										<span class="glyphicon glyphicon-plus"></span>
									</a>
								</td>
								<td colspan="4"></td>
								<td>
									<a href="<spring:url value="/order/${cart_id}"/>" class="btn btn-success">
										Complete the Purchase
										<span class="glyphicon glyphicon-usd"></span>
									</a>
								</td>
								<td></td>
							</tr>
						</table>
					</div>
					<br>
					<div class="pager" style="margin-top: 15px;">
						<li>
							<a href="javascript:history.back()">
								 <span class="glyphicon glyphicon-backward"></span> return
							</a>
						</li>
					</div>

				</div>
			</section>
		</div>
	</div>
</div>
<br>
<br>
<br>

<script src="/resources/js/controller.js" type="text/javascript"></script>
<%@include file="/WEB-INF/views/templates/footer.jsp"%>
