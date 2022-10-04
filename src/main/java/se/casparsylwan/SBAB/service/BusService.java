package se.casparsylwan.SBAB.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.casparsylwan.SBAB.exception.ApiRequestException;
import se.casparsylwan.SBAB.model.BusLine;
import se.casparsylwan.SBAB.model.BusStatusCodeAndMessage;
import se.casparsylwan.SBAB.model.BusStop;
import se.casparsylwan.SBAB.model.Jour;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BusService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.sl.se/api2}")
    private String baseUrl;


    @Value("${api.key-value}")
    private String key;

    @Value("${bus-param}")
    private String onlyBusParam;

    public BusService (ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    BusStatusCodeAndMessage httpResponseGood(String response) throws JsonProcessingException {

        int httpStatus = objectMapper
                .readTree(response)
                .get("StatusCode")
                .asInt();

        String message = objectMapper
                .readTree(response)
                .get("Message")
                .asText();

        BusStatusCodeAndMessage busStatusCodeAndMessage = new BusStatusCodeAndMessage(
                httpStatus,
                message
                );

        return busStatusCodeAndMessage;
    }

    String restTemplateCall(String model) throws JsonProcessingException {

        String url = baseUrl + "model=" + model + "&key=" + key + onlyBusParam;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Encoding", "gzip, deflate");
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class);

        BusStatusCodeAndMessage busStatusCodeAndMessage = httpResponseGood(responseEntity.getBody());

        if (responseEntity.getStatusCodeValue() < 299 && busStatusCodeAndMessage.getStatusCode() == 0) {

            String response = responseEntity.getBody();

            return objectMapper.readTree(response)
                    .get("ResponseData")
                    .get("Result")
                    .toString();

        } else if(responseEntity.getStatusCodeValue() > 299 && busStatusCodeAndMessage.getStatusCode() > 0){
            throw new ApiRequestException(busStatusCodeAndMessage.getMessage());
        } else {
            throw new ApiRequestException("Connection failed");
        }
    }

    public List<BusLine> getAllLines() {

        HashMap<String, BusLine> busLineHashMap = new HashMap<>();
        HashMap<String, BusStop> busStopsHashMap = new HashMap<>();

        Jour[] jours;
        BusStop[] busStops;

        String busStopsResponse;
        String busJourResponse;

        try {

            busStopsResponse = restTemplateCall("stop");
            busJourResponse = restTemplateCall("jour");

            jours = objectMapper.readValue(busJourResponse, Jour[].class);
            busStops = objectMapper.readValue(busStopsResponse, BusStop[].class);

        } catch (JsonProcessingException e) {

            throw new ApiRequestException("Json parsing error from Api");
        }
        // It can only be one stop in a stopArea and some stops do not have a stopArea.
        for (int i = 0; i < busStops.length; i++) {
            busStopsHashMap.put(busStops[i].getStopPointNumber(), busStops[i]);
        }

        for (int i = 0; i < jours.length; i++) {

            BusStop busStop = busStopsHashMap.get(jours[i].getJourneyPatternPointNumber());

            if (!busLineHashMap.containsKey(jours[i].getLineNumber())) {
                BusLine busLine = new BusLine();
                busLine.setLineNumber(jours[i].getLineNumber());
                if (busStop != null) {
                    busLine.getStopPointsNamesAndSetSize(busStop.getStopPointName());
                }
                busLineHashMap.put(jours[i].getLineNumber(), busLine);

            } else {

                if (busStop != null && (jours[i].getDirectionCode().equals("2") ||
                        jours[i].getDirectionCode().equals("1") )) {
                    String busStopName = busStop.getStopPointName();
                    busLineHashMap.get(jours[i].getLineNumber())
                            .getStopPointsNamesAndSetSize(busStopName);
                }
            }
        }

        List<BusLine> busLinesList = busLineHashMap
                .values()
                .stream()
                .sorted(Comparator.comparing(BusLine::getNumberOfStops).reversed())
                .collect(Collectors.toList());

        return busLinesList;
    }
}

