<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@include file="../../../webapp/WEB-INF/views/templates/header.jsp"%>

<div class="container-wrapper">
	<div class="container">
		<div>
			<br>
			<section>
				<div class="row text">
					<div class="col-md-8">
						<h1 class="alert alert-danger">
							<strong>Order Cancelled!</strong>
						</h1>
						<p>You can continue buying if you wish.</p>
					</div>
				</div>
			</section>
			<section>
				<p>
					<a href="<spring:url value="/products"/>" class="btn btn-primary">See products</a>
				</p>
			</section>
		</div>
	</div>
</div>
<br>
<br>
<br>
<br>
<br>
<br>
<br>

<script src="js/controller.js" type="text/javascript"></script>

<%@include file="../../../webapp/WEB-INF/views/templates/footer.jsp"%>
