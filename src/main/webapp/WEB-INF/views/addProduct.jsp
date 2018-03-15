<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="templates/header.jsp"%>


<div class="container-wrapper">
	<div class="container">
		<h1>Add Product</h1>
		<br> <br>

		<form:form
			action="${pageContext.request.contextPath}/admin/inventory/add"
			method="post" modelAttribute="product" enctype="multipart/form-data">

			<div class="form-group">
				<label for="name"> Product name</label>
				<form:errors path="name" cssStyle="color: red" />
				<form:input path="name" id="name" class="form-control" tabindex="1" />
			</div>

			<div class="form-group">
				<label for="manufacturer"> Manufacturer </label>
				<form:input path="manufacturer" id="manufacturer"
					class="form-control" tabindex="2" />
			</div>

			<div class="form-group">
				<label for="category"> Category </label>
				<form:errors path="category" cssStyle="color: red" />
				<form:select path="category" id="category" class="form-control"
					items="${category_list}" tabindex="3" />
			</div>

			<div class="form-group">
				<label for="description"> Product description</label>
				<form:textarea path="description" id="description"
					class="form-control" tabindex="4" />
			</div>

			<div class="form-group">
				<label for="condition"> Product condition</label> <label
					class="checkbox-inline">
					<form:radiobutton path="condition"
						id="condition" value="New" tabindex="5" /> New
				</label> <label class="checkbox-inline"> <form:radiobutton
						path="condicion" id="condicion" value="Usado" /> Used
				</label>
			</div>

			<div class="form-group">
				<label for="price"> Price $MXN</label>
				<form:errors path="price" cssStyle="color: red" />
				<form:input path="price" id="price" class="form-control"
					tabindex="6" />
			</div>

			<div class="form-group">
				<label for="availableUnit"> Units available </label>
				<form:errors path="availableUnit" cssStyle="color: red" />
				<form:input path="availableUnit"
					id="availableUnit" class="form-control" tabindex="7" />
			</div>

			<%-- SUBIR IMAGEN DE PRODUCTO  ===================--%>
			<div class="form-group">
				<label class="control-label" for="image">Upload Image</label> <br>
				<span>Recommended Size: 700 x 500.</span> <br>
				<form:input path="image" id="image" type="file"
					class="form:input-large" tabindex="8" size="2024000" />
				<br>
			</div>

			<%-- SUBMIT/CANCEL BUTTON  ===========--%>
			<a href="<c:url value="/admin/inventory" />" class="btn btn-default"
				tabindex="10"> Cancel </a>
			<input type="submit" value="Add" class="btn btn-primary"
				tabindex="9">
		</form:form>

	</div>
</div>


<%@include file="templates/footer.jsp"%>
