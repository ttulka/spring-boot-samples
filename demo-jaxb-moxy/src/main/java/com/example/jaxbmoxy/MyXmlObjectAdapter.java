package com.example.jaxbmoxy;

import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

class MyXmlObjectAdapter extends XmlAdapter<MyXmlObjectAdapter.MyXmlObjectAdapted, MyXmlObject> {

    @Override
    public MyXmlObject unmarshal(MyXmlObjectAdapted v) {
        return v.myXmlObject;
    }

    @Override
    public MyXmlObjectAdapted marshal(MyXmlObject v) {
        MyXmlObjectAdapted adapted = new MyXmlObjectAdapted();
        adapted.myXmlObject = v;
        adapted.myId = v.getMyId();
        adapted.nodeName = "obj_" + v.getOrder();
        return adapted;
    }

    static class MyXmlObjectAdapted {

        @XmlTransient
        public String nodeName;

        @XmlTransient
        public MyXmlObject myXmlObject;

        @XmlValue
        public int myId;
    }
}
