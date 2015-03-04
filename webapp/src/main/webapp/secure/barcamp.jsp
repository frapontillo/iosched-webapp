<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<script type="text/javascript">
    <!--
    function showHide(id) {
        var content = document.getElementById(id+"_content");
        if(content.style.display == 'block') {
            content.style.display = 'none';
        } else {
            content.style.display = 'block';
        }
    }
    //-->
</script>
</head>
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
                <li><a href="<c:url value='/secure/Tracks' />">Tracks</a></li>
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
    <div class="row"><div class="col-md-12">Your barcamp URL is <a href="https://conferenceengineer.com/barcamp/view/${conference.id}" target="_blank">https://conferenceengineer.com/barcamp/view/${conference.id}</a></div></div>
    <div class="row">&nbsp;</div>
    <c:if test="${not empty sessionScope.error}">
        <div class="row"><div class="alert alert-danger text-center">${sessionScope.error}</div></div>
        <c:set scope="session" var="error" value="" />
    </c:if>
    <c:if test="${not empty sessionScope.message}">
        <div class="row"><div class="alert alert-success text-center">${sessionScope.message}</div></div>
        <c:set scope="session" var="message" value="" />
    </c:if>

    <c:forEach var="talkholder" items="${sortedTalks}" varStatus="talkStatus">
        <c:set var="talk" value="${talkholder.talk}"/>

        <div class="modal fade" id="deleteTalkModal${talk.id}" tabindex="-1" role="dialog" aria-labelledby="deleteTalkModal${talk.id}" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Delete Barcamp Talk</h4>
                    </div>
                    <form accept-charset="utf-8" action="<c:url value='/secure/talks'/>" role="form" method="POST">
                        <input type="hidden" name="talkId" value="${talk.id}" />
                        <input type="hidden" name="action" value="delete" />
                        <input type="hidden" name="next" value="Barcamp" />
                        <div class="modal-body">
                            <p>Please confirm you wish to delete <c:out value="${talk.name}" /></p>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">OK</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="scheduleTalkModal${talk.id}" tabindex="-1" role="dialog" aria-labelledby="scheduleTalkModal${talk.id}" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Select Time Slot</h4>
                    </div>
                    <form accept-charset="utf-8" action="<c:url value='/secure/barcamp'/>" role="form" method="POST">
                        <input type="hidden" name="talkId" value="${talk.id}" />
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="scheduleTalkSlot${talk.id}">Time slot for <c:out value='${talk.name}'/></label>
                                <select name="slot" id="scheduleTalkSlot${talk.id}" class="form-control">
                                    <c:forEach var="slot" items="${slots}">
                                        <option value="${slot.id}">${slot.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Save</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">[${talkholder.votes}<span class="glyphicon glyphicon-star"></span>]&nbsp;
                <b onclick="showHide(${talk.id});"><c:out value="${talk.name}" /></b> by <c:forEach var="presenter" items="${talk.presenters}" varStatus="status">
                    <c:out value="${presenter}"/><c:if test="${not status.last}">,</c:if>&nbsp;
                </c:forEach>&nbsp;
                <a data-toggle="modal" href="#deleteTalkModal${talk.id}"><span class="glyphicon glyphicon-trash"></span></a>
                <a data-toggle="modal" href="#scheduleTalkModal${talk.id}"><span class="glyphicon glyphicon-time"></span></a>
            </div>
            <div  id="${talk.id}_content" onclick="showHide(${talk.id});" class="panel-body" style="display:none">${talk.shortDescription}</div>
        </div>
        <c:if test="${not talkStatus.last}"><div style="height:10px"></div></c:if>
    </c:forEach>
</div>
</body>
</html>
