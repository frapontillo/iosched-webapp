<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <style>
        .form-signin { padding: 30px; margin: 0 auto; border: 1px solid #888; border-radius: 10px; }
        .form-signin input { margin-top: 10px; margin-bottom: 10px; }
    </style>
</head>
<body>
<div class="row">&nbsp;</div>
<c:if test="${not empty error}">
    <div class="row"><div class="alert alert-danger text-center">${error}</div></div>
</c:if>

<div class="row">
    <div class="col-md-12">
       <h4>Thanks for registering!</h4>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <p>Your password has been emailed to you, once you've got it you'll be able to sign-in <a href="<c:url value='/login.jsp'/>">here</a>.</p>
    </div>
</div>

</body>
</html>