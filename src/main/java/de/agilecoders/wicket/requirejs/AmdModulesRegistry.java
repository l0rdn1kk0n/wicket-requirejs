package de.agilecoders.wicket.requirejs;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ResourceBundles;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A registry that knows which JavaScriptResourceReference to load for an AMD module by name
 *
 * Whenever {@link AmdModuleHeaderItem} is used it registers
 * its {@link org.apache.wicket.request.resource.JavaScriptResourceReference} in this registry.
 * Later in JavaScript code the application should use <em>wicket</em> plugin
 * (e.g. <em>wicket!my/fancy/module</em>) that will make a request to the mount path where
 * {@link de.agilecoders.wicket.requirejs.RequireJsMapper} is listening and it will use this
 * registry to return the JavaScript resource reference for the requested module
 */
public class AmdModulesRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(AmdModulesRegistry.class);

    private static final MetaDataKey<ConcurrentMap<String, JavaScriptResourceReference>> MAPPINGS =
            new MetaDataKey<ConcurrentMap<String, JavaScriptResourceReference>>() {
    };

    private ConcurrentMap<String, JavaScriptResourceReference> getMappings() {
        Application application = Application.get();
        ConcurrentMap<String, JavaScriptResourceReference> mappings = application.getMetaData(MAPPINGS);
        if (mappings == null) {
            mappings = new ConcurrentHashMap<>();
            application.setMetaData(MAPPINGS, mappings);
        }

        return mappings;
    }

    public ResourceReference getReference(String moduleName) {
        return getMappings().get(name(moduleName));
    }

    /**
     * Registers an AMD module in this registry
     *
     * @param moduleName The name of the AMD module, e.g. 'my/module'
     * @param reference  The JavaScriptResourceReference that loads the content of the module
     */
    public void register(String moduleName, JavaScriptResourceReference reference) {
        boolean registered = false;

        Map<String, String> mappings = RequireJsConfigHeaderItem.getMappings();

        if (Application.exists()) {
            ResourceBundles resourceBundles = Application.get().getResourceBundles();
            HeaderItem bundleItem = resourceBundles.findBundle(JavaScriptHeaderItem.forReference(reference, moduleName));

            if (bundleItem instanceof JavaScriptReferenceHeaderItem) {
                JavaScriptReferenceHeaderItem jsBundleItem = (JavaScriptReferenceHeaderItem) bundleItem;
                JavaScriptResourceReference bundleReference = (JavaScriptResourceReference) jsBundleItem.getReference();
                put(moduleName, bundleReference);
                mappings.put(moduleName, bundleReference.getName());
                registered = true;
            }
        }

        if (!registered) {
            put(moduleName, reference);
        }
    }

    private void put(String moduleName, JavaScriptResourceReference reference) {
        ResourceReference old = getMappings().putIfAbsent(name(moduleName), reference);
        if (old != null && !old.equals(reference)) {
            LOG.error("'{}' hasn't been registered as AMD module '{}' because there is another module with this name already: {}",
                      reference, moduleName, old);
        }
    }

    private String name(String moduleName) {
        Args.notNull(moduleName, "moduleName");
        if (!moduleName.endsWith(".js")) {
            moduleName += ".js";
        }
        return moduleName;
    }
}
