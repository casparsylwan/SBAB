package se.casparsylwan.SBAB.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import se.casparsylwan.SBAB.model.BusStatusCodeAndMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = JacksonAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class BusServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BusService busService = new BusService(new ObjectMapper());

    private String jsonStatus0 = "{\"StatusCode\": 0,\"Message\": null}";
    private String jsonStatus1020 = "{\"StatusCode\": 1020,\"Message\": \"error\"}";


    private String jsonSuccess = "{"
               + "\"StatusCode\": 0,"
               + "\"Message\": null,"
               + "\"ExecutionTime\": 385,"
               + "\"ResponseData\": {"
               + "\"Version\": \"2022-10-01 00:03\","
               + "\"Type\": \"TransportMode\","
               + "\"Result\": ["
        +"{"
        +    "\"DefaultTransportModeCode\": \"BUS\","
        +    "\"DefaultTransportMode\": \"buss\","
        +    "\"StopAreaTypeCode\": \"BUSTERM\","
        +    "\"LastModifiedUtcDateTime\": \"2007-08-24 00:00:00.000\","
        +    "\"ExistsFromDate\": \"2007-08-24 00:00:00.000\""
        +"]}";

    @Test
    void httpResponseGoodSucces() throws JsonProcessingException {
        assertEquals(busService.httpResponseGood(jsonStatus0), new BusStatusCodeAndMessage(0, "null"));
    }

    @Test
    void httpResponseGoodBad() throws JsonProcessingException {
        assertEquals(busService.httpResponseGood(jsonStatus1020), new BusStatusCodeAndMessage(1020, "error"));
    }

    @Test
    void restTemplateCall() throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Encoding", "gzip, deflate");
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> responseEntity = ResponseEntity
                .status(HttpStatus.OK)
                .body(jsonSuccess);

        BusService busStub = mock(BusService.class);
        when(busStub.httpResponseGood(null)).thenReturn(
                new BusStatusCodeAndMessage(0, "null")
        );


        busService.restTemplateCall("line");
        when(restTemplate.exchange(
                "https://api.sl.se/api2/LineData.json?model=line&key=778be1729fec4bda96b6251ebeb1f313&DefaultTransportModeCode=BUS",
                HttpMethod.GET,
                entity,
                String.class
        )).thenReturn(
             responseEntity
        );

        verify(restTemplate,times(1)).exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.<Class<String>>any());
    }

}