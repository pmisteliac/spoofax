package org.strategoxt.imp.runtime.services.menus.builders;

public class Separator implements IMenuContribution {

	@Override
	public String getCaption() {
		return "";
	}

	@Override
	public int getContributionType() {
		return IMenuContribution.SEPARATOR;
	}
}