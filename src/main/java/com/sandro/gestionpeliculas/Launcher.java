package com.sandro.gestionpeliculas;

// IMPORTANTE: Importamos App desde su nueva casa (la carpeta modelo)
import com.sandro.gestionpeliculas.modelo.App;

public class Launcher {
    public static void main(String[] args) {
        // Ahora sí sabe dónde encontrar a App
        App.main(args);
    }
}