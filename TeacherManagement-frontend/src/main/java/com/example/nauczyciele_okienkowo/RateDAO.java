package com.example.nauczyciele_okienkowo;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class RateDAO {

    public void saveRate(Rate rate) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(rate);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
