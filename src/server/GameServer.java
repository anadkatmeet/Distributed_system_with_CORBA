package server;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

public class GameServer {

	/**
	 * @param args
	 * @throws InvalidName
	 * @throws ServantAlreadyActive
	 * @throws WrongPolicy
	 * @throws ObjectNotActive
	 * @throws FileNotFoundException
	 * @throws AdapterInactive
	 */
	public static void main(String[] args) throws InvalidName,
			ServantAlreadyActive, WrongPolicy, ObjectNotActive,
			FileNotFoundException, AdapterInactive {

		new GameServer().initialize(args);

	}

	public void initialize(String[] args) throws InvalidName,
			ServantAlreadyActive, WrongPolicy, ObjectNotActive,
			FileNotFoundException, AdapterInactive {
		// creating three orb objects
		ORB orbNA = ORB.init(args, null);
		ORB orbEU = ORB.init(args, null);
		ORB orbAS = ORB.init(args, null);

		// creating rootPOAs
		POA rootPOANA = POAHelper.narrow(orbNA
				.resolve_initial_references("RootPOA"));
		POA rootPOAEU = POAHelper.narrow(orbEU
				.resolve_initial_references("RootPOA"));
		POA rootPOAAS = POAHelper.narrow(orbAS
				.resolve_initial_references("RootPOA"));

		// creating 3 gameservers
		GameServerImpl serverNA = new GameServerImpl(1411);
		GameServerImpl serverEU = new GameServerImpl(1412);
		GameServerImpl serverAS = new GameServerImpl(1413);

		byte[] idNA = rootPOANA.activate_object(serverNA);
		byte[] idEU = rootPOAEU.activate_object(serverEU);
		byte[] idAS = rootPOAAS.activate_object(serverAS);

		org.omg.CORBA.Object refNA = rootPOANA.id_to_reference(idNA);
		org.omg.CORBA.Object refEU = rootPOAEU.id_to_reference(idEU);
		org.omg.CORBA.Object refAS = rootPOAAS.id_to_reference(idAS);

		String iorNA = orbNA.object_to_string(refNA);
		String iorEU = orbEU.object_to_string(refEU);
		String iorAS = orbAS.object_to_string(refAS);

		PrintWriter file = new PrintWriter("ior.txt");
		file.println(iorNA);
		file.println(iorEU);
		file.println(iorAS);
		file.close();

		System.out.println("done");

		rootPOANA.the_POAManager().activate();
		rootPOAEU.the_POAManager().activate();
		rootPOAAS.the_POAManager().activate();
		

		orbNA.run();
		orbEU.run();
		orbAS.run();
		System.exit(0);
		// orbNA.destroy();
	}

	/*
	 * public void reinitialize(String[] args) throws InvalidName,
	 * ServantAlreadyActive, WrongPolicy, ObjectNotActive,
	 * FileNotFoundException, AdapterInactive{ ORB orbnew = ORB.init(args,
	 * null); POA rootPOANAAA =
	 * POAHelper.narrow(orbnew.resolve_initial_references("RootPOA"));
	 * GameServerImpl servernew= new GameServerImpl(1417); byte[] idNA =
	 * rootPOANAAA.activate_object(servernew); org.omg.CORBA.Object refNA =
	 * rootPOANAAA.id_to_reference(idNA); String iorNA =
	 * orbnew.object_to_string(refNA); System.out.println("server : " + iorNA);
	 * PrintWriter file = new PrintWriter("ior_r.txt"); file.println(iorNA);
	 * file.close(); rootPOANAAA.the_POAManager().activate(); orbnew.run();
	 * 
	 * }
	 */

}
