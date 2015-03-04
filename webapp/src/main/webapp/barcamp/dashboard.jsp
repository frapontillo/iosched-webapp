<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>${conference.name} Barcamp</title>
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
<div class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">${conference.name} Barcamp</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${empty user}">
                        <li><a href="<c:url value='/login.jsp'/>">Login</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="<c:url value='/Logout'/>">Logout</a></li>
                    </c:otherwise>
                </c:choose>
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

    <div class="modal fade" id="locked" tabindex="-1" role="dialog" aria-labelledby="locked" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Locked Voting</h4>
                </div>
                <div class="modal-body">
                    To ensure we can correctly track votes you need to enable cookies on your browser.
                </div>
                <div class="modal-footer">
                    <button type="button" data-dismiss="modal" class="btn btn-primary">OK</button>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <h4>Proposed Barcamp Talks</h4>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12" style="padding-bottom: 10px">
            Vote for the talks you'd like to see at the ${conference.name} barcamp!. If you want to find out more about
            a talk, click on it's title to read the details.
        </div>
    </div>

    <c:forEach var="talk" items="${talks}" varStatus="talkStatus">
        <div class="panel panel-default">
            <div class="panel-heading"><c:choose>
                <c:when test="${empty cookie.vid}"><a data-toggle="modal" href="#locked"><span class="glyphicon glyphicon-lock"></span></a></c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${talk.votedFor}">
                            <c:set var="lockClass" value="glyphicon glyphicon-star"/>
                            <c:set var="newVote" value="0" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="lockClass" value="glyphicon glyphicon-star-empty"/>
                            <c:set var="newVote" value="1" />
                        </c:otherwise>
                    </c:choose>
                    <a href="<c:url value='/barcamp/vote/${conference.id}?talk=${talk.talk.id}&vote=${newVote}'/>"><span class="${lockClass}"></span></a>
                </c:otherwise>
            </c:choose>
            <b onclick="showHide(${talk.talk.id});"><c:out value="${talk.talk.name}" /></b> by <c:forEach var="presenter" items="${talk.talk.presenters}" varStatus="status">
                <c:out value="${presenter}"/><c:if test="${not status.last}">,</c:if>&nbsp;
            </c:forEach></div>
            <div  id="${talk.talk.id}_content" onclick="showHide(${talk.talk.id});" class="panel-body" style="display:none">${talk.talk.shortDescription}</div>
        </div>
        <c:if test="${not talkStatus.last}"><div style="height:10px"></div></c:if>
    </c:forEach>

    <c:choose>
        <c:when test="${empty user}">
            <div class="alert alert-info">If you want to suggest a talk we'll need you to <a href="<c:url value='/register.jsp'/>"><b>Register</b></a>
            and <a href="<c:url value='/login.jsp' />"><b>Log in</b></a> so we can check you're registered to get in.</div>
        </c:when>
        <c:otherwise>
            <div class="modal fade" id="newTalkModal" tabindex="-1" role="dialog" aria-labelledby="newTalkModal" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title">Suggest a talk</h4>
                        </div>
                        <form accept-charset="utf-8" action="<c:url value='/barcamp/talks/${conference.id}'/>" role="form" method="POST">
                            <div class="modal-body">
                                <div class="form-group">
                                    <label for="userName">Your Name</label>
                                    <input type="text" id="userName" class="form-control" value="${user}" />
                                </div>
                                <div class="form-group">
                                    <label for="userEmail">Your Email</label>
                                    <input type="text" id="userEmail" class="form-control" readonly="readonly" value="${user.email}" />
                                </div>
                                <div class="form-group">
                                    <label for="title">Title</label>
                                    <input type="text" name="title" id="title" class="form-control" placeholder="A talk about something" />
                                </div>
                                <div class="form-group">
                                    <label for="newTalkDescription_${slot.id}">Description</label>
                                    <textarea name="description" id="newTalkDescription_${slot.id}" class="form-control"
                                              placeholder="This talk will cover something really interesting." ></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">Submit</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="row"><div class="col-md-12"><a data-toggle="modal" href="#newTalkModal" class="btn btn-primary">Suggest a talk</a></div></div>
            <div style="height:20px">&nbsp;</div>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>
