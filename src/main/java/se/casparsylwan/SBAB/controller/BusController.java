package se.casparsylwan.SBAB.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.casparsylwan.SBAB.model.BusLine;
import se.casparsylwan.SBAB.model.BusStop;
import se.casparsylwan.SBAB.service.BusService;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("bus")
@CrossOrigin(origins = "*")
@Slf4j
public class BusController {

    @Autowired
    private BusService busService;

    @GetMapping("all")
    public List<BusLine> getBusLines(){
        log.info("api/v1/bus/all called");
        return busService.getAllLines();
    }

}
