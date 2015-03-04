<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="modal fade" id="editSlotModal${slot.id}" tabindex="-1" role="dialog"
     aria-labelledby="editSlotModal${slot.id}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Edit Session Slot</h4>
            </div>
            <form accept-charset="utf-8" action="talkSlots" role="form" method="POST">
                <input type="hidden" name="slot" value="${slot.id}"/>

                <div class="modal-body">
                    <div class="form-group">
                        <label for="editStart${slot.id}">Start time (hh:mm);</label>
                        <input type="text" name="start" id="editStart${slot.id}" class="form-control"
                               value="${startTime}"/>
                    </div>
                    <div class="form-group">
                        <label for="editEnd${slot.id}">End time (hh:mm);</label>
                        <input type="text" name="end" id="editEnd${slot.id}" class="form-control" value="${endTime}"/>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Update</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="deleteSlotModal${slot.id}" tabindex="-1" role="dialog"
     aria-labelledby="deleteSlotModal${slot.id}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Delete Session Slot</h4>
            </div>
            <form accept-charset="utf-8" action="talkSlots" role="form" method="POST">
                <input type="hidden" name="id" value="${slot.id}"/>
                <input type="hidden" name="action" value="delete"/>

                <div class="modal-body">
                    <p>Please confirm you wish to delete the <c:out value="${startTime}"/>&nbsp;-&nbsp;<c:out
                            value="${endTime}"/>
                        slot for <c:out value="${conferenceDate}"/></p>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">OK</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="panel panel-default">
<div class="panel-heading" onclick="showHide(${slot.id});">
    <h3 class="panel-title">
        <c:out value="${startTime}"/>&nbsp;-&nbsp;<c:out value="${endTime}"/>&nbsp;
        <a data-toggle="modal" href="#editSlotModal${slot.id}"><span class="glyphicon glyphicon-pencil"></span></a>
        <a data-toggle="modal" href="#deleteSlotModal${slot.id}"><span class="glyphicon glyphicon-trash"></span></a>
    </h3>
</div>
<div class="panel-body">
<div class="modal fade" id="newTalkModal${slot.id}" tabindex="-1" role="dialog" aria-labelledby="newTalkModal${slot.id}"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Add A Session</h4>
            </div>
            <form accept-charset="utf-8" action="talks" role="form" method="POST">
                <input type="hidden" name="slot" value="${slot.id}"/>

                <div class="modal-body">
                    <div class="form-group">
                        <label for="newTalkSlot_${slot.id}">Time Slot</label>
                        <input type="text" id="newTalkSlot_${slot.id}" class="form-control" readonly="readonly"
                               value="${conferenceDate}, ${startTime}-${endTime}"/>
                    </div>
                    <div class="form-group">
                        <label for="newTalkTitle_${slot.id}">Title</label>
                        <input type="text" name="title" id="newTalkTitle_${slot.id}" class="form-control"
                               placeholder="A talk about something"/>
                    </div>
                    <div class="form-group">
                        <label for="newTalkPresenter_${slot.id}">Speaker</label>
                        <select name="presenter" id="newTalkPresenter_${slot.id}" class="form-control">
                            <c:forEach var="presenter" items="${conference.presenterList}">
                                <option value="${presenter.id}">${presenter.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="newTalkTrack_${slot.id}">Track</label>
                        <select name="track" id="newTalkTrack_${slot.id}" class="form-control">
                            <option value="-1">Keynote</option>
                            <c:forEach var="track" items="${conference.trackList}">
                                <option value="${track.id}">${track.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="newTalkLocation_${slot.id}">Location</label>
                        <select name="location" id="newTalkLocation_${slot.id}" class="form-control">
                            <c:forEach var="location" items="${conference.talkLocationList}">
                                <option value="${location.id}">${location.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="newTalkInfoURL_${slot.id}">More Info URL</label>
                        <input type="text" name="infoURL" id="newTalkInfoURL_${slot.id}" class="form-control"
                               placeholder="http://somewhere/page"/>
                    </div>
                    <div class="form-group">
                        <label for="newTalkDescription_${slot.id}">Description</label>
                        <textarea name="description" id="newTalkDescription_${slot.id}" class="form-control"
                                  placeholder="This talk will cover something really interesting."></textarea>
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
    <div class="col-md-12"><a data-toggle="modal" href="#newTalkModal${slot.id}" class="btn btn-default btn-sm">Add a
        session</a></div>
</div>
<div style="height: 10px">&nbsp;</div>
<c:if test="${not empty slot.talkList}">
    <c:forEach var="talk" items="${slot.talkList}">
        <c:set scope="request" var="talk" value="${talk}"/>
        <c:import url="/WEB-INF/includes/talk_details.jsp"/>
    </c:forEach>
</c:if>
</div>
</div>
