public abstract class Vehiculo {
    private String marca;
    private String modelo;
    private String placa;
    private double tarifaDiaria;
    private boolean disponible;
    
    public Vehiculo(String placa, String marca, String modelo, double tarifaDiaria) {
        if (placa == null || placa.trim().isEmpty()) { 
            throw new IllegalArgumentException("La placa no puede estar vacia");
        }
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacia");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacio");
        }
        if (!Double.isFinite(tarifaDiaria) || tarifaDiaria <= 0) {
            throw new IllegalArgumentException("La tarifa diaria debe ser un numero positivo y mayor que cero");
        }

        this.placa = placa.trim().toUpperCase();
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.tarifaDiaria = tarifaDiaria;
        this.disponible = true;    }

    public String getPlaca() {
        return placa;
    }
    public String getMarca() {
        return marca;
    }
    public String getModelo() {
        return modelo;
    }
    public double getTarifaDiaria() {
        return tarifaDiaria;
    }
    public boolean estaDisponible() {
        return disponible;
    }

    public boolean ocupar() {
        if (!disponible) {
            return false;
        }
        disponible = false;
        return true;
    }

    public boolean devolver() {
    if (disponible) {
        return false;
    }

    disponible = true;
    return true;
    }

    public abstract double calcularCosto(int dias);

}
