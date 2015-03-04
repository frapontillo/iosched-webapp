package com.conferenceengineer.server.utils;

import com.conferenceengineer.server.Configuration;
import com.conferenceengineer.server.utils.javamail.DevelopmentJavaMailUtils;
import com.conferenceengineer.server.utils.javamail.ProductionJavaMailUtils;

public final class JavaMailUtilsFactory {

    private static JavaMailUtils sInstance = null;

    private JavaMailUtilsFactory() { super(); }

    private static JavaMailUtils createInstance() {
        if(Configuration.isInDevelopment) {
            return new DevelopmentJavaMailUtils();
        }

        return new ProductionJavaMailUtils();
    }

    public static JavaMailUtils getJavaMailUtilsInstance() {
        synchronized (JavaMailUtilsFactory.class) {
            if(sInstance == null) {
                sInstance = createInstance();
            }
        }

        return sInstance;
    }
}
