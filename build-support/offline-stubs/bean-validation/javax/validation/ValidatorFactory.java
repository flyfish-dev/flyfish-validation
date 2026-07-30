package javax.validation; public interface ValidatorFactory extends AutoCloseable { Validator getValidator(); default void close() { } }
