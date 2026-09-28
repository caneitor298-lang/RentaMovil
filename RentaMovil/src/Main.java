import java.util.Scanner;
import java.util.Locale;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static RentaMovil empresa = new RentaMovil();

    public static void main(String[] args) {
        cargarVehiculosIniciales();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1:
                    registrarVehiculo();
                    break;
                case 2:
                    consultarFlota();
                    break;
                case 3:
                    cotizar();
                    break;
                case 4:
                    alquilar();
                    break;
                case 5:
                    devolver();
                    break;
                case 6:
                    System.out.println(empresa.generarReporte());
                    break;
                case 0:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void cargarVehiculosIniciales() {
        empresa.registrarVehiculo(
            new Automovil("A-101", "Toyota", "Corolla", 200, 5, false));
        empresa.registrarVehiculo(
            new Automovil("A-102", "Hyundai", "Tucson", 250, 5, true));

        empresa.registrarVehiculo(
            new Motocicleta("M-201", "Honda", "CB150", 100, 150));
        empresa.registrarVehiculo(
            new Motocicleta("M-202", "Yamaha", "FZ300", 130, 300));

        empresa.registrarVehiculo(
            new CamionetaCarga("C-301", "Isuzu", "NPR", 200, 1.5));
        empresa.registrarVehiculo(
            new CamionetaCarga("C-302", "Hino", "300", 300, 2.0));
    }

    private static void mostrarMenu() {
        System.out.println("\n===== RENTAMOVIL =====");
        System.out.println("1. Registrar vehículo");
        System.out.println("2. Consultar flota");
        System.out.println("3. Cotizar alquiler");
        System.out.println("4. Confirmar alquiler");
        System.out.println("5. Registrar devolución");
        System.out.println("6. Reporte general");
        System.out.println("0. Salir");
    }

    private static void registrarVehiculo() {
        System.out.println("\n1. Automóvil");
        System.out.println("2. Motocicleta");
        System.out.println("3. Camioneta de carga");
        int tipo = leerEntero("Tipo de vehículo: ");

        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo no válido.");
            return;
        }

        String placa = leerTexto("Placa: ");
        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");
        double tarifa = leerDecimalPositivo("Tarifa diaria: ");

        try {
            Vehiculo vehiculo;

            if (tipo == 1) {
                int pasajeros = leerEnteroPositivo("Cantidad de pasajeros: ");
                String respuesta = leerTexto("¿Es automático? (s/n): ");
                boolean automatico = respuesta.equalsIgnoreCase("s");

                vehiculo = new Automovil(
                    placa, marca, modelo, tarifa, pasajeros, automatico);
            } else if (tipo == 2) {
                int cilindraje = leerEnteroPositivo("Cilindraje en cc: ");

                vehiculo = new Motocicleta(
                    placa, marca, modelo, tarifa, cilindraje);
            } else {
                double capacidad = leerDecimalPositivo(
                    "Capacidad máxima en toneladas: ");

                vehiculo = new CamionetaCarga(
                    placa, marca, modelo, tarifa, capacidad);
            }

            if (empresa.registrarVehiculo(vehiculo)) {
                System.out.println("Vehículo registrado correctamente.");
            } else {
                System.out.println("No se registró: la placa ya existe.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se registró: " + e.getMessage());
        }
    }

    private static void consultarFlota() {
        System.out.println("\n===== FLOTA =====");

        for (Vehiculo vehiculo : empresa.obtenerVehiculos()) {
            mostrarVehiculo(vehiculo);
            System.out.println("--------------------");
        }
    }

    private static void cotizar() {
        String placa = leerTexto("Placa a cotizar: ");
        Vehiculo vehiculo = empresa.buscarPorPlaca(placa);

        if (vehiculo == null) {
            System.out.println("No existe un vehículo con esa placa.");
            return;
        }

        int dias = leerEnteroPositivo("Días de alquiler: ");
        mostrarVehiculo(vehiculo);
        System.out.println("Costo total: " + dinero(vehiculo.calcularCosto(dias)));
        System.out.println("Esta cotización no realiza el alquiler.");
    }

    private static void alquilar() {
        String placa = leerTexto("Placa a alquilar: ");
        Vehiculo vehiculo = empresa.buscarPorPlaca(placa);

        if (vehiculo == null) {
            System.out.println("No existe un vehículo con esa placa.");
            return;
        }
        if (!vehiculo.estaDisponible()) {
            System.out.println("El vehículo está ocupado y no puede alquilarse.");
            return;
        }

        int dias = leerEnteroPositivo("Días de alquiler: ");
        double total = vehiculo.calcularCosto(dias);

        mostrarVehiculo(vehiculo);
        System.out.println("Total a cobrar: " + dinero(total));
        String respuesta = leerTexto("¿Confirmar alquiler? (s/n): ");

        if (!respuesta.equalsIgnoreCase("s")) {
            System.out.println("Alquiler cancelado. No se realizó ningún cobro.");
            return;
        }

        if (empresa.confirmarAlquiler(placa, dias)) {
            System.out.println("Alquiler confirmado. Se cobró " + dinero(total));
        } else {
            System.out.println("No se pudo confirmar el alquiler.");
        }
    }

    private static void devolver() {
        String placa = leerTexto("Placa a devolver: ");

        if (empresa.registrarDevolucion(placa)) {
            System.out.println("Devolución registrada. No se realizó otro cobro.");
        } else {
            System.out.println(
                "No se pudo devolver: la placa no existe o el vehículo ya estaba disponible.");
        }
    }

    private static void mostrarVehiculo(Vehiculo vehiculo) {
        String tipo;

        if (vehiculo instanceof Automovil) {
            Automovil auto = (Automovil) vehiculo;
            tipo = "Automóvil";
            System.out.println("Pasajeros: " + auto.getPasajeros());
            System.out.println("Transmisión: "
                + (auto.esAutomatico() ? "Automática" : "Manual"));
        } else if (vehiculo instanceof Motocicleta) {
            Motocicleta moto = (Motocicleta) vehiculo;
            tipo = "Motocicleta";
            System.out.println("Cilindraje: " + moto.getCilindraje() + " cc");
        } else {
            CamionetaCarga camioneta = (CamionetaCarga) vehiculo;
            tipo = "Camioneta de carga";
            System.out.println("Capacidad: "
                + camioneta.getCapacidadToneladas() + " toneladas");
        }

        System.out.println("Tipo: " + tipo);
        System.out.println("Placa: " + vehiculo.getPlaca());
        System.out.println("Marca y modelo: "
            + vehiculo.getMarca() + " " + vehiculo.getModelo());
        System.out.println("Tarifa diaria: " + dinero(vehiculo.getTarifaDiaria()));
        System.out.println("Estado: "
            + (vehiculo.estaDisponible() ? "Disponible" : "Alquilado"));
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número entero válido.");
            }
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            int numero = leerEntero(mensaje);
            if (numero > 0) {
                return numero;
            }
            System.out.println("El número debe ser mayor que cero.");
        }
    }

    private static double leerDecimalPositivo(String mensaje) {
        while (true) {
            try {
                String entrada = leerTexto(mensaje).replace(',', '.');
                double numero = Double.parseDouble(entrada);

                if (Double.isFinite(numero) && numero > 0) {
                    return numero;
                }
            } catch (NumberFormatException e) {
                // Se muestra el mensaje de abajo y se vuelve a pedir el dato.
            }
            System.out.println("Ingresa un número mayor que cero.");
        }
    }

    private static String dinero(double monto) {
        return String.format(Locale.US, "Q%.2f", monto);
    }
}