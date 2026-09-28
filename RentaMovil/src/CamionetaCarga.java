public class CamionetaCarga extends Vehiculo {
    private double capacidadToneladas;

    public CamionetaCarga(String placa, String marca, String modelo,
                          double tarifaDiaria, double capacidadToneladas) {
        super(placa, marca, modelo, tarifaDiaria);

        if (!Double.isFinite(capacidadToneladas) || capacidadToneladas <= 0) {
            throw new IllegalArgumentException(
                "La capacidad debe ser mayor que cero."
            );
        }

        this.capacidadToneladas = capacidadToneladas;
    }

    public double getCapacidadToneladas() {
        return capacidadToneladas;
    }

    @Override
    public double calcularCosto(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días deben ser mayores que cero.");
        }

        return (getTarifaDiaria() + 100 * capacidadToneladas) * dias;
    }
}