<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@include file="templates/header.jsp"%>


<div class="container-wrapper">
	<div class="container">
		<h1 style="text-align: center">Inventory</h1>
		<hr>
		<br>

		<h1>Product</h1>
		<div class="table-responsive">
			<table class="table table-custom table-hover table-condensed">
				<thead>
					<tr class="bg-success">
						<th>Photo Thumb</th>
						<th>Name</th>
						<th>Manufacturer</th>
						<th>Category</th>
						<th>Condition</th>
						<th>Price</th>
						<th></th>
						<th></th>
						<th></th>
					</tr>
				</thead>
				<c:forEach items="${productList}" var="product">
					<tr>
						<td><img src="<c:url value="/resources/product_images/${product.id}.png"/>" alt="image"></td>
						<td>${product.name}</td>
						<td>${product.manufacturer}</td>
						<td>${product.category}</td>
						<td>${product.condition}</td>
						<td>
							<p class="price">
								<fmt:formatNumber type="currency">${product.price}</fmt:formatNumber> $MXN
							</p></td>
						<td>
							<a href="<spring:url value="/products/productDetails/${product.id}" />" class="btn btn-info">
							Details
							<span class=" glyphicon glyphicon-info-sign"></span>
						</a>
						</td>
						<td>
							<a href="<spring:url value="/admin/inventory/edit/${product.id}" />" class="btn btn-success">
							Edit
							<span class="glyphicon glyphicon-pencil"></span>
						</a>
						</td>
						<td>
							<a href="<spring:url value="/admin/inventory/remove/${product.id}" />" class="btn btn-danger">
							Remove
							<span class="glyphicon glyphicon-remove"></span>
						</a>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<a class="btn btn-primary"
			href="<spring:url value="/admin/inventory/add" />">Add product</a> <br>
		<div class="pager">
			<br>
			<li>
				<a href="<c:url value="/admin"/>">
					<span class="glyphicon glyphicon-backward"></span> Return
				</a>
			</li>
		</div>
	</div>
</div>

<%@include file="templates/footer.jsp"%>