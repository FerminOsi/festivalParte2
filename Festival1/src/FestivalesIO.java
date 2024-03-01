

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * La clase contiene méodos estáticos que permiten
 * cargar la agenda de festivales leyendo los datos desde
 * un fichero
 */
public class FestivalesIO {

    
    public static void cargarFestivales(AgendaFestivales agenda) {
        Scanner sc = null;
        try {
            sc = new Scanner(FestivalesIO.class.
                    getResourceAsStream("/festivales.csv"));
            while (sc.hasNextLine()) {
                String lineaFestival = sc.nextLine();
                Festival festival = parsearLinea(lineaFestival);
                agenda.addFestival(festival);
                
            }
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
        
    }

    /**
     * se parsea la línea extrayendo sus datos y creando y
     * devolviendo un objeto Festival
     * @param lineaFestival los datos de un festival
     * @return el festival creado
     */
    public static Festival parsearLinea(String lineaFestival) {
        String nombre;
        String lugar;
        LocalDate fechaIni = null;
        int duracion = 0;
        HashSet<Estilo> estilos = new HashSet<>();

        String[] festival = lineaFestival.split(":");


        nombre = festival[0].trim();
        String [] palabras = nombre.split("\\s+");
        StringBuilder nombreFormateado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()){
                palabra = palabra.substring(0,1).toUpperCase() + palabra.substring(1).toLowerCase();
            }
            nombre = nombreFormateado.append(palabra).append(" ").toString().trim();
        }
        lugar = festival[1].trim().toUpperCase();
        fechaIni = LocalDate.parse(festival[2].trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        duracion = Integer.parseInt(festival[3].trim());

                for (int i = 4; i < festival.length; i++){
                    String estilosTexto = festival[i].trim().toUpperCase();
                    estilos.add(Estilo.valueOf(estilosTexto.trim().toUpperCase()));
                }
        return new Festival(nombre, lugar, fechaIni, duracion, estilos);
    }
}
