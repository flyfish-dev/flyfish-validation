package org.springframework.beans.factory;
import java.util.stream.Stream;
public interface ObjectProvider<T> { Stream<T> orderedStream(); }
