<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<div class="row">&nbsp;</div>
<c:if test="${not empty error}">
    <div class="row"><div class="alert alert-danger text-center">${error}</div></div>
</c:if>

<div class="row">
    <div class="col-md-12">
       <h4>Forgotten Password</h4>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <p>Getting your password reset is an easy process, just follow these steps;</p>
    </div>
</div>

<form action="<c:url value='/Forgotten' />" method="post" role="form">

    <div class="row">
        <div class="col-md-12">
            <p><b>1.</b> Tell us the email address you signed up with. <span class="glyphicon glyphicon-check"></span></p>
        </div>
    </div>

    <div class="row">&nbsp;</div>

    <div class="row">
        <div class="col-md-12">
            <p><b>2.</b> Give us a secret word to include in your password reset email so you know it's from us. <span
                    class="glyphicon glyphicon-check"></span></p>
        </div>
    </div>

    <div class="row">&nbsp;</div>

    <div class="row">
        <div class="col-md-12">
            <p><b>3.</b> Hit the button. <span class="glyphicon glyphicon-check"></span></p>
        </div>
    </div>

    <div class="row">&nbsp;</div>

    <div class="row">
        <div class="col-md-12">
            <p><b>4.</b> Wait for the email to arrive.</p>
        </div>
    </div>

    <div class="row">&nbsp;</div>

    <div class="row">
        <div class="col-md-12">
            <p><b>5.</b> Click on the link in the email.</p>
        </div>
    </div>

</form>

</body>
</html>