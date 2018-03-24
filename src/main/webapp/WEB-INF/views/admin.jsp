<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="/WEB-INF/views/templates/header.jsp"%>
<br>
<br>
<div class="container">
	<br>
	<div class="list-group">
		<p class="list-group-item active">Operations</p>
		<a class="list-group-item" href="<c:url value="/admin/inventory" />">
			<span class="glyphicon glyphicon-th-list"></span> PRODUCTS INVENTORY
		</a> <a class="list-group-item" href="<c:url value="/admin/customer" /> ">
			<span class="glyphicon glyphicon-user"></span> LIST OF USERS
		</a>
	</div>
</div>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<%@include file="/WEB-INF/views/templates/footer.jsp"%>

