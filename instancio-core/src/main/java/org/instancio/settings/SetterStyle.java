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
package org.instancio.settings;

import org.instancio.documentation.ExperimentalApi;
import org.instancio.internal.util.StringUtils;

/**
 * Specifies the style of setter to use when
 * {@link AssignmentType#METHOD} is enabled.
 *
 * @see Keys#ASSIGNMENT_TYPE
 * @since 2.1.0
 */
@ExperimentalApi
public enum SetterStyle {

    /**
     * Standard setters with "set" prefix, for example {@code setFoo("value")}
     *
     * @since 2.1.0
     */
    SET,

    /**
     * Setters with "with" prefix, for example {@code withFoo("value")}
     *
     * @since 2.1.0
     */
    WITH,

    /**
     * Setters without a prefix, where the setter's name is the same
     * as the corresponding property name, for example {@code foo("value")}
     *
     * @since 2.1.0
     */
    PROPERTY;

    @Override
    public String toString() {
        return StringUtils.enumToString(this);
    }
}
