/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import fr.paris.lutece.plugins.announce.modules.workflow.business.TaskNotifyAnnounceConfig;
import fr.paris.lutece.plugins.announce.modules.workflow.service.TaskNotifyAnnounce;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflow.web.task.NoFormTaskComponent;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.action.ActionFilter;
import fr.paris.lutece.plugins.workflowcore.service.action.ActionService;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;


/**
 * 
 * NotifyAnnounceTaskComponent
 * 
 */
public class NotifyAnnounceTaskComponent extends NoFormTaskComponent
{
    // MARKS
    private static final String MARK_CONFIG = "config";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_DEFAULT_SENDER_NAME = "default_sender_name";

    // PARAMETERS
    private static final String PARAMETER_SUBJECT = "subject";
    private static final String PARAMETER_MESSAGE = "message";
    private static final String PARAMETER_SENDER_NAME = "sender_name";
    private static final String PARAMETER_SENDER_EMAIL = "sender_email";
    private static final String PARAMETER_RECIPIENTS_CC = "recipients_cc";
    private static final String PARAMETER_RECIPIENTS_BCC = "recipients_bcc";

    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFY_ANNOUNCE_CONFIG = "admin/plugins/announce/modules/workflow/task_notify_announce_config.html";

    // FIELDS
    private static final String FIELD_SUBJECT = "module.announce.workflow.task_notify_announce_config.label_subject";
    private static final String FIELD_MESSAGE = "module.announce.workflow.task_notify_announce_config.label_message";
    private static final String FIELD_SENDER_NAME = "module.announce.workflow.task_notify_announce_config.label_sender_name";
    private static final String FIELD_SENDER_EMAIL = "module.announce.workflow.task_notify_announce_config.label_sender_email";
    private static final String FIELD_SENDER_EMAIL_NOT_VALID = "module.announce.workflow.task_notify_announce_config.sender_email_not_valid";

    // MESSAGES
    private static final String MESSAGE_MANDATORY_FIELD = "module.announce.workflow.message.mandatory.field";
    private static final String MESSAGE_EMAIL_SENT_TO_USER = "module.announce.workflow.message.emailSentToUser";

    // SERVICES
    @Inject
    @Named( TaskNotifyAnnounce.CONFIG_SERVICE_BEAN_NAME )
    private ITaskConfigService _taskNotifyAnnounceConfigService;
    @Inject
    @Named( ActionService.BEAN_SERVICE )
    private ActionService _actionService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        TaskNotifyAnnounceConfig config = _taskNotifyAnnounceConfigService.findByPrimaryKey( task.getId( ) );

        ActionFilter filter = new ActionFilter( );
        Action action = _actionService.findByPrimaryKey( task.getAction( ).getId( ) );
        filter.setIdStateBefore( action.getStateAfter( ).getId( ) );

        String strDefaultSenderName = MailService.getNoReplyEmail( );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_CONFIG, config );
        model.put( MARK_DEFAULT_SENDER_NAME, strDefaultSenderName );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, locale );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFY_ANNOUNCE_CONFIG, locale, model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strSenderName = request.getParameter( PARAMETER_SENDER_NAME );
        String strSenderEmail = request.getParameter( PARAMETER_SENDER_EMAIL );
        String strSubject = request.getParameter( PARAMETER_SUBJECT );
        String strMessage = request.getParameter( PARAMETER_MESSAGE );
        String strRecipientsCc = request.getParameter( PARAMETER_RECIPIENTS_CC );
        String strRecipientsBcc = request.getParameter( PARAMETER_RECIPIENTS_BCC );
        String strError = StringUtils.EMPTY;

        if ( StringUtils.isBlank( strSenderName ) )
        {
            strError = FIELD_SENDER_NAME;
        }

        if ( StringUtils.isBlank( strSenderEmail ) )
        {
            strError = FIELD_SENDER_EMAIL;
        }
        else if ( StringUtils.isBlank( strSubject ) )
        {
            strError = FIELD_SUBJECT;
        }
        else if ( StringUtils.isBlank( strMessage ) )
        {
            strError = FIELD_MESSAGE;
        }

        if ( !StringUtil.checkEmail( strSenderEmail ) )
        {
            strError = FIELD_SENDER_EMAIL_NOT_VALID;
        }

        if ( !strError.equals( WorkflowUtils.EMPTY_STRING ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, locale ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields,
                    AdminMessage.TYPE_STOP );
        }

        TaskNotifyAnnounceConfig config = _taskNotifyAnnounceConfigService.findByPrimaryKey( task.getId( ) );
        Boolean bCreate = false;

        if ( config == null )
        {
            config = new TaskNotifyAnnounceConfig( );
            config.setIdTask( task.getId( ) );
            bCreate = true;
        }

        config.setMessage( strMessage );
        config.setSenderName( strSenderName );
        config.setSubject( strSubject );
        config.setRecipientsCc( StringUtils.isNotEmpty( strRecipientsCc ) ? strRecipientsCc : StringUtils.EMPTY );
        config.setRecipientsBcc( StringUtils.isNotEmpty( strRecipientsBcc ) ? strRecipientsBcc : StringUtils.EMPTY );

        if ( bCreate )
        {
            _taskNotifyAnnounceConfigService.create( config );
        }
        else
        {
            _taskNotifyAnnounceConfigService.update( config );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return I18nService.getLocalizedString( MESSAGE_EMAIL_SENT_TO_USER, locale );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }
}
