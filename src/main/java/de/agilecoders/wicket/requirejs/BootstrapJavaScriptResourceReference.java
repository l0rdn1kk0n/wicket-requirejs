package de.agilecoders.wicket.requirejs;

import org.apache.wicket.Application;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

import java.io.IOException;

/**
 * A {@link JavaScriptResourceReference} implementation that refers to the bootstrap javascript
 * file that initializes require.js and adds some plugins.
 *
 * @author miha
 */
public class BootstrapJavaScriptResourceReference extends JavaScriptResourceReference {

    /**
     * Construct.
     *
     * @param name the name of the resource
     */
    public BootstrapJavaScriptResourceReference(final String name) {
        super(BootstrapJavaScriptResourceReference.class, name);
    }

    /**
     * Construct. Uses "bootstrap.js" as resource name
     */
    public BootstrapJavaScriptResourceReference() {
        this("bootstrap.js");
    }

    /**
     * @return default markup encoding if there is an application assigned to this thread or "UTF-8" as fallback.
     */
    protected String encoding() {
        return Application.exists() ? Application.get().getMarkupSettings().getDefaultMarkupEncoding() : "UTF-8";
    }

    /**
     * @return content of this resource as string.
     */
    public String asText() {
        try {
            return IOUtils.toString(getResource().getCacheableResourceStream().getInputStream(), encoding());
        } catch (IOException e) {
            throw new WicketRuntimeException(e);
        } catch (ResourceStreamNotFoundException e) {
            throw new WicketRuntimeException(e);
        }
    }
}
