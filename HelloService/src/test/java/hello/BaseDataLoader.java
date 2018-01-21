package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.jpa.internal.util.PersistenceUtilHelper;

@Component
@Transactional
public class BaseDataLoader implements ApplicationListener<ApplicationReadyEvent> {
    final static private Logger LOG = LogManager.getLogger();

    @Autowired
    protected EntityManager entityManager;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    }

    protected <E> void isLoaded(Class<E> clazz, Object id) {
        try {
            Object obj = entityManager.getReference(clazz.getClass(), id);
            LOG.debug(PersistenceUtilHelper.isLoaded(obj).toString());
        } catch (Exception ex) {}
    }
}
