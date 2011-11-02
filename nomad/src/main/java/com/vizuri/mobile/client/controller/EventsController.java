package com.vizuri.mobile.client.controller;

import org.urish.gwtit.titanium.UI;
import org.urish.gwtit.titanium.ui.Window;

import com.vizuri.mobile.client.view.BaseView;
import com.vizuri.mobile.client.view.EventsView;

public class EventsController extends BaseController {
    public EventsController() {
        super();
        
        Window eventsWindow = UI.createWindow();
        eventsWindow.setExitOnClose(false);
        eventsWindow.setUrl("shared/js/menu.js");
        eventsWindow.setNavBarHidden(false);
        
        BaseView view = new EventsView(eventsWindow);
        this.baseView = view;
    }

    @Override
    public void updateData() {
        get("/events/", "Could not get events summary...");
    }
}
