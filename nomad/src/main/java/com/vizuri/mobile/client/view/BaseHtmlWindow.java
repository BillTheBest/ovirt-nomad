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

import org.urish.gwtit.client.util.Version;
import org.urish.gwtit.titanium.Filesystem;
import org.urish.gwtit.titanium.UI;
import org.urish.gwtit.titanium.ui.WebView;
import org.urish.gwtit.titanium.ui.Window;

public abstract class BaseHtmlWindow {
	private Window window;

	//TODO: Need to make this a URI, not String
	public BaseHtmlWindow(String url) {
		window = UI.createWindow();
		window.setOrientationModes(new int[] { UI.PORTRAIT, UI.LANDSCAPE_LEFT, UI.LANDSCAPE_RIGHT });

		WebView webView = UI.createWebView();
		webView.setBackgroundColor("white");
		webView.setUrl(htmlResourceUrl(url));
		window.add(webView);
	}

	private String htmlResourceUrl(String relativePath) {
		if (Version.android()) {
			return Filesystem.getFile(Filesystem.getResourcesDirectory() + relativePath).getNativePath();
		} else {
			return Filesystem.getResourcesDirectory() + relativePath;
		}
	}

	public Window getWindow() {
		return window;
	}
}
