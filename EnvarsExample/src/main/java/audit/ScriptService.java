package audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class ScriptService {

    private final ScriptRepository scriptRepository;

    private final EntityManager entityManager;

    @Autowired
    public ScriptService(ScriptRepository scriptRepository, EntityManager entityManager) {
        this.scriptRepository = scriptRepository;
        this.entityManager = entityManager;
    }

    public Script loadScript(long id) {
        return scriptRepository.findById(id).orElseThrow(ScriptException::new);
    }

    public Script saveScript(Script scriptPo) {
        return scriptRepository.save(scriptPo);
    }

    public Script loadScript(long id, int revision) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = revisionList(id);
        if (revisions.stream().anyMatch(p -> Objects.equals(p.intValue(), revision))) {
            return auditReader.find(Script.class, id, revision);
        }
        throw new ScriptException();
    }

    public List<Number> revisionList(long id) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        return auditReader.getRevisions(Script.class, id);
    }
}
