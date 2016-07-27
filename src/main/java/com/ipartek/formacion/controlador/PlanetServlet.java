package com.ipartek.formacion.controlador;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ipartek.formacion.Constantes;
import com.ipartek.formacion.controlador.listener.InitListener;
import com.ipartek.formacion.pojo.Planet;
import com.ipartek.formacion.service.ServicePlanet;
import com.ipartek.formacion.service.ServicePlanetImpArrayList;
import com.ipartek.formacion.service.ServicePlanetImplDB;

/**
 * Servlet implementation class PlanetServlet
 */
public class PlanetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatch;
	private ServicePlanet serviceP = ServicePlanetImplDB.getInstance();
	private final static Logger LOG = Logger.getLogger(PlanetServlet.class);

	/**
	 * Se ejecuta solo la primera vez que alguien llama al servlet
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

	}

	/**
	 * Este metodo se ejecuta al Destruirse el Servlet, por ejemplo cuando
	 * paramos el Servidor
	 */
	@Override
	public void destroy() {

		super.destroy();
		
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Antes de doGet o doPost");
		super.service(request, response);
		System.out.println("Despues de doGet o doPost");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int op = Integer.parseInt(request.getParameter("op"));
		LOG.debug("planetas : opcion seleccionada " + op);
		switch (op) {
		case Constantes.OP_LIST:
			listar(request, response);
			break;
		case Constantes.OP_NEW:
			nuevo(request, response);
			break;
		case Constantes.OP_DETAIL:
			detalle(request, response);
			break;
		case Constantes.OP_SEARCH:
			buscar(request, response);
			break;
		case Constantes.OP_DELETE:
			eliminar(request, response);
			break;
		default:
			listar(request, response);
			break;
		}

		dispatch.forward(request, response);

	}
	
	private void detalle(HttpServletRequest request, HttpServletResponse response) {

		long id = Long.parseLong(request.getParameter("id"));
		request.setAttribute("detail", serviceP.getById(id));
		dispatch = request.getRequestDispatcher(Constantes.VIEW_PLANET_DETAIL);

	}

	private void nuevo(HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("detail", new Planet());
		dispatch = request.getRequestDispatcher(Constantes.VIEW_PLANET_DETAIL);

	}

	private void listar(HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("list", serviceP.getAll());

		dispatch = request.getRequestDispatcher(Constantes.VIEW_PLANET_LIST);

	}

	

	private void eliminar(HttpServletRequest request, HttpServletResponse response) {

		long id = Long.parseLong(request.getParameter("id"));
		String msg = "Planeta con id: " + id + " no ha sido eliminado";
		if (serviceP.delete(id)) {
			msg = "Planeta con id: " + id + " ha sido eliminado";
			LOG.info("Planeta con id: " + id + " ha sido eliminado");
			request.setAttribute("msgBueno", msg);
		} else {
			LOG.info("Planeta con id: " + id + " no ha sido eliminado");
			request.setAttribute("msgMalo", msg);
		}

		listar(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int op = Integer.parseInt(request.getParameter("op"));
		switch (op) {
		case Constantes.OP_SEARCH:
			buscar(request, response);
			break;
		case Constantes.OP_SAVE:
			guardar(request, response);
			break;

		default:
			listar(request, response);
			break;
		}

		dispatch.forward(request, response);
	}

	private void buscar(HttpServletRequest request, HttpServletResponse response) {
		String busqueda = request.getParameter("s");

		ArrayList<Planet> planetasBusqueda = (ArrayList<Planet>) serviceP.search(busqueda);
		request.setAttribute("list", planetasBusqueda);
		dispatch = request.getRequestDispatcher(Constantes.VIEW_PLANET_LIST);

		String msg = "Busqueda [" + busqueda + "] 0 coincidencias";
		if (!planetasBusqueda.isEmpty()) {

			msg = "Busqueda [" + busqueda + "] " + planetasBusqueda.size() + " coincidencias";
		}
		request.setAttribute("msgBueno", msg);
		LOG.info("Busqueda realizada. Criterio: "+busqueda+" con "+planetasBusqueda.size()+" resultados");

	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) {

		// recoger parametros formulario
		long id = Long.parseLong(request.getParameter("id"));
		String imagen = request.getParameter("imagen");
		String nombre = request.getParameter("nombre");

		// crear Planeta
		Planet p = new Planet();
		p.setId(id);
		p.setImagen(imagen);
		p.setNombre(nombre);

		// guardar o modificar planeta en ArrayList
		String msg = "No se ha podido guardar el Planeta";

		try {
			request.setAttribute("detail", serviceP.save(p));
			msg = "El Planeta se ha guardado";
			LOG.info("Planeta guardado"+p.getNombre()+","+p.getId());
			request.setAttribute("msgBueno", msg);
		} catch (Exception e) {
			LOG.warn("Error al intentar guardar planeta"+p.getNombre()+","+p.getId());
			request.setAttribute("msgMalo", msg);

		}

		dispatch = request.getRequestDispatcher(Constantes.VIEW_PLANET_DETAIL);

	}

}