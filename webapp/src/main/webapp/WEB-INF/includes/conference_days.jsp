<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:forEach var="conferenceDay" items="${conference.dateList}">
    <fmt:formatDate var="conferenceDate" value="${conferenceDay.date}" pattern="dd MMMM"/>
    <a href="#day_${conferenceDay.id}" class="btn btn-default btn-sm"><c:out value="${conferenceDate}" /></a>&nbsp;
</c:forEach>
