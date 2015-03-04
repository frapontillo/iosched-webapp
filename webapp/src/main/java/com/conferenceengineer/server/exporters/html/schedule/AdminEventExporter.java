package com.conferenceengineer.server.exporters.html.schedule;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.TalkSlot;
import com.conferenceengineer.server.exporters.html.AbstractScheduleTalkSlotExporter;

public class AdminEventExporter extends AbstractScheduleTalkSlotExporter {
    public AdminEventExporter(TalkSlot talkSlot) {
        super(talkSlot);
    }

    @Override
    public String createRow() {
        return "<div class=\"col-md-12\"><div class=\"scheduleEvent\">"
                + mTalkSlot.getEvent()
                + "</div></div>";
    }
}
