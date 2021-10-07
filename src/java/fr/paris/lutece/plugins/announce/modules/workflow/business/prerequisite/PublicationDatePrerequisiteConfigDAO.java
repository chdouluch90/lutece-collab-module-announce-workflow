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

import fr.paris.lutece.plugins.announce.modules.workflow.service.AnnounceWorkflowPlugin;
import fr.paris.lutece.plugins.workflowcore.business.prerequisite.IPrerequisiteConfig;
import fr.paris.lutece.plugins.workflowcore.business.prerequisite.IPrerequisiteConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * DAO for publication date prerequisite
 */
public class PublicationDatePrerequisiteConfigDAO implements IPrerequisiteConfigDAO
{
    private static final String INSERT_PUBLICATION_DATE_PREREQUISITE = " INSERT INTO wf_prerequisite_announce_publication ( id_prerequisite, nb_days ) VALUES (?,?) ";
    private static final String UPDATE_PUBLICATION_DATE_PREREQUISITE = " UPDATE wf_prerequisite_announce_publication SET nb_days = ? WHERE id_prerequisite = ? ";
    private static final String DELETE_PUBLICATION_DATE_PREREQUISITE = " DELETE FROM wf_prerequisite_announce_publication WHERE id_prerequisite = ? ";
    private static final String SELECT_PUBLICATION_DATE_PREREQUISITE = " SELECT id_prerequisite, nb_days FROM wf_prerequisite_announce_publication WHERE id_prerequisite = ?";

    /**
     * {@inheritDoc}
     */
    @Override
    public void createConfig( IPrerequisiteConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( INSERT_PUBLICATION_DATE_PREREQUISITE, AnnounceWorkflowPlugin.getPlugin( ) );
        daoUtil.setInt( 1, config.getIdPrerequisite( ) );
        daoUtil.setInt( 2, ( (PublicationDatePrerequisiteConfig) config ).getNbDays( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateConfig( IPrerequisiteConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( UPDATE_PUBLICATION_DATE_PREREQUISITE, AnnounceWorkflowPlugin.getPlugin( ) );
        daoUtil.setInt( 1, ( (PublicationDatePrerequisiteConfig) config ).getNbDays( ) );
        daoUtil.setInt( 2, config.getIdPrerequisite( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConfig( int nIdPrerequisite )
    {
        DAOUtil daoUtil = new DAOUtil( DELETE_PUBLICATION_DATE_PREREQUISITE, AnnounceWorkflowPlugin.getPlugin( ) );
        daoUtil.setInt( 1, nIdPrerequisite );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPrerequisiteConfig findByPrimaryKey( int nIdPrerequisite )
    {
        DAOUtil daoUtil = new DAOUtil( SELECT_PUBLICATION_DATE_PREREQUISITE, AnnounceWorkflowPlugin.getPlugin( ) );
        daoUtil.setInt( 1, nIdPrerequisite );
        PublicationDatePrerequisiteConfig config = null;
        daoUtil.executeQuery( );
        if ( daoUtil.next( ) )
        {
            config = new PublicationDatePrerequisiteConfig( );
            config.setIdPrerequisite( daoUtil.getInt( 1 ) );
            config.setNbDays( daoUtil.getInt( 2 ) );
        }

        daoUtil.free( );

        return config;
    }

}
