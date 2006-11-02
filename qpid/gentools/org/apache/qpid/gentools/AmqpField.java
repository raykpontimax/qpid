/*
 *
 * Copyright (c) 2006 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.qpid.gentools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Node;

public class AmqpField implements Printable, NodeAware, VersionConsistencyCheck
{
	public LanguageConverter converter;
	public AmqpVersionSet versionSet;
	public AmqpDomainVersionMap domainMap;
	public AmqpOrdinalVersionMap ordinalMap;
	public String name;
	
	public AmqpField(String name, LanguageConverter converter)
	{
		this.name = name;
		this.converter = converter;
		versionSet = new AmqpVersionSet();
		domainMap = new AmqpDomainVersionMap();
		ordinalMap = new AmqpOrdinalVersionMap();
	}

	public void addFromNode(Node fieldNode, int ordinal, AmqpVersion version)
		throws AmqpParseException, AmqpTypeMappingException
	{
		versionSet.add(version);
		String domainType;
		// Early versions of the spec (8.0) used the "type" attribute instead of "domain" for some fields.
		try
		{
			domainType = converter.prepareDomainName(Utils.getNamedAttribute(fieldNode, Utils.ATTRIBUTE_DOMAIN));
		}
		catch (AmqpParseException e)
		{
			domainType = converter.prepareDomainName(Utils.getNamedAttribute(fieldNode, Utils.ATTRIBUTE_TYPE));
		}
		AmqpVersionSet thisVersionList = domainMap.get(domainType);
		if (thisVersionList == null) // First time, create new entry
		{
			thisVersionList = new AmqpVersionSet();
			domainMap.put(domainType, thisVersionList);
		}
		thisVersionList.add(version);
		thisVersionList = ordinalMap.get(ordinal);
		if (thisVersionList == null) // First time, create new entry
		{
			thisVersionList = new AmqpVersionSet();
			ordinalMap.put(ordinal, thisVersionList);
		}
		thisVersionList.add(version);
	}
	
	public boolean isCodeTypeConsistent(LanguageConverter converter)
	    throws AmqpTypeMappingException
	{
		if (domainMap.size() == 1)
			return true; // By definition
		ArrayList<String> codeTypeList = new ArrayList<String>();
		Iterator<String> itr = domainMap.keySet().iterator();
		while (itr.hasNext())
		{
			String domainName = itr.next();
			AmqpVersionSet versionSet = domainMap.get(domainName);
			String codeType = converter.getGeneratedType(domainName, versionSet.first());
			if (!codeTypeList.contains(codeType))
				codeTypeList.add(codeType);
		}
		return codeTypeList.size() == 1;
	}
	
	public boolean isConsistent(Generator generator)
        throws AmqpTypeMappingException
	{
		if (!isCodeTypeConsistent(generator))
			return false;
		if (ordinalMap.size() != 1)
			return false;
		// Since the various doamin names map to the same code type, add the version occurrences
		// across all domains to see we have all possible versions covered
		int vCntr = 0;
		Iterator<String> itr = domainMap.keySet().iterator();
		while (itr.hasNext())
			vCntr += domainMap.get(itr.next()).size();
		return vCntr == generator.globalVersionSet.size();
	}
	
	public void print(PrintStream out, int marginSize, int tabSize)
	{
		String margin = Utils.createSpaces(marginSize);
		out.println(margin + "[F] " + name + ": " + versionSet);

		Iterator<Integer> iItr = ordinalMap.keySet().iterator();
		while (iItr.hasNext())
		{
			Integer ordinalValue = iItr.next();
			AmqpVersionSet versionList = ordinalMap.get(ordinalValue);
			out.println(margin + "  [O] " + ordinalValue + " : " + versionList.toString());
		}

		Iterator<String> sItr = domainMap.keySet().iterator();
		while (sItr.hasNext())
		{
			String domainKey = sItr.next();
			AmqpVersionSet versionList = domainMap.get(domainKey);
			out.println(margin + "  [D] " + domainKey + " : " + versionList.toString());
		}
	}
	
	public boolean isVersionConsistent(AmqpVersionSet globalVersionSet)
	{
		if (!versionSet.equals(globalVersionSet))
			return false;
		if (!domainMap.isVersionConsistent(globalVersionSet))
			return false;
		if (!ordinalMap.isVersionConsistent(globalVersionSet))
			return false;
		return true;
	}
}
