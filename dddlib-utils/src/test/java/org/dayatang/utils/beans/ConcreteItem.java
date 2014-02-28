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
package org.dayatang.utils.beans;

import java.beans.Transient;

/**
 *
 * @author yyang
 */
public class ConcreteItem extends Item {
    
    private double price;

    public ConcreteItem(int id, String name) {
        super(id, name);
    }

    public ConcreteItem(int id, String name, boolean disabled) {
        super(id, name, disabled);
    }

    @Transient
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
