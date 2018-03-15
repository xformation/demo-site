<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="templates/header.jsp"%>


<div class="container-wrapper">
	<div class="container">
		<div class="page-header">
			<div class="col-md-6"></div>
			<h1>
				<span class="label label-info">Register to be able to make purchases on the site!</span>
			</h1>
			<br> <br>
			<h1>User register</h1>
		</div>
		<form:form action="${pageContext.request.contextPath}/register" method="post" modelAttribute="wrapper">
			<h3>User Information:</h3>

			<div class="form-group">
				<label for="name"> Full name</label>
				<form:errors path="customer.name" cssStyle="color: red" />
				<form:input path="customer.name" id="name" class="form-control" tabindex="1" />
			</div>

			<div class="form-group">
				<label for="email"> Email </label>
				<form:errors path="customer.email" cssStyle="color: red" />
				<form:input path="customer.email" id="email" class="form-control" tabindex="2" />
			</div>

			<div class="form-group">
				<label for="phone"> Telephone (10 digits) </label>
				<form:errors path="customer.phone" cssStyle="color: red" />
				<form:input path="customer.phone" id="phone" class="form-control" tabindex="3" />
			</div>

			<div class="form-group">
				<label for="username"> Username </label>
				<form:errors path="customer.username" cssStyle="color: red" />
				<form:input path="customer.username" id="username" class="form-control" tabindex="4" />
			</div>

			<div class="form-group">
				<label for="password"> Password </label>
				<form:errors path="customer.password" cssStyle="color: red" />
				<form:password path="customer.password" id="password" class="form-control" tabindex="5" />
			</div>

			<form:hidden path="billing.cardHolder" class="form-Control" />
			<form:hidden path="billing.cardNumber" class="form-Control" value="" />
			<form:hidden path="billing.expiryDate" id="billingCity" class="form-Control" />
			<form:hidden path="billing.securityCode" id="billingState" class="form-Control" />
			<form:hidden path="shipping.street" id="shippingStreet" class="form-Control" />
			<form:hidden path="shipping.division" id="shippingDivision" class="form-Control" />
			<form:hidden path="shipping.apartmentNumber" id="shippingApartmentNumber" class="form-Control" />
			<form:hidden path="shipping.city" id="shippingCity" class="form-Control" />
			<form:hidden path="shipping.state" id="shippingState" class="form-Control" />
			<form:hidden path="shipping.country" id="shippingCountry" class="form-Control" />
			<form:hidden path="shipping.zip" id="shippingZip" class="form-Control" />
			<br>
			<%-- SUBMIT/CANCEL BUTTON  ===========--%>
			<a href="<c:url value="/" />" class="btn btn-default" tabindex="19">
				Cancel </a>
			<input type="submit" value="Save" class="btn btn-primary" tabindex="18">
		</form:form>

	</div>
</div>


<%@include file="templates/footer.jsp"%>
