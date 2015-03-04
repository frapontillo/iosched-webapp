<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="modal fade" id="editEventModal${slot.id}" tabindex="-1" role="dialog"
     aria-labelledby="editEventModal${slot.id}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Edit <c:out value="${startTime}"/>&nbsp;-&nbsp;<c:out value="${endTime}"/> Event</h4>
            </div>
            <form accept-charset="utf-8" action="breakSlots" role="form" method="POST">
                <input type="hidden" name="slot" value="${slot.id}"/>

                <div class="modal-body">
                    <div class="form-group">
                        <label for="editStart_${slot.id}">Please enter the start time (hh:mm);</label>
                        <input type="text" name="start" id="editStart_${slot.id}" class="form-control"
                               placeholder="hh:mm" value="${startTime}"/>
                    </div>
                    <div class="form-group">
                        <label for="editEnd_${slot.id}">Please enter the end time (hh:mm);</label>
                        <input type="text" name="end" id="editEnd_${slot.id}" class="form-control"
                               placeholder="hh:mm" value="${endTime}"/>
                    </div>
                    <div class="form-group">
                        <label for="editEventTitle_${slot.id}">Title</label>
                        <input type="text" name="event" id="editEventTitle_${slot.id}" class="form-control"
                               value="${slot.event}"/>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="deleteEventModal${slot.id}" tabindex="-1" role="dialog"
     aria-labelledby="deleteEventModal${slot.id}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Delete <c:out value="${startTime}"/>&nbsp;-&nbsp;<c:out value="${endTime}"/> slot</h4>
            </div>
            <form accept-charset="utf-8" action="breakSlots" role="form" method="POST">
                <input type="hidden" name="id" value="${slot.id}"/>
                <input type="hidden" name="action" value="delete"/>

                <div class="modal-body">
                    <p>Please confirm you wish to delete the <c:out value="${startTime}"/>&nbsp;-&nbsp;<c:out
                            value="${endTime}"/> <c:out value="${slot.event}"/> slot.</p>
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
            <c:out value="${startTime}"/>&nbsp;-&nbsp;<c:out value="${endTime}"/>&nbsp;:&nbsp;<c:out value="${slot.event}"/>&nbsp;
            <a data-toggle="modal" href="#editEventModal${slot.id}"><span class="glyphicon glyphicon-pencil"></span></a>
            <a data-toggle="modal" href="#deleteEventModal${slot.id}"><span class="glyphicon glyphicon-trash"></span></a>
        </h3>
    </div>
</div>