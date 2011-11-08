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

import org.urish.gwtit.client.EventCallback;
import org.urish.gwtit.client.GwtTitaniumBootstrap;
import org.urish.gwtit.titanium.App;
import org.urish.gwtit.titanium.UI;
import org.urish.gwtit.titanium.Utils;
import org.urish.gwtit.titanium.app.Properties;
import org.urish.gwtit.titanium.ui.ActivityIndicator;
import org.urish.gwtit.titanium.ui.Tab;
import org.urish.gwtit.titanium.ui.TabGroup;
import org.urish.gwtit.titanium.ui.iphone.ActivityIndicatorStyle;

import com.google.gwt.core.client.JavaScriptObject;
import com.vizuri.mobile.client.controller.BaseController;
import com.vizuri.mobile.client.controller.ConfigurationController;
import com.vizuri.mobile.client.controller.DashboardController;
import com.vizuri.mobile.client.controller.EventsController;
import com.vizuri.mobile.client.controller.GuestsController;
import com.vizuri.mobile.client.controller.HostsController;
import com.vizuri.mobile.client.logger.Logger;
import com.vizuri.mobile.client.logger.LoggerFactory;
import com.vizuri.mobile.client.view.AboutWindow;

public class NomadModule extends GwtTitaniumBootstrap {
	private final static Logger logger = LoggerFactory.get(NomadModule.class);

	public final static TabGroup tabGroup = UI.createTabGroup();
	public final static BaseController dashboardController = new DashboardController();
	public final static BaseController guestsController = new GuestsController();
	public final static BaseController hostsController = new HostsController();
	public final static BaseController eventsController = new EventsController();
	public final static BaseController configurationController = new ConfigurationController();
	public final static ActivityIndicator activityIndicator = UI
			.createActivityIndicator();

	public final static int VM_PAGE_INDEX = 2;
	public final static int HOSTS_PAGE_INDEX = 1;

	@Override
	public void main() {
		UI.setBackgroundColor("#000");

		tabGroup.setBarColor("#006800");

		final Tab dashboard = UI.createTab();
		dashboard.setTitle("Dashboard");
		dashboard.setIcon("shared/images/KS_nav_ui.png");

		dashboard.setWindow(dashboardController.getBaseView().getWindow());
		tabGroup.addTab(dashboard);

		final Tab hosts = UI.createTab();
		hosts.setWindow(hostsController.getBaseView().getWindow());
		hosts.setIcon("shared/images/hosts.png");
		hosts.setTitle("Hosts");

		tabGroup.addTab(hosts);

		final Tab vms = UI.createTab();
		vms.setWindow(guestsController.getBaseView().getWindow());
		vms.setIcon("shared/images/vms.png");
		vms.setTitle("Guests");
		tabGroup.addTab(vms);

		// final Tab events = UI.createTab();
		// events.setWindow(eventsController.getBaseView().getWindow());
		// events.setIcon("KS_nav_ui.png");
		// events.setTitle("Events");
		// tabGroup.addTab(events);

		final Tab configuration = UI.createTab();
		configuration.setWindow(configurationController.getBaseView()
				.getWindow());
		configuration.setIcon("shared/images/tool.png");
		configuration.setTitle("Config");
		tabGroup.addTab(configuration);

		final Tab about = UI.createTab();
		about.setWindow(new AboutWindow().getWindow());
		about.setIcon("shared/images/help.png");
		about.setTitle("About");
		tabGroup.addTab(about);
		tabGroup.open();

		tabGroup.addEventListener("focus",
				new EventCallback<JavaScriptObject>() {
					public void onEvent(JavaScriptObject event) {
						String selectedTabTitle = tabGroup.getActiveTab()
								.getTitle();

						// TODO: ok, this is hacky, extend Tab and just call
						// active tab update data?
						if ("dashboard".equalsIgnoreCase(selectedTabTitle)) {
							dashboardController.updateData();
						} else if ("hosts".equalsIgnoreCase(selectedTabTitle)) {
							hostsController.updateData();
						} else if ("guests".equalsIgnoreCase(selectedTabTitle)) {
							guestsController.updateData();
							// } else if
							// ("events".equalsIgnoreCase(selectedTabTitle)) {
							// eventsController.updateData();
						}
					}
				});

		App.addEventListener(Events.TAB_NAVIGATE_REQUEST,
				new EventCallback<JavaScriptObject>() {
					public void onEvent(JavaScriptObject event) {
						logger.debug("Received tab navigate request...");
						if (event instanceof AppEvent) {
							int requestedTabIndex = Integer
									.valueOf(((AppEvent) event).getPayload()
											+ "");
							logger.debug("Navigate request is for index: "
									+ requestedTabIndex);
							switch (requestedTabIndex) {
							case 0:
								tabGroup.setActiveTab(dashboard);
								break;
							case 1:
								tabGroup.setActiveTab(hosts);
								break;
							case 2:
								tabGroup.setActiveTab(vms);
								break;
							case 3:
								tabGroup.setActiveTab(configuration);
								break;
							case 4:
								tabGroup.setActiveTab(about);
								break;
							default:
								break;
							}
						}
					}
				});

		// Activity indicator
		activityIndicator.setMessage("Loading...");
		activityIndicator.setStyle(ActivityIndicatorStyle.PLAIN);

		App.addEventListener(Events.WORKING,
				new EventCallback<JavaScriptObject>() {
					public void onEvent(JavaScriptObject event) {
						activityIndicator.show();
					}
				});
		App.addEventListener(Events.DONE_WORKING,
				new EventCallback<JavaScriptObject>() {
					public void onEvent(JavaScriptObject event) {
						activityIndicator.hide();
					}
				});

		// Login events
		App.addEventListener(Events.LOGIN_REQUIRED,
				new EventCallback<JavaScriptObject>() {
					public void onEvent(JavaScriptObject event) {
						Properties.setString(
								NomadProperties.USER_AUTHENTICATED, "false");
						if (Properties.hasProperty(NomadProperties.AUTH_TOKEN)) {
							Properties
									.removeProperty(NomadProperties.AUTH_TOKEN);
						}
						tabGroup.setActiveTab(configuration);
					}
				});

		App.addEventListener(Events.LOGIN_SUCCESSFUL,
				new EventCallback<JavaScriptObject>() {
					public void onEvent(JavaScriptObject event) {
						Properties.setString(
								NomadProperties.USER_AUTHENTICATED, "true");
						dashboardController.setLastDataUpdate(null);
						guestsController.setLastDataUpdate(null);
						hostsController.setLastDataUpdate(null);
						eventsController.setLastDataUpdate(null);
						tabGroup.setActiveTab(dashboard);
					}
				});

		App.addEventListener(Events.CONFIGURATION_UPDATED,
				new EventCallback<JavaScriptObject>() {
					public void onEvent(JavaScriptObject event) {
						String authToken = "Basic "
								+ Utils.base64encode(Properties.getString(
										NomadProperties.USERNAME, "x")
										+ "@"
										+ Properties.getString(
												NomadProperties.DOMAIN, "x")
										+ ":"
										+ Properties.getString(
												NomadProperties.PASSWORD, "x"));
						Properties.setString(NomadProperties.AUTH_TOKEN,
								authToken);

						configurationController.updateData();
					}
				});

		// Start things up, will initiate login if necessary
		dashboardController.updateData();

	}

}