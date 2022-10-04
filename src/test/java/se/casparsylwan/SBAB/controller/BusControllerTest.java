package se.casparsylwan.SBAB.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import se.casparsylwan.SBAB.model.BusLine;
import se.casparsylwan.SBAB.service.BusService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;



@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BusControllerTest {

    @Mock
    private BusService busService;

    @InjectMocks
    private BusController busControllerTest;


    @Test
    void getBusLines() {
        when(busService.getAllLines()).thenReturn(new ArrayList<BusLine>());
        assertEquals(busControllerTest.getBusLines(), new ArrayList<BusLine>());


    }
}