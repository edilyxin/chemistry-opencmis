/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.chemistry.opencmis.client.runtime.util;

import java.util.List;

import org.apache.chemistry.opencmis.client.runtime.util.AbstractPageFetch.PageFetchResult;

/**
 * Iterator for iterating over all items in a CMIS Collection. 
 * 
 * @param <T>
 */
public class CollectionIterator<T> extends AbstractIterator<T> {

    /**
     * Construct
     * 
     * @param skipCount
     * @param pageFetch
     */
    public CollectionIterator(long skipCount, AbstractPageFetch<T> pageFetch) {
        super(skipCount, pageFetch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        PageFetchResult<T> page = getCurrentPage();
        if (page == null) {
            return false;
        }
        
        List<T> items = page.getPage();
        if (items != null && getSkipOffset() < items.size()) {
            return true;
        }
        
        if (!getHasMoreItems()) {
            return false;
        }

        long totalItems = getTotalNumItems();
        if (totalItems < 0) {
            // we don't know better
            return true;
        }

        return (getSkipCount() + getSkipOffset()) < totalItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#next()
     */
    public T next() {
        PageFetchResult<T> page = getCurrentPage();
        if (page == null) {
            return null;
        }

        List<T> items = page.getPage();
        if (items == null || items.isEmpty()) {
            return null;
        }

        if (getSkipOffset() == items.size()) {
            page = incrementPage();
            items = page == null ? null : page.getPage();
        }

        if (items == null || items.isEmpty() || getSkipOffset() == items.size()) {
            return null;
        }

        return items.get(incrementSkipOffset());
    }

}
