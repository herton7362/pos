package com.kratos.kits.notification.config.annotation;

/**
 * A base class for {@link NotificationConfigurer} that allows subclasses to only implement
 * the methods they are interested in. It also provides a mechanism for using the
 * {@link NotificationConfigurer} and when done gaining access to the {@link NotificationBuilder}
 * that is being configured.
 *
 * @param <B> 当前正在构建的Builder
 * @author tang he
 * @since 1.0.0
 */
public abstract class NotificationConfigurerAdapter<B extends NotificationBuilder> implements NotificationConfigurer {
    private B notificationBuilder;

    /**
     * Sets the {@link NotificationBuilder} to be used. This is automatically set when using
     * {@link AbstractNotificationBuilder#apply(NotificationConfigurerAdapter)}
     *
     * @param builder the {@link NotificationBuilder} to set
     */
    void setBuilder(B builder) {
        this.notificationBuilder = builder;
    }

    /**
     * Gets the {@link NotificationBuilder}. Cannot be null.
     *
     * @return the {@link NotificationBuilder}
     * @throws IllegalStateException if {@link NotificationBuilder} is null
     */
    private B getBuilder() {
        if (notificationBuilder == null) {
            throw new IllegalStateException("notificationBuilder cannot be null");
        }
        return notificationBuilder;
    }

    /**
     * Return the {@link NotificationBuilder} when done using the {@link NotificationConfigurer}.
     * This is useful for method chaining.
     *
     * @return {@link NotificationBuilder}
     */
    public B and() {
        return getBuilder();
    }
}
