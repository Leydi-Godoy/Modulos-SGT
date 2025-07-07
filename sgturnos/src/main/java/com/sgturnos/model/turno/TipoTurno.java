package com.sgturnos.model.turno;


public class TipoTurno {
    
    public enum TipoTurno {
    MAÑANA("Mañana"),
    TARDE("Tarde"),
    NOCHE("Noche");

    private final String nombre;

    TipoTurno(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
    
    
    
    
}
