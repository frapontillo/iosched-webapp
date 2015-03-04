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
       <h2>A problem occurred</h2>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <h4>Unfortunately our systems encountered an error and you may need to <a href="/">Log back in</a>.</h4>

        <p>The problem has been reported to our support desk and we hope to fix it shortly.</p>
    </div>
</div>

</body>
</html>
