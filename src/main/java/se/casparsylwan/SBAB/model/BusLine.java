package se.casparsylwan.SBAB.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
@JsonIgnoreProperties(ignoreUnknown = true)
*/
public class BusLine {

    @JsonProperty("LineNumber")
    private String lineNumber;

/*
    private String lineDirection;

    @JsonProperty("LineDesignation")
    private String lineDesignation;

    @JsonProperty("DefaultTransportMode")
    private String defaultTransportMode;

    @JsonProperty("DefaultTransportModeCode")
    private String defaultTransportModeCode;
*/

    private Integer numberOfStops = 0;

    private Set<String> stopPointsNames = new HashSet<>();

    public void getStopPointsNamesAndSetSize(String name){
        stopPointsNames.add(name);
        numberOfStops = stopPointsNames.size();
    }
}
