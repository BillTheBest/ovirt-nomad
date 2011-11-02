package com.vizuri.mobile.client.model;

import org.urish.gwtit.titanium.xml.Document;

import com.google.gwt.core.client.JavaScriptObject;

public class XmlObjectWrapper extends JavaScriptObject {
    protected XmlObjectWrapper() {
    }
    
    public final native String evaluate(String expression) /*-{
        return this.doc.evaluate(expression).item(0).text;
    }-*/;
    
    public final native int getTagNameCount(String tagName) /*-{
        return this.doc.getElementsByTagName(tagName).length;
    }-*/;
    
    public final native Document getDoc()/*-{
        return this.doc;
    }-*/;
    
    public final native void setDoc(Document doc) /*-{
        this.doc = doc;
    }-*/;
    
    public final native String toXmlString() /*-{
        return this.doc.toString();
    }-*/;
    
    public static XmlObjectWrapper createObjectWrapper(Document doc) {
        XmlObjectWrapper wrapper = (XmlObjectWrapper)createObject();
        wrapper.setDoc(doc);
        return wrapper;
    }
}
