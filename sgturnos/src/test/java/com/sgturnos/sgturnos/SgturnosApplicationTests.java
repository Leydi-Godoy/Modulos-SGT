package com.sgturnos.sgturnos;

import com.sgturnos.service.PlanificacionTurnosService;
import java.time.YearMonth;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class PlanificacionTurnosServiceTest {

    @Autowired
    private PlanificacionTurnosService service;

    @Test
    void generarMallaTest() {
        YearMonth mes = YearMonth.of(2025, 8);
        var malla = service.generarMalla(mes);
        assertFalse(malla.isEmpty());
        System.out.println("Malla generada: " + malla.size() + " registros");
    }
}