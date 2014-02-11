/*
 * Copyright 2014 Dayatang Open Source..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dayatang.domain.specification;

/**
 *
 * @author yyang
 */
public class LengthSpecification extends AbstractSpecification<String> {

    private final int min;
    
    private final int max;

    public LengthSpecification(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public LengthSpecification(int max) {
        this(0, max);
    }
    
    @Override
    public boolean isSatisfiedBy(String t) {
        return t != null && t.length() >= min && t.length() <= max;
    }
    
}
