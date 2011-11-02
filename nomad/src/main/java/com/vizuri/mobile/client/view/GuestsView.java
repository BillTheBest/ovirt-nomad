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
package com.vizuri.mobile.client.view;

import org.urish.gwtit.client.font.Font;
import org.urish.gwtit.client.font.FontWeight;
import org.urish.gwtit.titanium.UI;
import org.urish.gwtit.titanium.ui.ImageView;
import org.urish.gwtit.titanium.ui.Label;
import org.urish.gwtit.titanium.ui.TableView;
import org.urish.gwtit.titanium.ui.TableViewRow;
import org.urish.gwtit.titanium.ui.Window;

import com.vizuri.mobile.client.ClientUtil;
import com.vizuri.mobile.client.logger.Logger;
import com.vizuri.mobile.client.logger.LoggerFactory;
import com.vizuri.mobile.client.model.XmlObjectWrapper;

public class GuestsView extends BaseView {
    private final static Logger logger = LoggerFactory.get(GuestsView.class);

    public GuestsView(Window window) {
        super(window);
        layout();
    }

    @Override
    public void layout() {
        window.setTitle("RHEVM Mobile");
        window.setBackgroundColor("#810000");
    }

    @Override
    public void updateReceived(XmlObjectWrapper response) {
        if (container != null) {
            window.remove(container);
        }
        int guestCount = response.getTagNameCount("vm");
        logger.debug("Displaying guests summary for " + guestCount + " vms.");
        container = UI.createTableView();
        container.setTop(0);
        container.setHeight("90%");
        container.setBackgroundColor("#FFF");
        container.setBorderWidth(0);

        if (guestCount > 0) {
            for (int i = 1; i <= guestCount; i++) {
                ((TableView)container).appendRow(
                        createSummaryRow(
                                response.evaluate("//vm[" + i + "]/name"),
                                response.evaluate("//vm[" + i + "]/os/@type"),			//Not available in the MOCK API
                                response.evaluate("//vm[" + i + "]/status/state")));		//Not available in the MOCK API
            }
        } else {
            ClientUtil.showAlertMessage("No data", "No guests found");
        }
        window.add(container);
    }

    private TableViewRow createSummaryRow(String vmName, String osType, String vmStatus) {
        TableViewRow row = UI.createTableViewRow();

        ImageView osTypeImage = UI.createImageView();
        osTypeImage.setImage(determineOsTypeImage(osType));
        osTypeImage.setWidth(32);
        osTypeImage.setHeight(32);
        osTypeImage.setLeft(4);
        osTypeImage.setTop(2);

        Label vmDisplayName = UI.createLabel();
        vmDisplayName.setText(vmName);
        vmDisplayName.setFont(Font.createFont("Arial", 16, FontWeight.BOLD));
        vmDisplayName.setWidth("auto");
        vmDisplayName.setTextAlign("left");
        vmDisplayName.setTop(5);
        vmDisplayName.setLeft(50);
        vmDisplayName.setHeight(20);

        ImageView statusImage = UI.createImageView();
        statusImage.setImage(determineStatusImage(vmStatus));
        statusImage.setWidth(16);
        statusImage.setHeight(16);
        statusImage.setRight(10);

        row.add(osTypeImage);
        row.add(vmDisplayName);
        row.add(statusImage);

        row.setClassName("vm_row");

        return row;
    }

    private String determineOsTypeImage(String osType) {
        String shortOsType = osType != null && osType.length() >= 3 ? osType.substring(0,2) : "";
        logger.debug("Converted osType: " + osType + " to : " + shortOsType);
        if("rhe".equalsIgnoreCase(shortOsType)) {
            return "redhat.png";
        }

        if("win".equalsIgnoreCase(shortOsType)) {
            return "win40.png";
        }
        return ("amiga.png");
    }

    private String determineStatusImage(String status) {
        if("up".equalsIgnoreCase(status)) {
            return "green_up_arrow.gif";
        }
        return "red_down_arrow.gif";
    };

}
