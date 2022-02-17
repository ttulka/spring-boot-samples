package com.example.jaxbmoxy;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.eclipse.persistence.oxm.annotations.XmlVariableNode;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "myDocument")
@XmlType(propOrder = { "structureVersion", "myObjects" })
public class MyXml implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer structureVersion = 0;
    private List<MyXmlObject> myObjects;

    @XmlElement(name = "myVersion")
    public Integer getStructureVersion() {
        return structureVersion;
    }

    public void setStructureVersion(Integer structureVersion) {
        this.structureVersion = structureVersion;
    }

    @XmlElementWrapper(name = "myObjects")
    @XmlVariableNode("nodeName")
    @XmlJavaTypeAdapter(MyXmlObjectAdapter.class)
    public List<MyXmlObject> getMyObjects() {
        return myObjects;
    }

    public void setMyObjects(List<MyXmlObject> myObjects) {
        this.myObjects = myObjects;
    }
}
