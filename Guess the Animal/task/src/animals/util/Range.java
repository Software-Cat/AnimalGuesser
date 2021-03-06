/*
 * Copyright © Bowen Wu 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package animals.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Range<T extends Number> {

    @NotNull
    private final T boundOne;

    @NotNull
    private final T boundTwo;

    private final boolean lowerInclusive;

    private final boolean upperInclusive;

    public Range(@NotNull T boundOne, @NotNull T boundTwo, boolean lowerInclusive, boolean upperInclusive) {
        this.boundOne = boundOne;
        this.boundTwo = boundTwo;
        this.lowerInclusive = lowerInclusive;
        this.upperInclusive = upperInclusive;
    }

    public Range(@NotNull T boundOne, @NotNull T boundTwo) {
        this(boundOne, boundTwo, true, false);
    }

    public T getLowerBound() {
        if (boundOneDouble() < boundTwoDouble()) {
            return boundOne;
        } else {
            return boundTwo;
        }
    }

    public T getUpperBound() {
        if (boundOneDouble() > boundTwoDouble()) {
            return boundOne;
        } else {
            return boundTwo;
        }
    }

    public <U extends Number> boolean isInRange(@Nullable final U number) {
        if (number == null) {
            return false;
        }

        double doubleValue = number.doubleValue();

        boolean satisfyLower = false;
        boolean satisfyUpper = false;

        if (lowerInclusive) {
            satisfyLower = doubleValue >= getLowerBound().doubleValue();
        } else {
            satisfyLower = doubleValue > getLowerBound().doubleValue();
        }

        if (upperInclusive) {
            satisfyUpper = doubleValue <= getUpperBound().doubleValue();
        } else {
            satisfyUpper = doubleValue < getUpperBound().doubleValue();
        }

        return satisfyLower && satisfyUpper;
    }

    private double boundOneDouble() {
        return boundOne.doubleValue();
    }

    private double boundTwoDouble() {
        return boundTwo.doubleValue();
    }
}
