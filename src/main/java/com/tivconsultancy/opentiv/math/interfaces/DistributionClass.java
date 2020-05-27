/* 
 * Copyright 2020 TIVConsultancy.
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
package com.tivconsultancy.opentiv.math.interfaces;

import java.util.List;

/**
 *
 * @author Thomas
 * @param <T>
 */
public interface DistributionClass<T> extends Position{
    
    /**
     * Adds the content to the bin
     * @param loAdd content to be added.
     */
    public void addContent(T loAdd);
    
    /**
     * Returns the minimum allowed in the bin (e.g. min size)
     * @return minimum feature represented as a double.
     */
    public Double getMinBorder();
    
    /**
     * Returns the maximum allowed in the bin (e.g. max size)
     * @return maximum feature represented as a double.
     */
    public Double getMax();
    
    /**
     * This methods checks if the feature represented as double is
     * inside the bin.
     * @param dValue feature represented as double
     * @return true if inside, false if outside
     */
    public boolean isInside(Double dValue);
    
    /**
     * Returns the content inside the bin.
     * @return content of the bin as List.
     */
    public List<T> getContent();
    
}
