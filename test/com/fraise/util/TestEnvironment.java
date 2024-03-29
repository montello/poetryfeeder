package com.fraise.util;

import java.util.HashMap;
import java.util.Map;

import com.google.apphosting.api.ApiProxy;

/**
 * A dummy test environment, similar to
 * 
 * http://code.google.com/appengine/docs/java/howto/
 * unittesting.html#Establishing_The_Execution_Environment
 * 
 */
class TestEnvironment implements ApiProxy.Environment {

  private Map<String, Object> attributes = new HashMap<String, Object>();

  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public String getAppId() {
    return "Unit Tests";
  }

  public String getVersionId() {
    return "1.0";
  }

  public void setDefaultNamespace(String s) {
  }

  public String getRequestNamespace() {
    return "gmail.com";
  }

  public String getDefaultNamespace() {
    return "gmail.com";
  }

  public String getAuthDomain() {
    return null;
  }

  public boolean isLoggedIn() {
    return false;
  }

  public String getEmail() {
    return null;
  }

  public boolean isAdmin() {
    return false;
  }
}
