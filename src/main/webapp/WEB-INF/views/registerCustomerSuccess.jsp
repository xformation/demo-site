<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@include file="/WEB-INF/views/templates/header.jsp"%>

<div class="container-wrapper">
	<div class="container">
		<div>
			<br>
			<section>
				<div class="row text">
					<div class="col-md-8">
						<h1 style="text-shadow: 1px 1px 2px dodgerblue">
							<strong>Successful customer registration</strong>
						</h1>
					</div>
				</div>
			</section>
			<c:redirect url="/login" />
		</div>
	</div>
</div>
<br>
<br>
<br>
<br>

<script src="${pageContext.request.contextPath}/resources/js/controller.js" type="text/javascript"></script>
<%@include file="/WEB-INF/views/templates/footer.jsp"%>