package base.enums;

public enum Paises {
    JAPON("Japón"),
    ESTADOSUNIDOS("Estados Unidos"),
    COREASUR("Corea del Sur"),
    FRANCIA("Francia"),
    CHINA("China");

    private final String valor;

    Paises(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
