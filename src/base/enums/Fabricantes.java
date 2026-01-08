package base.enums;

public enum Fabricantes {
    NINTENDO("Nintendo"),
    SONY("Sony"),
    SEGA("Sega"),
    MICROSOFT("Microsoft"),
    VALVECORPORATION("Valve Corporation");

    private final String valor;

    Fabricantes(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
