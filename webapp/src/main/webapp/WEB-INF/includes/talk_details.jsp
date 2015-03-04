<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="modal fade" id="deleteTalkModal${talk.id}" tabindex="-1" role="dialog"
     aria-labelledby="deleteTalkModal${talk.id}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Delete talk</h4>
            </div>
            <form accept-charset="utf-8" action="talks" role="form" method="POST">
                <input type="hidden" name="talkId" value="${talk.id}"/>
                <input type="hidden" name="action" value="delete"/>

                <div class="modal-body">
                    <p>Please confirm you wish to delete the talk &quot;<c:out value="${talk.name}"/>&quot;</p>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">OK</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="editTalkModal${talk.id}" tabindex="-1" role="dialog"
     aria-labelledby="editTalkModal${talk.id}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Edit Session</h4>
            </div>
            <form accept-charset="utf-8" action="talks" role="form" method="POST">
                <input type="hidden" name="talkId" value="${talk.id}"/>

                <div class="modal-body">
                    <div class="form-group">
                        <label for="editTalkSlot_${talk.id}">Time Slot</label>
                        <select name="slot" class="form-control" id="editTalkSlot_${talk.id}">
                            <c:forEach var="optionDay" items="${conference.dateList}">
                                <c:forEach var="optionSlot" items="${optionDay.talkSlotList}">
                                    <c:if test="${empty optionSlot.event}">
                                        <c:choose>
                                            <c:when test="${optionSlot.id eq slot.id}"><c:set var="optionSelected"
                                                                                              value="selected='selected'"/></c:when>
                                            <c:otherwise><c:set var="optionSelected" value=""/></c:otherwise>
                                        </c:choose>
                                        <option value="${optionSlot.id}" ${optionSelected}>
                                            <fmt:formatDate value="${optionDay.date}" pattern="dd MMMM yyyy"/>,
                                            <fmt:formatDate value="${optionSlot.start.time}"
                                                            pattern="HH:mm"/>-<fmt:formatDate
                                                value="${optionSlot.end.time}" pattern="HH:mm"/>
                                        </option>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="editTalkTitle_${talk.id}">Title</label>
                        <input type="text" name="title" id="editTalkTitle_${talk.id}" class="form-control"
                               value="${talk.name}"/>
                    </div>
                    <div class="form-group">
                        <label for="editTalkTrack_${talk.id}">Track</label>
                        <select name="track" id="editTalkTrack_${talk.id}" class="form-control">
                            <c:choose>
                                <c:when test="${talk.type == 2}">
                                    <option value="-1" selected="selected">Keynote</option>
                                    <c:forEach var="track" items="${conference.trackList}">
                                        <option value="${track.id}">${track.name}</option>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <option value="-1">Keynote</option>
                                    <c:forEach var="track" items="${conference.trackList}">
                                        <c:choose>
                                            <c:when test="${talk.track.id == track.id}">
                                                <option value="${track.id}"
                                                        selected="selected">${track.name}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${track.id}">${track.name}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="editTalkLocation_${talk.id}">Location</label>
                        <select name="location" id="editTalkLocation_${talk.id}" class="form-control">
                            <c:forEach var="location" items="${conference.talkLocationList}">
                                <c:choose>
                                    <c:when test="${talk.location.id == location.id}">
                                        <option value="${location.id}"
                                                selected="selected">${location.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${location.id}">${location.name}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="editTalkInfoURL_${talk.id}">More Info URL</label>
                        <input type="text" name="infoURL" id="editTalkInfoURL_${talk.id}" class="form-control"
                               value="${talk.informationURL}"/>
                    </div>
                    <div class="form-group">
                        <label for="editTalkDescription_${talk.id}">Description</label>
                        <textarea name="description" id="editTalkDescription_${slot.id}"
                                  class="form-control">${talk.shortDescription}</textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="addPresenterModal${talk.id}" tabindex="-1" role="dialog"
     aria-labelledby="addPresenterModal${talk.id}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Add A Presenter</h4>
            </div>
            <form accept-charset="utf-8" action="talks" role="form" method="POST">
                <input type="hidden" name="talkId" value="${talk.id}"/>

                <div class="modal-body">
                    <div class="form-group">
                        <label for="addPresenterModal${slot.id}">Speaker</label>
                        <select name="presenter" id="addPresenterModal${slot.id}" class="form-control">
                            <c:forEach var="presenter" items="${conference.presenterList}">
                                <option value="${presenter.id}">${presenter.name}</option>
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


<div class="row">
    <a name="talk_${talk.id}">&nbsp;</a>

    <div class="col-md-12">
        <h4>
            <span onclick="showHide('t${talk.id}');">[
                <c:choose>
                    <c:when test="${talk.type == 2}">
                        Keynote
                    </c:when>
                    <c:otherwise>
                        ${talk.track.name}
                    </c:otherwise>
                </c:choose>]
                ${talk.name}
            </span>&nbsp;
            <a data-toggle="modal" href="#editTalkModal${talk.id}"><span
                    class="glyphicon glyphicon-pencil"></span></a>
            <a data-toggle="modal" href="#deleteTalkModal${talk.id}"><span
                    class="glyphicon glyphicon-trash"></span></a>
        </h4>

        <p>${talk.location.name}</p>

        <c:forEach var="presenter" items="${talk.presenters}" varStatus="status">
        <div class="modal fade" id="removePresenterModal${talk.id}${presenter.id}" tabindex="-1"
             role="dialog" aria-labelledby="removePresenterModal${talk.id}${presenter.id}"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Remove A Presenter</h4>
                    </div>
                    <form accept-charset="utf-8" action="talks" role="form" method="POST">
                        <input type="hidden" name="talkId" value="${talk.id}">
                        <input type="hidden" name="presenter" value="${presenter.id}">
                        <input type="hidden" name="delete" value="true">

                        <div class="modal-body">
                            <p>Remove ${presenter.name} from ${talk.name}.</p>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Confirm</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <a data-toggle="modal"
           href="#removePresenterModal${talk.id}${presenter.id}">${presenter.name}</a><c:if
            test="${not status.last}">,</c:if>&nbsp;
        </form>
        </c:forEach>
        <a data-toggle="modal" href="#addPresenterModal${talk.id}" class="btn btn-default btn-xs">Add speaker</a>

        <div id="t${talk.id}_hiddenhint" onclick="showHide('t${talk.id}');">
            &hellip;
        </div>
        <div id="t${talk.id}_content" style="display:none">
            <p>${talk.shortDescription}</p>
        </div>
    </div>
</div>
