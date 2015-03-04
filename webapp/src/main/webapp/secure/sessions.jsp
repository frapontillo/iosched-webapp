<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<script type="text/javascript">
    <!--
    function showHide(id) {
        var content = document.getElementById(id+"_content");
        var hiddenHint = document.getElementById(id+"_hiddenhint");
        if(content.style.display == 'block') {
            content.style.display = 'none';
            hiddenHint.style.display = 'block';
        } else {
            content.style.display = 'block';
            hiddenHint.style.display = 'none';
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
                <li class="active"><a href="#">Schedule</a></li>
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
    <c:if test="${not empty error}">
        <div class="row"><div class="alert alert-danger text-center">${error}</div></div>
        <div class="row">&nbsp;</div>
    </c:if>

    <div class="modal fade" id="addDayModal" tabindex="-1" role="dialog" aria-labelledby="addDayModal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Add Day</h4>
                </div>
                <form accept-charset="utf-8" action="conferenceDays" role="form" method="POST">
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="newDay">Please enter date (dd-mm-yyyy);</label>
                            <input type="text" name="date" id="newDay" class="form-control" placeholder="dd-mm-yyyy" />
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
            <c:import url="/WEB-INF/includes/conference_days.jsp"/>
            <div class="spacer">&nbsp;</div>
            <a data-toggle="modal" href="#addDayModal" class="btn btn-primary btn-sm">Add Day</a>
        </div>
    </div>

    <c:forEach var="conferenceDay" items="${conference.dateList}">
        <fmt:formatDate var="conferenceDate" scope="request" value="${conferenceDay.date}" pattern="dd MMMM yyyy"/>
        <fmt:formatDate var="dateCode" scope="request" value="${conferenceDay.date}" pattern="yyyyMMdd"/>
        <c:set var="conferenceDay" scope="request" value="${conferenceDay}" />

        <c:import url="/WEB-INF/includes/day_title.jsp" />

        <div style="padding-top:5px">&nbsp;</div>

        <c:forEach var="slot" items="${conferenceDay.talkSlotList}">
            <fmt:formatDate var="startTime" scope="request" value="${slot.start.time}" pattern="HH:mm"/>
            <fmt:formatDate var="endTime" scope="request" value="${slot.end.time}" pattern="HH:mm"/>
            <c:set scope="request" var="slot" value="${slot}"/>

            <p><a name="slot_${slot.id}">&nbsp;</a></p>

            <c:choose>
                <c:when test="${slot.event eq null}">
                    <c:import url="/WEB-INF/includes/talk_slot.jsp" />
                </c:when>
                <c:otherwise>
                    <jsp:include page="/WEB-INF/includes/event_slot.jsp" />
                </c:otherwise>
            </c:choose>

        </c:forEach>
    </c:forEach>
    <div class="row">&nbsp;</div>

</div>
</body>
</html>
