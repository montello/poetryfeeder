package com.fraise.util;

import java.io.File;
import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocal;
import com.google.appengine.tools.development.ApiProxyLocalFactory;
import com.google.appengine.tools.development.LocalServerEnvironment;
import com.google.apphosting.api.ApiProxy;

public class TestInitializer {

    private final ApiProxy.Environment environment;

    private PersistenceManagerFactory pmf;

    public PersistenceManagerFactory getPersistenceManagerFactory() {
        if (pmf == null) {
            Properties newProperties = new Properties();
            newProperties.put("javax.jdo.PersistenceManagerFactoryClass",
                    "org.datanucleus.store.appengine.jdo.DatastoreJDOPersistenceManagerFactory");
            newProperties.put("javax.jdo.option.ConnectionURL", "appengine");
            newProperties.put("javax.jdo.option.NontransactionalRead", "true");
            newProperties.put("javax.jdo.option.NontransactionalWrite", "true");
            newProperties.put("javax.jdo.option.RetainValues", "true");
            newProperties.put("datanucleus.appengine.autoCreateDatastoreTxns", "true");
            newProperties.put("datanucleus.appengine.allowMultipleRelationsOfSameType", "true");
            pmf = JDOHelper.getPersistenceManagerFactory(newProperties);
        }
        return pmf;
    }

    /**
     * Constructor
     * 
     * @param environmentOrNull
     *            the environment that should be used for the test (null to use
     *            the default)
     */
    public TestInitializer(ApiProxy.Environment environmentOrNull) {
        this.environment = (environmentOrNull == null) ? new TestEnvironment() : environmentOrNull;
    }

    /** Constructor with default parameters */
    public TestInitializer() {
        this(null);
    }

    /**
     * Sets up a unit test with the options stored in this object. This method
     * should only be called once per object.
     */
    public void setUp() throws Exception {

        ApiProxyLocal proxy = new ApiProxyLocalFactory().create(new LocalServerEnvironment() {

            @Override
            public void waitForServerToStart() throws InterruptedException {

            }

            @Override
            public int getPort() {
                return 0;
            }

            @Override
            public File getAppDir() {
                return new File(".");
            }

            @Override
            public String getAddress() {
                return null;
            }
        });
        proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
        ApiProxy.setDelegate(proxy);
        ApiProxy.setEnvironmentForCurrentThread(environment);
    }

    public void tearDown(boolean testWasSuccessful) throws Exception {
        Transaction txn = DatastoreServiceFactory.getDatastoreService().getCurrentTransaction(null);
        try {
            if (txn != null) {
                try {
                    txn.rollback();
                } finally {
                    if (testWasSuccessful) {
                        throw new IllegalStateException("Datastore service still has an active txn.  Please "
                                + "rollback or commit all txns before test completes.");
                    }
                }
            }
        } finally {
            ApiProxy.clearEnvironmentForCurrentThread();
        }
    }
}
