/*
 * Copyright 2002-2006 the original author or authors.
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
package org.springframework.webflow;

/**
 * A service interface that maps attributes between two flows.
 * <p>
 * An attribute mapper may map attributes of a parent flow down to a child flow
 * as <i>input</i> when the child is spawned as a subflow. In addition, a
 * mapper may map output attributes of a subflow into a resuming parent flow, as
 * <i>output</i> when the child session ends and control is returned to the
 * parent flow.
 * <p>
 * For example, say you have the following parent flow session:
 * <p>
 * 
 * <pre>
 *   Parent Flow Session
 *   -------------------
 *   : flow = myFlow
 *   : flowScope = [map:attribute1=value1, attribute2=value2, attribute3=value3]
 * </pre>
 * 
 * <p>
 * For the "Parent Flow Session" above, there are 3 attributes in flow scope
 * ("attribute1", "attribute2" and "attribute3", respectively). Any of these
 * three attributes may be mapped as input down to child subflows when those
 * subflows are spawned. An implementation of this interface performs the actual
 * mapping, encapsulating knowledge of <i>which</i> attributes should be
 * mapped, and <i>how</i> they will be mapped (for example, will the same
 * attribute names be used between flows or not?).
 * <p>
 * For example:
 * <p>
 * 
 * <pre>
 *   Flow Attribute Mapper Configuration
 *   -----------------------------------
 *   : inputMappings = [map:attribute1-&gt;attribute1, attribute3-&gt;attribute4]
 *   : outputMappings = [map:attribute4-&gt;attribute3]
 * </pre>
 * 
 * <p>
 * The above example "Flow Attribute Mapper" specifies
 * <code>inputMappings</code> that define which parent attributes to map as
 * input to the child. In this case, two attributes in the parent are mapped,
 * "attribute1" and "attribute3". "attribute1" is mapped with the name
 * "attribute1" (given the same name in both flows), while "attribute3" is
 * mapped to "attribute4", given a different name that is local to the child
 * flow.
 * <p>
 * Likewise, when a child flow ends, the <code>outputMappings</code> define
 * which output attributes to map into the parent. In this case the attribute
 * "attribute4" will be mapped up to the parent as "attribute3", updating the
 * value of "attribute3" in the parent's flow scope. Note: only output
 * attributes exposed by the end state of the ending subflow are eligible for
 * mapping.
 * <p>
 * Note: because FlowAttributeMappers are singletons, take care not to store
 * and/or modify caller-specific state in a unsafe manner. The
 * FlowAttributeMapper methods run in an independently executing thread on each
 * invocation, so make sure you deal only with local data or internal,
 * thread-safe services.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface FlowAttributeMapper {

	/**
	 * Create a map of attributes that should be passed as <i>input</i> to a
	 * spawning child flow.
	 * <p>
	 * Attributes set in the <code>Map</code> returned by this method will be
	 * added to flow scope of the child subflow session when the session is
	 * spawned and activated.
	 * @param context the current request execution context, which gives access
	 * to the parent flow scope, the request scope, any event parameter, etc.
	 * @return a map of attributes (name=value pairs) to pass as input to the
	 * spawning subflow
	 */
	public AttributeMap createSubflowInput(RequestContext context);

	/**
	 * Map output attributes of an ended subflow to a resuming parent flow
	 * session. This maps the <i>output</i> of the child as new input to the
	 * resuming parent.
	 * @param subflowOutput the output attributes exposed by the ended subflow
	 * @param context the current request execution context, which gives access
	 * to the parent flow scope
	 */
	public void mapSubflowOutput(UnmodifiableAttributeMap subflowOutput, RequestContext context);
}