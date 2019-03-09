package com.journaldev.hibernate.main;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.journaldev.hibernate.model.Address;
import com.journaldev.hibernate.model.Employee;
import com.journaldev.hibernate.util.HibernateUtil;


public class HibernateNamedQueryExample {

	static{
		System.out.println("Before log4j configuration");
		DOMConfigurator.configure("log4j.xml");
		System.out.println("After log4j configuration");
	}
	
	private static Logger logger = Logger.getLogger(HibernateNamedQueryExample.class);
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		

		
		// Prep work
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		//HQL Named Query Example
		Query query = session.getNamedQuery("HQL_GET_ALL_EMPLOYEE");
		List<Employee> empList = query.list();
		for (Employee emp : empList) {
			logger.info("List of Employees::" + emp.getId() + ","
					+ emp.getAddress().getCity());
		}

		query = session.getNamedQuery("HQL_GET_EMPLOYEE_BY_ID");
		query.setInteger("id", 2);
		Employee emp = (Employee) query.uniqueResult();
		logger.info("Employee Name=" + emp.getName() + ", City="
				+ emp.getAddress().getCity());

		query = session.getNamedQuery("HQL_GET_EMPLOYEE_BY_SALARY");
		query.setInteger("salary", 200);
		empList = query.list();
		for (Employee emp1 : empList) {
			logger.info("List of Employees::" + emp1.getId() + ","
					+ emp1.getSalary());
		}

		query = session.getNamedQuery("@HQL_GET_ALL_ADDRESS");
		List<Address> addressList = query.list();
		for (Address addr : addressList) {
			logger.info("List of Address::" + addr.getId() + "::"
					+ addr.getZipcode() + "::" + addr.getEmployee().getName());
		}
		
		//Native SQL Named Query Example
		query = session.getNamedQuery("@SQL_GET_ALL_ADDRESS");
		List<Object[]> addressObjArray = query.list();
		for(Object[] row : addressObjArray){
			for(Object obj : row){
				logger.info(obj + "::");
			}
			logger.info("\n");
		}
		
		query = session.getNamedQuery("SQL_GET_ALL_EMP_ADDRESS");
		addressObjArray = query.list();
		for(Object[] row : addressObjArray){
			Employee e = (Employee) row[0];
			logger.info("Employee Info::"+e);
			Address a = (Address) row[1];
			logger.info("Address Info::"+a);
		}
		// rolling back to save the test data
		tx.commit();

		// closing hibernate resources
		sessionFactory.close();
	}

}
