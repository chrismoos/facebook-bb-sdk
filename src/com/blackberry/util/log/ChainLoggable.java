package com.blackberry.util.log;

public interface ChainLoggable extends Loggable {

	public String getName();

	public void setName(String pName);

	public int getLevel();

	public void setLevel(int pLevel);

	public boolean getAdditive();

	public void setAdditive(boolean pAdditive);

	public ChainLoggable getParent();

	public void setParent(ChainLoggable pParent);

}
