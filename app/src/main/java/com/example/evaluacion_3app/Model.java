package com.example.evaluacion_3app;

import java.io.Serializable;

public class Model implements Serializable {
    private int laboratorio;
    private String nombre;
    private String descripcion;
    private String fecha;
    private String hora;
    private String rut;
    private int id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRut() {
        return rut;
    }
    public void setRut(String rut) {
        this.rut = rut;
    }
    public int getLaboratorio() {
        return laboratorio;
    }
    public void setLaboratorio(int laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getNombreLaboratorio(){
        String[] arrayLab = {"C2", "LINF", "LEICA", "LNET", "LTEL"};
        return arrayLab[laboratorio];
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre= nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion= descripcion;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha= fecha;
    }
    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora= hora;
    }
}
