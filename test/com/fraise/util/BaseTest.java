package com.fraise.util;

import javax.jdo.PersistenceManager;

import junit.framework.TestCase;

import com.google.apphosting.api.ApiProxy;

/**
 * A simple test base class that can be extended to build unit tests that
 * properly construct and tear down App Engine test environments.
 */
public abstract class BaseTest extends TestCase {

    private boolean wasSuccess;

    /**
     * The initializer used in this test.
     */
    protected TestInitializer initializer;

    /**
     * Returns the environment that should be used for the unit tests, or null
     * if the default should be used.
     */
    protected ApiProxy.Environment getEnvironmentOrNull() {
        return null;
    }

    /**
     * Sets up the App Engine environment.
     */
    @Override
    protected void setUp() throws Exception {
        if (initializer != null) {
            throw new UnsupportedOperationException("setup may only be called once!");
        }
        super.setUp();
        initializer = new TestInitializer(getEnvironmentOrNull());
        initializer.setUp();
    }

    /**
     * Runs the test and remembers whether an exception was thrown (needed for
     * proper teardown of test)
     */
    @Override
    protected void runTest() throws Throwable {
        wasSuccess = false;
        super.runTest();
        wasSuccess = true;
    }

    /**
     * Deconstructs the App Engine environment.
     */
    @Override
    protected void tearDown() throws Exception {
        if (initializer == null) {
            throw new UnsupportedOperationException("setup must be called before teardown");
        }
        super.tearDown();
        initializer.tearDown(wasSuccess);
    }

    /**
     * Convenience method: gets a new PersistenceManager
     */
    public PersistenceManager newPersistenceManager() {
        return initializer.getPersistenceManagerFactory().getPersistenceManager();
    }

}
