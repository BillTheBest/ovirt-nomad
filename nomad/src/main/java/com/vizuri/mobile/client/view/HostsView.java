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
import org.urish.gwtit.titanium.ui.Label;
import org.urish.gwtit.titanium.ui.TableView;
import org.urish.gwtit.titanium.ui.TableViewRow;
import org.urish.gwtit.titanium.ui.Window;

import com.vizuri.mobile.client.ClientUtil;
import com.vizuri.mobile.client.logger.Logger;
import com.vizuri.mobile.client.logger.LoggerFactory;
import com.vizuri.mobile.client.model.XmlObjectWrapper;

public class HostsView extends BaseView {
    private static final Logger logger = LoggerFactory.get(HostsView.class);
    public HostsView(Window window) {
        super(window);
        layout();
    }

    @Override
    public void layout() {
        this.window.setTitle("RHEVM Mobile");
        this.window.setBackgroundColor("#810000");
    }

    @Override
    public void updateReceived(XmlObjectWrapper response) {
        if (container != null) {
            window.remove(container);
        }
        int hostCount = response.getTagNameCount("host");
        logger.debug("Displaying host summary for : " + hostCount + " hosts.");
        int containerHeight = 175 * hostCount;
        container = UI.createTableView();
        container.setTop(0);
        container.setHeight(containerHeight);
        container.setBackgroundColor("#FFF");
        container.setBorderWidth(0);

        if (hostCount > 0) {
            for (int i = 1; i <= hostCount; i++) {
                ((TableView)container).appendRow(
                        createSummaryRow(
                                response.evaluate("//host[" + i + "]/@id"),
                                response.evaluate("//host[" + i + "]/name"),
                                response.evaluate("//host[" + i + "]/address"), 					//Not available in the MOCK API
                                response.evaluate("//host[" + i + "]/status/state"), 				//Not available in the MOCK API
                                response.evaluate("//host[" + i + "]/storage_manager"), 			//Not available in the MOCK API
                                response.evaluate("//host[" + i + "]/power_management/enabled")));	//Not available in the MOCK API
            }
        } else {
            ClientUtil.showAlertMessage("No data", "No hosts found");
        }
        window.add(container);
    }

    private TableViewRow createSummaryRow(String hostId, String name, String address, String status, String storageManager, String powerManagementEnabled) {
        TableViewRow row = UI.createTableViewRow();
        row.setSelectedBackgroundColor("#fff");
        row.setHeight(175);
        row.setClassName("datarow");

        Label title = UI.createLabel();

        title.setColor("#810000");
        title.setFont(Font.createFont("Arial", 20, FontWeight.NORMAL));
        title.setLeft(5);
        title.setTop(2);
        title.setHeight(50);
        title.setWidth("90%");
        title.setText(name);
        row.add(title);

        Label addressLabel =UI.createLabel();
        addressLabel.setColor("#222");
        addressLabel.setFont(Font.createFont("Arial", 16, FontWeight.BOLD));
        addressLabel.setLeft(25);
        addressLabel.setTop(45);
        addressLabel.setHeight(25);
        addressLabel.setText(address);
        row.add(addressLabel);

        Label statusLabel = UI.createLabel();
        statusLabel.setColor("#222");
        statusLabel.setFont(Font.createFont("Arial", 16, FontWeight.BOLD));
        statusLabel.setLeft(25);
        statusLabel.setTop(75);
        statusLabel.setHeight(25);
        statusLabel.setText("Status: ");
        row.add(statusLabel);

        String statusColor = "#810000";
        if ("up".equalsIgnoreCase(status)) {
            statusColor = "#006600";
        }
        Label statusValueLabel = UI.createLabel();
        statusValueLabel.setColor(statusColor);
        statusValueLabel.setFont(Font.createFont("Arial", 16, FontWeight.BOLD));
        statusValueLabel.setLeft(275);
        statusValueLabel.setTop(75);
        statusValueLabel.setHeight(25);
        statusValueLabel.setText(status);
        row.add(statusValueLabel);

        Label storageLabel = UI.createLabel();
        storageLabel.setColor("#222");
        storageLabel.setFont(Font.createFont("Arial", 16, FontWeight.BOLD));
        storageLabel.setLeft(25);
        storageLabel.setTop(105);
        storageLabel.setHeight(25);
        storageLabel.setText("Storage Manager: ");
        row.add(storageLabel);

        Label storageValueLabel = UI.createLabel();
        storageValueLabel.setColor("#222");
        storageValueLabel.setFont(Font.createFont("Arial", 16, FontWeight.NORMAL));
        storageValueLabel.setLeft(275);
        storageValueLabel.setTop(105);
        storageValueLabel.setHeight(25);
        storageValueLabel.setText(storageManager);
        row.add(storageValueLabel);

        Label pmLabel = UI.createLabel();
        pmLabel.setColor("#222");
        pmLabel.setFont(Font.createFont("Arial", 16, FontWeight.BOLD));
        pmLabel.setLeft(25);
        pmLabel.setTop(135);
        pmLabel.setHeight(25);
        pmLabel.setText("Power Managment Enabled: ");
        row.add(pmLabel);

        Label pmValueLabel = UI.createLabel();
        pmValueLabel.setColor("#222");
        pmValueLabel.setFont(Font.createFont("Arial", 16, FontWeight.NORMAL));
        pmValueLabel.setLeft(257);
        pmValueLabel.setTop(135);
        pmValueLabel.setHeight(25);
        pmValueLabel.setText(powerManagementEnabled);
        row.add(pmValueLabel);

        // todo: create click event with host id for detail view opening
        return row;
    }

}
