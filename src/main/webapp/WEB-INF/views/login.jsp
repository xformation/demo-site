<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="/WEB-INF/views/templates/header.jsp"%>

<!-- Top content -->
<div class="top-content">
	<div class="container">
		<c:if test="${not empty msg}">
			<div class="msg alert alert-info">
				<strong>${msg}</strong>
			</div>
			<c:redirect url="/login" />
		</c:if>
		<div class="row">
			<div class="col-sm-6 col-sm-offset-3 form-box">
				<div class="page-header center">
					<h1>ITESCA Store</h1>
				</div>
				<div class="form-top">
					<div class="center">
						<h3 style="color: #3e8f3e">Log in</h3>
					</div>
				</div>
				<div class="form-bottom">
					<form:form name="loginForm" action="${pageContext.request.contextPath}/signin"
							method="post" modelAttribute="login">
						<c:if test="${not empty error}">
							<div class="error" style="color: red">${error}</div>
						</c:if>
						<c:if test="${not empty redirect}">
							<input type="hidden" name="redirect" value="${redirect}"/>
						</c:if>
						<div class="form-group">
							<label class="sr-only" for="username">Username</label>
							<input type="text" name="username" placeholder="Username"
								class="form-username form-control" id="username" tabindex="1">
						</div>
						<div class="form-group">
							<label class="sr-only" for="password"> Password </label>
							<input type="password" name="password" placeholder="Password"
								class="form-password form-control" id="password" tabindex="2">
						</div>
						<br>
						<button type="submit" value="Submit" class="btn btn-primary" tabindex="3">Log in</button>
					</form:form>
				</div>
			</div>
		</div>
		<br> <br>
		<div class="description center">
			<h4>
				Not yet a member?
				<a class="label label-success" href="<c:url value="/register"/>"
					tabindex="4"> Register </a>
			</h4>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/templates/footer.jsp"%>
<script>
	$(document).ready(
			function() {
				/* Form validation */
				$('.login-form input[type="text"], .login-form input[type="password"], .login-form textarea')
						.on('focus', function() {
							$(this).removeClass('input-error');
						});
				$('.login-form')
						.on('submit', function(e) {
							$(this).find('input[type="text"], input[type="password"], textarea')
									.each(function() {
										if ($(this).val() == "") {
											e.preventDefault();
											$(this).addClass('input-error');
										} else {
											$(this).removeClass('input-error');
										}
									});
						});
			});
</script>