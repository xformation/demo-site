<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/views/templates/header.jsp" %>


<div class="container-wrapper">
    <div class="container">
        <div class="page-header">
            <div class="col-md-6"></div>
            <br>
            <h1 class="alert alert-success">Datos de Entrega <small>Ingresar la informacion de ubicacion deseada para entrega.</small></h1>
        </div>
        <form:form commandName="order" class="form">
            <h3>Registrar Informacion de Entrega: </h3>

            <div class="form-group">
                <label for="shipping_calle">Calle</label>
                <form:input path="cart.customer.shippingAddress.calle" id="shipping_calle"
                            class="form-control"
                            tabindex="1"/>
            </div>
            <div class="form-group">
                <label for="shipping_numero">No. Exterior</label>
                <form:input path="cart.customer.shippingAddress.numero_apartamento" id="shipping_numero"
                            class="form-control"
                            tabindex="2"/>
            </div>
            <div class="form-group">
                <label for="shipping_fraccionamiento">Fraccionamiento / Colonia</label>
                <form:input path="cart.customer.shippingAddress.fraccionamiento" id="shipping_fraccionamiento"
                            class="form-control"
                            tabindex="3"/>
            </div>
            <div class="form-group">
                <label for="shipping_ciudad">Ciudad</label>
                <form:input path="cart.customer.shippingAddress.ciudad" id="shipping_ciudad"
                            class="form-control"
                            tabindex="4"/>
            </div>
            <div class="form-group">
                <label for="shipping_estado">Estado</label>
                <form:input path="cart.customer.shippingAddress.estado" id="shipping_estado"
                            class="form-control"
                            tabindex="5"/>
            </div>
            <div class="form-group">
                <label for="shipping_pais">Pais</label>
                <form:input path="cart.customer.shippingAddress.pais" id="shipping_pais" class="form-control"
                            tabindex="6"/>
            </div>
            <div class="form-group">
                <label for="shipping_zip">Codigo Postal</label>
                <form:input path="cart.customer.shippingAddress.zip" id="shipping_zip" class="form-control"
                            tabindex="7"/>
            </div>
            <input type="hidden" name="_flowExecutionKey"/>

            <br><br>
            <%-- BACK/CANCELAR/NEXT  ===========--%>

            <button class="btn btn-danger" tabindex="9" name="_eventId_cancel"> Cancelar</button>
            <input type="submit" value="Continuar" class="btn btn-primary" tabindex="8"
                   name="_eventId_shippingDetailCollected">
            <br>
            <button class="btn btn-default" style="margin-top:15px;" tabindex="10"
                    name="_eventId_backToCollectCustomerInfo"> Volver
            </button>

        </form:form>

    </div>
</div>


<%@include file="/WEB-INF/views/templates/footer.jsp" %>

