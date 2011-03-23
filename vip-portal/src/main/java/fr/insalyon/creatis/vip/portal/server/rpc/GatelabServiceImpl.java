/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.portal.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.portal.server.business.gatelab.GatelabInputs;
import fr.insalyon.creatis.vip.portal.client.rpc.GatelabService;
import fr.insalyon.creatis.vip.portal.server.dao.DAOException;
import fr.insalyon.creatis.vip.portal.server.dao.DAOFactory;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Ibrahim Kallel, Rafael Silva
 */
public class GatelabServiceImpl extends RemoteServiceServlet implements GatelabService {

    public Map<String, String> getGatelabWorkflowInputs(String workflowID) {
        try {
            GatelabInputs gateinputs = new GatelabInputs(workflowID);
            long nb = DAOFactory.getDAOFactory().getGatelabDAO(workflowID).getNumberParticles();
            Map<String, String> inputMap = new HashMap<String, String>();
            inputMap = gateinputs.getWorkflowInputs();
            inputMap.put("runnedparticles", "" + nb);
            return inputMap;
        } catch (DAOException ex) {
            return null;
        }
    }

    public long getNumberParticles(String workflowID) {
        try {
            return DAOFactory.getDAOFactory().getGatelabDAO(workflowID).getNumberParticles();
        } catch (DAOException ex) {
            return 0;
        }

    }

    public void StopWorkflowSimulation(String workflowID) {
        try {
            DAOFactory.getDAOFactory().getGatelabDAO(workflowID).StopWorkflowSimulation();
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }
}
