<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@include file="/WEB-INF/views/templates/header.jsp"%>

<!-- Page Content -->
<div class="container" ng-app="cartApp" ng-controller="cartCtrl">

	<!-- Portfolio Item Heading -->
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">${product.name}
				<small> ${product.manufacturer}</small>
			</h1>
		</div>
	</div>
	<!-- /.row -->

	<!-- Portfolio Item Row -->
	<div class="row">

		<div class="col-md-6">
			<img class="img-responsive detalles"
				src="<c:url value="/resources/product_images/${product.id}.png" />"
				alt="${product.name}">
		</div>

		<div class="col-md-6">
			<h3 class="text-info">Description</h3>
			<p>${product.description}</p>
			<h3 class="text-info">Product details</h3>
			<table class="table table-hover table-condensed">
				<thead>
					<tr>
						<td></td>
						<td></td>
					</tr>
				</thead>
				<tr>
					<td><h4>Brand</h4></td>
					<td><h4>${product.manufacturer}</h4></td>
				</tr>
				<tr>
					<td><h4>Category</h4></td>
					<td><h4>${product.category}</h4></td>
				</tr>
				<tr>
					<td><h4>Condition</h4></td>
					<td><h4>${product.condition}</h4></td>
				</tr>
				<tr>
					<td><h4>Units available:</h4></td>
					<td><h4>${product.stockCount}</h4></td>
				</tr>
				<tr>
					<td><h4>Price:</h4></td>
					<td><h4 class="precio">$${product.price} MXN</h4></td>
				</tr>
				<tr>
					<%-- the rowspan attr is used as a hack to override the table-hover effect --%>
					<td rowspan="2">
						<div class="pager">
							<li><a href="javascript:history.back()"> <span
									class="glyphicon glyphicon-backward"></span> return
							</a></li>
						</div>
					</td>
					<td rowspan="2">
						<c:if test="${curCustomer != null}">
							<a href="#" class="btn btn-primary"
								ng-click="addToCart('${curCustomer.username}', '${product.id}')"
								data-toggle="modal" data-target="#myModal"> Buy <span
								class="glyphicon glyphicon-usd"></span></a>
						</c:if>
						<c:if test="${curCustomer == null}">
							<button type="button" href="<spring:url value="/login"/>" class="btn btn-primary">
								Login <span class="glyphicon glyphicon-usd"></span>
							</button>
						</c:if>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>


<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
	<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="btn btn-danger pull-right"
					data-dismiss="modal">Close</button>
				<h3 class="modal-title" style="text-shadow: 1px 1px 2px dodgerblue">
					Successful operation</h3>
			</div>
			<div class="modal-body">
				<h3 style="text-shadow: 1px 1px 2px darkorchid">The product is added to your shopping cart with success.</h3>
				<br>
			</div>
		</div>

	</div>
</div>

<!-- /.row -->

<script src="${pageContext.request.contextPath}/resources/js/controller.js" type="text/javascript"></script>
<%@include file="/WEB-INF/views/templates/footer.jsp"%>
