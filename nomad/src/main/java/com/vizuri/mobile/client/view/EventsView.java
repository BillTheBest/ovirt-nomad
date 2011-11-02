package com.vizuri.mobile.client.view;

import org.urish.gwtit.client.font.Font;
import org.urish.gwtit.client.font.FontStyle;
import org.urish.gwtit.client.font.FontWeight;
import org.urish.gwtit.titanium.UI;
import org.urish.gwtit.titanium.app.Properties;
import org.urish.gwtit.titanium.ui.Label;
import org.urish.gwtit.titanium.ui.Switch;
import org.urish.gwtit.titanium.ui.TableView;
import org.urish.gwtit.titanium.ui.TableViewRow;
import org.urish.gwtit.titanium.ui.Window;
import org.urish.gwtit.titanium.ui.events.SwitchChangeEvent;
import org.urish.gwtit.titanium.ui.events.SwitchChangeHandler;

import com.vizuri.mobile.client.ClientUtil;
import com.vizuri.mobile.client.NomadProperties;
import com.vizuri.mobile.client.logger.Logger;
import com.vizuri.mobile.client.logger.LoggerFactory;
import com.vizuri.mobile.client.model.XmlObjectWrapper;

public class EventsView extends BaseView {
    private final static Logger logger = LoggerFactory.get(EventsView.class);
    private XmlObjectWrapper lastResponse;
    private final Switch normalEventsSwitch = UI.createSwitch();
    private int maxNumberOfEvents = 25; // TODO make a number slider thing

    public EventsView(Window window) {
        super(window);
        layout();
    }

    @Override
    public void layout() {
        window.setTitle("RHEVM Mobile");
        window.setBackgroundColor("#810000");

        normalEventsSwitch.setValue("true".equals(Properties.getString(NomadProperties.EXCLUDE_NORMAL_EVENTS, "")));
        normalEventsSwitch.setTitle("Exclude Normal Events");
        normalEventsSwitch.setTitleOn("Normal Events Excluded");
        normalEventsSwitch.setTitleOff("Normal Events Included");
        normalEventsSwitch.setTop(0);
        normalEventsSwitch.setWidth("80%");
        window.add(normalEventsSwitch);

        normalEventsSwitch.addChangeHandler(new SwitchChangeHandler() {
            public void onSwitchChange(SwitchChangeEvent event) {
                Properties.setString(NomadProperties.EXCLUDE_NORMAL_EVENTS, normalEventsSwitch.getValue() ? "true" : "false");
                updateView();
            }
        });
    }

    @Override
    public void updateReceived(XmlObjectWrapper response) {
        lastResponse = response;
        updateView();
    }

    public void updateView() {
        if (container != null) {
            window.remove(container);
        }
        int eventCount = lastResponse.getTagNameCount("event");
        logger.debug("Displaying events summary for " + eventCount + " events.");
        container = UI.createTableView();
        container.setTop(45);
        container.setHeight("80%");
        container.setBackgroundColor("#FFF");
        container.setBorderWidth(0);

        if (eventCount > 0) {
            int displayedCount = 0;
            for (int i = 1; i <= eventCount; i++) {
                String severity = lastResponse.evaluate("//event[" + i + "]/severity");

                if (!(normalEventsSwitch.getValue() && "normal".equalsIgnoreCase(severity))) {
                    ((TableView)container).appendRow(
                            createSummaryRow(
                                    lastResponse.evaluate("//event[" + i + "]/@id"),
                                    lastResponse.evaluate("//event[" + i + "]/description"),
                                    severity));
                    displayedCount++;
                    if (displayedCount >= maxNumberOfEvents) {
                        break;
                    }
                }
            }
        } else {
            ClientUtil.showAlertMessage("No data", "No events found");
        }
        window.add(container);
    }

    private TableViewRow createSummaryRow(String eventId, String description, String severity) {
        TableViewRow row = UI.createTableViewRow();
        row.setBackgroundSelectedColor("#fff");
        row.setHeight(75);
        row.setClassName("datarow");

        Label title = UI.createLabel();
        title.setColor("#222");
        title.setFont(Font.createFont("Arial", 16, FontWeight.NORMAL));
        title.setLeft(5);
        title.setTop(2);
        title.setHeight(50);
        title.setWidth("90%");
        title.setText(description);
        row.add(title);

        String severityColor = "#810000";

        if ("warning".equalsIgnoreCase(severity)) {
            severityColor = "#FF9933";
        } else if ("normal".equalsIgnoreCase(severity)) {
            severityColor = "#006600";
        }

        Label details = UI.createLabel();
        details.setColor(severityColor);
        details.setFont(Font.createFont("Arial", 16, FontStyle.ITALICS, FontWeight.BOLD));
        details.setLeft(25);
        details.setTop(45);
        details.setHeight(25);
        details.setWidth(200);
        details.setText(severity);
        row.add(details);

        // TODO: event handler for event detail
        return row;
    }

}
