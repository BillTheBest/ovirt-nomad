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
import org.urish.gwtit.client.font.FontStyle;
import org.urish.gwtit.client.font.FontWeight;
import org.urish.gwtit.titanium.UI;
import org.urish.gwtit.titanium.ui.Label;
import org.urish.gwtit.titanium.ui.TableView;
import org.urish.gwtit.titanium.ui.TableViewRow;
import org.urish.gwtit.titanium.ui.Window;
import org.urish.gwtit.titanium.ui.events.ClickEvent;
import org.urish.gwtit.titanium.ui.events.ClickHandler;

import com.vizuri.mobile.client.ClientUtil;
import com.vizuri.mobile.client.Events;
import com.vizuri.mobile.client.NomadModule;
import com.vizuri.mobile.client.model.XmlObjectWrapper;

public class DashboardView extends BaseView {

    public DashboardView(Window window) {
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
        //ClientUtil.showAlertMessage("Response", "VM total: " + response.evaluate("//summary/vms/total"));
        if (container != null) {
            window.remove(container);
        }

        container = UI.createTableView();
        container.setTop(0);
        container.setHeight(300);
        container.setBackgroundColor("#FFF");
        container.setBorderWidth(0);

        ((TableView)container).appendRow(createSummaryRow("Guests",
                response.evaluate("//summary/vms/total"),
                response.evaluate("//summary/vms/active"),
                NomadModule.VM_PAGE_INDEX));
        ((TableView)container).appendRow(createSummaryRow("Hosts",
                response.evaluate("//summary/hosts/total"),
                response.evaluate("//summary/hosts/active"),
                NomadModule.HOSTS_PAGE_INDEX));
        ((TableView)container).appendRow(createSummaryRow("Users",
                response.evaluate("//summary/users/total"),
                response.evaluate("//summary/users/active"),
                -1));
        ((TableView)container).appendRow(createSummaryRow("Storage Domains",
                response.evaluate("//summary/storage_domains/total"),
                response.evaluate("//summary/storage_domains/active"),
                -1));

        window.add(container);
    }

    private TableViewRow createSummaryRow(String rowTitle, String totalCount, String activeCount, final int navigateTarget) {
        TableViewRow row = UI.createTableViewRow();
        row.setBackgroundSelectedColor("#fff");
        row.setHeight(75);
        row.setClassName("datarow");

        Label title = UI.createLabel();
        title.setColor("#810000");
        title.setFont(Font.createFont("Arial", 18, FontWeight.BOLD));
        title.setLeft(5);
        title.setTop(2);
        title.setHeight(50);
        title.setWidth(200);
        title.setText(rowTitle);

        row.add(title);

        Label details = UI.createLabel();
        details.setColor("#222");
        details.setFont(Font.createFont("Arial", 16, FontStyle.ITALICS, FontWeight.NORMAL));
        details.setLeft(25);
        details.setTop(45);
        details.setHeight(25);
        details.setWidth(200);
        details.setText(totalCount + " total; " + activeCount + " active");
        row.add(details);

        if (navigateTarget >= 0) {
            row.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    Events.fireAppEvent(Events.TAB_NAVIGATE_REQUEST, navigateTarget);
                }
            });
        } else {
            row.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    ClientUtil.showAlertMessage("Navigate Request", "Coming Soon!");
                }
            });
        }
        return row;
    }

}
