package hello;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import java.util.Objects;

import hello.utils.FetchTestLoadEventListener;

public class BaseTest {
    protected FetchTestLoadEventListener loadEventListener;

    @Autowired
    public void afterBootstrap(EntityManager entityManager) {
        Objects.requireNonNull(entityManager);
        synchronized (this) {
            if (loadEventListener == null) {
                loadEventListener = new FetchTestLoadEventListener(entityManager.getEntityManagerFactory());
          }
        }
    }
}
