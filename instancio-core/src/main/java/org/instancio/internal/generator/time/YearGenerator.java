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
package org.instancio.internal.generator.time;

import org.instancio.Random;
import org.instancio.generator.GeneratorContext;
import org.instancio.generator.specs.YearSpec;
import org.instancio.internal.ApiValidator;
import org.instancio.internal.context.Global;

import java.time.Year;

public class YearGenerator extends JavaTimeTemporalGenerator<Year>
        implements YearSpec {

    public YearGenerator() {
        this(Global.generatorContext());
    }

    public YearGenerator(final GeneratorContext context) {
        super(context,
                Year.of(1970),
                Year.now().plusYears(50));
    }

    @Override
    public String apiMethod() {
        return "year()";
    }

    @Override
    public YearGenerator past() {
        super.past();
        return this;
    }

    @Override
    public YearGenerator future() {
        super.future();
        return this;
    }

    @Override
    public YearGenerator range(final Year start, final Year end) {
        super.range(start, end);
        return this;
    }

    @Override
    public YearGenerator nullable() {
        super.nullable();
        return this;
    }

    @Override
    Year getLatestPast() {
        return Year.now().minusYears(1);
    }

    @Override
    Year getEarliestFuture() {
        return Year.now().plusYears(1);
    }

    @Override
    void validateRange() {
        ApiValidator.isTrue(min.compareTo(max) <= 0, "Start must not exceed end: %s, %s", min, max);
    }

    @Override
    public Year generateNonNullValue(final Random random) {
        return Year.of(random.intRange(min.getValue(), max.getValue()));
    }
}
