package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class TareaTest {
	
	Empleado e1 = new Empleado(1111, "Cacho", Tipo.CONTRATADO ,100.0);
	Empleado e2 = new Empleado(2222, "Ricardo", Tipo.EFECTIVO ,100.0);
	Tarea t1 = new Tarea(1, "Soy la tarea 1", 8);
	Tarea t2 = new Tarea(2, "Soy la tarea 2", 4);
	
	@Test
	public void asignarEmpleadoTest() {
		try {
			t1.asignarEmpleado(e1);
			//t1.asignarEmpleado(e2);
		} catch (TareaIncorrecta e) {
			fail("Fallo");
		}
		assertTrue(true);
	}

}
