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
                <li><a href="#">Admin</a></li>
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
    <c:if test="${not empty sessionScope.error}">
        <div class="row"><div class="alert alert-danger text-center">${sessionScope.error}</div></div>
        <c:set scope="session" var="error" value="" />
    </c:if>
    <c:if test="${not empty sessionScope.message}">
        <div class="row"><div class="alert alert-success text-center">${sessionScope.message}</div></div>
        <c:set scope="session" var="message" value="" />
    </c:if>

    <c:if test="${not empty serverStatus}">
        <div class="row">
            <c:choose>
                <c:when test='${requestScope.serverStatusType eq "Good"}'><c:set var="statusClass">alert-info</c:set></c:when>
                <c:when test='${requestScope.serverStatusType eq "Bad"}'><c:set var="statusClass">alert-warning</c:set></c:when>
                <c:when test='${requestScope.serverStatusType eq "Ugly"}'><c:set var="statusClass">alert-danger</c:set></c:when>
                <c:otherwise><c:set var="statusClass">alert-info</c:set></c:otherwise>
            </c:choose>
            <div class="alert ${statusClass} text-center">${serverStatus}</div>
        </div>
    </c:if>

    <ul class="nav nav-tabs" id="tabs">
        <li><a href="#conference" data-toggle="tab">Conference</a></li>
        <li><a href="#app" data-toggle="tab">App Static Data</a></li>
        <li><a href="#ota" data-toggle="tab">OTA Updates</a></li>
        <li><a href="#user" data-toggle="tab">Your Account</a></li>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="conference">
            <div class="row">
                <div class="col-md-12">
                    <h4>Time Zone</h4>
                    <form accept-charset="utf-8" action="<c:url value='/secure/timezone' />"
                          role="form" method="POST">
                        <div class="form-group">
                            <select name="timezone" id="timezone" class="form-control">
                                <c:forEach var="timezone" items="${timezones}"><c:choose>
                                        <c:when test="${conference.timezone eq timezone}"><c:set var="selected" value="selected=\"selected\"" /></c:when>
                                        <c:otherwise><c:set var="selected" value="" /></c:otherwise>
                                    </c:choose><option ${selected}>${timezone}</option></c:forEach>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">Update</button>
                    </form>
                </div>
            </div>

            <div class="row">&nbsp;</div>

            <div class="row">
                <div class="col-md-12">
                    <h4>Hashtag</h4>
                    <p>If you plan to use the IOSched14 based app you <b>must</b> have a conference hashtag.</p>
                    <form accept-charset="utf-8" action="setConferenceHashtag" role="form" method="POST">
                        <div class="form-group">
                            <input type="text" name="hashtag" id="hashtag" class="form-control" value="${conference.hashtag}" />
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">Update</button>
                    </form>
                </div>
            </div>

            <div class="row">&nbsp;</div>

            <div class="row">
                <div class="col-md-12">
                    <h4>Collaborators</h4>
                    <ul>
                        <c:forEach var="permission" items="${requestScope.conference.collaborators}">
                            <li><c:out value="${permission.systemUser}"/></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <p>If you want to invite other people to work on this schedule, enter their email address below.</p>
                    <form accept-charset="utf-8" action="invite" class="form-horizontal" role="form" method="POST">
                        <div class="form-group">
                            <label for="email" class="col-lg-2 control-label">Email Address</label>
                            <div class="col-lg-4"><input type="text" name="email" id="email" class="form-control" /></div>
                        </div>
                        <div class="col-lg-6 text-right">
                            <button type="submit" class="btn btn-primary btn-sm">Send Invite</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="tab-pane active" id="app">
            <div class="row">
                <div class="col-md-12">
                    <h3>Schedule in a page</h3>
                    <p>The &quot;Schedule in a page&quot; provides you with a single HTML page you can host
                    which provides all the details of the talks in your conference.</p>
                    <ul>
                        <li><a href="<c:url value='/secure/scheduleInAPage?conference=${conference.id}'/>">Download</a></li>
                    </ul>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <h3><a href="https://github.com/FunkyAndroid/iosched/tree/iosched2014_conferenceengineer_master">IOSched 2014</a></h3>
                    <h4>Downloads</h4>
                    <ul>
                        <li><a href="<c:url value='/secure/io14BootstrapFile'/>">IO14 Bootstrap File</a></li>
                    </ul>
                    <h4>Build Configuration</h4>
                    <c:if test="${not empty requestScope.conference.publicationEndpoints}">
                        <div class="row">
                            <div class="col-md-3 text-right">
                                CONFERENCE_DATA_MANIFEST_URL :
                            </div>
                            <div class="col-md-9">
                                <c:forEach var="endpoint" items="${requestScope.conference.publicationEndpoints}">
                                    <c:out value="${endpoint.value.url}" />
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>

                    <div class="row">
                        <div class="col-md-3 text-right">
                            FEEDBACK_API_CODE :
                        </div>
                        <div class="col-md-9">
                            <c:out value="${requestScope.conference.id}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3 text-right">
                            FEEDBACK_API_KEY :
                        </div>
                        <div class="col-md-9">
                            <c:out value="${requestScope.conference.survey.apiKey}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3 text-right">
                            SURVEY_ID :
                        </div>
                        <div class="col-md-9">
                            <c:out value="${requestScope.conference.survey.id}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3 text-right">
                            FEEDBACK_URL :
                        </div>
                        <div class="col-md-9">
                            https://conferenceengineer.com/submitSurvey
                        </div>
                    </div>

                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <h3><a href="https://github.com/FunkyAndroid/iosched/tree/iosched2013_conferenceengineer_master">IOSched 2013</a></h3>
                    <h4>Downloads</h4>
                    <ul>
                        <li><a href="<c:url value='/secure/talkLocations' />" target="_blank">Rooms JSON</a></li>
                        <li><a href="<c:url value='/secure/searchSuggestions' />" target="_blank">Search Suggestions JSON</a></li>
                        <li><a href="<c:url value='/secure/breakSlots' />" target="_blank">Common Slots JSON</a></li>
                        <li><a href="<c:url value='/secure/talks' />" target="_blank">Sessions JSON</a></li>
                        <li><a href="<c:url value='/secure/speakers' />" target="_blank">Speakers JSON</a></li>
                        <li><a href="<c:url value='/secure/tracks' />" target="_blank">Tracks JSON</a></li>
                        <li><a href="<c:url value='/secure/trackSessions' />" target="_blank">Track/Sessions JSON</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="tab-pane active" id="ota">
            <div class="spacer">&nbsp;</div>
            <div class="row">
                <div class="col-md-12">
                    Last published :&nbsp;
                    <c:choose>
                        <c:when test="${requestScope.conference.metadata == null || requestScope.conference.metadata.lastPublished == null}"><i>Never</i></c:when>
                        <c:otherwise><fmt:formatDate value="${requestScope.conference.metadata.lastPublished.time}" pattern="MMM dd, yyyy 'at' HH:mm"/></c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div style="height: 10px">&nbsp;</div>
            <div class="row">
                <div class="col-md-12">
                    <a href="<c:url value='/secure/publish' />" class="btn btn-primary">Publish Now</a>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <p><b>Please Note:</b> The updates are delivered by a world-wide content delivery network,
                        and so there may be a delay of up to 30 minutes between you publishing updates and your
                        attendees starting to receive them.</p>
                </div>
            </div>
        </div>
        <div class="tab-pane active" id="user">
            <div class="row">
                <div class="col-md-12">
                    <div class="spacer">&nbsp;</div>
                    <form accept-charset="utf-8" action="users" class="form-horizontal" role="form" method="POST">
                        <input type="hidden" name="id" value="${requestScope.user.id}" />
                        <div class="form-group">
                            <label for="login" class="col-lg-2 control-label">Your login</label>
                            <div class="col-lg-4"><input type="text" id="login" class="form-control" readonly="readonly" value="${requestScope.user}" /></div>
                        </div>
                        <div class="form-group">
                            <label for="password1" class="col-lg-2 control-label">New password</label>
                            <div class="col-lg-4"><input type="password" name="password1" id="password1" class="form-control" /></div>
                        </div>
                        <div class="form-group">
                            <label for="password2" class="col-lg-2 control-label">Confirm new password</label>
                            <div class="col-lg-4"><input type="password" name="password2" id="password2" class="form-control" /></div>
                        </div>
                        <div class="col-lg-6 text-right">
                            <button type="submit" class="btn btn-primary btn-sm">Update</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script language="javascript">
    $(window).load(function() {
        $('#tabs a[href="#conference"]').tab('show');
    });
</script>
</body>
</html>
