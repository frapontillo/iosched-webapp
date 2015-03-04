package com.conferenceengineer.server.exporters.html.schedule;

import com.conferenceengineer.server.datamodel.Presenter;
import com.conferenceengineer.server.datamodel.Talk;
import com.conferenceengineer.server.datamodel.TalkSlot;
import com.conferenceengineer.server.datamodel.Track;
import com.conferenceengineer.server.exporters.html.AbstractScheduleTalkSlotExporter;

import java.util.List;

public class MultiTalkExporter extends AbstractScheduleTalkSlotExporter {
    private int mBlockSize;

    public MultiTalkExporter(TalkSlot talkSlot, int blockSize) {
        super(talkSlot);
        mBlockSize = blockSize;
    }

    @Override
    public String createRow() {
        List<Talk> talks = mTalkSlot.getTalkList();
        if(talks.size() == 0) {
            return "";
        }

        StringBuilder row = new StringBuilder();

        if(talks.size() > 1) {
            addMultipleTalksToRow(row, talks);
        } else {
            handleSingleTalkRow(row, talks);
        }

        return row.toString();
    }

    private void handleSingleTalkRow(final StringBuilder row, List<Talk> talks) {
        Talk onlyTalk = talks.get(0);
        if(onlyTalk.getType() != null && onlyTalk.getType() == Talk.TYPE_KEYNOTE) {
            addTalk(row, onlyTalk, 12);
        } else {
            addMultipleTalksToRow(row, talks);
        }
    }



    private void addMultipleTalksToRow(final StringBuilder row, List<Talk> talks) {
        for(Talk talk : talks) {
            addTalk(row, talk, mBlockSize);
        }
    }

    private void addTalk(final StringBuilder row, Talk talk, int blockSize) {
        row.append(generateTalkDetailsDialog(talk));
        row.append("<div class=\"col-md-");
        row.append(blockSize);
        row.append(" talk");

        Track track = talk.getTrack();
        if(track != null) {
            row.append(" track-");
            row.append(sanitiseTrackName(track.getName()));
        }

        boolean isKeynote = (talk.getType() != null && talk.getType() == Talk.TYPE_KEYNOTE);
        if(isKeynote) {
            row.append(" talkKeynote");
        }

        row.append("\"><div class=\"talkTitle\">");
        row.append("<a data-toggle=\"modal\" href=\"#talk");
        row.append(talk.getId());
        row.append("\">");

        if(isKeynote) {
            row.append("Keynote : ");
        }

        row.append(talk.getName());
        row.append("</a></div>");

        row.append("<div class=\"talkRoom\">");
        row.append(talk.getLocation().getName());
        row.append("</div>");

        row.append("<div class=\"talkPresenters\">");
        for(Presenter presenter:talk.getPresenters()) {
            row.append("<div class=\"talkPresenter\">");
            row.append(presenter.getName());
            row.append("</div>");
        }
        row.append("</div>");

        row.append("</div>");
    }

    private String sanitiseTrackName(final String trackName) {
        StringBuilder sanitisedName = new StringBuilder(trackName.length());
        for(char c : trackName.toCharArray()) {
            if(Character.isLetterOrDigit(c)) {
                sanitisedName.append(c);
            }
        }
        return sanitisedName.toString();
    }


    private String generateTalkDetailsDialog(final Talk talk) {
        return "<div class=\"modal fade\" id=\"talk"+talk.getId()+"\" tabindex=\"-1\" role=\"dialog\"\n" +
                "     aria-labelledby=\"talk"+talk.getId()+"\" aria-hidden=\"true\">\n" +
                "    <div class=\"modal-dialog\">\n" +
                "        <div class=\"modal-content\">\n" +
                "            <div class=\"modal-header\">\n" +
                "                <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>\n" +
                "                <h4 class=\"modal-title\">"+talk.getName()+"</h4>\n" +
                "            </div>\n" +
                "                <div class=\"modal-body\">\n" +
                talk.getShortDescription() +
                "                <br /><br /> \n"+
                talk.getLocation().getName() +
                "                </div>\n" +
                "                <div class=\"modal-footer\">\n" +
                "                    <button class=\"btn btn-primary\" data-dismiss=\"modal\">OK</button>\n" +
                "                </div>\n" +
                "            </form>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n";
    }
}
