/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.announce.modules.workflow.business.prerequisite;

import fr.paris.lutece.plugins.workflowcore.business.prerequisite.DefaultPrerequisiteConfig;

import javax.validation.constraints.Min;

/**
 * Configuration of publication date prerequisite
 */
public class PublicationDatePrerequisiteConfig extends DefaultPrerequisiteConfig
{
    /**
     * Type of this prerequisite
     */
    public static final String PREREQUISITE_TYPE = "announce.workflow.publicationDate";

    @Min( value = 1, message = "module.announce.workflow.publication_date_prerequisite_config.labelMinNbDays" )
    private int _nNbDays;

    /**
     * Get the number of days to wait before executing the action
     * 
     * @return The number of days to wait before executing the action
     */
    public int getNbDays( )
    {
        return _nNbDays;
    }

    /**
     * Set the number of days to wait before executing the action
     * 
     * @param nNbDays
     *            The number of days to wait before executing the action
     */
    public void setNbDays( int nNbDays )
    {
        this._nNbDays = nNbDays;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrerequisiteType( )
    {
        return PREREQUISITE_TYPE;
    }
}
