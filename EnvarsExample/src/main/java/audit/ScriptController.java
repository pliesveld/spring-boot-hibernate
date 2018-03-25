package audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/script")
public class ScriptController {

    @Autowired private ScriptService scriptService;

    static public long SCRIPT_ID = 1L;

    @GetMapping
    public ResponseEntity getScript() {
        return allRevisionsOfScript();
    }

    @PutMapping
    public ResponseEntity putScript(@Valid @RequestBody Script script) {
        Script scriptPo = scriptService.loadScript(SCRIPT_ID);
        scriptPo.setText(script.getText());
        scriptService.saveScript(scriptPo);
        return allRevisionsOfScript();
    }

    @PostMapping
    public ResponseEntity postScript(@Valid @RequestBody Script script) {
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping
    public ResponseEntity delteScript() {
        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity allRevisionsOfScript() {
        List<Number> revisions = scriptService.revisionList(SCRIPT_ID);

        List<Script> scripts = new ArrayList<>(revisions.size());

        for(Number revision : revisions) {
            Script script = scriptService.loadScript(SCRIPT_ID, revision.intValue());
            script.setRevision(revision.intValue());
            scripts.add(script);
        }
        return ResponseEntity.ok(scripts);
    }

    @GetMapping("/{revision}")
    public ResponseEntity getScriptAtRevision(@PathVariable(value = "revision",required = true) int revision) {
        Script script = scriptService.loadScript(SCRIPT_ID, revision);
        script.setRevision(revision);
        return ResponseEntity.ok(script);
    }
}
