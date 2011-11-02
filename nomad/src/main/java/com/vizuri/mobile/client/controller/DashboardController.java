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

import org.urish.gwtit.titanium.UI;
import org.urish.gwtit.titanium.ui.Window;

import com.vizuri.mobile.client.view.BaseView;
import com.vizuri.mobile.client.view.DashboardView;

public class DashboardController extends BaseController {

    public DashboardController() {
        super();
        
        Window dashboardWindow = UI.createWindow();
        dashboardWindow.setExitOnClose(false);
        dashboardWindow.setUrl("shared/js/menu.js");
        dashboardWindow.setNavBarHidden(false);
        
        BaseView view = new DashboardView(dashboardWindow);
        this.baseView = view;
    }

    @Override
    public void updateData() {
        get("/", "Could not get dashboard summary...");
    }
    
    

}
