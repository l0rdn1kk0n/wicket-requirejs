package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.webjars.WicketWebjars;
import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Configuration class that adds the {@link IRequireJsSettings} to an {@link Application} and
 * provides access to them via a static getter method {@link #settings()}/{@link #settings(org.apache.wicket.Application)}.
 *
 * @author miha
 */
public final class RequireJs {

    /**
     * The {@link org.apache.wicket.MetaDataKey} used to retrieve the {@link IRequireJsSettings} from the Wicket {@link Appendable}.
     */
    private static final MetaDataKey<IRequireJsSettings> REQUIRE_JS_SETTINGS_META_DATA_KEY = new MetaDataKey<IRequireJsSettings>() {
    };

    /**
     * installs given settings to given application
     *
     * @param application current application assigned to this thread
     */
    public static void install(final WebApplication application) {
        install(application, null);
    }

    /**
     * installs given settings to given application
     *
     * @param application current application assigned to this thread
     * @param settings    the configuration settings for RequireJs
     */
    public static void install(final WebApplication application, IRequireJsSettings settings) {
        if (application.getMetaData(REQUIRE_JS_SETTINGS_META_DATA_KEY) == null) {
            WicketWebjars.install(application);

            if (settings == null) {
                settings = new RequireJsSettings();
            }

            application.setMetaData(REQUIRE_JS_SETTINGS_META_DATA_KEY, settings);
            application.mount(new RequireJsMapper(settings));
        }
    }

    /**
     * returns require.js specific settings for given {@code application}.
     *
     * @param application current application assigned to this thread
     * @return require.js specific settings
     * @throws IllegalStateException if there are no settings set before
     */
    public static IRequireJsSettings settings(final Application application) {
        final IRequireJsSettings settings = application.getMetaData(REQUIRE_JS_SETTINGS_META_DATA_KEY);

        if (settings == null) {
            throw new IllegalStateException("you've forgot to install your require.js settings. "
                                            + "Please call RequireJs#install() in your Application#init() method.");
        }

        return settings;
    }

    /**
     * returns require.js specific settings for current application assigned to this thread.
     *
     * @return require.js specific settings
     * @throws IllegalStateException if there are no settings set before or there is no
     *                               application assigned to this thread
     */
    public static IRequireJsSettings settings() {
        if (Application.exists()) {
            return settings(Application.get());
        } else {
            throw new IllegalStateException("there is no application assigned to this thread.");
        }
    }

    /**
     * Private construct to prevent instantiation.
     */
    private RequireJs() {
        throw new UnsupportedOperationException();
    }
}
