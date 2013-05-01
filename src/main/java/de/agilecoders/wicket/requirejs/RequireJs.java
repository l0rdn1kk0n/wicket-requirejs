package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.webjars.util.WicketWebjars;
import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;

/**
 * TODO miha: document class purpose
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
    public static void install(final Application application) {
        install(application, new RequireJsSettings());
    }

    /**
     * installs given settings to given application
     *
     * @param application current application assigned to this thread
     * @param settings the configuration settings for RequireJs
     */
    public static void install(final Application application, final IRequireJsSettings settings) {
        application.setMetaData(REQUIRE_JS_SETTINGS_META_DATA_KEY, settings);

        application.setHeaderResponseDecorator(new IHeaderResponseDecorator()
        {
            @Override
            public IHeaderResponse decorate(IHeaderResponse response)
            {
                return new FilteringHeaderResponse(response);
            }
        });

        WicketWebjars.install(application);
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
            throw new IllegalStateException("you've forgot to install your requirejs settings. "
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
