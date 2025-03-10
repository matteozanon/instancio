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
package org.instancio.generator.specs;

import org.instancio.documentation.ExperimentalApi;

import java.math.BigDecimal;

/**
 * Generator spec for {@link BigDecimal} values
 * that supports {@link AsGeneratorSpec}.
 *
 * @since 2.6.0
 */
public interface BigDecimalAsGeneratorSpec
        extends BigDecimalGeneratorSpec, AsGeneratorSpec<BigDecimal> {

    /**
     * {@inheritDoc}
     *
     * <p>Note that this method is incompatible with {@link #precision(int)}.
     * For details, see {@link #precision(int)} javadoc.
     */
    @Override
    BigDecimalAsGeneratorSpec min(BigDecimal min);

    /**
     * {@inheritDoc}
     *
     * <p>Note that this method is incompatible with {@link #precision(int)}.
     * For details, see {@link #precision(int)} javadoc.
     */
    @Override
    BigDecimalAsGeneratorSpec max(BigDecimal max);

    /**
     * {@inheritDoc}
     *
     * <p>Note that this method is incompatible with {@link #precision(int)}.
     * For details, see {@link #precision(int)} javadoc.
     */
    @Override
    BigDecimalAsGeneratorSpec range(BigDecimal min, BigDecimal max);

    @Override
    BigDecimalAsGeneratorSpec nullable();

    @Override
    BigDecimalAsGeneratorSpec scale(int scale);

    /**
     * {@inheritDoc}
     *
     * @since 3.3.0
     */
    @Override
    @ExperimentalApi
    BigDecimalAsGeneratorSpec precision(int precision);
}
