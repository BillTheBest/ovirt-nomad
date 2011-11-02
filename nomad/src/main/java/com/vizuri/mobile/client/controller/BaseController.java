/*
 * Copyright 2011, Vizuri, a division of AEM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vizuri.mobile.client.controller;

import java.util.Date;

import org.urish.gwtit.client.EventCallback;
import org.urish.gwtit.titanium.App;
import org.urish.gwtit.titanium.Network;
import org.urish.gwtit.titanium.app.Properties;
import org.urish.gwtit.titanium.network.HTTPClient;

import com.google.gwt.core.client.JavaScriptObject;
import com.vizuri.mobile.client.ClientUtil;
import com.vizuri.mobile.client.Events;
import com.vizuri.mobile.client.NomadProperties;
import com.vizuri.mobile.client.logger.Logger;
import com.vizuri.mobile.client.logger.LoggerFactory;
import com.vizuri.mobile.client.model.XmlObjectWrapper;
import com.vizuri.mobile.client.view.BaseView;

public class BaseController {
    private final static Logger logger = LoggerFactory.get(BaseController.class);

    protected BaseView baseView;
    protected Date lastDataUpdate;
    protected Long cacheLifespan = 60L;  // Seconds to keep cached copy

    public BaseController() {
    }

    public BaseController(BaseView baseView) {
        this.baseView = baseView;
    }

    protected void get(final String endpoint, String errorMessage) {
        if (Network.getOnline() && Properties.hasProperty(NomadProperties.AUTH_TOKEN)) {
            if (!isDataCacheExpired()) {
                //ClientUtil.showAlertMessage("Cache", "using cached results...");
                return;
            }

            final HTTPClient getRequest = Network.createHTTPClient();

            getRequest.setValidatesSecureCertificate(false);
            getRequest.setTimeout(10000);

            final String displayMessage = (errorMessage != null && errorMessage.length() > 0) ? errorMessage : "An Error Occurred";
            getRequest.setOnerror(new EventCallback<JavaScriptObject>() {
                public void onEvent(JavaScriptObject event) {
                    logger.error(displayMessage);
                    ClientUtil.showAlertMessage("Error", event.toString());
                    App.fireEvent(Events.DONE_WORKING, null);
                }
            });

            if (baseView != null) {
                getRequest.setOnload(new EventCallback<JavaScriptObject>() {
                    public void onEvent(JavaScriptObject event) {
                        if (getRequest.getStatus() >= 200 && getRequest.getStatus() < 300) {
                            baseView.updateReceived(XmlObjectWrapper.createObjectWrapper(getRequest.getResponseXML()));
                        } else {
                            ClientUtil.showAlertMessage("Request Error", "Received HTTP status: " + getRequest.getStatus());
                        }
                        lastDataUpdate = new Date();
                        App.fireEvent(Events.DONE_WORKING, null);
                    }
                });
            } else {
                getRequest.setOnload(new EventCallback<JavaScriptObject>() {
                    public void onEvent(JavaScriptObject event) {
                        ClientUtil.showAlertMessage("Received data!", event.toString());
                        App.fireEvent(Events.DONE_WORKING, null);
                    }
                });
            }

            App.fireEvent(Events.WORKING, null);

            String requestUrl = Properties.getString(NomadProperties.RHEV_URL, "") + endpoint;
            logger.info("Requesting: " + requestUrl);
            getRequest.open("GET", requestUrl);
            getRequest.setRequestHeader("Authorization", Properties.getString(NomadProperties.AUTH_TOKEN, ""));
            //getRequest.setRequestHeader("Accept", "application/json");
            getRequest.send();
        } else if(!ClientUtil.isUserAuthenticated()) {
            App.fireEvent(Events.LOGIN_REQUIRED, null);
        } else {
            ClientUtil.showAlertMessage("Network Error", "A network connection and configured authentication token is required to login remotely.");
        }
    }

    private boolean isDataCacheExpired() {
        boolean cacheExpired = true;

        try {
            if (lastDataUpdate != null) {
                long msElapsed = (new Date().getTime() - lastDataUpdate.getTime());
                cacheExpired = (msElapsed/1000) > cacheLifespan;
            }
        } catch (Exception e) {
            logger.error("Couldn't calculate cache expiry..." + e.getMessage());
            cacheExpired = false;
        }
        return cacheExpired;
    }

    public void updateData() {
        ClientUtil.showAlertMessage("Base Class", "Received update request in base class.");
    }

    public BaseView getBaseView() {
        return baseView;
    }

    public void setBaseView(BaseView baseView) {
        this.baseView = baseView;
    }

    public Date getLastDataUpdate() {
        return lastDataUpdate;
    }

    public Long getCacheLifespan() {
        return cacheLifespan;
    }

    public void setLastDataUpdate(Date lastDataUpdate) {
        this.lastDataUpdate = lastDataUpdate;
    }

    public void setCacheLifespan(Long cacheLifespan) {
        this.cacheLifespan = cacheLifespan;
    }


}
