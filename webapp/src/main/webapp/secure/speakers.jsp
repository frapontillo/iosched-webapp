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
                <li class="active"><a href="#">Speakers</a></li>
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
    <c:if test="${not empty error}">
        <div class="row"><div class="alert alert-danger text-center">${error}</div></div>
        <div class="row">&nbsp;</div>
    </c:if>

    <div class="row">
        <div class="col-md-12">
            <a data-toggle="modal" href="#addSpeaker" class="btn btn-primary btn-sm">Add Speaker</a>
        </div>
    </div>

    <div class="row">&nbsp;</div>

    <div class="modal fade" id="addSpeaker" tabindex="-1" role="dialog" aria-labelledby="addSpeaker" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Add Speaker</h4>
                </div>
                <form accept-charset="utf-8" action="speakers" role="form" method="POST">
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="newSpeakerName">Speaker name;</label>
                            <input type="text" name="name" id="newSpeakerName" class="form-control" placeholder="Al Sutton" />
                        </div>
                        <div class="form-group">
                            <label for="newSpeakerImage">URL for the speakers picture;</label>
                            <input type="text" name="imageURL" id="newSpeakerImage" class="form-control" placeholder="http://www.xyz.com/pic_of_al.png" />
                        </div>
                        <div class="form-group">
                            <label for="newSpeakerSocialURL">URL for the speakers social network;</label>
                            <input type="text" name="socialURL" id="newSpeakerSocialURL" class="form-control" placeholder="http://www.twitter.com/alsutton" />
                        </div>
                        <div class="form-group">
                            <label for="newSpeakerShortBio">Short Biography;</label>
                            <textarea name="shortBio" id="newSpeakerShortBio" class="form-control" placeholder="Al has been talking for ages." rows="2"></textarea>
                        </div>
                        <div class="form-group">
                            <label for="newSpeakerLongBio">Long Biography;</label>
                            <textarea name="longBio" id="newSpeakerLongBio" class="form-control" placeholder="Al has been talking for ages and is still talking :O" rows="5"></textarea>
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
            <c:forEach var="presenter" items="${conference.presenterList}">
                <div class="modal fade" id="editSpeaker_${presenter.id}" tabindex="-1" role="dialog" aria-labelledby="editSpeaker_${presenter.id}" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Edit Speaker</h4>
                            </div>
                            <form accept-charset="utf-8" action="speakers" role="form" method="POST">
                            <input type="hidden" name="speakerId" value="${presenter.id}" />
                                <div class="modal-body">
                                    <div class="form-group">
                                        <label for="editSpeaker_${presenter.id}_Name">Speaker name;</label>
                                        <input type="text" name="name" id="editSpeaker_${presenter.id}_Name" class="form-control" value="${presenter.name}" />
                                    </div>
                                    <div class="form-group">
                                        <label for="editSpeaker_${presenter.id}_Image">URL for the speakers picture;</label>
                                        <input type="text" name="imageURL" id="editSpeaker_${presenter.id}_Image" class="form-control" value="${presenter.imageURL}" />
                                    </div>
                                    <div class="form-group">
                                        <label for="editSpeaker_${presenter.id}_SocialURL">URL for the speakers social network;</label>
                                        <input type="text" name="socialURL" id="editSpeaker_${presenter.id}_SocialURL" class="form-control" value="${presenter.socialLink}" />
                                    </div>
                                    <div class="form-group">
                                        <label for="editSpeaker_${presenter.id}_ShortBio">Short Biography;</label>
                                        <textarea name="shortBio" id="editSpeaker_${presenter.id}_ShortBio" class="form-control" rows="2">${presenter.shortBiography}</textarea>
                                    </div>
                                    <div class="form-group">
                                        <label for="editSpeaker_${presenter.id}_LongBio">Long Biography;</label>
                                        <textarea name="longBio" id="editSpeaker_${presenter.id}_LongBio" class="form-control" rows="5">${presenter.longBiography}</textarea>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-primary">Save</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <h4>${presenter.name}&nbsp;<a data-toggle="modal" href="#editSpeaker_${presenter.id}"><span class="glyphicon glyphicon-pencil"></span></a></h4>
                <p>${presenter.shortBiography}</p>
            </c:forEach>
        </div>
    </div>

    <div class="row">&nbsp;</div>
</div>

</body>
</html>
