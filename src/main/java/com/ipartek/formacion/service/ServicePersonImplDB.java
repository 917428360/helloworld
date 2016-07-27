package com.ipartek.formacion.service;

import java.util.List;

import com.ipartek.formacion.model.dao.PersonDAO;
import com.ipartek.formacion.model.dao.PersonDAOImpl;
import com.ipartek.formacion.pojo.Person;

public class ServicePersonImplDB implements ServicePerson{

	private static ServicePersonImplDB INSTANCE = null;
	private PersonDAO daoPersona;
	
	private ServicePersonImplDB() {
		daoPersona = PersonDAOImpl.getInstance();
}

	public static ServicePersonImplDB getInstance() {
		if (INSTANCE == null) {
			createInstance();
		}
		return INSTANCE;
	}

	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ServicePersonImplDB();
		}
	}

	@Override
	public List<Person> getAll() {		
		System.out.println("Buscado ultimas 1.000 personas ");
		return daoPersona.getAll();
	}

	@Override
	public Person getById(long id) {
		System.out.println("Buscado persona [" + id + "]" );
		return daoPersona.getById(id);
	}

	@Override
	public boolean delete(long id) {	
		System.out.println("Eliminado persona [" + id + "]" );
		return daoPersona.delete(id);
	}

	@Override
	public Person save(Person p) throws Exception {
		boolean resul = false;
		if ( p.isNew() ){
			resul = daoPersona.create(p);
			System.out.println("Insertado " + p.toString()  );
		}else{
			System.out.println("Modificado " + p.toString()  );
			resul = daoPersona.update(p);
		}
				
		if (resul==false){
			System.out.println("Excepcion " + p.toString()  );
			throw new Exception("Excepcion salvando Persona " + p.toString() );
		}
		
		return p;
	}

	@Override
	public List<Person> search(String criterio) {
		System.out.println("Buscar planetas criterio " + criterio  );
		return daoPersona.search(criterio);
	}

}