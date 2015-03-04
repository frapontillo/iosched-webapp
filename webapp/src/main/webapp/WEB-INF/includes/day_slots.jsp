<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:forEach var="slot" items="${currentDay.talkSlotList}">
    <fmt:formatDate var="startTime" value="${slot.start.time}" pattern="HH:mm"/>
    <fmt:formatDate var="endTime" value="${slot.end.time}" pattern="HH:mm"/>
    <a href="#slot_${slot.id}" class="btn btn-default btn-sm">${startTime} - ${endTime}</a>&nbsp;
</c:forEach>
