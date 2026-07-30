package dev.flyfish.validation.example.boot4;

import java.util.Collections;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.flyfish.validation.FlyfishValidator;

/** 演示先执行声明式约束，再执行数据库关联规则。 */
@RestController
@RequestMapping("/users")
public final class RegisterController {
    private final FlyfishValidator validator;

    public RegisterController(FlyfishValidator validator) {
        this.validator = validator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> register(
    @Valid @RequestBody RegisterCommand command) {
        validator.validateBusinessOrThrow(command, "username-unique");
        return Collections.<String, Object>singletonMap(
        "username", command.getUsername());
    }
}
