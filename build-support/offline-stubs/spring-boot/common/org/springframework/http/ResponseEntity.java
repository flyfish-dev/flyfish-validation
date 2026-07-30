package org.springframework.http;
public class ResponseEntity<T> {
 private final T body;
 public ResponseEntity(T body){this.body=body;}
 public T getBody(){return body;}
 public static BodyBuilder status(int status){return new BodyBuilder();}
 public static class BodyBuilder {
  public BodyBuilder headers(HttpHeaders headers){return this;}
  public <T> ResponseEntity<T> body(T body){return new ResponseEntity<T>(body);}
 }
}
