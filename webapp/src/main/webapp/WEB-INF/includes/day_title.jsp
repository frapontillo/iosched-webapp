<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="modal fade" id="deleteDayModal${conferenceDay.id}" tabindex="-1" role="dialog"
     aria-labelledby="deleteDayModal${conferenceDay.id}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Delete Session Slot</h4>
            </div>
            <form accept-charset="utf-8" action="conferenceDays" role="form" method="POST">
                <input type="hidden" name="id" value="${conferenceDay.id}"/>
                <input type="hidden" name="action" value="delete"/>

                <div class="modal-body">
                    <p>Please confirm you wish to remove <c:out value="${conferenceDate}"/> from
                        your conference schedule.</p>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">OK</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="addSessionModal${dateCode}" tabindex="-1" role="dialog"
     aria-labelledby="addSessionModal${dateCode}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Add Session Slot</h4>
            </div>
            <form accept-charset="utf-8" action="talkSlots" role="form" method="POST">
                <input type="hidden" name="day" value="${conferenceDay.id}"/>

                <div class="modal-body">
                    <div class="form-group">
                        <label for="newStart_${dateCode}">Please enter the start time (hh:mm);</label>
                        <input type="text" name="start" id="newStart_${dateCode}" class="form-control"
                               placeholder="hh:mm"/>
                    </div>
                    <div class="form-group">
                        <label for="newEnd_${dateCode}">Please enter the end time (hh:mm);</label>
                        <input type="text" name="end" id="newEnd_${dateCode}" class="form-control" placeholder="hh:mm"/>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="addBreakModal${dateCode}" tabindex="-1" role="dialog"
     aria-labelledby="addBreakModal${dateCode}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Add Break/Admin Event</h4>
            </div>
            <form accept-charset="utf-8" action="breakSlots" role="form" method="POST">
                <input type="hidden" name="day" value="${conferenceDay.id}"/>

                <div class="modal-body">
                    <div class="form-group">
                        <label for="newStart_${dateCode}">Please enter the start time (hh:mm);</label>
                        <input type="text" name="start" id="newStart_${dateCode}" class="form-control"
                               placeholder="hh:mm"/>
                    </div>
                    <div class="form-group">
                        <label for="newEnd_${dateCode}">Please enter the end time (hh:mm);</label>
                        <input type="text" name="end" id="newEnd_${dateCode}" class="form-control"
                               placeholder="hh:mm"/>
                    </div>
                    <div class="form-group">
                        <label for="newName_${dateCode}">Please enter a name (e.g. Registration, Coffee Break);</label>
                        <input type="text" name="name" id="newName_${dateCode}" class="form-control"
                               placeholder="Coffee Break"/>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<p><a name="day_${conferenceDay.id}">&nbsp;</a></p>

<div class="row">
    <div class="col-md-12">
        <h2><c:out value="${conferenceDate}"/>
            <a data-toggle="modal" href="#deleteDayModal${conferenceDay.id}"><span
                    class="glyphicon glyphicon-trash"></span></a>
        </h2>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <c:set var="currentDay" scope="request" value="${conferenceDay}"/>
        <c:import url="/WEB-INF/includes/day_slots.jsp"/>
        <div class="spacer">&nbsp;</div>
        <a data-toggle="modal" href="#addSessionModal${dateCode}" class="btn btn-primary btn-sm">Add Session Slot</a>
        &nbsp;
        <a data-toggle="modal" href="#addBreakModal${dateCode}" class="btn btn-primary btn-sm">Add Break/Admin
            Event</a>

    </div>
</div>
