/*
 * Copyright 2022-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.instancio.internal.annotation;

import org.instancio.documentation.InternalApi;
import org.instancio.generator.Generator;
import org.instancio.generator.GeneratorContext;
import org.instancio.internal.util.Sonar;

import java.lang.annotation.Annotation;
import java.util.function.BiFunction;

/**
 * A function that provides a {@link Generator} for a given annotation.
 *
 * @since 3.3.0
 */
@InternalApi
@FunctionalInterface
interface AnnotationGeneratorFn extends BiFunction<Annotation, GeneratorContext, Generator<?>> {

    @Override
    @SuppressWarnings(Sonar.GENERIC_WILDCARD_IN_RETURN)
    Generator<?> apply(Annotation annotation, GeneratorContext context);
}
