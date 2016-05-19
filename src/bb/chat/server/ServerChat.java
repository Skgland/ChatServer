package bb.chat.server;

import bb.chat.chat.BasicChat;
import bb.chat.command.*;
import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommandRegistry;
import bb.chat.network.handler.DefaultPacketHandler;
import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUser;
import bb.chat.security.BasicUserDatabase;
import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 04.04.2015.
 */
@SuppressWarnings("ClassNamePrefixedWithPackageName")
public class ServerChat extends BasicChat {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(ServerChat.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile("ChatServer")));
	}



	//initialises super and sets server specific stuff
	public ServerChat(final IConnectionManager imessagehandler,final BasicPermissionRegistrie bpr,final BasicUserDatabase bud,final ICommandRegistry icr) {
		super(imessagehandler, bpr, bud, icr);

		log.entering(this.getClass().getName(),"Constructor");

		icr.addCommand(Help.class);
		icr.addCommand(bb.chat.command.List.class);
		icr.addCommand(Register.class);
		icr.addCommand(Rename.class);
		icr.addCommand(Whisper.class);
		icr.addCommand(Disconnect.class);
		icr.addCommand(Stop.class);
		icr.addCommand(Save.class);
		icr.addCommand(Permission.class);
		icr.addCommand(Debug.class);

		log.finest("Registering DefaultPacketHandler");
		imessagehandler.getPacketDistributor().registerPacketHandler(new DefaultPacketHandler(this));

		//noinspection PublicMethodWithoutLogging
		LOCAL = new IChatActor() {

			BasicUser user;

			{
				//noinspection PublicMethodWithoutLogging
				user = new BasicUser() {

					final List<String> den = new ArrayList<>();
					final List<String> perm = new ArrayList<>();
					final List<String> group = new ArrayList<>();

					{
						perm.add("*");
					}

					@Override
					public int getUserID() {
						return -1;
					}

					@Override
					public String getUserName() {
						return "SERVER";
					}

					@Override
					public List<String> getUserDeniedPermission() {
						return den;
					}

					@Override
					public List<String> getUserPermission() {
						return perm;
					}

					@Override
					public List<String> getGroups() {
						return group;
					}
				};
			}

			@Override
			public IIOHandler getIIOHandler() {
				return imessagehandler.LOCAL();
			}

			@Override
			public boolean isDummy() {
				return true;
			}

			@Override
			public String getActorName() {
				return "SERVER";
			}

			@Override
			public boolean setActorName(String name,boolean notify) {
				return true;
			}

			@Override
			public boolean isLoggedIn() {
				return true;
			}

			@Override
			public void setUser(BasicUser u) {
			}

			@Override
			public BasicUser getUser() {
				return user;
			}
		};
	}
}
