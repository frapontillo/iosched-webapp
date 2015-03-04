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
                <li><a href="<c:url value='/secure/Tracks' />">Tracks</a></li>
                <li><a href="<c:url value='/secure/Rooms' />">Rooms</a></li>
                <li><a href="<c:url value='/secure/Speakers' />">Speakers</a></li>
                <li><a href="<c:url value='/secure/Schedule' />">Schedule</a></li>
                <li class="active"><a href="#">Feedback</a></li>
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

    <c:forEach var="conferenceDay" items="${requestScope.conference.dateList}">
        <fmt:formatDate var="conferenceDate" scope="request" value="${conferenceDay.date}" pattern="dd MMMM yyyy"/>
        <c:set var="conferenceDay" scope="request" value="${conferenceDay}" />

        <div class="row"><div class="col-md-12"><h4><c:out value="${conferenceDate}" /></h4></div></div>

        <c:forEach var="slot" items="${conferenceDay.talkSlotList}">
            <fmt:formatDate var="startTime" scope="request" value="${slot.start.time}" pattern="HH:mm"/>
            <fmt:formatDate var="endTime" scope="request" value="${slot.end.time}" pattern="HH:mm"/>
            <c:set scope="request" var="slot" value="${slot}"/>

            <c:if test="${slot.event eq null}">
                <c:if test="${not empty slot.talkList}">
                    <div class="row"><div class="col-md-12"><h5>${startTime} - ${endTime}</h5></div></div>

                    <c:forEach var="talk" items="${slot.talkList}">
                        <h5><c:out value="${talk.name}" /></h5>
                        <c:choose>
                            <c:when test="${not empty talk.answers}">
                                <c:forEach var="response" items="${talk.surveyResponses}">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <p><b><c:out value="${response.question.question}" /></b><br/>
                                                <c:forEach var="responseLine" items="${response.summaries}">
                                                    ${responseLine}<br />
                                                </c:forEach>
                                            </p>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p><i>No responses have been received.</i></p>
                            </c:otherwise>
                        </c:choose>
                        <div class="spacer">&nbsp;</div>
                    </c:forEach>
                </c:if>
            </c:if>

        </c:forEach>
        <div class="spacer">&nbsp;</div>
    </c:forEach>
    <div class="row">&nbsp;</div>

    <div class="row">&nbsp;</div>
</div>

</body>
</html>
