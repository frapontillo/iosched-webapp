<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<body>
<c:set var="conference" value="${requestScope.conference}" />
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
                <li><a href="<c:url value='/secure/Admin'/>">Admin</a></li>
                <li><a href="<c:url value='/secure/Tracks' />">Tracks</a></li>
                <li class="active"><a href="#">Rooms</a></li>
                <li><a href="<c:url value='/secure/Speakers' />">Speakers</a></li>
                <li><a href="<c:url value='/secure/Schedule' />">Schedule</a></li>
                <li><a href="<c:url value='/secure/Feedback' />">Feedback</a></li>
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
        <div class="row">&nbsp;</div>
    </c:if>

    <div class="row">
        <div class="col-md-12">
            <a data-toggle="modal" href="#addLocation" class="btn btn-primary btn-sm">Add Location</a>
        </div>
    </div>

    <div class="row">&nbsp;</div>

    <div class="modal fade" id="addLocation" tabindex="-1" role="dialog" aria-labelledby="addLocation" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Add Location</h4>
                </div>
                <form accept-charset="utf-8" action="<c:url value='/secure/talkLocations'/>" role="form" method="POST">
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="newLocationName">Please enter the name of the location;</label>
                            <input type="text" name="name" id="newLocationName" class="form-control" placeholder="Room 1" />
                        </div>
                        <div class="form-group">
                            <label for="newLocationAddress">Please any helpful location hint;</label>
                            <input type="text" name="address" id="newLocationAddress" class="form-control" placeholder="Level 2" />
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary">Save</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <c:forEach var="location" items="${conference.talkLocationList}">
                <div class="modal fade" id="editLocation${location.id}" tabindex="-1" role="dialog" aria-labelledby="editLocation${location.id}" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Edit Location</h4>
                            </div>
                            <form accept-charset="utf-8" action="<c:url value='/secure/talkLocations'/>" role="form" method="POST">
                            <input type="hidden" name="id" value="${location.id}" />
                                <div class="modal-body">
                                    <div class="form-group">
                                        <label for="editLocation${location.id}Name">Please enter the name of the location;</label>
                                        <input type="text" name="name" id="editLocation${location.id}Name" class="form-control" value="${location.name}" />
                                    </div>
                                    <div class="form-group">
                                        <label for="editLocation${location.id}Address">Please any helpful location hint;</label>
                                        <input type="text" name="address" id="editLocation${location.id}Address" class="form-control" value="${location.address}" />
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-primary">Update</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <h4>${location.name}&nbsp;<a data-toggle="modal" href="#editLocation${location.id}"><span class="glyphicon glyphicon-pencil"></span></a></h4>
            </c:forEach>
        </div>
    </div>

    <div class="row">&nbsp;</div>

</div>
</body>
</html>
