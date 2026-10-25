package es.unex.mdai.omerta.model;

public class NegocioLegal {
    private Long id;
    private String nombre;
    private String tipo;
    private String encargado;
    private double ingresosLegales;
    private double dineroBlanqueado;
    private int lvlRiesgoFBI;
    private boolean activo;

    public NegocioLegal() {
        this.activo = true;
    }

    public NegocioLegal(Long id, String nombre, String tipo, String encargado, double ingresosLegales, double dineroBlanqueado, int lvlRiesgoFBI, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.encargado = encargado;
        this.ingresosLegales = ingresosLegales;
        this.dineroBlanqueado = dineroBlanqueado;
        this.lvlRiesgoFBI = lvlRiesgoFBI;
        this.activo = activo;
    }

    // Calculados para Thymeleaf
    public double getIngresoTotalReal() {
        return this.ingresosLegales + this.dineroBlanqueado;
    }

    public double getPorcentajeBlanqueo() {
        double total = getIngresoTotalReal();
        return total == 0 ? 0.0 : (this.dineroBlanqueado / total) * 100.0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEncargado() { return encargado; }
    public void setEncargado(String encargado) { this.encargado = encargado; }

    public double getIngresosLegales() { return ingresosLegales; }
    public void setIngresosLegales(double ingresosLegales) { this.ingresosLegales = ingresosLegales; }

    public double getDineroBlanqueado() { return dineroBlanqueado; }
    public void setDineroBlanqueado(double dineroBlanqueado) { this.dineroBlanqueado = dineroBlanqueado; }

    public int getLvlRiesgoFBI() { return lvlRiesgoFBI; }
    public void setLvlRiesgoFBI(int lvlRiesgoFBI) { this.lvlRiesgoFBI = lvlRiesgoFBI; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}