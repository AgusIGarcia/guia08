package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.EmpleadoException;
import frsf.isi.died.guia08.problema01.modelo.TareaIncorrecta;

public class AppRRHHTest {
	
	AppRRHH app = new AppRRHH();
	
	@Test
	public void inicializarTodo() {
		app.agregarEmpleadoContratado(1111, "Cacho", 100.0);
		app.agregarEmpleadoEfectivo(2222, "Ricardo", 100.0);
		app.imprimirEmpleados();
		try {
			app.asignarTarea(1111, 1, "holaqetal", 8);
			app.empezarTarea(1111, 1);
			app.terminarTarea(1111, 1);
			app.asignarTarea(2222, 2, "holakeasejeje", 8);
			app.empezarTarea(2222, 2);
			app.terminarTarea(2222, 2);
		} catch (EmpleadoException e) {
			System.out.println(e.getMessage());
		} catch (TareaIncorrecta e) {
			System.out.println(e.getMessage());
		}

		
	}


}
