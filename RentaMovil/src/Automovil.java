public class Automovil extends Vehiculo {
    private int pasajeros;
    private boolean automatico;

    public Automovil(String placa, String marca, String modelo,
                     double tarifaDiaria, int pasajeros, boolean automatico) {
        super(placa, marca, modelo, tarifaDiaria);

        if (pasajeros <= 0) {
            throw new IllegalArgumentException("Los pasajeros deben ser mayores que cero.");
        }

        this.pasajeros = pasajeros;
        this.automatico = automatico;
    }

    public int getPasajeros() {
        return pasajeros;
    }

    public boolean esAutomatico() {
        return automatico;
    }

    @Override
    public double calcularCosto(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días deben ser mayores que cero.");
        }

        double recargoDiario = automatico ? 50 : 0;
        return (getTarifaDiaria() + recargoDiario) * dias;
    }
}