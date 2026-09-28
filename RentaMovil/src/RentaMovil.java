import java.util.ArrayList;

public class RentaMovil {
    private ArrayList<Vehiculo> vehiculos;
    private double ingresosAcumulados;

    public RentaMovil() {
        vehiculos = new ArrayList<>();
        ingresosAcumulados = 0;
    }

    public boolean registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null || buscarPorPlaca(vehiculo.getPlaca()) != null) {
            return false;
        }

        vehiculos.add(vehiculo);
        return true;
    }

    public Vehiculo buscarPorPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                return vehiculo;
            }
        }

        return null;
    }

    public boolean confirmarAlquiler(String placa, int dias) {
        Vehiculo vehiculo = buscarPorPlaca(placa);

        if (vehiculo == null || dias <= 0 || !vehiculo.estaDisponible()) {
            return false;
        }

        double costo = vehiculo.calcularCosto(dias);

        if (!vehiculo.ocupar()) {
            return false;
        }

        ingresosAcumulados += costo;
        return true;
    }

    public boolean registrarDevolucion(String placa) {
        Vehiculo vehiculo = buscarPorPlaca(placa);

        if (vehiculo == null) {
            return false;
        }

        return vehiculo.devolver();
    }

    public ArrayList<Vehiculo> obtenerVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    public double getIngresosAcumulados() {
        return ingresosAcumulados;
    }

    public String generarReporte() {
        int autos = 0, autosDisponibles = 0;
        int motos = 0, motosDisponibles = 0;
        int camionetas = 0, camionetasDisponibles = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo instanceof Automovil) {
                autos++;
                if (vehiculo.estaDisponible()) {
                    autosDisponibles++;
                }
            } else if (vehiculo instanceof Motocicleta) {
                motos++;
                if (vehiculo.estaDisponible()) {
                    motosDisponibles++;
                }
            } else if (vehiculo instanceof CamionetaCarga) {
                camionetas++;
                if (vehiculo.estaDisponible()) {
                    camionetasDisponibles++;
                }
            }
        }

        return "\n--- REPORTE GENERAL ---"
            + "\nVehículos registrados: " + vehiculos.size()
            + "\nAutomóviles: " + autos
            + " | Disponibles: " + autosDisponibles
            + " | Alquilados: " + (autos - autosDisponibles)
            + "\nMotocicletas: " + motos
            + " | Disponibles: " + motosDisponibles
            + " | Alquiladas: " + (motos - motosDisponibles)
            + "\nCamionetas: " + camionetas
            + " | Disponibles: " + camionetasDisponibles
            + " | Alquiladas: " + (camionetas - camionetasDisponibles)
            + "\nIngresos acumulados: Q"
            + String.format("%.2f", ingresosAcumulados);
    }
}