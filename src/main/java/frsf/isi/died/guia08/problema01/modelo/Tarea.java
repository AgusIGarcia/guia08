package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDateTime;

public class Tarea{

	private Integer id;
	private String descripcion;
	private Integer duracionEstimada;
	private Empleado empleadoAsignado;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Boolean facturada;
	
	
	public Tarea(Integer id, String descripcion, Integer duracionEstimada) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.duracionEstimada = duracionEstimada;
		this.empleadoAsignado = null;
		this.fechaInicio = null;
		this.fechaFin = null;
		this.facturada = false;
	}
	
	public boolean pendiente() {
		if (this.fechaFin == null) return true;
		return false;
	}
	

	public void asignarEmpleado(Empleado e) throws TareaIncorrecta{
		// si la tarea ya tiene un empleado asignado
		// y tiene fecha de finalizado debe lanzar una excepcion
		if(this.empleadoAsignado != null || !this.pendiente()) throw new TareaIncorrecta("Tarea ya asignada o finalizada.");
		else this.empleadoAsignado = e;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(Integer duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getFacturada() {
		return facturada;
	}

	public void setFacturada(Boolean facturada) {
		this.facturada = facturada;
	}

	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}
	
	public int horasInvertidas() {
		if(!this.pendiente()) {
			if(this.fechaFin.getYear() == this.fechaInicio.getYear())
				return (this.fechaFin.getDayOfYear() - this.fechaInicio.getDayOfYear()) * 4;
			else {
				int difAnios = this.fechaFin.getYear() - this.fechaInicio.getYear();
				int diasSobrantes = 365 - this.fechaInicio.getDayOfYear();
				return (this.fechaFin.getDayOfYear() + (difAnios - 1) * 365 + diasSobrantes) * 4;
			}
		}
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Tarea) return this.id.equals(((Tarea)o).id);
		return false;
	}
	
	public String asCsv() {
		return this.id + ";" + this.descripcion + ";" + this.horasInvertidas() + ";" + this.empleadoAsignado.getCuil() + ";" + this.empleadoAsignado.getNombre();
	}
	
	
}
