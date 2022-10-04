package se.casparsylwan.SBAB.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusStop {

    @JsonProperty("StopPointNumber")
    private String stopPointNumber;

    @JsonProperty("StopPointName")
    private String stopPointName;

/*
    private String direction = "";
*/

    @JsonProperty("StopAreaNumber")
    private String stopAreaNumber;

    @JsonProperty("LocationNorthingCoordinate")
    private String LocationNorthingCoordinate;

    @JsonProperty("LocationEastingCoordinate")
    private String LocationEastingCoordinate;

    @JsonProperty("ZoneShortName")
    private String ZoneShortName;

    @JsonProperty("StopAreaTypeCode")
    private String StopAreaTypeCode;
}
