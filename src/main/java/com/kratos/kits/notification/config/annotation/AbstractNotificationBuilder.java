package com.kratos.kits.notification.config.annotation;

import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.LinkedHashMap;

/**
 * <p>
 * A base {@link NotificationBuilder} that allows {@link NotificationConfigurer} to be applied to
 * it. This makes modifying the {@link NotificationBuilder} a strategy that can be customized
 * and broken up into a number of {@link NotificationConfigurer} objects that have more
 * specific goals than that of the {@link NotificationBuilder}.
 * </p>
 *
 * <p>
 * For example, a {@link NotificationBuilder} may build an {@link DelegatingFilterProxy}, but
 * a {@link NotificationConfigurer} might populate the {@link NotificationBuilder} with the
 * filters necessary for session management, form based login, authorization, etc.
 * </p>
 *
 * @author tang he
 *
 * @param <B> The type of this builder (that is returned by the base class)
 */
public class AbstractNotificationBuilder<B extends NotificationBuilder> implements NotificationBuilder<B> {
    private final LinkedHashMap<Class<? extends NotificationConfigurer>, NotificationConfigurer> configurers = new LinkedHashMap<>();

    /**
     * Gets the {@link NotificationConfigurer} by its class name or <code>null</code> if not
     * found. Note that object hierarchies are not considered.
     *
     * @param clazz {@link NotificationConfigurer} class name
     * @return {@link NotificationConfigurer}
     */
    @SuppressWarnings("unchecked")
    public <C extends NotificationConfigurer> C getConfigurer(Class<C> clazz) {
        return (C) this.configurers.get(clazz);
    }

    /**
     * Applies a {@link NotificationConfigurerAdapter} to this {@link NotificationBuilder} and
     * invokes {@link NotificationConfigurerAdapter#setBuilder(NotificationBuilder)}.
     *
     * @param configurer {@link NotificationConfigurerAdapter}
     * @return {@link NotificationConfigurerAdapter}
     */
    @SuppressWarnings("unchecked")
    private <C extends NotificationConfigurerAdapter> C apply(C configurer)
            throws Exception {
        configurer.setBuilder(this);
        this.configurers.put(configurer.getClass(), configurer);
        return configurer;
    }

    /**
     * If the {@link NotificationConfigurer} has already been specified get the original,
     * otherwise apply the new {@link NotificationConfigurerAdapter}.
     *
     * @param configurer the {@link NotificationConfigurer} to apply if one is not found for
     * this {@link NotificationConfigurer} class.
     * @return the current {@link NotificationConfigurer} for the configurer passed in
     */
    @SuppressWarnings("unchecked")
    protected <C extends NotificationConfigurerAdapter> C getOrApply(
            C configurer) throws Exception {
        C existingConfig =  (C) getConfigurer(configurer.getClass());
        if (existingConfig != null) {
            return existingConfig;
        }
        return apply(configurer);
    }
}
