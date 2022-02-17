package com.example.jaxbmoxy;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

class MyXmlObjectXmlAdapter extends XmlAdapter<> {

    static class AdaptedMyXmlObject {

        @XmlTransient
        public String nodeName;

        @XmlTransient
        public MyXmlObject myXmlObject;

        @XmlValue
        public int myId;
    }
}
