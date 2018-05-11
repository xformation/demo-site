<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="/WEB-INF/views/templates/header.jsp"%>

<div class="container-wrapper">
	<div class="container">
		<div class="page-header">
			<div class="col-md-6"></div>
			<br>
			<h1 class="alert alert-success">
				Delivery Details <small> Enter the desired location information for delivery.</small> 
			</h1>
		</div>
		<form:form action="${pageContext.request.contextPath}/shopping/saveShipping" modelAttribute="orderDTO" class="form">
			<form:hidden path="cartId" name="cartId"/>
			<form:hidden path="ssmId" name="ssmId"/>
			<form:hidden path="customerId" name="customerId"/>
			<form:hidden path="billingId" name="billingId"/>
			<form:hidden path="shippingId" name="shippingId"/>
			<h3>Register Delivery Information:</h3>

			<div class="form-group">
				<label for="street">Street</label>
				<form:input path="shipping.street" id="street" class="form-control" tabindex="1" />
			</div>
			<div class="form-group">
				<label for="division">Division</label>
				<form:input path="shipping.division" id="division" class="form-control" tabindex="2" />
			</div>
			<div class="form-group">
				<label for="apartmentNumber">Apartment number</label>
				<form:input path="shipping.apartmentNumber" id="apartmentNumber" class="form-control" tabindex="3" />
			</div>
			<div class="form-group">
				<label for="city">City</label>
				<form:input path="shipping.city" id="city" class="form-control" tabindex="4" />
			</div>
			<div class="form-group">
				<label for="state">State</label>
				<form:input path="shipping.state" id="state" class="form-control" tabindex="5" />
			</div>
			<div class="form-group">
				<label for="country">Country</label>
				<form:input path="shipping.country" id="country" class="form-control" tabindex="6" />
			</div>
			<div class="form-group">
				<label for="zip">Postal Code</label>
				<form:input path="shipping.zip" id="zip" class="form-control" tabindex="7" />
			</div>
			<input type="hidden" name="_flowExecutionKey" />

			<br>
			<br>
			<%-- BACK/CANCELAR/NEXT  ===========--%>
			<input type="submit" class="btn btn-danger" tabindex="9" name="Cancel" value="Cancel"/>
			<input type="submit" name="Continue" class="btn btn-primary" value="Continue" tabindex="8"/>
			<br>
			<input class="btn btn-default" tabindex="10" value="Return" onclick="javascript: history.back();"/>

		</form:form>

	</div>
</div>

<%@include file="/WEB-INF/views/templates/footer.jsp"%>
