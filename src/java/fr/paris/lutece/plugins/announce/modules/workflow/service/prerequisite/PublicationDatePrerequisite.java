/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.announce.modules.workflow.service.prerequisite;

import fr.paris.lutece.plugins.announce.business.Announce;
import fr.paris.lutece.plugins.announce.business.AnnounceHome;
import fr.paris.lutece.plugins.announce.modules.workflow.business.prerequisite.PublicationDatePrerequisiteConfig;
import fr.paris.lutece.plugins.workflowcore.business.prerequisite.IPrerequisiteConfig;
import fr.paris.lutece.plugins.workflowcore.service.prerequisite.IAutomaticActionPrerequisiteService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Prerequisite that check that an announce has been publicated for longer than
 * a given number of days
 */
public class PublicationDatePrerequisite implements IAutomaticActionPrerequisiteService
{
    private static final String CONFIGURATION_DAO_BEAN_NAME = "announce-workflow.publicationDatePrerequisiteConfigDAO";

    private static final String TEMPLATE_PULICATION_DATE_PREREQUISITE_CONFIG = "admin/plugins/announce/modules/workflow/prerequisite_publication_date_config.html";

    private static final String MESSAGE_PREREQUISITE_PUBLICATION_DATE = "module.announce.workflow.publication_date_prerequisite.title";

    private static final String MARK_CONFIG = "config";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrerequisiteType( )
    {
        return PublicationDatePrerequisiteConfig.PREREQUISITE_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitleI18nKey( )
    {
        return MESSAGE_PREREQUISITE_PUBLICATION_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasConfiguration( )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPrerequisiteConfig getEmptyConfiguration( )
    {
        return new PublicationDatePrerequisiteConfig( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigurationDaoBeanName( )
    {
        return CONFIGURATION_DAO_BEAN_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigHtml( IPrerequisiteConfig config, HttpServletRequest request, Locale locale )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_CONFIG, config );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PULICATION_DATE_PREREQUISITE_CONFIG, locale,
                model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canActionBePerformed( int nIdResource, String strResourceType, IPrerequisiteConfig config,
            int nIdAction )
    {
        if ( StringUtils.equals( Announce.RESOURCE_TYPE, strResourceType ) )
        {
            Announce announce = AnnounceHome.findByPrimaryKey( nIdResource );
            if ( announce.getTimePublication( ) <= 0 )
            {
                return false;
            }
            Calendar calendar = new GregorianCalendar( );
            calendar.add( Calendar.DATE, -1 * ( (PublicationDatePrerequisiteConfig) config ).getNbDays( ) );

            if ( announce.getTimePublication( ) > calendar.getTimeInMillis( ) )
            {
                return false;
            }
        }
        return true;
    }

}
