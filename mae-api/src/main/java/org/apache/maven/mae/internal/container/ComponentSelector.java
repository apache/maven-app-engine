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

package org.apache.maven.mae.internal.container;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ComponentSelector
    implements Iterable<Map.Entry<ComponentKey<?>, ComponentKey<?>>>
{

    private Map<ComponentKey<?>, ComponentKey<?>> remappedComponentHints =
        new HashMap<ComponentKey<?>, ComponentKey<?>>();

    public ComponentSelector()
    {
    }

    public ComponentSelector merge( final ComponentSelector selectorToCopy )
    {
        if ( selectorToCopy != null && !selectorToCopy.isEmpty() )
        {
            final Map<ComponentKey<?>, ComponentKey<?>> result = new HashMap<ComponentKey<?>, ComponentKey<?>>();
            result.putAll( selectorToCopy.remappedComponentHints );

            if ( !remappedComponentHints.isEmpty() )
            {
                result.putAll( remappedComponentHints );
            }

            remappedComponentHints = result;
        }

        return this;
    }

    public boolean isEmpty()
    {
        return remappedComponentHints.isEmpty();
    }

    public boolean hasOverride( final ComponentKey<?> key )
    {
        return remappedComponentHints.containsKey( key );
    }

    public <T> boolean hasOverride( final Class<T> role, final String hint )
    {
        final ComponentKey<T> check = new ComponentKey<T>( role, hint );
        return remappedComponentHints.containsKey( check );
    }

    public <T> boolean hasOverride( final Class<T> role )
    {
        final ComponentKey<T> check = new ComponentKey<T>( role );
        return remappedComponentHints.containsKey( check );
    }

    public Set<ComponentKey<?>> getKeysOverriddenBy( final Class<?> role, final String hint )
    {
        @SuppressWarnings( { "rawtypes", "unchecked" } )
        final ComponentKey check = new ComponentKey( role, hint );

        final Set<ComponentKey<?>> result = new HashSet<ComponentKey<?>>();
        for ( final Map.Entry<ComponentKey<?>, ComponentKey<?>> mapping : remappedComponentHints.entrySet() )
        {
            if ( mapping.getValue().equals( check ) )
            {
                result.add( mapping.getKey() );
            }
        }

        return result;
    }

    public <T> ComponentSelector setSelection( final ComponentKey<T> originalKey, final String newHint )
    {
        remappedComponentHints.put( originalKey, new ComponentKey<T>( originalKey.getRoleClass(), newHint ) );
        return this;
    }

    public <T> ComponentSelector setSelection( final Class<T> role, final String oldHint, final String newHint )
    {
        final ComponentKey<T> originalKey = new ComponentKey<T>( role, oldHint );
        remappedComponentHints.put( originalKey, new ComponentKey<T>( role, newHint ) );
        return this;
    }

    public <T> ComponentSelector setSelection( final Class<T> role, final String newHint )
    {
        final ComponentKey<T> originalKey = new ComponentKey<T>( role );
        remappedComponentHints.put( originalKey, new ComponentKey<T>( role, newHint ) );
        return this;
    }

    @Override
    public Iterator<Entry<ComponentKey<?>, ComponentKey<?>>> iterator()
    {
        return new HashSet<Map.Entry<ComponentKey<?>, ComponentKey<?>>>( remappedComponentHints.entrySet() ).iterator();
    }

    @SuppressWarnings( "unchecked" )
    public <T> ComponentKey<T> getOverride( final Class<T> role, final String hint )
    {
        return (ComponentKey<T>) remappedComponentHints.get( new ComponentKey<T>( role, hint ) );
    }

    @SuppressWarnings( "unchecked" )
    public <T> ComponentKey<T> getOverride( final ComponentKey<T> key )
    {
        return (ComponentKey<T>) remappedComponentHints.get( key );
    }

}
