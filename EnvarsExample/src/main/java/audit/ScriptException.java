package audit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ScriptException extends RuntimeException {
    public ScriptException() {
        super("Script Exception");
    }
}
