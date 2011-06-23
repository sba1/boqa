package sonumina.b4oweb.server;

import java.util.List;

import sonumina.b4oweb.client.B4OService;
import sonumina.b4oweb.shared.SharedTerm;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class B4OServiceImpl extends RemoteServiceServlet implements B4OService
{

	@Override
	public int getNumberOfTerms()
	{
		return B4OCore.getNumberTerms(null); 
	}

	@Override
	public int getNumberOfTerms(String pattern)
	{
		return B4OCore.getNumberTerms(pattern);
	}


	@Override
	public SharedTerm[] getNamesOfTerms(List<Integer> ids)
	{
		return getNamesOfTerms(null,ids);
	}

	@Override
	public SharedTerm[] getNamesOfTerms(String pattern, List<Integer> ids)
	{
		if (ids == null) return new SharedTerm[0];

		SharedTerm [] names = new SharedTerm[ids.size()];

		int i=0;
		for (int id : ids)
		{
			names[i] = new SharedTerm();
			names[i].requestId = id;
			names[i].term = B4OCore.getTerm(id).getName();
			i++;
		}
		return names;
	}
}
