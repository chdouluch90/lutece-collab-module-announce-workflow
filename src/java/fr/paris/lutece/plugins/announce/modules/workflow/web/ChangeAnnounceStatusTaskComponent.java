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
package fr.paris.lutece.plugins.announce.modules.workflow.web;

import fr.paris.lutece.plugins.announce.modules.workflow.business.TaskChangeAnnounceStatusConfig;
import fr.paris.lutece.plugins.announce.modules.workflow.service.TaskChangeAnnounceStatus;
import fr.paris.lutece.plugins.workflow.web.task.NoFormTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * ChangeAnnounceStatusTaskComponent
 */
public class ChangeAnnounceStatusTaskComponent extends NoFormTaskComponent
{
    // TEMPLATES
    private static final String TEMPLATE_TASK_CHANGE_APPOINTMENT_STATUS_CONFIG = "admin/plugins/announce/modules/workflow/task_change_announce_status_config.html";

    // MESSAGES
    private static final String MESSAGE_LABEL_STATUS_PUBLISHED = "module.announce.workflow.task_change_announce_status.labelPublishAnnounce";
    private static final String MESSAGE_LABEL_STATUS_UNPUBLISHED = "module.announce.workflow.task_change_announce_status.labelUnpublishAnnounce";

    // MARKS
    private static final String MARK_CONFIG = "config";
    private static final String MARK_REF_LIST_STATUS = "refListStatus";

    // PARAMETERS
    private static final String PARAMETER_APPOINTMENT_STATUS = "status";

    // SERVICES
    @Inject
    @Named( TaskChangeAnnounceStatus.CONFIG_SERVICE_BEAN_NAME )
    private ITaskConfigService _taskChangeAnnounceStatusConfigService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        boolean bPublish = Boolean.parseBoolean( request.getParameter( PARAMETER_APPOINTMENT_STATUS ) );

        TaskChangeAnnounceStatusConfig config = _taskChangeAnnounceStatusConfigService.findByPrimaryKey( task.getId( ) );
        Boolean bCreate = false;

        if ( config == null )
        {
            config = new TaskChangeAnnounceStatusConfig( );
            config.setIdTask( task.getId( ) );
            bCreate = true;
        }

        config.setPublish( bPublish );

        if ( bCreate )
        {
            _taskChangeAnnounceStatusConfigService.create( config );
        }
        else
        {
            _taskChangeAnnounceStatusConfigService.update( config );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        TaskChangeAnnounceStatusConfig config = _taskChangeAnnounceStatusConfigService.findByPrimaryKey( task.getId( ) );

        ReferenceList refListStatus = new ReferenceList( );
        refListStatus.addItem( Boolean.FALSE.toString( ), I18nService.getLocalizedString( MESSAGE_LABEL_STATUS_UNPUBLISHED, locale ) );
        refListStatus.addItem( Boolean.TRUE.toString( ), I18nService.getLocalizedString( MESSAGE_LABEL_STATUS_PUBLISHED, locale ) );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_CONFIG, config );
        model.put( MARK_REF_LIST_STATUS, refListStatus );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_CHANGE_APPOINTMENT_STATUS_CONFIG, locale, model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        TaskChangeAnnounceStatusConfig config = _taskChangeAnnounceStatusConfigService.findByPrimaryKey( task.getId( ) );

        return I18nService.getLocalizedString( config.getPublish( ) ? MESSAGE_LABEL_STATUS_PUBLISHED : MESSAGE_LABEL_STATUS_UNPUBLISHED, locale );
    }
}
