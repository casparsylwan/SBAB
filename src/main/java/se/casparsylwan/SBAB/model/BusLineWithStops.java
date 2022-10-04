package se.casparsylwan.SBAB.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusLineWithStops {

    private String BusNumber;
    private String direction;
    private HashMap<String, BusStop> stops = new HashMap<>();

}
