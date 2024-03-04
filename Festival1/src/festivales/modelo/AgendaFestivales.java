package festivales.modelo;

import java.util.*;


/**
 * Esta clase guarda una agenda con los festivales programados
 * en una serie de meses
 *
 * La agenda guardalos festivales en una colecci?n map
 * La clave del map es el mes (un enumerado festivales.modelo.festivales.modelo.Mes)
 * Cada mes tiene asociados en una colecci?n ArrayList
 * los festivales  de ese mes
 *
 * Solo aparecen los meses que incluyen alg?n festival
 *
 * Las claves se recuperan en orden alfab?ico
 *
 */
public class AgendaFestivales {
    private TreeMap<Mes, ArrayList<Festival>> agenda;
    
    public AgendaFestivales() {
        this.agenda = new TreeMap<>();
    }
    /**
     * a?ade un nuevo festival a la agenda
     *
     * Si la clave (el mes en el que se celebra el festival)
     * no existe en la agenda se crear? una nueva entrada
     * con dicha clave y la colecci?n formada por ese ?nico festival
     *
     * Si la clave (el mes) ya existe se a?ade el nuevo festival
     * a la lista de festivales que ya existe ese ms
     * insert?ndolo de forma que quede ordenado por nombre de festival.
     * Para este segundo caso usa el metodo de ayuda
     * obtenerPosicionDeInsercion()
     *
     */
    public void addFestival(Festival festival) {
        int pos = 0;
        ArrayList<Festival> fest = new ArrayList<>();
        fest.add(festival);
        if (!this.agenda.containsKey(festival.getMes())){
            this.agenda.put(festival.getMes(),fest);
        }else {
            this.agenda.get(festival.getMes()).add(obtenerPosicionDeInsercion(fest, festival),festival);
        }
    }

    /**
     *
     * @param festivales una lista de festivales
     * @param festival
     * @return la posici?n en la que deber?a ir el nuevo festival
     * de forma que la lista quedase ordenada por nombre
     */
    private int obtenerPosicionDeInsercion(ArrayList<Festival> festivales, Festival festival) {
        int pos = 0;

        Comparator<Festival> comparador = Comparator.comparing(Festival::getNombre);

        TreeSet<Festival> festiOrdenado = new TreeSet<>(comparador);
        festiOrdenado.addAll(festivales);
        for (Festival iterador : festiOrdenado) {
            if (iterador.getNombre().compareTo(festival.getNombre()) > 0) {
                pos++;
            }
        }
        return pos;
    }

    /**
     * Representaci?n textual del festival
     * De forma eficiente
     *  Usa el conjunto de entradas para recorrer el map
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<Mes, ArrayList<Festival>>> conjuntoEntry = this.agenda.entrySet();
        for (Map.Entry<Mes, ArrayList<Festival>> entry : conjuntoEntry) {
            Mes meses = entry.getKey();
            ArrayList<Festival> festivalesAlMes  = entry.getValue();
            sb.append(meses).append("  (").append(festivalesEnMes(meses)).append(" festival/es)").append("\n");
            for (Festival festival : festivalesAlMes){
                sb.append(festival.toString());
            }

        }
        return sb.toString();
    }

    /**
     *
     * @param mes el mes a considerar
     * @return la cantidad de festivales que hay en ese mes
     * Si el mes no existe se devuelve -1
     */
    public int festivalesEnMes(Mes mes) {
        int cantMeses = 0;
         if (this.agenda.containsKey(mes)){
              cantMeses = this.agenda.get(mes).size();
         }
        return cantMeses;
    }

    /**
     * Se trata de agrupar todos los festivales de la agenda
     * por estilo.
     * Cada estilo que aparece en la agenda tiene asociada una colecci?n
     * que es el conjunto de nombres de festivales que pertenecen a ese estilo
     * Importa el orden de los nombres en el conjunto
     *
     * Identifica el tipo exacto del valor de retorno
     */
    public TreeMap<Estilo, TreeSet<String>>   festivalesPorEstilo() {

        TreeMap<Estilo, TreeSet<String>> festPorEstilos = new TreeMap<>();

        for (ArrayList<Festival> festivalesMes : this.agenda.values()) {

            for (Festival festival : festivalesMes) {
                // Obtenemos el conjunto de estilos del festival
                HashSet<Estilo> estilos = festival.getEstilos();

                // Iteramos sobre cada estilo y lo agregamos al TreeMap
                for (Estilo estilo : estilos) {
                    // Si el estilo no existe en el TreeMap, lo inicializamos
                    if (!festPorEstilos.containsKey(estilo)) {
                        festPorEstilos.put(estilo, new TreeSet<>());
                    }
                    // Agregamos el nombre del festival al conjunto correspondiente al estilo
                    festPorEstilos.get(estilo).add(festival.getNombre());
                }
            }
        }
        return festPorEstilos;
    }

    /**
     * Se cancelan todos los festivales organizados en alguno de los
     * lugares que indica el conjunto en el mes indicado. Los festivales
     * concluidos o que no empezados no se tienen en cuenta
     * Hay que borrarlos de la agenda
     * Si el mes no existe se devuelve -1
     *
     * Si al borrar de un mes los festivales el mes queda con 0 festivales
     * se borra la entrada completa del map
     */
    public int cancelarFestivales(HashSet<String> lugares, Mes mes) {


        ArrayList<Festival> festivales = this.agenda.get(mes);
        int contadorBorrados = 0;

        if (festivales.isEmpty()) {
            return -1;
        }
        Iterator<Festival> it = festivales.iterator();

        while (it.hasNext()) {

            Festival festival = it.next();
            if (festival.haConcluido() || festival.empiezaAntesQue(festival)) {
                it.remove();
            }else if (lugares.contains(festival.getLugar())) {
                it.remove();
                contadorBorrados++;
            }
        }
        if (festivales.isEmpty()) {
            this.agenda.remove(mes);
        }

        return contadorBorrados;
    }
}

