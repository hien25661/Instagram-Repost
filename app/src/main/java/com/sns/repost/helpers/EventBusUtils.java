package com.sns.repost.helpers;


import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Hien Nguyen on 10/15/2015.
 */
public class EventBusUtils {
    /** Convenience singleton for apps using a process-wide EventBus instance. */
    @NonNull
    public static Holder getDefault() {
        return new Holder(EventBus.getDefault());
    }

    /**
     * Registers the given subscriber to receive events. Subscribers must call {@link EventBus#unregister(Object)} once they
     * are no longer interested in receiving events.
     * <p/>
     * Subscribers have event handling methods that are identified by their name, typically called "onEvent". Event
     * handling methods must have exactly one parameter, the event. If the event handling method is to be called in a
     * specific thread, a modifier is appended to the method name. Valid modifiers match one of the {link ThreadMode}
     * enums. For example, if a method is to be called in the UI/main thread by EventBus, it would be called
     * "onEventMainThread".
     */
    @NonNull
    public static Holder register(Object subscriber) {
        EventBus eventBus = EventBus.getDefault();
        register(eventBus, subscriber);

        return new Holder(eventBus);
    }

    /**
     * Registers the given subscriber to receive events. Subscribers must call {@link EventBus#unregister(Object)} once they
     * are no longer interested in receiving events.
     * <p/>
     * Subscribers have event handling methods that are identified by their name, typically called "onEvent". Event
     * handling methods must have exactly one parameter, the event. If the event handling method is to be called in a
     * specific thread, a modifier is appended to the method name. Valid modifiers match one of the {link ThreadMode}
     * enums. For example, if a method is to be called in the UI/main thread by EventBus, it would be called
     * "onEventMainThread".
     */
    public static void register(Holder holder, Object subscriber) {
        if (holder != null) {
            register(holder.eventBus, subscriber);
        }
    }

    /**
     * Registers the given subscriber to receive events. Subscribers must call {@link EventBus#unregister(Object)} once they
     * are no longer interested in receiving events.
     * <p/>
     * Subscribers have event handling methods that are identified by their name, typically called "onEvent". Event
     * handling methods must have exactly one parameter, the event. If the event handling method is to be called in a
     * specific thread, a modifier is appended to the method name. Valid modifiers match one of the {link ThreadMode}
     * enums. For example, if a method is to be called in the UI/main thread by EventBus, it would be called
     * "onEventMainThread".
     */
    private static void register(EventBus eventBus, Object subscriber) {
        if (eventBus != null && subscriber != null && !eventBus.isRegistered(subscriber)) {
            try {
                eventBus.register(subscriber);
            } catch (Exception e) {
                Log.e("DEBUG", "com.gnt.stixchat.utils.EventBusUtils.register, ", e);
            }
        }
    }

    /** Unregisters the given subscriber from all event classes. */
    public static void unregister(Object subscriber) {
        EventBus eventBus = EventBus.getDefault();
        unregister(eventBus, subscriber);
    }

    /** Unregisters the given subscriber from all event classes. */
    public static void unregister(Holder holder, Object subscriber) {
        if (holder != null) {
            unregister(holder.eventBus, subscriber);
        }
    }

    /** Unregisters the given subscriber from all event classes. */
    private static void unregister(EventBus eventBus, Object subscriber) {
        if (eventBus != null && subscriber != null && eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber);
        }
    }

    /** Posts the given event to the event bus. */
    public static void post(Object event) {
        post(EventBus.getDefault(), event);
    }

    /** Posts the given event to the event bus. */
    private static void post(EventBus eventBus, Object event) {
        eventBus.post(event);
    }

    /**
     * Event Bus Holder
     */
    public static class Holder {

        /** Event Bus Instance */
        private final EventBus eventBus;

        /**
         * Default Constructor
         * @param eventBus Instance
         */
        private Holder(EventBus eventBus) {
            this.eventBus = eventBus;
        }

        /** Posts the given event to the event bus. */
        public void post(Object event) {
            EventBusUtils.post(eventBus, event);
        }
    }

}
