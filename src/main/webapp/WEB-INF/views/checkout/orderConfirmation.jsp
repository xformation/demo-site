<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fwt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@include file="/WEB-INF/views/templates/header.jsp"%>
<jsp:useBean id="now" class="java.util.Date" />


<div class="container-wrapper">
	<div class="container">
		<div class="page-header">
			<div class="col-md-6"></div>
			<br>
			<h1 class="alert alert-success text-center">Purchase receipt</h1>
		</div>

		<div class="container">
			<div class="row">

				<form:form action="${pageContext.request.contextPath}/shopping/confirm" modelAttribute="orderDTO" class="form-horizontal">
					<form:hidden path="cartId" name="cartId"/>
					<form:hidden path="ssmId" name="ssmId"/>
					<form:hidden path="customerId" name="customerId"/>
					<form:hidden path="billingId" name="billingId"/>
					<form:hidden path="shippingId" name="shippingId"/>
					<div
						class="well col-xs-10 col-sm-10 col-md-6 col-xs-offset-1 col-sm-offset-1 col-md-offset-3">
						<div class="text-center">
							<h1>Receipt</h1>
							<hr>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-6 col-md-6">
								<address>
									<b>Shipping information</b> <br>
									${orderDTO.shipping.street},
									${orderDTO.shipping.apartmentNumber} <br>
									${orderDTO.shipping.division},
									${orderDTO.shipping.city} <br>
									${orderDTO.shipping.country},
									${orderDTO.shipping.zip}
								</address>
							</div>
							<div class="col-xs-6 col-sm-6 col-md-6 text-right">
								<p>
									<b>Shipping date</b><br>
									<fwt:formatDate type="date" value="${now}" />
								</p>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-6 col-md-6">
								<address>
									<b>Collection Details</b> <br>
									${orderDTO.billing.cardHolder},
									${orderDTO.billing.cardNumber} <br>
									${orderDTO.billing.expiryDate},
									${orderDTO.billing.securityCode} <br>
								</address>
							</div>
						</div>
						<div class="row">
							<table class="table table-hoover table-condensed">
								<thead>
									<tr>
										<th>Product</th>
										<th>#</th>
										<th class="text-center">Price</th>
										<th class="text-center">Total</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="cartItem" items="${orderDTO.cart.items}">
										<tr>
											<td class="col-md-9"><em>${cartItem.product.name}</em></td>
											<td class="col-md-1 text-center">${cartItem.quantity}</td>
											<td class="col-md-1 text-center">
												<fmt:formatNumber type="currency">${cartItem.product.price}</fmt:formatNumber>
											</td>
											<td class="col-md-1 text-center">
												<fmt:formatNumber type="currency">${cartItem.totalPrice}</fmt:formatNumber>
											</td>
										</tr>
									</c:forEach>
									<tr>
										<td colspan="2" class="text-right">
											<h4>
												<strong>Grand Total: </strong>
											</h4>
										</td>
										<td colspan="2" class="text-right precio">
											<h4>
												<strong>$ ${orderDTO.cart.grandTotal} MXN</strong>
											</h4>
										</td>
									</tr>
								</tbody>
							</table>
						</div>

						<input type="hidden" name="_flowExecutionKey" /> <br>
						<br>
						<%-- BACK/CANCELAR/NEXT  ===========--%>

						<button type="submit" class="btn btn-danger" tabindex="13" name="Cancel">Cancel</button>
						<input type="submit" value="Complete Order" class="btn btn-success"
							 tabindex="12" name="Complete"> <br>
						<button class="btn btn-default" style="margin-top: 15px;" tabindex="14"
								name="Return" onclick="javascript: history.back();">
							Return</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/templates/footer.jsp"%>
