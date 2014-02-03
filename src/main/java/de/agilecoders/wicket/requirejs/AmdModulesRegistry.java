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
 *
 */
public class AmdModulesRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(AmdModulesRegistry.class);

    private static final MetaDataKey<ConcurrentMap<String, JavaScriptResourceReference>> K =
            new MetaDataKey<ConcurrentMap<String, JavaScriptResourceReference>>() {
    };

    private ConcurrentMap<String, JavaScriptResourceReference> getMappings() {
        Application application = Application.get();
        ConcurrentMap<String, JavaScriptResourceReference> mappings = application.getMetaData(K);
        if (mappings == null) {
            mappings = new ConcurrentHashMap<>();
            application.setMetaData(K, mappings);
        }

        return mappings;
    }

    public ResourceReference getReference(String moduleName) {
        return getMappings().get(name(moduleName));
    }

    /**
     *
     * @param moduleName
     * @param reference
     */
    public void register(String moduleName, JavaScriptResourceReference reference) {
        boolean registered = false;

        Map<String, String> mappings = RequireJsConfig.getMappings();

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
