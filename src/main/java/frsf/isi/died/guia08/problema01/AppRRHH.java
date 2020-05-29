package frsf.isi.died.guia08.problema01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.EmpleadoException;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.TareaIncorrecta;

public class AppRRHH {

	private List<Empleado> empleados;
	
	public AppRRHH() {
		this.empleados = new ArrayList<Empleado>();
	}
	
	public void imprimirEmpleados() {
		System.out.println(this.empleados.toString());
	}
	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		Empleado e = new Empleado(cuil,nombre,Tipo.CONTRATADO,costoHora);
		// agregarlo a la lista
		this.empleados.add(e);
	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		Empleado e = new Empleado(cuil,nombre,Tipo.EFECTIVO,costoHora);
		// agregarlo a la lista		
		this.empleados.add(e);
	}
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) throws EmpleadoException, TareaIncorrecta {
		// crear una Tarea
		Tarea t = new Tarea(idTarea,descripcion,duracionEstimada);
		// con el método buscarEmpleado() de esta clase
		Optional<Empleado> aux = this.buscarEmpleado(p -> p.getCuil().equals(cuil));
		//  asignar la tarea
		if(aux.isEmpty()) throw new EmpleadoException("ERROR: Empleado con CUIL:" + cuil + " no encontrado.");
		else aux.get().asignarTarea(t);
	}
	
	public void empezarTarea(Integer cuil,Integer idTarea) throws EmpleadoException, TareaIncorrecta {
		// busca el empleado por cuil en la lista de empleados
		// con el método buscarEmpleado() actual de esta clase
		Optional<Empleado> aux = this.buscarEmpleado(p -> p.getCuil().equals(cuil));
		// e invoca al método comenzar tarea
		if(aux.isEmpty()) throw new EmpleadoException("ERROR: Empleado con CUIL:" + cuil + " no encontrado.");
		else aux.get().comenzar(idTarea);
	}
	
	public void terminarTarea(Integer cuil,Integer idTarea) throws EmpleadoException, TareaIncorrecta {
		Optional<Empleado> aux = this.buscarEmpleado(p -> p.getCuil().equals(cuil));
		if(aux.isEmpty()) throw new EmpleadoException("ERROR: Empleado con CUIL:" + cuil + " no encontrado.");
		else aux.get().finalizar(idTarea);
	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
			try(Reader fileReader = new FileReader(nombreArchivo)) {
				try(BufferedReader in = new BufferedReader(fileReader)){
					String linea = null;
					while((linea = in.readLine())!=null) {
						String[] fila = linea.split(";");
							this.agregarEmpleadoContratado(Integer.valueOf(fila[0]), fila[1], Double.valueOf(fila[2]));
					}
				}
			}		

		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
						this.agregarEmpleadoEfectivo(Integer.valueOf(fila[0]), fila[1], Double.valueOf(fila[2]));
				}
			}
		}
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado		
	}

	public void cargarTareasCSV(String nombreArchivo) throws FileNotFoundException,IOException, NumberFormatException, EmpleadoException, TareaIncorrecta {
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
						this.asignarTarea(Integer.valueOf(fila[0]), Integer.valueOf(fila[1]), fila[2], Integer.valueOf(fila[3]));
					}
			}
		}
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
	}
	
	private void guardarTareasTerminadasCSV() throws IOException {
		List<List<Tarea>> todasLasTareas = this.empleados.stream().map(Empleado::getTareas).collect(Collectors.toList());
		List<String> listaCsvs = todasLasTareas.stream().flatMap(l -> l.stream()).filter(t -> !t.getFacturada() && !t.pendiente()).map(Tarea::asCsv).collect(Collectors.toList());
		System.out.println(listaCsvs.toString());
		try(Writer fileWriter= new FileWriter("tareas.csv",true)) {
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				for(String st : listaCsvs) {
					out.write(st + System.getProperty("line.separator"));
				}
			}
			}

		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() throws IOException {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}
