package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Predicate;

public class Empleado {

	public enum Tipo { CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	private Predicate<Tarea> puedeAsignarTarea;
	private Function<Tarea, Double> calculoPagoPorTarea;	
	
	
	public Empleado(Integer cuil, String nombre, Tipo tipo, Double costoHora) {
		super();
		this.cuil = cuil;
		this.nombre = nombre;
		this.tipo = tipo;
		this.costoHora = costoHora;
		this.tareasAsignadas = new ArrayList<Tarea>();
		this.puedeAsignarTarea = (t) -> { 
			switch (this.tipo) {
			case CONTRATADO: 
				if(tareasAsignadas.stream().filter((s1) -> s1.pendiente()).count() < 5) return true;	
				return false;
			case EFECTIVO: 
				if(tareasAsignadas.stream().
						filter((s1) -> s1.pendiente()).
						mapToInt(Tarea::getDuracionEstimada).
						reduce((acum,hses) -> {return acum + hses;}).orElse(0) <= 15 ) return true;		
				return false;
			}
			return false;
		};
		this.calculoPagoPorTarea = (t) -> {
			double basico = t.getDuracionEstimada() * this.costoHora;
			if(t.pendiente()) {
				return basico;
			}
			else {
				int hsInvertidas = t.horasInvertidas();
				if (hsInvertidas == t.getDuracionEstimada()) return basico;
				else {
					switch (this.tipo) {
					case CONTRATADO: 
						if(hsInvertidas < t.getDuracionEstimada()) return basico * 1.3;
						else if (hsInvertidas > (t.getDuracionEstimada() + 8)) return basico * 0.75;
						else return basico;
					case EFECTIVO: 
						if(hsInvertidas < t.getDuracionEstimada()) return basico * 1.2;
						else return basico;
					}
				}
			}
			return 0.0;
		};
	}
	public Integer getCuil() {
		return this.cuil;
	}
	
	
	public Double salario() {
		// cargar todas las tareas no facturadas
		// calcular el costo
		// marcarlas como facturadas.
		Optional<Double>  sal;
		Predicate<Tarea> noPendNoFact =  (t) -> !t.pendiente() && !t.getFacturada();
		sal = this.tareasAsignadas.stream().filter(noPendNoFact).map(calculoPagoPorTarea).reduce((acum,salar) -> {return acum + salar;});
		this.tareasAsignadas.stream().filter(noPendNoFact).forEach((tar) -> tar.setFacturada(true));
		return sal.orElse(0.0);
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		return this.calculoPagoPorTarea.apply(t);
	}
		
	public Boolean asignarTarea(Tarea t) throws TareaIncorrecta {
		if(this.puedeAsignarTarea.test(t)) {
			t.asignarEmpleado(this);
			this.tareasAsignadas.add(t);
			return true;
		}
		return false;
	}
	
	public void comenzar(Integer idTarea) throws TareaIncorrecta {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora actual
		if (this.tareasAsignadas.stream().anyMatch(t -> t.getId().equals(idTarea))) 
			this.tareasAsignadas.stream().filter(t -> t.getId().equals(idTarea)).forEach(s -> s.setFechaInicio(LocalDateTime.now()));
		else throw new TareaIncorrecta("La tarea no se encuentra asignada");
		
	}
	
	public void finalizar(Integer idTarea) throws TareaIncorrecta {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		if (this.tareasAsignadas.stream().anyMatch(t -> t.getId().equals(idTarea))) 
			this.tareasAsignadas.stream().filter(t -> t.getId().equals(idTarea)).forEach(s -> s.setFechaFin(LocalDateTime.now()));
		else throw new TareaIncorrecta("La tarea no se encuentra asignada");
	}

	public void comenzar(Integer idTarea,String fecha) throws TareaIncorrecta {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		if (this.tareasAsignadas.stream().anyMatch(t -> t.getId().equals(idTarea))) {
			DateTimeFormatter form = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			LocalDateTime aux = LocalDateTime.parse(fecha,form);
			this.tareasAsignadas.stream().filter(t -> t.getId().equals(idTarea)).forEach(s -> s.setFechaInicio(aux));
		}
		else throw new TareaIncorrecta("La tarea no se encuentra asignada");
		
	}
	
	public void finalizar(Integer idTarea,String fecha) throws TareaIncorrecta {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		if (this.tareasAsignadas.stream().anyMatch(t -> t.getId().equals(idTarea))) {
			DateTimeFormatter form = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			LocalDateTime aux = LocalDateTime.parse(fecha,form);
			this.tareasAsignadas.stream().filter(t -> t.getId().equals(idTarea)).forEach(s -> s.setFechaFin(aux));
		}
		else throw new TareaIncorrecta("La tarea no se encuentra asignada");
	}
	
	@Override
	public String toString() {
		return "[CUIL: " + this.cuil + " - Nombre: " + this.nombre + " - Costo/h: " + this.costoHora + "]";
	}
	public String getNombre() {
		return this.nombre;
	}
	
	public List<Tarea> getTareas(){
		return this.tareasAsignadas;
	}
}
