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
import fr.paris.lutece.plugins.announce.modules.workflow.business.TaskNotifyAnnounceConfig;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.LuteceUserService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.string.StringUtil;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * Workflow task to notify a user of an announce
 */
public class TaskNotifyAnnounce extends SimpleTask
{
    /**
     * Name of the bean of the config service of this task
     */
    public static final String CONFIG_SERVICE_BEAN_NAME = "announce-workflow.taskNotifyAnnounceConfigService";

    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFY_MAIL = "admin/plugins/announce/modules/workflow/task_notify_announce_mail.html";

    // MARKS
    private static final String MARK_ANNOUNCE = "announce";
    private static final String MARK_MESSAGE = "message";
    private static final String MARK_LIST_RESPONSE = "listResponse";

    // SERVICES
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    @Named( CONFIG_SERVICE_BEAN_NAME )
    private ITaskConfigService _taskNotifyAnnounceConfigService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        TaskNotifyAnnounceConfig config = _taskNotifyAnnounceConfigService.findByPrimaryKey( this.getId( ) );
        Announce announce = AnnounceHome.findByPrimaryKey( resourceHistory.getIdResource( ) );

        LuteceUser user = LuteceUserService.getLuteceUserFromName( announce.getUserName( ) );

        String strEmail = user != null ? user.getEmail( ) : announce.getUserName( );
        if ( StringUtil.checkEmail( strEmail ) )
        {
            this.sendEmail( announce, resourceHistory, request, locale, config, strEmail );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig( )
    {
        _taskNotifyAnnounceConfigService.remove( this.getId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        TaskNotifyAnnounceConfig config = _taskNotifyAnnounceConfigService.findByPrimaryKey( this.getId( ) );

        if ( config != null )
        {
            return config.getSubject( );
        }

        return StringUtils.EMPTY;
    }

    /**
     * Send an email to a user
     * 
     * @param announce
     *            The announce
     * @param resourceHistory
     *            The resource history
     * @param request
     *            The request
     * @param locale
     *            The locale
     * @param notifyAnnounceConfig
     *            The task configuration
     * @param strEmail
     *            The address to send the email to
     */
    @SuppressWarnings( "deprecation" )
    public void sendEmail( Announce announce, ResourceHistory resourceHistory, HttpServletRequest request, Locale locale,
            TaskNotifyAnnounceConfig notifyAnnounceConfig, String strEmail )
    {
        if ( ( notifyAnnounceConfig != null ) && ( resourceHistory != null ) && Announce.RESOURCE_TYPE.equals( resourceHistory.getResourceType( ) )
                && ( announce != null ) )
        {
            if ( StringUtils.isEmpty( notifyAnnounceConfig.getSenderEmail( ) ) || !StringUtil.checkEmail( notifyAnnounceConfig.getSenderEmail( ) ) )
            {
                notifyAnnounceConfig.setSenderEmail( MailService.getNoReplyEmail( ) );
            }

            if ( StringUtils.isBlank( notifyAnnounceConfig.getSenderName( ) ) )
            {
                notifyAnnounceConfig.setSenderName( notifyAnnounceConfig.getSenderEmail( ) );
            }

            Map<String, Object> model = new HashMap<String, Object>( );

            model.put( MARK_ANNOUNCE, announce );
            model.put( MARK_MESSAGE, notifyAnnounceConfig.getMessage( ) );

            List<Response> listResponse = AnnounceHome.findListResponse( announce.getId( ), false );
            model.put( MARK_LIST_RESPONSE, listResponse );

            String strSubject = AppTemplateService.getTemplateFromStringFtl( notifyAnnounceConfig.getSubject( ), locale, model ).getHtml( );

            boolean bHasRecipients = ( StringUtils.isNotBlank( notifyAnnounceConfig.getRecipientsBcc( ) )
                    || StringUtils.isNotBlank( notifyAnnounceConfig.getRecipientsCc( ) ) );

            String strContent = AppTemplateService
                    .getTemplateFromStringFtl( AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFY_MAIL, locale, model ).getHtml( ), locale, model )
                    .getHtml( );

            if ( bHasRecipients )
            {
                MailService.sendMailHtml( strEmail, notifyAnnounceConfig.getRecipientsCc( ), notifyAnnounceConfig.getRecipientsBcc( ),
                        notifyAnnounceConfig.getSenderName( ), notifyAnnounceConfig.getSenderEmail( ), strSubject, strContent );
            }
            else
            {
                MailService.sendMailHtml( strEmail, notifyAnnounceConfig.getSenderName( ), notifyAnnounceConfig.getSenderEmail( ), strSubject, strContent );
            }
        }
    }
}
