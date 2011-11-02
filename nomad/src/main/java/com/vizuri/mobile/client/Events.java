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
package com.vizuri.mobile.client;

import org.urish.gwtit.titanium.App;

public class Events {
    public static final String DONE_WORKING = "doneWorking";
    public static final String WORKING = "working";
    public static final String TAB_NAVIGATE_REQUEST = "tabNavigateRequest";
    public static final String LOGIN_REQUIRED = "loginRequired";
    public static final String LOGIN_SUCCESSFUL = "loginSuccessful";
    public static final String CONFIGURATION_UPDATED = "configurationUpdated";
    
    public static void fireAppEvent(String eventName, Object payload) {
        AppEvent appEvent = (AppEvent)AppEvent.createObject();
        appEvent.setPayload(payload);
        App.fireEvent(eventName, appEvent);
    }
}
