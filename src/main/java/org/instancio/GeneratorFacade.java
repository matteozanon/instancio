package org.instancio;

import org.instancio.generator.Generator;
import org.instancio.generator.GeneratorMap;
import org.instancio.generator.GeneratorSettings;
import org.instancio.model.ArrayNode;
import org.instancio.model.ClassNode;
import org.instancio.model.ModelContext;
import org.instancio.model.Node;
import org.instancio.reflection.ImplementationResolver;
import org.instancio.reflection.InterfaceImplementationResolver;
import org.instancio.util.Random;
import org.instancio.util.ReflectionUtils;
import org.instancio.util.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

class GeneratorFacade {
    private static final Logger LOG = LoggerFactory.getLogger(GeneratorFacade.class);

    private final GeneratorMap generatorMap = new GeneratorMap();
    private final AncestorTree ancestorTree = new AncestorTree();
    private final ImplementationResolver implementationResolver = new InterfaceImplementationResolver();
    private final ModelContext<?> context;

    public GeneratorFacade(final ModelContext<?> context) {
        this.context = context;
    }

    GeneratorResult<?> generateNodeValue(Node node, @Nullable Object owner) {
        final Class<?> effectiveType = node.getKlass();
        final Object ancestor = ancestorTree.getObjectAncestor(owner, node.getParent());

        if (ancestor != null) {
            LOG.debug("{} has a circular dependency to {}. Not setting field value.",
                    owner.getClass().getSimpleName(), ancestor.getClass().getSimpleName());

            return GeneratorResult.nullResult();
        }

        final Optional<GeneratorResult<?>> optionalResult = attemptGenerateViaContext(node.getField());
        if (optionalResult.isPresent()) {
            return optionalResult.get();
        }

        if (effectiveType.isPrimitive()) {
            return GeneratorResult.build(generatorMap.get(effectiveType).generate());
        }

        if (node instanceof ArrayNode) {
            return generateArray(node);
        }

        final Generator<?> generator = generatorMap.get(effectiveType);

        final GeneratorResult<?> result;

        if (generator == null) {

            if (effectiveType.isInterface()) {
                return resolveImplementationAndGenerate(node, owner, effectiveType);
            }

            GeneratorSettings settings = null;
            if (Collection.class.isAssignableFrom(effectiveType) || Map.class.isAssignableFrom(effectiveType)) {
                settings = GeneratorSettings.builder().dataStructureSize(2).build();
            }
            result = GeneratorResult.builder(ReflectionUtils.instantiate(effectiveType)).withSettings(settings).build();

        } else {
            LOG.trace("Using '{}' generator to create '{}'", generator.getClass().getSimpleName(), effectiveType.getName());

            // If we already know how to generate this object, we don't need to collect its fields
            result = GeneratorResult.builder(generator.generate()).withSettings(generator.getSettings()).build();
            LOG.trace("Generated {} using '{}' generator ", result, generator.getClass().getSimpleName());
        }


        ancestorTree.setObjectAncestor(result.getValue(), new AncestorTree.InstanceNode(owner, node.getParent()));
        return result;
    }

    private GeneratorResult<?> generateArray(Node node) {
        final Class<?> componentType = ((ArrayNode) node).getElementNode().getKlass();
        final Generator<?> generator = generatorMap.getArrayGenerator(componentType);
        final Object arrayObject = generator.generate();
        return GeneratorResult.build(arrayObject);
    }

    /**
     * Resolve an implementation class for the given interface and attempt to generate it.
     * This method should not be called for JDK classes, such as Collection interfaces.
     */
    private GeneratorResult<?> resolveImplementationAndGenerate(
            final Node parentNode,
            @Nullable final Object owner,
            final Class<?> interfaceClass) {
        Verify.isNotArrayCollectionOrMap(interfaceClass);

        LOG.debug("No generator for interface '{}'", interfaceClass.getName());

        Class<?> implementor = implementationResolver.resolve(interfaceClass).orElse(null);
        if (implementor == null) {
            LOG.debug("Interface '{}' has no implementation", interfaceClass.getName());
            return null;
        }
        ClassNode implementorClassNode = new ClassNode(parentNode.getNodeContext(), implementor, null, null, parentNode);
        return generateNodeValue(implementorClassNode, owner);
    }

    /**
     * If the context has enough information to generate a value for the field, then do so.
     * If not, return an empty {@link Optional} and proceed with the main generation flow.
     * <p>
     * TODO: hierarchy.setAncestorOf(value, owner) must be done for all generated objects
     *  unless they are from JDK classes
     */
    private Optional<GeneratorResult<?>> attemptGenerateViaContext(@Nullable final Field field) {
        if (field == null) return Optional.empty();

        if (context.getNullableFields().contains(field) && Random.trueOrFalse()) {
            // We can return a null 'GeneratorResult' or a null 'GeneratorResult.value'
            // Returning a null 'GeneratorResult.value' will ensure that a field value
            // will be overwritten with null. Otherwise, field value would retain its
            // old value (if one was assigned).
            return Optional.of(GeneratorResult.nullResult());
        }

        GeneratorResult<?> result = null;
        if (context.getUserSuppliedFieldGenerators().containsKey(field)) {
            Generator<?> generator = context.getUserSuppliedFieldGenerators().get(field);

            result = GeneratorResult.builder(generator.generate())
                    .withSettings(generator.getSettings())
                    .build();

        } else if (context.getUserSuppliedClassGenerators().containsKey(field.getType())) {
            Generator<?> generator = context.getUserSuppliedClassGenerators().get(field.getType()); // XXX what if field.getType returns Object?
            result = GeneratorResult.builder(generator.generate())
                    .withSettings(generator.getSettings())
                    .build();
        }
        return Optional.ofNullable(result);
    }

}
