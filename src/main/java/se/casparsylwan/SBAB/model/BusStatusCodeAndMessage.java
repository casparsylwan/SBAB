package se.casparsylwan.SBAB.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusStatusCodeAndMessage {

    @JsonProperty("StatusCode")
    private int statusCode;

    @JsonProperty("Message")
    private String message;
}
