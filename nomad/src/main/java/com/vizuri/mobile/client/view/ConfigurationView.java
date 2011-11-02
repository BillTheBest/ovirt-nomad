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

import org.urish.gwtit.titanium.App;
import org.urish.gwtit.titanium.Platform;
import org.urish.gwtit.titanium.UI;
import org.urish.gwtit.titanium.app.Properties;
import org.urish.gwtit.titanium.ui.Button;
import org.urish.gwtit.titanium.ui.TextField;
import org.urish.gwtit.titanium.ui.View;
import org.urish.gwtit.titanium.ui.Window;
import org.urish.gwtit.titanium.ui.events.ClickEvent;
import org.urish.gwtit.titanium.ui.events.ClickHandler;

import com.vizuri.mobile.client.Events;
import com.vizuri.mobile.client.NomadProperties;
import com.vizuri.mobile.client.model.XmlObjectWrapper;

public class ConfigurationView extends BaseView {

	public ConfigurationView(Window window) {
		super(window);
		layout();
	}

	public void layout() {
		//Hack to work around Titanium vertical layout issues on iPhone
		final String myPlatform = Platform.getOsname();
		if ("iphone".equalsIgnoreCase(myPlatform)) {
			layoutIPhone();
		} else if ("android".equalsIgnoreCase(myPlatform)) {
			layoutAndroid();
		}

	}

	private void layoutIPhone() {

		window.setTitle("Configuration Settings");
		window.setBackgroundColor("#810000");
		container = UI.createView();
		container.setWidth("80%");
		container.setTop(20);

		final TextField userName = UI.createTextField();
		userName.setTop(0);
		userName.setHeight(30);
		userName.setWidth("100%");
		userName.setHintText("Username");
		userName.setBackgroundColor("#FFF");
		userName.setBorderStyle(UI.INPUT_BORDERSTYLE_ROUNDED);
		container.add(userName);

		if (Properties.hasProperty(NomadProperties.USERNAME)) {
			userName.setValue(Properties
					.getString(NomadProperties.USERNAME, ""));
		}

		final TextField domain = UI.createTextField();
		domain.setTop(40);
		domain.setHeight(30);
		domain.setWidth("100%");
		domain.setBackgroundColor("#FFF");
		domain.setHintText("Domain");
		domain.setBorderStyle(UI.INPUT_BORDERSTYLE_ROUNDED);
		container.add(domain);

		if (Properties.hasProperty(NomadProperties.DOMAIN)) {
			domain.setValue(Properties.getString(NomadProperties.DOMAIN, ""));
		}

		final TextField password = UI.createTextField();
		password.setTop(80);
		password.setHeight(30);
		password.setWidth("100%");
		password.setHintText("Password");
		password.setBackgroundColor("#FFF");
		// password.setPasswordMask(true); this don't work :(
		password.setBorderStyle(UI.INPUT_BORDERSTYLE_ROUNDED);
		container.add(password);

		if (Properties.hasProperty(NomadProperties.PASSWORD)) {
			password.setValue(Properties
					.getString(NomadProperties.PASSWORD, ""));
		}

		final TextField rhevUrl = UI.createTextField();
		rhevUrl.setTop(120);
		rhevUrl.setHeight(30);
		rhevUrl.setWidth("100%");
		rhevUrl.setHintText("RHEV URL");
		rhevUrl.setBackgroundColor("#FFF");
		rhevUrl.setBorderStyle(UI.INPUT_BORDERSTYLE_ROUNDED);
		container.add(rhevUrl);

		if (Properties.hasProperty(NomadProperties.RHEV_URL)) {
			rhevUrl.setValue(Properties.getString(NomadProperties.RHEV_URL, ""));
		}

		View buttonContainer = UI.createView();
		buttonContainer.setWidth("100%");
		buttonContainer.setTop(160);
		buttonContainer.setHeight(75);

		Button button = UI.createButton();
		button.setTitle("Save");
		button.setLeft(0);
		button.setTop(0);
		button.setHeight(30);
		button.setWidth("100%");

		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Properties.setString(NomadProperties.USERNAME,
						userName.getValue());
				Properties.setString(NomadProperties.PASSWORD,
						password.getValue());
				Properties.setString(NomadProperties.DOMAIN, domain.getValue());
				Properties.setString(NomadProperties.RHEV_URL,
						rhevUrl.getValue());
				App.fireEvent(Events.CONFIGURATION_UPDATED, null);
			}
		});

		buttonContainer.add(button);

		container.add(buttonContainer);
		window.add(container);

	}

	private void layoutAndroid() {
		window.setTitle("Configuration Settings");
		window.setBackgroundColor("#810000");
		container = UI.createView();
		container.setLayout("vertical");
		container.setWidth("80%");
		container.setTop(20);

		final TextField userName = UI.createTextField();
		userName.setWidth("100%");
		userName.setHintText("Username");
		userName.setBorderStyle(UI.INPUT_BORDERSTYLE_ROUNDED);
		container.add(userName);

		if (Properties.hasProperty(NomadProperties.USERNAME)) {
			userName.setValue(Properties
					.getString(NomadProperties.USERNAME, ""));
		}

		final TextField domain = UI.createTextField();
		domain.setWidth("100%");
		domain.setHintText("Domain");
		domain.setBorderStyle(UI.INPUT_BORDERSTYLE_ROUNDED);
		container.add(domain);

		if (Properties.hasProperty(NomadProperties.DOMAIN)) {
			domain.setValue(Properties.getString(NomadProperties.DOMAIN, ""));
		}

		final TextField password = UI.createTextField();
		password.setWidth("100%");
		password.setHintText("Password");
		// password.setPasswordMask(true); this don't work :(
		password.setBorderStyle(UI.INPUT_BORDERSTYLE_ROUNDED);
		container.add(password);

		if (Properties.hasProperty(NomadProperties.PASSWORD)) {
			password.setValue(Properties
					.getString(NomadProperties.PASSWORD, ""));
		}

		final TextField rhevUrl = UI.createTextField();
		rhevUrl.setWidth("100%");
		rhevUrl.setHintText("RHEV URL");
		rhevUrl.setBorderStyle(UI.INPUT_BORDERSTYLE_ROUNDED);
		container.add(rhevUrl);

		if (Properties.hasProperty(NomadProperties.RHEV_URL)) {
			rhevUrl.setValue(Properties.getString(NomadProperties.RHEV_URL, ""));
		}

		View buttonContainer = UI.createView();
		buttonContainer.setLayout("horizontal");
		buttonContainer.setWidth("100%");
		buttonContainer.setTop(20);

		Button button = UI.createButton();
		button.setTitle("Save");
		button.setLeft(0);
		button.setWidth("48%");

		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Properties.setString(NomadProperties.USERNAME,
						userName.getValue());
				Properties.setString(NomadProperties.PASSWORD,
						password.getValue());
				Properties.setString(NomadProperties.DOMAIN, domain.getValue());
				Properties.setString(NomadProperties.RHEV_URL,
						rhevUrl.getValue());
				App.fireEvent(Events.CONFIGURATION_UPDATED, null);
			}
		});

		buttonContainer.add(button);

		container.add(buttonContainer);
		window.add(container);
	}

	@Override
	public void updateReceived(XmlObjectWrapper response) {
		App.fireEvent(Events.LOGIN_SUCCESSFUL, null);
	}

}
