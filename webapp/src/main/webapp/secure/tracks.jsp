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
                <li><a href="<c:url value='/secure/Admin'/>">Admin</a></li>
                <li class="active"><a href="#">Tracks</a></li>
                <li><a href="<c:url value='/secure/Rooms' />">Rooms</a></li>
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
    <c:if test="${not empty requestScope.error}">
        <div class="row"><div class="alert alert-danger text-center">${requestScope.error}</div></div>
        <div class="row">&nbsp;</div>
    </c:if>

    <div class="row">
        <div class="col-md-12">
            <a data-toggle="modal" href="#addTrackModal" class="btn btn-primary btn-sm">Add Track</a>
        </div>
    </div>

    <div class="row">&nbsp;</div>

    <div class="modal fade" id="addTrackModal" tabindex="-1" role="dialog" aria-labelledby="addTrackModal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Add Track</h4>
                </div>
                <form accept-charset="utf-8" action="<c:url value='/secure/tracks' />" role="form" method="POST">
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="newTrackName">Track Name;</label>
                            <input type="text" name="name" id="newTrackName" class="form-control" placeholder="Design" />
                        </div>
                        <div class="form-group">
                            <label for="newTrackColour">Track Colour;</label>
                            <input type="text" name="colour" id="newTrackColour" class="form-control" placeholder="#336699" />
                        </div>
                        <div class="form-group">
                            <label for="newTrackDescription">Description;</label>
                            <textarea name="description" id="newTrackDescription" class="form-control"></textarea>
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
            <c:forEach var="track" items="${requestScope.conference.trackList}">
                <div class="modal fade" id="editTrackModal${track.id}" tabindex="-1" role="dialog" aria-labelledby="editTrackModal${track.id}" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Edit Track</h4>
                            </div>
                            <form accept-charset="utf-8" action="<c:url value='/secure/tracks' />" role="form" method="POST">
                                <input type="hidden" name="id" value="${track.id}" />
                                <div class="modal-body">
                                    <div class="form-group">
                                        <label for="editTrack${track.id}Name">Track name;</label>
                                        <input type="text" name="name" id="editTrack${track.id}Name" class="form-control" value="${track.name}" placeholder="Design"/>
                                    </div>
                                    <div class="form-group">
                                        <label for="editTrack${track.id}Colour">Track Colour;</label>
                                        <input type="text" name="colour" id="editTrack${track.id}Colour" class="form-control" value="${track.colour}" placeholder="#336699"/>
                                    </div>
                                    <div class="form-group">
                                        <label for="editTrack${track.id}Description">Description;</label>
                                        <textarea name="description" id="editTrack${track.id}Description" class="form-control">${track.description}</textarea>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-primary">Update</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${not empty track.colour}">
                        <c:set var="track_colour" value="${track.colour}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="track_colour" value="#336699" />
                    </c:otherwise>
                </c:choose>


                <h4><span class="glyphicon glyphicon-stop" style="color: ${track_colour}"></span>&nbsp;
                    ${track.name}&nbsp;<a data-toggle="modal" href="#editTrackModal${track.id}"><span class="glyphicon glyphicon-pencil"></span></a></h4>
            </c:forEach>
        </div>
    </div>

    <div class="row">&nbsp;</div>
</div>
</body>
</html>
