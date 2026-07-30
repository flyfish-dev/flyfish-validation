package org.springframework.http;
import java.util.LinkedHashMap;
public class HttpHeaders extends LinkedHashMap<String,java.util.List<String>> {
 private static final long serialVersionUID=1L;
 public void add(String name,String value){computeIfAbsent(name,k->new java.util.ArrayList<String>()).add(value);}
}
