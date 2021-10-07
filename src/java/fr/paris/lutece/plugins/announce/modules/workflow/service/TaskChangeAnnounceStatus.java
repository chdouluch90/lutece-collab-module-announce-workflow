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
package fr.paris.lutece.plugins.announce.modules.workflow.service;

import fr.paris.lutece.plugins.announce.business.Announce;
import fr.paris.lutece.plugins.announce.business.AnnounceHome;
import fr.paris.lutece.plugins.announce.modules.workflow.business.TaskChangeAnnounceStatusConfig;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.i18n.I18nService;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * Workflow task to change the publication status of an announce
 */
public class TaskChangeAnnounceStatus extends SimpleTask
{
    /**
     * Name of the bean of the config service of this task
     */
    public static final String CONFIG_SERVICE_BEAN_NAME = "announce-workflow.taskChangeAnnounceStatusConfigService";

    // Messages
    private static final String MESSAGE_PUBLISH_ANNOUNCE = "module.announce.workflow.task_change_announce_status.labelPublishAnnounce";
    private static final String MESSAGE_UNPUBLISH_ANNOUNCE = "module.announce.workflow.task_change_announce_status.labelUnpublishAnnounce";

    // SERVICES
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    @Named( CONFIG_SERVICE_BEAN_NAME )
    private ITaskConfigService _taskChangeAppointmentStatusConfigService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        TaskChangeAnnounceStatusConfig config = _taskChangeAppointmentStatusConfigService.findByPrimaryKey( this.getId( ) );

        if ( ( config != null ) && ( resourceHistory != null ) && Announce.RESOURCE_TYPE.equals( resourceHistory.getResourceType( ) ) )
        {
            // We get the appointment to update
            Announce announce = AnnounceHome.findByPrimaryKey( resourceHistory.getIdResource( ) );

            if ( announce != null )
            {
                if ( config.getPublish( ) )
                {
                    AnnounceHome.setPublished( announce );
                }
                else
                {
                    AnnounceHome.setSuspended( announce );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig( )
    {
        _taskChangeAppointmentStatusConfigService.remove( this.getId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        TaskChangeAnnounceStatusConfig config = _taskChangeAppointmentStatusConfigService.findByPrimaryKey( this.getId( ) );

        if ( config != null )
        {
            return I18nService.getLocalizedString( config.getPublish( ) ? MESSAGE_PUBLISH_ANNOUNCE : MESSAGE_UNPUBLISH_ANNOUNCE, locale );
        }

        return StringUtils.EMPTY;
    }
}
