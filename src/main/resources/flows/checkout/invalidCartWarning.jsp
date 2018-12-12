<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@include file="/WEB-INF/views/templates/header.jsp"%>

<div class="container-wrapper">
	<div class="container">
		<div>
			<br>
			<section>
				<div class="row text">
					<div class="col-md-8">
						<h1>
							<strong>Empty Shopping Cart</strong>
						</h1>
					</div>
				</div>
			</section>
			<section>
				<p>
					<a href="<spring:url value="/productos"/>" class="btn btn-primary">
						See products
					</a>
				</p>
			</section>
		</div>
	</div>
</div>
<br>
<br>
<br>
<br>

<script src="/resources/js/controller.js" type="text/javascript"></script>
<%@include file="/WEB-INF/views/templates/footer.jsp"%>
