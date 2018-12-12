<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ include file="/WEB-INF/views/templates/header.jsp"%>

<div class="container-wrapper">
	<div class="container">
		<div class="page-header">
			<div class="col-md-6"></div>
			<br>
			<h1 class="alert alert-success">
				Buyer Data <small> (confirm that your information is correct)</small>
			</h1>
			<hr>
			<form:form modelAttribute="wrapper" class="form">
				<h3>User Information:</h3>
				<div class="form-group">
					<label for="name">First name</label>
					<form:input path="order.customer.name" id="name" class="form-Control" tabindex="1" />
				</div>
				<div class="form-group">
					<label for="email">Email</label>
					<form:input path="order.customer.email" id="email" class="form-control" tabindex="2" />
				</div>
				<div class="form-group">
					<label for="phone"> Phone (10 digits) </label>
					<form:input path="order.customer.phone" id="phone" class="form-control" tabindex="3" />
				</div>
				<hr>
				<h3 class="alert alert-warning">
					Register Collection Information:
				</h3>
				<div class="form-group">
					<label for="cardHolder">Holder Card (credit / debit)</label>
					<form:input path="order.billing.cardHolder" id="cardHolder"
							class="form-control" onkeydown="upperCaseF(this)" tabindex="4" />
				</div>
				<div class="form-group">
					<label for="cardNumber">Card number </label>
					<form:input path="order.billing.cardNumber"
							id="cardNumber" class="form-control" maxlength="19" tabindex="5" />
				</div>
				<div class="form-group">
					<label for="expiryDate">Expiration date</label>
					<form:input path="order.billing.expiryDate"
							id="expiryDate" class="form-control" maxlength="5" tabindex="6" />
				</div>
				<div class="form-group">
					<label for="securityCode">Security code</label>
					<form:input path="order.billing.securityCode"
							id="securityCode" class="form-control" maxlength="3" tabindex="7" />
				</div>
				<input type="hidden" name="_flowExecutionKey" />
				<br>
				<br>
				<%-- CANCEL/NEXT  ===========--%>
				<button class="btn btn-danger" tabindex="9" name="_eventId_cancel">Cancel</button>
				<input type="submit" value="Continue" class="btn btn-primary" tabindex="8"
						name="_eventId_customerInfoCollected">
			</form:form>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/templates/footer.jsp"%>
