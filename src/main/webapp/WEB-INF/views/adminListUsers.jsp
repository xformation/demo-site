<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file="/WEB-INF/views/templates/header.jsp"%>

<div class="container-wrapper">
	<div class="container">
		<div class="page-header">
			<h1>List of Users</h1>
		</div>

		<table class="table table-striped table-hover">
			<thead>
				<tr class="bg-success">
					<th>First name</th>
					<th>Email</th>
					<th>Phone</th>
					<th>Username</th>
					<th>State</th>
				</tr>
			</thead>
			<c:forEach items="${customerList}" var="customer">
				<tr>
					<td>${customer.name}</td>
					<td>${customer.email}</td>
					<td>${customer.phone}</td>
					<td>${customer.username}</td>
					<c:if test="${customer.enabled == true}">
						<td>Active</td>
					</c:if>
					<c:if test="${customer.enabled == false}">
						<td>Inactive</td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
		<div class="pager">
			<br>
			<li>
				<a href="<c:url value="/admin"/>">
					<span class="glyphicon glyphicon-backward"></span> return
				</a>
			</li>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/views/templates/footer.jsp"%>
