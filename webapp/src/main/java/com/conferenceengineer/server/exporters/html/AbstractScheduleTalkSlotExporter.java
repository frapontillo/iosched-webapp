package com.conferenceengineer.server.exporters.html;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.TalkSlot;

/**
 * Base class for exporting schedule entried.
 */
public abstract class AbstractScheduleTalkSlotExporter {
    protected TalkSlot mTalkSlot;

    public AbstractScheduleTalkSlotExporter(final TalkSlot talkSlot) {
        mTalkSlot = talkSlot;
    }

    protected abstract String createRow();

    public String toHtml() {
        return "<div class=\"row\">" + createRow() + "</div>";
    }

    public String toString() {
        return toHtml();
    }
}
