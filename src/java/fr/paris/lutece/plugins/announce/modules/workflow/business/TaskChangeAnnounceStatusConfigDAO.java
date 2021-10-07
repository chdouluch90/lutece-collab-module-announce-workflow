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
package fr.paris.lutece.plugins.announce.modules.workflow.business;

import fr.paris.lutece.plugins.announce.modules.workflow.service.AnnounceWorkflowPlugin;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * TaskChangeAnnounceStatusConfigDAO
 */
public class TaskChangeAnnounceStatusConfigDAO implements ITaskConfigDAO<TaskChangeAnnounceStatusConfig>
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task,announce_published "
            + "FROM workflow_task_change_announce_status_cf WHERE id_task=?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_task_change_announce_status_cf( " + "id_task,announce_published)" + "VALUES (?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_task_change_announce_status_cf SET announce_published = ?" + " WHERE id_task = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_task_change_announce_status_cf WHERE id_task = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( TaskChangeAnnounceStatusConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, AnnounceWorkflowPlugin.getPlugin( ) );

        int nPos = 0;

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.setBoolean( ++nPos, config.getPublish( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( TaskChangeAnnounceStatusConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, AnnounceWorkflowPlugin.getPlugin( ) );

        int nPos = 0;

        daoUtil.setBoolean( ++nPos, config.getPublish( ) );

        daoUtil.setInt( ++nPos, config.getIdTask( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskChangeAnnounceStatusConfig load( int nIdTask )
    {
        TaskChangeAnnounceStatusConfig config = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, AnnounceWorkflowPlugin.getPlugin( ) );

        daoUtil.setInt( 1, nIdTask );

        daoUtil.executeQuery( );

        int nPos = 0;

        if ( daoUtil.next( ) )
        {
            config = new TaskChangeAnnounceStatusConfig( );
            config.setIdTask( daoUtil.getInt( ++nPos ) );
            config.setPublish( daoUtil.getBoolean( ++nPos ) );
        }

        daoUtil.free( );

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdState )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, AnnounceWorkflowPlugin.getPlugin( ) );

        daoUtil.setInt( 1, nIdState );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
