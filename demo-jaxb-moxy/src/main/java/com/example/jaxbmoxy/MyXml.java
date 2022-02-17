package de.inovex.rational.wa48.web.rest.dto.scriptexport;

import java.io.Serializable;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlVariableNode;

@XmlRootElement(name = "kprozesse")
@XmlType(propOrder = { "structureVersion", "unlockedCookingProgramChainProcessIds", "unlockedCareRecipeChainProcessIds" })
public class ChainProcessesXml implements Serializable {

    public static final String DEFAULT_FILE_NAME = "kprozesse.xml";

    private static final long serialVersionUID = 1L;

    private Integer structureVersion = 0;

    private List<OrderedChainProcessId> unlockedCookingProgramChainProcessIds;

    private List<OrderedChainProcessId> unlockedCareRecipeChainProcessIds;

    @XmlElement(name = "_1")
    public Integer getStructureVersion() {
        return structureVersion;
    }

    public void setStructureVersion(Integer structureVersion) {
        this.structureVersion = structureVersion;
    }

    @XmlElementWrapper(name = "_2")
    @XmlVariableNode(value = "nodeName", type = CookingProgramChainProcessIdXmlAdapter.AdaptedOrderedChainCookingProgramId.class)
    @XmlJavaTypeAdapter(CookingProgramChainProcessIdXmlAdapter.class)
    public List<OrderedChainProcessId> getUnlockedCookingProgramChainProcessIds() {
        return unlockedCookingProgramChainProcessIds;
    }

    public void setUnlockedCookingProgramChainProcessIds(List<OrderedChainProcessId> unlockedCookingProgramChainProcessIds) {
        this.unlockedCookingProgramChainProcessIds = unlockedCookingProgramChainProcessIds;
    }

    @XmlElementWrapper(name = "_3")
    @XmlVariableNode(value = "nodeName", type = CareRecipeChainProcessIdXmlAdapter.AdaptedOrderedChainCareRecipeId.class)
    @XmlJavaTypeAdapter(CareRecipeChainProcessIdXmlAdapter.class)
    public List<OrderedChainProcessId> getUnlockedCareRecipeChainProcessIds() {
        return unlockedCareRecipeChainProcessIds;
    }

    public void setUnlockedCareRecipeChainProcessIds(List<OrderedChainProcessId> unlockedCareRecipeChainProcessIds) {
        this.unlockedCareRecipeChainProcessIds = unlockedCareRecipeChainProcessIds;
    }
}
