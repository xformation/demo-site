<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fwt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@include file="/WEB-INF/views/templates/header.jsp" %>
<jsp:useBean id="now" class="java.util.Date"/>


<div class="container-wrapper">
    <div class="container">
        <div class="page-header">
            <div class="col-md-6"></div>
            <br>
            <h1 class="alert alert-success text-center">Recibo de Compra</h1>
        </div>

        <div class="container">
            <div class="row">

                <form:form commandName="order" class="form-horizontal">
                    <div class="well col-xs-10 col-sm-10 col-md-6 col-xs-offset-1 col-sm-offset-1 col-md-offset-3">
                        <div class="text-center">
                            <h1>Recibo</h1>
                            <hr>
                        </div>
                        <div class="row">
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <address>
                                    <b>Datos de Envio</b>
                                    <br>
                                        ${order.cart.customer.shippingAddress.calle}, ${order.cart.customer.shippingAddress.numero_apartamento}
                                    <br>
                                        ${order.cart.customer.shippingAddress.fraccionamiento}, ${order.cart.customer.shippingAddress.ciudad}
                                    <br>
                                        ${order.cart.customer.shippingAddress.pais}, ${order.cart.customer.shippingAddress.zip}
                                </address>
                            </div>
                            <div class="col-xs-6 col-sm-6 col-md-6 text-right">
                                <p><b>Fecha de Envio</b><br><fwt:formatDate type="date" value="${now}"/></p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <address>
                                    <b>Datos de Cobro</b>
                                    <br>
                                        ${order.cart.customer.billingAddress.titular_tarjeta}, ${order.cart.customer.billingAddress.numero_tarjeta}
                                    <br>
                                        ${order.cart.customer.billingAddress.fecha_expiracion}, ${order.cart.customer.billingAddress.codigo_seguridad}
                                    <br>
                                </address>
                            </div>
                        </div>
                        <div class="row">
                            <table class="table table-hoover table-condensed">
                                <thead>
                                <tr>
                                    <th>Producto</th>
                                    <th>#</th>
                                    <th class="text-center">Precio</th>
                                    <th class="text-center">Total</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="cartItem" items="${order.cart.cart_items}">
                                    <tr>
                                        <td class="col-md-9"><em>${cartItem.producto.nombre}</em></td>
                                        <td class="col-md-1 text-center">${cartItem.cantidad}</td>
                                        <td class="col-md-1 text-center">
                                            <fmt:formatNumber
                                                    type="currency">${cartItem.producto.precio}</fmt:formatNumber>
                                        </td>
                                        <td class="col-md-1 text-center">
                                            <fmt:formatNumber
                                                    type="currency">${cartItem.precio_total}</fmt:formatNumber>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <tr>

                                    <td colspan="2" class="text-right">
                                        <h4><strong>Gran Total: </strong></h4>
                                    </td>
                                    <td colspan="2" class="text-right precio">
                                        <h4><strong>$ ${order.cart.gran_total} MXN</strong></h4>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>

                        <input type="hidden" name="_flowExecutionKey"/>

                        <br><br>
                            <%-- BACK/CANCELAR/NEXT  ===========--%>

                        <button class="btn btn-danger" tabindex="13" name="_eventId_cancel">
                            Cancelar
                        </button>
                        <input type="submit" value="Completar Compra" class="btn btn-success" tabindex="12"
                               name="_eventId_orderConfirmed">

                        <br>
                        <button class="btn btn-default" style="margin-top: 15px;" tabindex="14"
                                name="_eventId_backToCollectShippingDetail"> Volver
                        </button>
                    </div>
                </form:form>
            </div>

        </div>
    </div>
</div>


<%@include file="/WEB-INF/views/templates/footer.jsp" %>

