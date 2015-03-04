<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<body>
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<c:url value='/secure/conference'/>"><div class="logostyle">CE</div></a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="<c:url value='/Logout'/>">Logout</a></li>
            </ul>
        </div>
    </div>
</div>
<div class="container">
    <div class="row">&nbsp;</div>
    <c:if test="${not empty sessionScope.error}">
        <div class="row"><div class="alert alert-danger text-center">${sessionScope.error}</div></div>
        <c:set scope="session" var="error" value="" />
    </c:if>
    <c:if test="${not empty sessionScope.message}">
        <div class="row"><div class="alert alert-success text-center">${sessionScope.message}</div></div>
        <c:set scope="session" var="message" value="" />
    </c:if>

    <div class="row">
        <div class="col-md-12">
            <h4>Select Conference</h4>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
           <p>Please select the conference you wish to work on;</p>
        </div>
    </div>

    <div class="modal fade" id="addConference" tabindex="-1" role="dialog" aria-labelledby="addConference" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Add Conference</h4>
                </div>
                <form accept-charset="utf-8" action="<c:url value='/secure/conference' />" role="form" method="POST">
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="name">Please enter the new conference name;</label>
                            <input type="text" name="name" id="name" class="form-control" />
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary">Create</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <c:forEach var="permission" items="${requestScope.user.permissions}">
                <c:url var="conference_url" value="/secure/conference">
                    <c:param name="id" value="${permission.conference.id}" />
                </c:url>
                <p><a href="${conference_url}">${permission.conference.name}</a></p>
            </c:forEach>
        </div>
    </div>

    <div class="row">&nbsp;</div>
    <div class="row">
        <div class="col-md-12">
            <a data-toggle="modal" href="#addConference" class="btn btn-primary btn-sm">Create New Conference</a>
        </div>
    </div>


    <div class="row">&nbsp;</div>
</div>
</body>
</html>
