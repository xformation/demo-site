<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="templates/header.jsp"%>


<div class="container-wrapper">
	<div class="container">
		<h1>Modify Product</h1>
		<br> <br>

		<form:form
			action="${pageContext.request.contextPath}/admin/inventory/edit"
			method="post" modelAttribute="product" enctype="multipart/form-data">
			<form:hidden path="product_id" value="${product.product_id}" />

			<div class="form-group">
				<label for="name"> Product name</label>
				<form:errors path="name" cssStyle="color: red" />
				<form:input path="name" id="name" class="form-control"
					value="${product.name}" />
			</div>

			<div class="form-group">
				<label for="manufacturer"> Manufacturer </label>
				<form:input path="manufacturer" id="manufacturer"
					class="form-control" value="${product.manufacturer}" />
			</div>

			<div class="form-group">
				<label for="category"> Category </label>
				<form:errors path="category" cssStyle="color: red" />
				<form:select path="category" id="category" class="form-control"
					items="${categoryList}" />
			</div>

			<div class="form-group">
				<label for="description"> Product description</label>
				<form:textarea path="description" id="description"
					class="form-control" value="${product.description}" />
			</div>

			<div class="form-group">
				<label for="condition"> Product condition</label> <br>
				<label class="checkbox-inline">
					<form:radiobutton path="condition" id="condition" value="New" /> New
				</label>
				<label class="checkbox-inline">
					<form:radiobutton path="condition" id="condition" value="Used" /> Used
				</label>
			</div>

			<div class="form-group">
				<label for="price"> Price $MXN</label>
				<form:errors path="price" cssStyle="color: red" />
				<form:input path="price" id="price" class="form-control" value="${product.price}" />
			</div>

			<div class="form-group">
				<label for="availableUnit"> Available units </label>
				<form:errors path="availableUnit" cssStyle="color: red" />
				<form:input path="availableUnit" id="availableUnit" class="form-control"
					value="${product.availableUnit}" />
			</div>

			<%-- Upload product image  ===================--%>
			<div class="form-group">
				<label class="control-label" for="image">Upload Image</label>
				<form:input path="image" id="image" type="file" class="form:input-large" />
			</div>

			<%-- SUBMIT/CANCEL BUTTON  ===========--%>
			<a href="<c:url value="/admin/inventario" />" class="btn btn-default"> Cancel </a>
			<input type="submit" value="Save" class="btn btn-primary">
		</form:form>

	</div>
</div>

<%@include file="templates/footer.jsp"%>
