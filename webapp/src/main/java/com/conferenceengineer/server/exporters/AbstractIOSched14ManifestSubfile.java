package com.conferenceengineer.server.exporters;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastExport;
import com.conferenceengineer.server.datamodel.LastExportDAO;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.LastModificationDAO;

import javax.persistence.EntityManager;
import java.util.Calendar;

public abstract class AbstractIOSched14ManifestSubfile extends AbstractIOSched14Exporter {

    private EntityManager mEntityManager;
    private LastExport mLastExport;

    public AbstractIOSched14ManifestSubfile(final EntityManager em, final Conference conference) {
        super(conference);
        mEntityManager = em;
        mLastExport = LastExportDAO.getInstance().getByNameOrCreate(em, mConference, getExportName());
    }

    public boolean hasDependantDataBeenModifiedSinceLastExport() {
        if(mLastExport == null || mLastExport.getLastExport() == null) {
            return true;
        }

        LastModificationDAO lmDAO = LastModificationDAO.getInstance();
        for(String dataName : getDependantDataNames()) {
            LastModification lastModification = lmDAO.getByName(mEntityManager, mConference, dataName);
            if(lastModification == null) {
                continue;
            }

            if(mLastExport.getLastExport().before(lastModification.getLastModification())) {
                return true;
            }
        }

        return false;
    }

    public String getFilename() {
        return getExportName()+"_v"+mLastExport.getSerialNumber()+".json";
    }

    public void increaseExportSerialNumber() {
        mLastExport.setSerialNumber(mLastExport.getSerialNumber()+1);
    }

    public void recordExport() {
        mLastExport.setLastExport(Calendar.getInstance());
    }

    protected abstract String getExportName();

    protected abstract String[] getDependantDataNames();
}
