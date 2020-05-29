package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;


public class EmpleadoTest {

	// IMPORTANTE
	// ESTA CLASE ESTA ANOTADA COMO @IGNORE por lo que no ejecutará ningun test
	// hasta que no borre esa anotación.

		Empleado e1 = new Empleado(1111, "Cacho", Tipo.CONTRATADO ,100.0);
		Empleado e2 = new Empleado(2222, "Ricardo", Tipo.EFECTIVO ,100.0);
		Tarea t1 = new Tarea(1, "Soy la tarea 1", 8);
		Tarea t2 = new Tarea(2, "Soy la tarea 2", 4);


	@Test
	public void testSalario() {
		try {
			e1.asignarTarea(t1);		
			e1.comenzar(1);
			e1.finalizar(1);
			e2.asignarTarea(t2);
			e2.comenzar(2, "10-10-2010 10:10");
			e2.finalizar(2, "15-10-2010 10:10");
		} catch (TareaIncorrecta e) {
			System.out.println(e.getMessage());
		}
		assertTrue(e1.salario().equals(1040.0));
		assertTrue(e2.salario().equals(400.0));
	}

	@Test
	public void testCostoTarea() {
		assertTrue(e1.costoTarea(t1).equals(800.0));
	}

	@Test
	public void testAsignarTarea() {
		
		try {
			e1.asignarTarea(t1);
			//e2.asignarTarea(t1); Si la activamos falla
		} catch (TareaIncorrecta e) {
			fail("Fallo");
		}
		assertTrue(true);
	}

	@Test
	public void testComenzarInteger() {
		
		try {
			e1.asignarTarea(t1);
			e1.comenzar(1);
			//e2.comenzar(1);
		} catch (TareaIncorrecta e) {
			fail("Fallo");
		}
		assertTrue(true);
		
	}

	@Test
	public void testFinalizarInteger() {
		try {
			e1.asignarTarea(t1);
			e1.comenzar(1);
			e1.finalizar(1);
			//e2.comenzar(1);
		} catch (TareaIncorrecta e) {
			fail("Fallo");
		}
		assertTrue(!t1.pendiente());
		assertTrue(t1.horasInvertidas() == 0);
	}

}
