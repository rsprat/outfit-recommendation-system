package rob.ors.core.model.api;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class DBConnection {
	private static SessionFactory sessionFactory;
	
	static
	{
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	private DBConnection()
	{		
	}

	public SessionFactory sessionFactory()
	{
		return this.sessionFactory;
	}
	
	public static Session session()
	{
		//if(sessionFactory.getCurrentSession() == null )sessionFactory.openSession();		
		return sessionFactory.getCurrentSession();
	}
	
	public static Transaction transaction()
	{
		if(session().getTransaction().isActive()) return session().getTransaction();
		return session().beginTransaction();
	}

	
}
