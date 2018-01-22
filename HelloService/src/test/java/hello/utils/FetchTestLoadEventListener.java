package hello.utils;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.boot.MetadataSources;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.DuplicationStrategy;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.reflections.Reflections;

/**
 * Useful when testing how much data was loaded by Hibernate, counts
 * the loaded entity instances by type.
 */
public class FetchTestLoadEventListener implements PostLoadEventListener {

    final static private Logger LOG = LogManager.getLogger("org.hibernate.SQL");

    protected Map<Class,Integer> loadCount = new HashMap<Class,Integer>();

    private EntityManagerFactory emf;

    public FetchTestLoadEventListener(EntityManagerFactory emf) {
        this.emf = emf;
        ServiceRegistry serviceRegistry = ((SessionFactoryImplementor) emf.unwrap(org.hibernate.SessionFactory.class)).getServiceRegistry();
        serviceRegistry.getService(EventListenerRegistry.class).addDuplicationStrategy(new DuplicationStrategy() {
                                                                                           @Override
                                                                                           public boolean areMatch(Object listener, Object original) {
                                                                                               return Objects.equals(listener, original);
                                                                                           }

                                                                                           @Override
                                                                                           public Action getAction() {
                                                                                               return Action.REPLACE_ORIGINAL;
                                                                                           }
                                                                                       });
               // serviceRegistry.getService(EventListenerRegistry.class).appendListeners(EventType.POST_LOAD, this);
    }

//    public void getDDL(String packageName) {
//
//        ServiceRegistry serviceRegistry = ((SessionFactoryImplementor) emf.unwrap(org.hibernate.SessionFactory.class)).getServiceRegistry();
//
//        while (serviceRegistry.getParentServiceRegistry() != null)
//            serviceRegistry = serviceRegistry.getParentServiceRegistry();
//        MetadataSources metadata = new MetadataSources(serviceRegistry);
//
//        new Reflections(packageName)
//                .getTypesAnnotatedWith(Entity.class)
//                .forEach(metadata::addAnnotatedClass);
//
//        EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.STDOUT);
//
//        SchemaExport export = new SchemaExport();
//        export.setDelimiter(";");
//        export.setFormat(true);
//        export.setHaltOnError(true);
//        export.createOnly(targetTypes, metadata.buildMetadata());
//
//    }


    @Override
    public void onPostLoad(PostLoadEvent event) {
        Class entityType = event.getEntity().getClass();
        if (!loadCount.containsKey(entityType)) {
            loadCount.put(entityType, 0);
        }
        loadCount.put(entityType, loadCount.get(entityType) + 1);
    }

    public int getLoadCount(Class entityType) {
        return loadCount.containsKey(entityType) ? loadCount.get(entityType) : 0;
    }

    public void reset() {
        loadCount.clear();
    }

    public void printLoadCount(Class entityType) {
        //LOG.debug("{}: {}", entityType.getSimpleName(), getLoadCount(entityType));
    }

    public void printAllLoadCounts() {
        // emf.getMetamodel().getEntities().iterator().forEachRemaining((e)-> { LOG.debug("Entity {}", e.getJavaType().getSimpleName());});
        emf.getMetamodel().getEntities().stream().map(Type::getJavaType).forEach(this::printLoadCount);
    }
}