package com.conferenceengineer.server.exporters.html;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.ConferenceDay;
import com.conferenceengineer.server.datamodel.TalkSlot;
import com.conferenceengineer.server.exporters.html.schedule.AdminEventExporter;
import com.conferenceengineer.server.exporters.html.schedule.MultiTalkExporter;

import java.text.SimpleDateFormat;

/**
 * Export the conference schedule in an HTML format.
 */
public class ScheduleExporter {
    protected Conference mConference;

    private static final String HTML_HEADER = "<!DOCTYPE html>\n" +
            "<html lang=\"en\"> \n" +
            "<head>\n" +
            "<meta charset=\"utf-8\"/>\n" +
            "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css\">\n" +
            "<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>\n" +
            "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js\"></script>\n" +
            "<style>\n" +
            ".day { padding-bottom: 20px }\n" +
            ".timeSlot { border-radius: 5px; background-color: white; padding: 20px; margin-bottom:10px; border: 1px solid #ddd; border-bottom: 2px solid #ddd }\n" +
            ".day { padding-bottom: 20px }\n" +
            ".dayTitle { font-size: 18pt; font-weight: bold }\n" +
            ".timeSlotTime { font-weight: bold; font-size: 14pt; }\n" +
            ".talkTitle { font-weight: bold }\n" +
            ".talkPresenters { padding-top: 5px; font-style: italic; } \n" +
            ".talkRoom { font-size: 8pt; } \n" +
            ".scheduleEvent { color: #aaa; text-align: center; } \n" +
            "</style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div id=\"wrap\">\n" +
            "<div class=\"container\">\n";

    private static final String HTML_FOOTER = "<div id=\"footer\">\n" +
            "</div><div class=\"container\">\n" +
            "        <p class=\"text-center\">Prepared by <a href=\"https://conferenceengineer.com/\" target=\"_blank\">ConferenceEngineer.com</a> which is &copy;Copyright 2013-2014 <a target=\"_blank\" href=\"http://funkyandroid.com/\">Funky Android Ltd.</a>, All rights reserved.</p>\n" +
            "    </div>\n" +
            "</div></body></html>";

    public ScheduleExporter(final Conference conference) {
        mConference = conference;
    }

    public String createExport() {
        StringBuilder export = new StringBuilder();

        int slotSize = workOutTalkBlockSize();

        for(ConferenceDay day : mConference.getDateList()) {
            export.append("<div class=\"day\">");
            export.append(createDayHeader(day));
            for(TalkSlot talkSlot : day.getTalkSlotList()) {
                export.append(createTalkSlotExport(talkSlot, slotSize));
            }
            export .append("</div>\n");
        }

        return HTML_HEADER+export.toString()+HTML_FOOTER;
    }

    private String createDayHeader(ConferenceDay day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        return "<div class=\"row\"><div class=\"col-md-12 dayTitle\">"
            +  simpleDateFormat.format(day.getDate())
            +  "</div></div>";
    }

    private String createTalkSlotExport(final TalkSlot talkSlot, final int slotSize) {
        if(talkSlot.getTalkList().isEmpty() && talkSlot.getEvent() == null) {
            return "";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        AbstractScheduleTalkSlotExporter exporter = getExporter(talkSlot, slotSize);
        if(exporter == null) {
            return "";
        }

        return "<div class=\"row timeSlot\"><div class=\"col-md-2 timeSlotTime\">"
                + simpleDateFormat.format(talkSlot.getStart().getTime())
                + " - "
                + simpleDateFormat.format(talkSlot.getEnd().getTime())
                + "</div><div class=\"col-md-10\">"
                + exporter.toHtml()
                + "</div></div>";
    }

    private AbstractScheduleTalkSlotExporter getExporter(final TalkSlot talkSlot, final int slotSize) {
        if(talkSlot.getEvent() != null) {
            return new AdminEventExporter(talkSlot);
        }

        return new MultiTalkExporter(talkSlot, slotSize);
    }

    private int workOutTalkBlockSize() {
        int maxTalkCount = 1;

        for(ConferenceDay day : mConference.getDateList()) {
            for(TalkSlot talkSlot : day.getTalkSlotList()) {
                int talkCount = talkSlot.getTalkList().size();
                if(talkCount > maxTalkCount) {
                    maxTalkCount = talkCount;
                }
            }
        }

        int size = 12/maxTalkCount;
        if(size * maxTalkCount > 12) {
            size--;
        }
        if(size == 0) {
            size = 1;
        }
        return size;
    }
}
