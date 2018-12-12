<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="/WEB-INF/views/templates/header.jsp"%>

<div class="container-wrapper">
	<div class="container">
		<div class="page-header">
			<div class="col-md-6"></div>
			<br>
			<h1 class="alert alert-success">
				Delivery Details <small> Enter the desired location information for delivery.</ Small> 
			</h1>
		</div>
		<form:form modelAttribute="wrapper" class="form">
			<h3>Register Delivery Information:</h3>

			<div class="form-group">
				<label for="street">Street</label>
				<form:input path="order.shipping.street" id="street" class="form-control" tabindex="1" />
			</div>
			<div class="form-group">
				<label for="division">Division</label>
				<form:input path="order.shipping.division" id="division" class="form-control" tabindex="2" />
			</div>
			<div class="form-group">
				<label for="apartmentNumber">Apartment number</label>
				<form:input path="order.shipping.apartmentNumber" id="apartmentNumber" class="form-control" tabindex="3" />
			</div>
			<div class="form-group">
				<label for="city">City</label>
				<form:input path="order.shipping.city" id="city" class="form-control" tabindex="4" />
			</div>
			<div class="form-group">
				<label for="state">State</label>
				<form:input path="order.shipping.state" id="state" class="form-control" tabindex="5" />
			</div>
			<div class="form-group">
				<label for="country">Country</label>
				<form:input path="order.shipping.country" id="country" class="form-control" tabindex="6" />
			</div>
			<div class="form-group">
				<label for="zip">Postal Code</label>
				<form:input path="order.shipping.zip" id="zip" class="form-control" tabindex="7" />
			</div>
			<input type="hidden" name="_flowExecutionKey" />

			<br>
			<br>
			<%-- BACK/CANCELAR/NEXT  ===========--%>

			<button class="btn btn-danger" tabindex="9" name="_eventId_cancel">Cancel</button>
			<input type="submit" value="Continue" class="btn btn-primary"
				tabindex="8" name="_eventId_shippingDetailCollected">
			<br>
			<button class="btn btn-default" style="margin-top: 15px;"
				tabindex="10" name="_eventId_backToCollectCustomerInfo">
				Return</button>

		</form:form>

	</div>
</div>

<%@include file="/WEB-INF/views/templates/footer.jsp"%>
