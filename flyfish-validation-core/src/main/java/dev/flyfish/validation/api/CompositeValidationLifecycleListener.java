package dev.flyfish.validation.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** 按 order 稳定执行多个生命周期监听器。 */
public final class CompositeValidationLifecycleListener
    implements ValidationLifecycleListener {
    private final List<ValidationLifecycleListener> delegates;
    private final boolean propagateListenerException;

    public CompositeValidationLifecycleListener(
    Collection<? extends ValidationLifecycleListener> values,
    boolean propagateListenerException) {
        List<ValidationLifecycleListener> copy =
        new ArrayList<ValidationLifecycleListener>();
        if (values != null) {
            for (ValidationLifecycleListener value : values) {
                if (value != null) { copy.add(value); }
            }
        }
        Collections.sort(copy, new Comparator<ValidationLifecycleListener>() {
            @Override public int compare(ValidationLifecycleListener left,
            ValidationLifecycleListener right) {
                return Integer.compare(left.order(), right.order());
            }
        });
        this.delegates = Collections.unmodifiableList(copy);
        this.propagateListenerException = propagateListenerException;
    }

    @Override public void beforeValidation(final ValidationInvocation invocation) {
        each(new ListenerCall() { @Override public void call(ValidationLifecycleListener value) {
                value.beforeValidation(invocation); }});
    }
    @Override public void afterSuccess(final ValidationInvocation invocation,
    final ValidationReport report, final long elapsedNanos) {
        each(new ListenerCall() { @Override public void call(ValidationLifecycleListener value) {
                value.afterSuccess(invocation, report, elapsedNanos); }});
    }
    @Override public void afterFailure(final ValidationInvocation invocation,
    final ValidationReport report, final long elapsedNanos) {
        each(new ListenerCall() { @Override public void call(ValidationLifecycleListener value) {
                value.afterFailure(invocation, report, elapsedNanos); }});
    }
    @Override public void afterException(final ValidationInvocation invocation,
    final RuntimeException exception, final long elapsedNanos) {
        each(new ListenerCall() { @Override public void call(ValidationLifecycleListener value) {
                value.afterException(invocation, exception, elapsedNanos); }});
    }

    private void each(ListenerCall call) {
        for (ValidationLifecycleListener delegate : delegates) {
            try { call.call(delegate); }
            catch (RuntimeException exception) {
                if (propagateListenerException) { throw exception; }
            }
        }
    }
    private interface ListenerCall { void call(ValidationLifecycleListener value); }
}
