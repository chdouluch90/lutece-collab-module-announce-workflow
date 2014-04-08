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
package fr.paris.lutece.plugins.announce.modules.workflow.business;

import fr.paris.lutece.plugins.announce.modules.workflow.service.AnnounceWorkflowPlugin;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * 
 * TaskNotifyAnnounceConfigDAO
 * 
 */
public class TaskNotifyAnnounceConfigDAO implements ITaskConfigDAO<TaskNotifyAnnounceConfig>
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task,sender_name,sender_email,subject,message,recipients_cc,recipients_bcc "
            + "FROM workflow_task_notify_announce_cf WHERE id_task=?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_task_notify_announce_cf( "
            + "id_task,sender_name,sender_email,subject,message,recipients_cc,recipients_bcc)"
            + "VALUES (?,?,?,?,?,?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_task_notify_announce_cf "
            + " SET sender_name = ?, sender_email = ?, subject = ?, message = ?, recipients_cc = ?, recipients_bcc = ?"
            + " WHERE id_task = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_task_notify_announce_cf WHERE id_task = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( TaskNotifyAnnounceConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, AnnounceWorkflowPlugin.getPlugin( ) );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, config.getIdTask( ) );
        daoUtil.setString( nIndex++, config.getSenderName( ) );
        daoUtil.setString( nIndex++, config.getSenderEmail( ) );
        daoUtil.setString( nIndex++, config.getSubject( ) );
        daoUtil.setString( nIndex++, config.getMessage( ) );
        daoUtil.setString( nIndex++, config.getRecipientsCc( ) );
        daoUtil.setString( nIndex, config.getRecipientsBcc( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( TaskNotifyAnnounceConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, AnnounceWorkflowPlugin.getPlugin( ) );

        int nIndex = 1;

        daoUtil.setString( nIndex++, config.getSenderName( ) );
        daoUtil.setString( nIndex++, config.getSenderEmail( ) );
        daoUtil.setString( nIndex++, config.getSubject( ) );
        daoUtil.setString( nIndex++, config.getMessage( ) );
        daoUtil.setString( nIndex++, config.getRecipientsCc( ) );
        daoUtil.setString( nIndex++, config.getRecipientsBcc( ) );

        daoUtil.setInt( nIndex, config.getIdTask( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskNotifyAnnounceConfig load( int nIdTask )
    {
        TaskNotifyAnnounceConfig config = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, AnnounceWorkflowPlugin.getPlugin( ) );

        daoUtil.setInt( 1, nIdTask );

        daoUtil.executeQuery( );

        int nIndex = 1;

        if ( daoUtil.next( ) )
        {
            config = new TaskNotifyAnnounceConfig( );
            config.setIdTask( daoUtil.getInt( nIndex++ ) );
            config.setSenderName( daoUtil.getString( nIndex++ ) );
            config.setSenderEmail( daoUtil.getString( nIndex++ ) );
            config.setSubject( daoUtil.getString( nIndex++ ) );
            config.setMessage( daoUtil.getString( nIndex++ ) );
            config.setRecipientsCc( daoUtil.getString( nIndex++ ) );
            config.setRecipientsBcc( daoUtil.getString( nIndex ) );
        }

        daoUtil.free( );

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdTask )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, AnnounceWorkflowPlugin.getPlugin( ) );

        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
